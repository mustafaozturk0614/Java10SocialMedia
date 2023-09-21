package com.bilgeadam.service;


import com.bilgeadam.dto.request.ActivateRequestDto;
import com.bilgeadam.dto.request.AuthUpdateRequestDto;
import com.bilgeadam.dto.request.LoginRequestDto;
import com.bilgeadam.dto.request.RegisterRequestDto;
import com.bilgeadam.dto.response.RegisterResponseDto;
import com.bilgeadam.exception.AuthManagerException;
import com.bilgeadam.exception.ErrorType;
import com.bilgeadam.manager.IUserManager;
import com.bilgeadam.mapper.IAuthMapper;
import com.bilgeadam.rabbitmq.model.MailModel;
import com.bilgeadam.rabbitmq.producer.ActivationProducer;
import com.bilgeadam.rabbitmq.producer.MailProducer;
import com.bilgeadam.rabbitmq.producer.RegisterProducer;
import com.bilgeadam.repository.IAuthRepository;
import com.bilgeadam.repository.entity.Auth;
import com.bilgeadam.repository.enums.EStatus;
import com.bilgeadam.utility.CodeGenerator;
import com.bilgeadam.utility.JwtTokenManager;
import com.bilgeadam.utility.ServiceManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/*
    1-
    Register işlemi yapcaz
    dto alsın dto dönsün
    request dto --> username, email, password
    response dto --> username, id, activationCode
    repodan controllera kadar yazalım


    GetMapping --> veriler nerden geliyor url
    PostMapping --> Body

    2- Login methodu yazalım
        dto alsın eğer veritabnında kayıt varsa true dönsün yoksa false dönsün

    3- Active status --> Boolean dönsün

    */

@Service
public class AuthService extends ServiceManager<Auth, Long> {

    private final IAuthRepository authRepository;

    private final JwtTokenManager jwtTokenManager;

    private final IUserManager userManager;

    private final RegisterProducer registerProducer;

    private final ActivationProducer activationProducer;

    private final MailProducer mailProducer;

    public AuthService(IAuthRepository authRepository, JwtTokenManager jwtTokenManager, IUserManager userManager, RegisterProducer registerProducer, ActivationProducer activationProducer, MailProducer mailProducer) {
        super(authRepository);
        this.authRepository = authRepository;
        this.jwtTokenManager = jwtTokenManager;
        this.userManager = userManager;
        this.registerProducer = registerProducer;
        this.activationProducer = activationProducer;
        this.mailProducer = mailProducer;
    }

    @Transactional
    public RegisterResponseDto register(RegisterRequestDto dto) {
        Auth auth = IAuthMapper.INSTANCE.toAuth(dto);
        auth.setActivationCode(CodeGenerator.generateCode());
        if (authRepository.existsByUsername(dto.getUsername())) {
            throw new AuthManagerException(ErrorType.USERNAME_ALREADY_EXIST);
        }
        save(auth);
        // bir metot yazacağiz 2 microservis arası haberleşme için
        String token = jwtTokenManager.createToken(auth.getId(), auth.getRole())
                .orElseThrow(() -> new AuthManagerException(ErrorType.INVALID_TOKEN));


        userManager.save(IAuthMapper.INSTANCE.toUserSaveRequestDto(auth), "Bearer " + token);


        RegisterResponseDto responseDto = IAuthMapper.INSTANCE.toRegisterResponseDto(auth);


        responseDto.setToken(token);

        return responseDto;
    }

    @Transactional
    public RegisterResponseDto registerWithRabbitMq(RegisterRequestDto dto) {
        Auth auth = IAuthMapper.INSTANCE.toAuth(dto);
        auth.setActivationCode(CodeGenerator.generateCode());
        if (authRepository.existsByUsername(dto.getUsername())) {
            throw new AuthManagerException(ErrorType.USERNAME_ALREADY_EXIST);
        }
        save(auth);
        //rabbit mq ile haberleştireceğiz
        registerProducer.sendNewUser(IAuthMapper.INSTANCE.toRegisterModel(auth));


        //register token olusturma
        RegisterResponseDto responseDto = IAuthMapper.INSTANCE.toRegisterResponseDto(auth);
        String token = jwtTokenManager.createToken(auth.getId())
                .orElseThrow(() -> new AuthManagerException(ErrorType.INVALID_TOKEN));

        responseDto.setToken(token);
        // mail atma işlemi için mail servis ile haberleşilecek
        MailModel mailModel = IAuthMapper.INSTANCE.toMailModel(auth);
        mailModel.setToken(token);
//        mailProducer.sendMail(MailModel
//                .builder().username(auth.getUsername()).email(auth.getEmail())
//                .activationCode(auth.getActivationCode())
//                .token(token)
//                .build());
        mailProducer.sendMail(mailModel);
        return responseDto;
    }


    public String login(LoginRequestDto dto) {
        Optional<Auth> optionalAuth = authRepository.findOptionalByUsernameAndPassword(dto.getUsername(), dto.getPassword());
        if (optionalAuth.isEmpty()) {
            throw new AuthManagerException(ErrorType.LOGIN_ERROR);
        }
        if (!optionalAuth.get().getStatus().equals(EStatus.ACTIVE)) {
            throw new AuthManagerException(ErrorType.ACCOUNT_NOT_ACTIVE);
        }

        return jwtTokenManager.createToken(optionalAuth.get().getId(), optionalAuth.get().getRole())
                .orElseThrow(() -> new AuthManagerException(ErrorType.TOKEN_NOT_CREATED));
    }

    @Transactional
    public String activateStatus(ActivateRequestDto dto) {
        Optional<Long> id = jwtTokenManager.getIdFromToken(dto.getToken());
        if (id.isEmpty()) {
            throw new AuthManagerException(ErrorType.INVALID_TOKEN);
        }
        Optional<Auth> optionalAuth = findById(id.get());
        if (optionalAuth.isEmpty()) {
            throw new AuthManagerException(ErrorType.USER_NOT_FOUND);
        }
        if (optionalAuth.get().getStatus().equals(EStatus.ACTIVE)) {
            throw new AuthManagerException(ErrorType.ALREADY_ACTIVE);
        }
        if (dto.getActivationCode().equals(optionalAuth.get().getActivationCode())) {
            optionalAuth.get().setStatus(EStatus.ACTIVE);
            update(optionalAuth.get());
            // userManager.activateStatus(dto.getToken()); // open feign ile haberleşme
            activationProducer.activateStatus(dto.getToken()); // rabbitmq ile haberleşme
            return "Hesabınız aktive edilmiştir";
        } else {
            throw new AuthManagerException(ErrorType.INVALID_CODE);
        }
    }


    public String updateAuth(AuthUpdateRequestDto dto) {
        Optional<Auth> auth = findById(dto.getId());
        if (auth.isEmpty()) {
            throw new AuthManagerException(ErrorType.USER_NOT_FOUND);
        }
        auth.get().setEmail(dto.getEmail());
        auth.get().setUsername(dto.getUsername());
        update(auth.get());
        return "Guncelleme başarılı";
    }

    @Transactional
    public String deleteAuth(String token) {
        Optional<Long> id = jwtTokenManager.getIdFromToken(token);
        if (id.isEmpty()) {
            throw new AuthManagerException(ErrorType.INVALID_TOKEN);
        }
        Optional<Auth> auth = findById(id.get());
        if (auth.isEmpty()) {
            throw new AuthManagerException(ErrorType.USER_NOT_FOUND);
        }
        if (auth.get().getStatus().equals(EStatus.DELETED)) {
            throw new AuthManagerException(ErrorType.USER_NOT_FOUND, "Hesap zaten silinmiş");
        }
        auth.get().setStatus(EStatus.DELETED);
        update(auth.get());
        userManager.deleteById("Bearer " + token);
        return id + "id li kullanıcı başarıyla slindi";
    }
}

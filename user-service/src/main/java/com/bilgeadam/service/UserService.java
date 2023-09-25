package com.bilgeadam.service;



import com.bilgeadam.dto.request.AuthUpdateRequestDto;
import com.bilgeadam.dto.request.UserProfileUpdateRequestDto;
import com.bilgeadam.dto.request.UserSaveRequestDto;
import com.bilgeadam.dto.response.UserProfileFindAllResponseDto;
import com.bilgeadam.dto.response.UserProfileResponseDto;
import com.bilgeadam.exception.ErrorType;
import com.bilgeadam.exception.UserManagerException;
import com.bilgeadam.manager.IAuthManager;
import com.bilgeadam.mapper.IUserMapper;
import com.bilgeadam.rabbitmq.model.RegisterModel;
import com.bilgeadam.rabbitmq.producer.RegisterElasticProducer;
import com.bilgeadam.repository.IUserRepository;
import com.bilgeadam.repository.entity.UserProfile;
import com.bilgeadam.repository.enums.EStatus;
import com.bilgeadam.utility.JwtTokenManager;
import com.bilgeadam.utility.ServiceManager;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;
/*
    update metodu yazalım
    update metodu userporfile bilgilerimi guncelleyecek aynı zamanda
    update metodunda username veya email 2 sinden biri değişmiş ise
    auth servisdede guncelleme yapsın
 */

@Service
public class UserService extends ServiceManager<UserProfile, String> {
    private final IUserRepository userRepository;
    private final JwtTokenManager jwtTokenManager;

    private final IAuthManager authManager;

    private final CacheManager cacheManager;

    private final RegisterElasticProducer registerElasticProducer;

    public UserService(IUserRepository userRepository, JwtTokenManager jwtTokenManager, IAuthManager authManager, CacheManager cacheManager, RegisterElasticProducer registerElasticProducer) {
        super(userRepository);
        this.userRepository = userRepository;
        this.jwtTokenManager = jwtTokenManager;
        this.authManager = authManager;
        this.cacheManager = cacheManager;
        this.registerElasticProducer = registerElasticProducer;
    }

    public Boolean createNewUser(UserSaveRequestDto dto) {
        try {
            UserProfile userProfile= IUserMapper.INSTANCE.toUserProfile(dto);

          save(userProfile);
          return true;
        }catch (Exception e){
            throw  new UserManagerException(ErrorType.USER_NOT_CREATED);
        }
    }

    public String activateStatus(String token) {
        Optional<Long> authId=jwtTokenManager.getAuthIdFromToken(token);
        if (authId.isEmpty()){
            throw  new UserManagerException(ErrorType.INVALID_TOKEN);
        }
        Optional<UserProfile> userProfile=userRepository.findByAuthId(authId.get());
        if (userProfile.isEmpty()){
            throw  new UserManagerException(ErrorType.USER_NOT_FOUND);
        }
        userProfile.get().setStatus(EStatus.ACTIVE);
        update(userProfile.get());
        return "Hesabınız aktive olmuştur";
    }
    @Transactional
    public String updateUserProfile(UserProfileUpdateRequestDto dto){
        Authentication authorization = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Authorization: " + authorization.getPrincipal());
       Optional<Long> authId=jwtTokenManager.getAuthIdFromToken(dto.getToken()) ;
       if (authId.isEmpty()){
           throw new UserManagerException(ErrorType.INVALID_TOKEN);
       }
       Optional<UserProfile> userProfile=userRepository.findByAuthId(authId.get());
       if (userProfile.isEmpty()){
           throw  new UserManagerException(ErrorType.USER_NOT_FOUND);

       }
       if (!userProfile.get().getEmail().equals(dto.getEmail())
               ||!userProfile.get().getUsername().equals(dto.getUsername())){

           userProfile.get().setEmail(dto.getEmail());
           userProfile.get().setUsername(dto.getUsername());
           // auth servise istek atan bir metot yazılacak
            authManager.updateAuth(AuthUpdateRequestDto.builder()
                    .email(dto.getEmail()).username(dto.getUsername())
                            .id(authId.get())
                    .build(),"Bearer "+ dto.getToken()
            );

       }
//       userProfile= Optional.of( IUserMapper.INSTANCE.toUserProfile(dto));
//        System.out.println(userProfile);
        userProfile.get().setAbout(dto.getAbout());
        userProfile.get().setPhone(dto.getPhone());
        userProfile.get().setAddress(dto.getAddress());
        userProfile.get().setName(dto.getName());
        userProfile.get().setSurName(dto.getSurName());
        userProfile.get().setAvatar(dto.getAvatar());
        update(userProfile.get());
      //  cacheManager.getCache("find_by_username").evict(userProfile.get().getUsername()); // silme işlemi
        cacheManager.getCache("find_by_username").put(userProfile.get().getUsername(),userProfile.get()); //guncelleme
        return  "Güncelleme başarılı";


    }

    public Boolean createNewUserWithRabbitmq(RegisterModel model) {
        try {
            UserProfile userProfile= IUserMapper.INSTANCE.toUserProfile(model);
            save(userProfile);
            registerElasticProducer.sendNewUser(IUserMapper.INSTANCE.toRegisterElasticModel(userProfile));
            return true;
        }catch (Exception e){
            throw  new UserManagerException(ErrorType.USER_NOT_CREATED);
        }
    }

    @Cacheable(value = "find_by_username")
    public UserProfile findByUsername(String username){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Optional<UserProfile> userProfile=userRepository.findByUsername(username);
    if (userProfile.isEmpty()){
        throw new UserManagerException(ErrorType.USER_NOT_FOUND);
    }
         return userProfile.get();
    }

    @Cacheable(value = "find_by_status")
    public List<UserProfile> findByStatus(EStatus status) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        List<UserProfile> list=userRepository.findByStatus(status);
//        if (list.isEmpty()){
//            throw new RuntimeException("Herhangi bir veri bulunamdı");
//        }
        return list;
    }

    @Cacheable(value = "find_by_status2",key = "#status.toUpperCase(T(java.util.Locale).ENGLISH)")
    public List<UserProfile> findByStatus(String status) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        EStatus myStatus;
        try {
            myStatus = EStatus.valueOf(status.toUpperCase(Locale.ENGLISH));
        } catch (Exception e) {
            throw new UserManagerException(ErrorType.STATUS_NOT_FOUND);
        }
        return   userRepository.findByStatus(myStatus);

    }


    public String deleteUserProfile(String token) {
        Optional<Long> authId=jwtTokenManager.getAuthIdFromToken(token.substring(7));

        Optional<UserProfile> userProfile=userRepository.findByAuthId(authId.get());
        if (userProfile.isEmpty()){
            throw new UserManagerException(ErrorType.USER_NOT_FOUND);
        }
        userProfile.get().setStatus(EStatus.DELETED);
        update(userProfile.get());
        cacheManager.getCache("find_by_username").evict(userProfile.get().getUsername());
        return userProfile.get().getId()+ "id li kullancı slinmiştir";
    }

    public List<UserProfileFindAllResponseDto> findAllUserProfile() {
        List<UserProfile> userProfileList=findAll();
      return  userProfileList.stream()
              .map(x->IUserMapper.INSTANCE.toUserProfileFindAllResponseDto(x)).collect(Collectors.toList());

    }

    public Page<UserProfile> findAllByPageable(int pageSize, int pageNumber, String direction, String sortParameter) {
        Sort sort=Sort.by(Sort.Direction.fromString(direction),sortParameter);
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);
        return userRepository.findAll(pageable);
    }

    public Slice<UserProfile> findAllBySLice(int pageSize, int pageNumber, String direction, String sortParameter) {
        Sort sort=Sort.by(Sort.Direction.fromString(direction),sortParameter);
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);
        return userRepository.findAll(pageable);
    }

    public Optional<UserProfile> findByUserWithAuthId(Long authId){
        Optional<UserProfile> userProfile = userRepository.findByAuthId(authId);
        if (userProfile.isEmpty()){
            throw new UserManagerException(ErrorType.USER_NOT_FOUND);
        }
        return userProfile;
    }

    //for post service, postUpdate()
    public UserProfileResponseDto findByUserSimpleDataWithAuthId(Long authId){
        Optional<UserProfile> userProfile = userRepository.findByAuthId(authId);
        if (userProfile.isEmpty()){
            throw new UserManagerException(ErrorType.USER_NOT_FOUND);
        }

        UserProfileResponseDto userProfileResponseDto = UserProfileResponseDto.builder()
                .userId(userProfile.get().getId())
                .username(userProfile.get().getUsername())
                .userAvatar(userProfile.get().getAvatar())
                .build();
        return userProfileResponseDto;
    }
}

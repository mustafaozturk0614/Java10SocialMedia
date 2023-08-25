package com.bilgeadam.service;



import com.bilgeadam.dto.request.AuthUpdateRequestDto;
import com.bilgeadam.dto.request.UserProfileUpdateRequestDto;
import com.bilgeadam.dto.request.UserSaveRequestDto;
import com.bilgeadam.exception.ErrorType;
import com.bilgeadam.exception.UserManagerException;
import com.bilgeadam.manager.IAuthManager;
import com.bilgeadam.mapper.IUserMapper;
import com.bilgeadam.repository.IUserRepository;
import com.bilgeadam.repository.entity.UserProfile;
import com.bilgeadam.repository.enums.EStatus;
import com.bilgeadam.utility.JwtTokenManager;
import com.bilgeadam.utility.ServiceManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
/*
    update metodu yazalım
    update metodu userporfile bilgilerimi guncelleyecek aynı zamanda
    update metodunda username veya email 2 sinden biri değişmiş ise
    auth servisdede guncelleme yapsın
 */

@Service
public class UserService extends ServiceManager<UserProfile, Long> {
    private final IUserRepository userRepository;
    private final JwtTokenManager jwtTokenManager;

    private final IAuthManager authManager;

    public UserService(IUserRepository userRepository, JwtTokenManager jwtTokenManager, IAuthManager authManager) {
        super(userRepository);
        this.userRepository = userRepository;
        this.jwtTokenManager = jwtTokenManager;
        this.authManager = authManager;
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
                    .build()

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
        return  "Güncelleme başarılı";


    }
}

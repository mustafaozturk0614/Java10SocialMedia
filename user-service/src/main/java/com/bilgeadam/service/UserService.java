package com.bilgeadam.service;



import com.bilgeadam.dto.request.UserSaveRequestDto;
import com.bilgeadam.exception.ErrorType;
import com.bilgeadam.exception.UserManagerException;
import com.bilgeadam.mapper.IUserMapper;
import com.bilgeadam.repository.IUserRepository;
import com.bilgeadam.repository.entity.UserProfile;
import com.bilgeadam.repository.enums.EStatus;
import com.bilgeadam.utility.JwtTokenManager;
import com.bilgeadam.utility.ServiceManager;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;

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

    public UserService(IUserRepository userRepository, JwtTokenManager jwtTokenManager) {
        super(userRepository);
        this.userRepository = userRepository;
        this.jwtTokenManager = jwtTokenManager;
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
}

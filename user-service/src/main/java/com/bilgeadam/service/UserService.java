package com.bilgeadam.service;



import com.bilgeadam.dto.request.UserSaveRequestDto;
import com.bilgeadam.exception.ErrorType;
import com.bilgeadam.exception.UserManagerException;
import com.bilgeadam.mapper.IUserMapper;
import com.bilgeadam.repository.IUserRepository;
import com.bilgeadam.repository.entity.UserProfile;
import com.bilgeadam.utility.JwtTokenManager;
import com.bilgeadam.utility.ServiceManager;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;

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
public class UserService extends ServiceManager<UserProfile, Long> {
    private final IUserRepository userRepository;
    private final JwtTokenManager jwtTokenManager;

    public UserService(IUserRepository userRepository, JwtTokenManager jwtTokenManager) {
        super(userRepository);
        this.userRepository = userRepository;
        this.jwtTokenManager = jwtTokenManager;
    }

    public UserProfile createNewUser(UserSaveRequestDto dto) {
        try {
            UserProfile userProfile= IUserMapper.INSTANCE.toUserProfile(dto);
            return save(userProfile);
        }catch (Exception e){
            throw  new UserManagerException(ErrorType.USER_NOT_CREATED);
        }
    }
}

package com.bilgeadam.service;

import com.bilgeadam.repository.ILikeRepository;
import com.bilgeadam.repository.entity.Like;
import com.bilgeadam.utility.JwtTokenManager;
import com.bilgeadam.utility.ServiceManager;
import org.springframework.stereotype.Service;


@Service
public class LikeService extends ServiceManager<Like, String> {
    private final ILikeRepository likeRepository;
    private final JwtTokenManager jwtTokenManager;


    public LikeService(ILikeRepository likeRepository, JwtTokenManager jwtTokenManager) {
        super(likeRepository);
        this.likeRepository = likeRepository;
        this.jwtTokenManager = jwtTokenManager;
    }


}

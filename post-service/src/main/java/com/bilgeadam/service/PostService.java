package com.bilgeadam.service;

import com.bilgeadam.repository.IPostRepository;
import com.bilgeadam.repository.entity.Post;
import com.bilgeadam.utility.JwtTokenManager;
import com.bilgeadam.utility.ServiceManager;
import org.springframework.stereotype.Service;


@Service
public class PostService extends ServiceManager<Post, String> {
    private final IPostRepository postRepository;
    private final JwtTokenManager jwtTokenManager;


    public PostService(IPostRepository postRepository, JwtTokenManager jwtTokenManager) {
        super(postRepository);
        this.postRepository = postRepository;
        this.jwtTokenManager = jwtTokenManager;
    }


}

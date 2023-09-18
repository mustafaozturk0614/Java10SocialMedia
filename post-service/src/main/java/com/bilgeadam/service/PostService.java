package com.bilgeadam.service;

import com.bilgeadam.dto.request.CreateNewPostRequestDto;
import com.bilgeadam.dto.response.UserProfileResponseDto;
import com.bilgeadam.exception.ErrorType;
import com.bilgeadam.exception.PostManagerException;
import com.bilgeadam.mapper.IPostMapper;
import com.bilgeadam.rabbitmq.model.CreatePostModel;
import com.bilgeadam.rabbitmq.producer.CreatePostProducer;
import com.bilgeadam.repository.IPostRepository;
import com.bilgeadam.repository.entity.Post;
import com.bilgeadam.utility.JwtTokenManager;
import com.bilgeadam.utility.ServiceManager;
import org.springframework.stereotype.Service;



@Service
public class PostService extends ServiceManager<Post, String> {
    private final IPostRepository postRepository;
    private final JwtTokenManager jwtTokenManager;
    private final CreatePostProducer createPostProducer;


    public PostService(IPostRepository postRepository, JwtTokenManager jwtTokenManager, CreatePostProducer createPostProducer) {
        super(postRepository);
        this.postRepository = postRepository;
        this.jwtTokenManager = jwtTokenManager;
        this.createPostProducer = createPostProducer;
    }

/*
bu metodun controller' ını yazınız.
createPost işlemi için user-service' de consumer işlemlerini yapınız.
 */
    public Post createPost(String token, CreateNewPostRequestDto dto){
        Long authId = jwtTokenManager.getAuthIdFromToken(token)
                .orElseThrow(() -> {throw new PostManagerException(ErrorType.INVALID_TOKEN);});
        CreatePostModel createPostModel = CreatePostModel.builder()
                .authId(authId)
                .build();
        UserProfileResponseDto userProfile = (UserProfileResponseDto) createPostProducer.createPost(createPostModel);
        Post post = Post.builder()
                .userId(userProfile.getUserId())
                .username(userProfile.getUsername())
                .userAvatar(userProfile.getUserAvatar())
                .content(dto.getContent())
                .mediaUrls(dto.getMediaUrls())
                .build();
        return save(post);
    }
}

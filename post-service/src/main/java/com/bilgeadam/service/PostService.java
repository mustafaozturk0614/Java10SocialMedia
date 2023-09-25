package com.bilgeadam.service;

import com.bilgeadam.dto.request.CreateNewPostRequestDto;
import com.bilgeadam.dto.request.UpdatePostRequestDto;
import com.bilgeadam.dto.response.UserProfileResponseDto;
import com.bilgeadam.exception.ErrorType;
import com.bilgeadam.exception.PostManagerException;
import com.bilgeadam.manager.IUserManager;
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
    private final IUserManager userManager;


    public PostService(IPostRepository postRepository, JwtTokenManager jwtTokenManager, CreatePostProducer createPostProducer, IUserManager userManager) {
        super(postRepository);
        this.postRepository = postRepository;
        this.jwtTokenManager = jwtTokenManager;
        this.createPostProducer = createPostProducer;
        this.userManager = userManager;
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

    public Post updatePost(String token, String postId, UpdatePostRequestDto dto){
        Long authId = jwtTokenManager.getAuthIdFromToken(token)
                .orElseThrow(() -> {throw new PostManagerException(ErrorType.INVALID_TOKEN);});
        UserProfileResponseDto userProfile = userManager.findByUserSimpleDataWithAuthId(authId).getBody();
        Post post = postRepository.findById(postId).get();
        if (userProfile.getUserId().equals(post.getUserId())){
            //eklenen media url varsa listeye ekle
            dto.getAddMediaUrls().stream().forEach(mu -> post.getMediaUrls().add(mu));
            //silinen media url varsa listeden çıkar
            dto.getAddMediaUrls().removeAll(dto.getRemoveMediaUrls());
            post.setContent(dto.getContent());
            post.setUserId(userProfile.getUserId());
            post.setUsername(userProfile.getUsername());
            post.setUserAvatar(userProfile.getUserAvatar());
            save(post);
            return post;
        }
        throw new RuntimeException("Post Güncellenemedi");
    }
}

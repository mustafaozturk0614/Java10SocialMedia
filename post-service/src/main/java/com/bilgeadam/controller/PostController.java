package com.bilgeadam.controller;

import com.bilgeadam.dto.request.CreateNewPostRequestDto;
import com.bilgeadam.repository.entity.Post;
import com.bilgeadam.service.PostService;
import com.bilgeadam.utility.JwtTokenManager;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.bilgeadam.constant.EndPoints.*;

@RestController
@RequestMapping(POST)
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final JwtTokenManager jwtTokenManager;

    @PostMapping("/create-post/{token}")
    public ResponseEntity<Post> createPost(@PathVariable String token, @RequestBody CreateNewPostRequestDto dto){
        return ResponseEntity.ok(postService.createPost(token, dto));
    }
}

package com.bilgeadam.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Document
public class Like extends BaseEntity{
    @Id
    private String id;
    private String userId;
    private String postId; //beğenilen post
    private String commentId; //beğenilen yorum
    private String username;
    private String userAvatar;
}

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
    private String postId; // id = 2,  id = 3
    private String commentId; //null , comemntId = 3
    private String username;
    private String userAvatar;
}

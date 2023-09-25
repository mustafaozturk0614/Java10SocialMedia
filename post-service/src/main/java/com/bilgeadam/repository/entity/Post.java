package com.bilgeadam.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
@Document
public class Post extends BaseEntity{
    @Id
    private String id;
    private String userId;
    private String username; //java10 -->javaboost10
    private String userAvatar;
    private String content;
    private List<String> mediaUrls;
    private List<String> likes;
    private List<String> comments;
}

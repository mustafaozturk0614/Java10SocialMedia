package com.bilgeadam.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Document
public class Comment extends BaseEntity {
    @Id
    private String id;
    private String userId; //yorum yapan kişinin id si
    private String postId;
    private String username; //yorum yapan kişinin adı
    private String userAvatar;
    private String comment; //yapılan yorum
    private List<String> subCommentId; //commentId
    private List<String> commentLikes; //userId
}


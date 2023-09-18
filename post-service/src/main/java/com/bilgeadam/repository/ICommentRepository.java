package com.bilgeadam.repository;

import com.bilgeadam.repository.entity.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ICommentRepository extends MongoRepository<Comment, String> {


}

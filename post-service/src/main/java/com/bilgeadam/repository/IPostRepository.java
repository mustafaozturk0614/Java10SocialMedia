package com.bilgeadam.repository;


import com.bilgeadam.repository.entity.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IPostRepository extends MongoRepository<Post, String> {


}

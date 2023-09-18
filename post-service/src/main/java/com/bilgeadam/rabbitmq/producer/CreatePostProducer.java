package com.bilgeadam.rabbitmq.producer;

import com.bilgeadam.rabbitmq.model.CreatePostModel;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreatePostProducer {

    private String exchange = "post-exchange";
    private String createPostBindingKey = "post-bindingkey";

    private final RabbitTemplate rabbitTemplate;

    public Object createPost(CreatePostModel model){
        return rabbitTemplate.convertSendAndReceive(exchange, createPostBindingKey, model);
    }
}

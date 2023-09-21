package com.bilgeadam.config.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
    //auth register queue
    @Value("${rabbitmq.register-queue}")
    private String registerQueueName;

    @Bean
    Queue registerQueue(){
        return new Queue(registerQueueName);
    }

    //create post consumer

    private String createPostQueue = "post-queue";

    @Bean
    Queue createPostQueue(){
        return new Queue(createPostQueue);
    }

}

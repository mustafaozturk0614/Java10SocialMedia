package com.bilgeadam.config.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    //create post
    private String exchange = "post-exchange";
    private String createPostQueue = "post-queue";
    private String createPostBindingKey = "post-bindingkey";

    @Bean
    Queue createPostQueue(){
        return new Queue(createPostQueue);
    }

    @Bean
    DirectExchange exchange(){
        return new DirectExchange(exchange);
    }

    @Bean
    public Binding createPostBindingKey(final Queue createPostQueue, final DirectExchange exchange){
        return BindingBuilder.bind(createPostQueue).to(exchange).with(createPostBindingKey);
    }

}

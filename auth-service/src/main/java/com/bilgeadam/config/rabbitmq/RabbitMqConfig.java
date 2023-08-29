package com.bilgeadam.config.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
        @Value("${rabbitmq.auth-exchange}")
        private String exchange;
        @Value("${rabbitmq.register-binding-key}")
        private String registerBindingKey;

       @Value("${rabbitmq.register-queue}")
        private String registerQueueName;

        @Bean
        DirectExchange exchange(){
            return new DirectExchange(exchange);
        }

        @Bean
        Queue registerQueue(){
            return new Queue(registerQueueName);
        }

        @Bean
        public Binding bindingRegister(final Queue registerQueue ,final DirectExchange exchange ){
            return BindingBuilder.bind(registerQueue).to(exchange).with(registerBindingKey);
        }

}

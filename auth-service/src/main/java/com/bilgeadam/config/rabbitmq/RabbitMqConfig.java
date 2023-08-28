package com.bilgeadam.config.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

        private String exchange="auth-exchange";
        private String registerBindingKey="register-key";
        private String registerQueue="register-queue";

        @Bean
        DirectExchange exchange(){
            return new DirectExchange(exchange);
        }

        @Bean
        Queue registerQueue(){
            return new Queue(registerQueue);
        }

        @Bean
        public Binding bindingRegister(final Queue registerQueue ,final DirectExchange exchange ){
            return BindingBuilder.bind(registerQueue).to(exchange).with(registerBindingKey);
        }

}

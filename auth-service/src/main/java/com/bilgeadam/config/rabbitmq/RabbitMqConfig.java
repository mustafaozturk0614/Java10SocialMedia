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
    private String registerBindingKey;  //unique --> her bir mesaj isteğine göre özel üretilmelidir
    @Value("${rabbitmq.register-queue}")
    private String registerQueueName;  //unique --> her bir mesaj isteğine göre özel üretilmelidir
    @Value("${rabbitmq.activation-binding-key}")
    private String activationBindingKey;
    @Value("${rabbitmq.activation-queue}")
    private String activationQueueName;

    @Value("${rabbitmq.mail-queue}")
    private String  mailQueueName;
    @Value("${rabbitmq.mail-binding-key}")
    private String  mailBindingKey;


    @Bean
    DirectExchange exchange() {
        return new DirectExchange(exchange);
    }

    // Register işlemleri
    @Bean
    Queue registerQueue() {
        return new Queue(registerQueueName);
    }

    @Bean
    public Binding bindingRegister(final Queue registerQueue, final DirectExchange exchange) {
        return BindingBuilder.bind(registerQueue).to(exchange).with(registerBindingKey);
    }

    // activation işlemleri
    @Bean
    public Queue activationQueue() {
        return new Queue(activationQueueName);
    }

    @Bean
    public Binding bindingActivation(final Queue activationQueue, final DirectExchange exchange) {
        return BindingBuilder.bind(activationQueue).to(exchange).with(activationBindingKey);
    }

    // mail işlemleri

    @Bean
    public Queue mailQueue(){
        return new Queue(mailQueueName);
    }

    @Bean
    public Binding bindingMail(final Queue mailQueue, final DirectExchange exchange) {
        return BindingBuilder.bind(mailQueue).to(exchange).with(mailBindingKey);
    }

}

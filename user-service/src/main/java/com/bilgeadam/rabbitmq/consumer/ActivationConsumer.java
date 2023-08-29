package com.bilgeadam.rabbitmq.consumer;

import com.bilgeadam.rabbitmq.model.RegisterModel;
import com.bilgeadam.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActivationConsumer {

    private final UserService userService;
    @RabbitListener(queues = ("${rabbitmq.activation-queue}"))
    public void activateStatus(String token){
        log.info("token=> {}",token);
        userService.activateStatus(token);
    }

}

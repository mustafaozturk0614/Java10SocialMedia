package com.bilgeadam.rabbitmq.consumer;

import com.bilgeadam.rabbitmq.model.MailModel;
import com.bilgeadam.service.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailConsumer {

    private final MailService mailService;

    @RabbitListener(queues = "${rabbitmq.mail-queue}")
    public void sendEmail(MailModel model){
        log.info("model==> {}",model);
        mailService.sendMail(model);
    }

}

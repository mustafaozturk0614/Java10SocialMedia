package com.bilgeadam.service;




import com.bilgeadam.rabbitmq.model.MailModel;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class MailService{

    private final JavaMailSender javaMailSender;

    public void sendMail(MailModel model) {
        SimpleMailMessage mailMessage=new SimpleMailMessage();
        mailMessage.setFrom("${java10_mail}");
        mailMessage.setTo(model.getEmail());
        mailMessage.setSubject("AKTIVASYON KODUNUZ...");
        mailMessage.setText("Merhaba "+model.getUsername()+"\n"+"token=>>>> "+model.getToken()+"\n"+"aktivasyon kodu :"+ model.getActivationCode() );
        javaMailSender.send(mailMessage);
        System.out.println("Mail gonderildi....");
    }



}

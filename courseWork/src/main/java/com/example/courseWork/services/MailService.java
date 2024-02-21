package com.example.courseWork.services;

import com.example.courseWork.models.MailStructure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class MailService {

    private final JavaMailSender javaMailSender;

    @Autowired
    public MailService(JavaMailSender javaMailSender){
        this.javaMailSender = javaMailSender;
    }

    @Value("${spring.mail.username}")
    private String fromMail;
    public void sendMail(String mail, MailStructure messageStructure){
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(fromMail);
        simpleMailMessage.setSubject(messageStructure.getSubject());
        simpleMailMessage.setText(messageStructure.getMessage());
        simpleMailMessage.setTo(mail);
        javaMailSender.send(prepareMailMessage(simpleMailMessage));
    }

    private SimpleMailMessage prepareMailMessage(SimpleMailMessage simpleMailMessage) {
        Properties properties = new Properties();
        properties.setProperty("mail.smtp.starttls.enable", "true");
        properties.setProperty("mail.smtp.auth", "true");

        JavaMailSenderImpl mailSender = (JavaMailSenderImpl) javaMailSender;
        mailSender.setJavaMailProperties(properties);

        return simpleMailMessage;
    }
}

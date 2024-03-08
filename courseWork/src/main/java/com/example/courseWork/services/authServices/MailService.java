package com.example.courseWork.services.authServices;

import com.example.courseWork.models.authModel.MailStructure;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class MailService {

    private final JavaMailSender javaMailSender;

    @Autowired
    public MailService(JavaMailSender javaMailSender){
        this.javaMailSender = javaMailSender;

        JavaMailSenderImpl senderImpl = (JavaMailSenderImpl) javaMailSender;
        senderImpl.setJavaMailProperties(getMailProperties());
    }

    private Properties getMailProperties() {
        Properties properties = new Properties();
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.starttls.enable", "true");
        properties.setProperty("mail.smtp.ssl.trust", "smtp.gmail.com"); // Для Gmail
        properties.setProperty("mail.smtp.host", "smtp.gmail.com"); // Или другой хост почтового сервера
        properties.setProperty("mail.smtp.port", "587"); // Порт STARTTLS

        return properties;
    }

    @Value("${spring.mail.username}")
    private String fromMail;
    public void sendMail(String mail, MailStructure messageStructure){
        try{
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        helper.setFrom(fromMail);
        helper.setSubject(messageStructure.getSubject());
        helper.setText(messageStructure.getMessage(),true);
        helper.setTo(mail);

       /* SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(fromMail);
        simpleMailMessage.setSubject(messageStructure.getSubject());
        simpleMailMessage.setText(messageStructure.getMessage(),true);
        simpleMailMessage.setTo(mail);*/
        javaMailSender.send(mimeMessage);}
        catch(Exception e){
            e.printStackTrace();
        }
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

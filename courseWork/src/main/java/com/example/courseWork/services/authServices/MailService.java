package com.example.courseWork.services.authServices;

import com.example.courseWork.models.authModel.MailStructure;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class MailService {
    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String fromMail;

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
        properties.setProperty("mail.smtp.ssl.trust", "smtp.gmail.com");
        properties.setProperty("mail.smtp.host", "smtp.gmail.com");
        properties.setProperty("mail.smtp.port", "587");
        return properties;
    }

    public void sendMail(String mail, MailStructure messageStructure){
        try{
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(fromMail);
            helper.setSubject(messageStructure.getSubject());
            helper.setText(messageStructure.getMessage(),true);
            helper.setTo(mail);
            javaMailSender.send(mimeMessage);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}

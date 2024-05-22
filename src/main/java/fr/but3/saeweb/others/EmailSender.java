package fr.but3.saeweb.others;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Component
public class EmailSender {

    @Autowired
    private JavaMailSender sender;

    @Autowired
    private AllProperties allProperties;

    
    public void sendEmail(String clientEmail,String subject,String content) throws MessagingException{
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom(allProperties.getEmailSender());
        helper.setTo(clientEmail);
        helper.setSubject(subject);
        helper.setText(content);
        sender.send(message);
    }
    
}

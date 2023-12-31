package com.example.ecomerce.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {
    @Autowired
    private JavaMailSender mailSender;

//    public void sendSimpleEmail(String toEmail,
//                                String subject,
//                                String body
//    ) {
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setFrom("ecomerceapp2023@gmail.com");
//        message.setTo(toEmail);
//        message.setText(body);
//        message.setSubject(subject);
//        mailSender.send(message);
//        System.out.println("Mail Send...");
//
//
//    }

    public void sendConfirmationEmail(String email, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("ecomerceapp2023@gmail.com");
        message.setTo(email);
        message.setSubject("Email Confirmation");
        message.setText("Please click on the following link to confirm your email: "
                + "http://localhost:4200/confirm?token=" + token);
        mailSender.send(message);
    }

    public void sendHtmlEmail(String email, String subject, String htmlContent) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom("ecomerceapp2023@gmail.com");
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(htmlContent, true); // Set the HTML content to true
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
            // Handle the exception accordingly
        }

    }
}

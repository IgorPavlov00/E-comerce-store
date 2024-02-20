package com.example.ecomerce.email;

import com.example.ecomerce.dto.CartItemDto;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;

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

    public void sendHtmlEmailWithImageAttachment(String email, String subject, String htmlContent, byte[] imageData, String imageName) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom("ecomerceapp2023@gmail.com");
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(htmlContent, true); // Set the HTML content to true

            // Add image attachment
            ByteArrayResource imageAttachment = new ByteArrayResource(imageData);
            helper.addAttachment(imageName, imageAttachment, "image/png"); // Set content type as per your image type

            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
            // Handle the exception accordingly
        }
    }


    public void sendHtmlEmailWithImageAttachments(String email, String subject, List<CartItemDto> cartItems, List<byte[]> attachmentDataList, List<String> attachmentNames) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom("ecomerceapp2023@gmail.com");
            helper.setTo(email);
            helper.setSubject(subject);

            // Generate HTML content
            String htmlContent = generateHtmlContent(cartItems, attachmentDataList,attachmentNames);
            helper.setText(htmlContent, true); // Set the HTML content to true

            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
            // Handle the exception accordingly
        }
    }

    private String generateHtmlContent(List<CartItemDto> cartItems, List<byte[]> attachmentDataList, List<String> attachmentNames) {
        StringBuilder htmlContentBuilder = new StringBuilder();
        htmlContentBuilder.append("<html><body>");
        htmlContentBuilder.append("<div style='text-align: center; padding: 20px; border: 1px solid #ddd; border-radius: 5px;background-color:bisque;'>");
        htmlContentBuilder.append("<h1>Thank you for shopping at our store!</h1>");

        // Add image for the header
        String imageUrl = "https://www.creativefabrica.com/wp-content/uploads/2018/10/Shopping-cart-logo-by-DEEMKA-STUDIO-580x406.jpg";
        htmlContentBuilder.append("<img src='" + imageUrl + "' alt='Cart Image' style='max-width: 90%; max-height:40%; border-radius:5px;'>");

        htmlContentBuilder.append("<br><br>");
        htmlContentBuilder.append("<p>You ordered:</p>");
        htmlContentBuilder.append("<br><br>");

        // Table header
        htmlContentBuilder.append("<table style='width:100%;'>");
        htmlContentBuilder.append("<tr>");
        htmlContentBuilder.append("<th style='width: 25.33%; text-align:center;'>Name</th>");
        htmlContentBuilder.append("<th style='width: 25.33%; text-align:center;'>Description</th>");
        htmlContentBuilder.append("<th style='width: 25.33%; text-align:center;'>Price</th>");
        htmlContentBuilder.append("<th style='width: 25.33%; text-align:center;'>Image</th>");
        htmlContentBuilder.append("</tr>");

        // Table rows for cart items
        for (int i = 0; i < cartItems.size(); i++) {
            CartItemDto cartItem = cartItems.get(i);
            byte[] imageData = attachmentDataList.get(i);
            String attachmentName = attachmentNames.get(i);

            htmlContentBuilder.append("<tr>");
            htmlContentBuilder.append("<td style='width: 25.33%; text-align:center;'>").append(cartItem.getName()).append("</td>");
            htmlContentBuilder.append("<td style='width: 25.33%; text-align:center;'>").append(cartItem.getDescription()).append("</td>");
            htmlContentBuilder.append("<td style='width: 25.33%; text-align:center;'>").append(cartItem.getPrice()).append("$</td>");
            htmlContentBuilder.append("<td style='width: 25.33%; text-align:center;'><img src='cid:").append(attachmentName).append("' alt='Product Image' style='max-width: 100px; max-height: 100px;'></td>");
            htmlContentBuilder.append("</tr>");
        }

        // Close table
        htmlContentBuilder.append("</table>");

        // Close the HTML content
        htmlContentBuilder.append("</div></body></html>");

        return htmlContentBuilder.toString();
    }

    private String generateHtmlContent(List<CartItemDto> cartItems) {
        StringBuilder htmlContentBuilder = new StringBuilder();
        htmlContentBuilder.append("<html><body>");
        htmlContentBuilder.append("<div style='text-align: center; padding: 20px; border: 1px solid #ddd; border-radius: 5px;background-color:bisque;'>");
        htmlContentBuilder.append("<h1>Thank you for shopping at our store!</h1>");

        // Add image for the header
        String imageUrl = "https://www.creativefabrica.com/wp-content/uploads/2018/10/Shopping-cart-logo-by-DEEMKA-STUDIO-580x406.jpg";
        htmlContentBuilder.append("<img src='" + imageUrl + "' alt='Cart Image' style='max-width: 90%; max-height:40%; border-radius:5px;'>");

        htmlContentBuilder.append("<br><br>");
        htmlContentBuilder.append("<p>You ordered:</p>");
        htmlContentBuilder.append("<br><br>");

        // Table header
        htmlContentBuilder.append("<table style='width:100%;'>");
        htmlContentBuilder.append("<tr>");
        htmlContentBuilder.append("<th style='width: 25.33%; text-align:center;'>Name</th>");
        htmlContentBuilder.append("<th style='width: 25.33%; text-align:center;'>Description</th>");
        htmlContentBuilder.append("<th style='width: 25.33%; text-align:center;'>Price</th>");
        htmlContentBuilder.append("<th style='width: 25.33%; text-align:center;'>Image</th>");
        htmlContentBuilder.append("</tr>");

        // Table rows for cart items
        for (CartItemDto cartItem : cartItems) {
            htmlContentBuilder.append("<tr>");
            htmlContentBuilder.append("<td style='width: 25.33%; text-align:center;'>").append(cartItem.getName()).append("</td>");
            htmlContentBuilder.append("<td style='width: 25.33%; text-align:center;'>").append(cartItem.getDescription()).append("</td>");
            htmlContentBuilder.append("<td style='width: 25.33%; text-align:center;'>").append(cartItem.getPrice()).append("$</td>");
            htmlContentBuilder.append("<td style='width: 25.33%; text-align:center;'><img src='").append(cartItem.getImagePath()).append("' alt='Product Image' style='max-width: 100px; max-height: 100px;'></td>");
            htmlContentBuilder.append("</tr>");
        }

        // Close table
        htmlContentBuilder.append("</table>");

        // Close the HTML content
        htmlContentBuilder.append("</div></body></html>");

        return htmlContentBuilder.toString();
    }

    public void sendHtmlEmailWithImageAttachments2(String email, String subject, String htmlContent, List<String> imagePaths, List<String> attachmentNames) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom("ecomerceapp2023@gmail.com");
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(htmlContent, true); // Set the HTML content to true

            // Add all attachments
            for (int i = 0; i < imagePaths.size(); i++) {
                String imagePath = imagePaths.get(i);
                String attachmentName = attachmentNames.get(i);

                // Load image from classpath resources
                ClassPathResource resource = new ClassPathResource(imagePath);
                helper.addInline(attachmentName, resource);
            }

            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
            // Handle the exception accordingly
        }
    }
}

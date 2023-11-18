package com.example.ecomerce.controller;


import com.example.ecomerce.dto.RegisterDTO;
import com.example.ecomerce.email.EmailSenderService;
import com.example.ecomerce.model.Shoes;
import com.example.ecomerce.model.User;
import com.example.ecomerce.model.VerificationToken;
import com.example.ecomerce.service.ShoeService;
import com.example.ecomerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailSenderService emailSenderService;



    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> registerUser(@RequestBody RegisterDTO user) {
        User k = new User(user);
        k.setAktivan(false);
        this.userService.save(k);
        String token = UUID.randomUUID().toString();

        // Save the token, email, and timestamp in the database
        userService.createVerificationToken(k, token);

        // Build the HTML content for the email with the dynamic link and token
//        String confirmationLink = "http://localhost:4200/confirm?token=" + token;
//        String htmlContent = "<html><body>" +
//                "<div style='text-align: center; padding: 20px; border: 1px solid #ddd; border-radius: 5px;'>" +
//                "<h1>Welcome to Our Platform"+" "+k.getName()+" "+k.getLastname()+"!</h1>" +
//                "<p>Click the button below to verify your account:</p>" +
//                "<a href='" + confirmationLink + "' style='display: inline-block; padding: 10px 20px; background-color: #4caf50; color: #ffffff; text-decoration: none; border-radius: 5px;'>Confirm Email</a>" +
//                "</div></body></html>";
        String confirmationLink = "http://localhost:4200/confirm?token=" + token;
        String imageUrl = "https://www.creativefabrica.com/wp-content/uploads/2018/10/Shopping-cart-logo-by-DEEMKA-STUDIO-580x406.jpg";

        String htmlContent = "<html><body>" +
                "<div style='text-align: center; padding: 20px; border: 1px solid #ddd; border-radius: 5px;background-color:bisque;'>" +
                "<h1 >Welcome to Our Store " + k.getName() + " " + k.getLastname() + "!</h1>" +
                "<p>Click the button below to verify your account:</p>" +
                "<a href='" + confirmationLink + "' style='display: inline-block; padding: 10px 20px; background-color: #4caf50; color: #ffffff; text-decoration: none; border-radius: 5px;'>Confirm Email</a>" +
                "<br><br>" +
                "<img src='" + imageUrl + "' alt='Cart Image' style='max-width: 100%; max-height:60%; border-radius:5px;'><br>" +
                "</div></body></html>";

        // Send the email with HTML content
        emailSenderService.sendHtmlEmail(k.getEmail(), "Activate Your Account", htmlContent);

        return ResponseEntity.ok(k);
    }

//    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<?> registerUser(@RequestBody RegisterDTO user) {
//        User k = new User(user);
//        k.setAktivan(false);
//        this.userService.save(k);
//        String token = UUID.randomUUID().toString();
//
//        // Save the token, email, and timestamp in the database
//        userService.createVerificationToken(k, token);
//
//        // Build the HTML content for the email with the dynamic link and token
//        String confirmationLink = "http://localhost:4200/confirm?token=" + token;
//
//        String htmlContent = "<head>\n" +
//                "  <title>Complete your registration by verifying your account</title>\n" +
//                "  <style type=\"text/css\">\n" +
//                "    /* Your CSS styles here */\n" +
//                "  </style>\n" +
//                "</head>\n" +
//                "<body leftmargin=\"0\" marginwidth=\"0\" topmargin=\"0\" marginheight=\"0\" offset=\"0\" style=\"background-color:#edeff0; margin-bottom: 26px;\">\n" +
//                "    <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" height=\"100%\" width=\"100%\" id=\"bodyTable\" style=\"background-color:#edeff0; margin-bottom: 26px;\">\n" +
//                "        <tbody><tr>\n" +
//                "            <td align=\"center\" valign=\"top\" id=\"bodyCell\">\n" +
//                "                <!-- BEGIN TEMPLATE // -->\n" +
//                "                <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" id=\"templateContainer\" style=\"max-width:600px;\">\n" +
//                "                    <tbody><tr>\n" +
//                "                        <td align=\"center\" valign=\"top\">\n" +
//                "                            <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" id=\"templateHeader\" style=\"margin-top: 36px; margin-bottom: 26px;\">\n" +
//                "                                <tbody><tr>\n" +
//                "                                    <td align=\"center\">\n" +
//                "                                        <a href=\"https://www.shapeways.com?etId=151322913&amp;utm_source=automated-contact&amp;utm_medium=email&amp;utm_campaign=verify-account&amp;utm_content=0\" target=\"_blank\"><img src=\"https://www.shapeways.com/rrstatic/img/email/sw-logo-email-2x-20131114.png\" style=\"max-width:180px;\" alt=\"Shapeways\"></a>\n" +
//                "                                    </td>\n" +
//                "                                </tr>\n" +
//                "                            </tbody></table>\n" +
//                "                        </td>\n" +
//                "                    </tr>\n" +
//                "                    <tr>\n" +
//                "                        <td align=\"center\" valign=\"top\">\n" +
//                "                            <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\n" +
//                "                                <tbody><tr>\n" +
//                "                                    <td valign=\"top\">\n" +
//                "                                        <img src=\"https://www.shapeways.com/rrstatic/img/email/email-rounded-top.jpg\" width=\"100%\" style=\"vertical-align:bottom;\">\n" +
//                "                                    </td>\n" +
//                "                                </tr>\n" +
//                "                            </tbody></table>\n" +
//                "                        </td>\n" +
//                "                    </tr>\n" +
//                "                    <tr>\n" +
//                "                        <td>\n" +
//                "                            <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"background-color:#ffffff;\">\n" +
//                "                                <tbody><tr>\n" +
//                "                                    <td>\n" +
//                "                                        <!-- Body of email will go here -->\n" +
//                "                                        <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"background-color: #ffffff;font-family:Helvetica, Arial, sans-serif;color:#445258;line-height: 22px;\">\n" +
//                "                                            <tbody><tr>\n" +
//                "                                                <td style=\"text-align: center; display: block; padding: 0px 20px 20px 20px;\">\n" +
//                "                                                    <h1 style=\"color:#183643;text-align: center; font-size: 24px; letter-spacing: 1px; margin-bottom: 30px;\">Activate your account</h1>\n" +
//                "                                                </td>\n" +
//                "                                            </tr>\n" +
//                "                                            <tr>\n" +
//                "                                                <td style=\"padding: 0px 16.66% 40px 16.66%; text-align: center; display: block;\">\n" +
//                "                                                    <img style=\"width: 100%\" src=\"https://www.shapeways.com/rrstatic/img/email/email-verification-three-step.png\" alt=\"Activate Your  Account\">\n" +
//                "                                                </td>\n" +
//                "                                            </tr>\n" +
//                "                                            <tr>\n" +
//                "                                                <td style=\"text-align: center; font-family:Helvetica, Arial, sans-serif;\">\n" +
//                "                                                    <a href=\"" + confirmationLink + "\" title=\"Activate Now\" style=\"background: #4fc9ff; border: solid 1px #02B2FF; border-radius: 2px; padding: 3% 7%; color: #ffffff; font-weight: bold; font-size: 14px; letter-spacing: .25px; -webkit-font-smoothing: antialiased; text-transform: uppercase; text-decoration: none; line-height:18px; display: inline-block;\" target=\"_blank\">Activate Now</a>\n" +
//                "                                                </td>\n" +
//                "                                            </tr>\n" +
//                "                                            <!-- ... other content ... -->\n" +
//                "                                        </tbody></table>\n" +
//                "                                    </td>\n" +
//                "                                </tr>\n" +
//                "                                <tr>\n" +
//                "                                    <td valign=\"top\">\n" +
//                "                                        <img src=\"https://www.shapeways.com/rrstatic/img/email/email-rounded-bottom.jpg\" width=\"100%\" style=\"vertical-align:top;\">\n" +
//                "                                    </td>\n" +
//                "                                </tr>\n" +
//                "                            </tbody></table>\n" +
//                "                        </td>\n" +
//                "                    </tr>\n" +
//                "                </tbody></table>\n" +
//                "            </td>\n" +
//                "        </tr>\n" +
//                "    </tbody></table>\n" +
//                "</body>";
//
//        // Send the email with HTML content
//        emailSenderService.sendHtmlEmail(k.getEmail(), "Activate Your Account", htmlContent);
//
//        return ResponseEntity.ok(k);
//    }



    @GetMapping(value = "/confirm", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> confirmEmail(@RequestParam("token") String token) {

        // Retrieve the token from the database
        VerificationToken verificationToken = userService.getVerificationToken(token);
        System.out.println(verificationToken.getUser());
        // If the token is valid, update the user's account
        if (verificationToken != null && !verificationToken.isExpired()) {
            userService.enableUser(verificationToken.getUser());

            return ResponseEntity.ok(verificationToken);
        } else {
            return (ResponseEntity<?>) ResponseEntity.internalServerError();
        }
    }

    @GetMapping("/allusers")
    public List<User> getAllShoes() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getShoesById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }
}

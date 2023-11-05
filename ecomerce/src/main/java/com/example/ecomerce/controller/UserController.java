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
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailSenderService emailSenderService;

//    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<?> registerUser(@RequestBody RegisterDTO registerDTO) {
//        System.out.println(registerDTO.toString());
//        User k = new User(registerDTO);
//        this.userService.save(k);
//        String token = UUID.randomUUID().toString();
//
//        // Save the token, email, and timestamp in the database
//        userService.createVerificationToken(k, token);
//
//        // Send the email with the link
//
//        emailSenderService.sendSimpleEmail(k.getEmail(), token);
//        return ResponseEntity.ok(registerDTO);
//    }



//    @GetMapping(value = "/confirm", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<?> confirmEmail(@RequestParam("token") String token) {
//
//        // Retrieve the token from the database
//        VerificationToken verificationToken = userService.getVerificationToken(token);
//        System.out.println(verificationToken.getUser());
//        // If the token is valid, update the user's account
//        if (verificationToken != null && !verificationToken.isExpired()) {
//            userService.enableUser(verificationToken.getUser());
//
//            return ResponseEntity.ok(verificationToken);
//        } else {
//            return (ResponseEntity<?>) ResponseEntity.internalServerError();
//        }
//    }

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

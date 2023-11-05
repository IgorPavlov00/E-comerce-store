package com.example.ecomerce.service;

import com.example.ecomerce.dto.RegisterDTO;
import com.example.ecomerce.model.User;
import com.example.ecomerce.model.VerificationToken;
//import com.example.ecomerce.repository.TokenRepository;
import com.example.ecomerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

//    @Autowired
//    private TokenRepository tokenRepo;
//
//    public void createVerificationToken(User user, String token) {
//        VerificationToken myToken = new VerificationToken(token, user);
//        tokenRepo.save(myToken);
//    }
//
//    public VerificationToken getVerificationToken(String token) {
//        return tokenRepo.findByToken(token);
//    }
//    public void enableUser(User user) {
//        user.setAktivan(true);
//        this.userRepository.save(user);
//    }
//


    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }


    public void save(User k) {
        this.userRepository.save(k);
    }
}

package com.hritvik.LeanPlatformTechnologiesAssignment.service;


import com.hritvik.LeanPlatformTechnologiesAssignment.model.Role;
import com.hritvik.LeanPlatformTechnologiesAssignment.model.Users;
import com.hritvik.LeanPlatformTechnologiesAssignment.respository.IuserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    IuserRepo userRepository;

    public ResponseEntity<String> registerUser(Users user) throws NoSuchAlgorithmException {
        // Check if the username is already taken
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return new ResponseEntity<>("Username already exists", HttpStatus.BAD_REQUEST);
        }


        // Encrypt the password before saving (you might want to use a more secure method)

        user.setPassword(PasswordEncrypter.encryptPassword(user.getPassword()));

        // saving user details to database

        userRepository.save(user);
        return new ResponseEntity<>("User registered successfully", HttpStatus.OK);
    }

    public List<Users> getAllConsultants(String username) {
        //searching for username in DB

        Optional<Users> user = userRepository.findByUsername(username);
        if(user.isPresent()){
            return userRepository.findByRole(Role.valueOf("Consultant"));
        }

        return null;




    }
}

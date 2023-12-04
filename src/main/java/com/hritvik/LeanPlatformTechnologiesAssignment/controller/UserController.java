package com.hritvik.LeanPlatformTechnologiesAssignment.controller;

import com.hritvik.LeanPlatformTechnologiesAssignment.model.Users;
import com.hritvik.LeanPlatformTechnologiesAssignment.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser( @Valid @RequestBody Users user) throws NoSuchAlgorithmException {
        return userService.registerUser(user);
    }

    @GetMapping("/consultants")
    public ResponseEntity<List<Users>> getAllConsultants(@RequestParam ("username")String username) {
        List<Users> consultants = userService.getAllConsultants(username);

        return new ResponseEntity<>(consultants, HttpStatus.OK);
    }
}

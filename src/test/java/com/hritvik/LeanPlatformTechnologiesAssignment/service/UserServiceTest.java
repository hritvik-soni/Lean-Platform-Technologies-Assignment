package com.hritvik.LeanPlatformTechnologiesAssignment.service;

import com.hritvik.LeanPlatformTechnologiesAssignment.model.Role;
import com.hritvik.LeanPlatformTechnologiesAssignment.model.Users;
import com.hritvik.LeanPlatformTechnologiesAssignment.respository.IuserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private IuserRepo userRepository;

    @InjectMocks
    private UserService userService;

    private Users user;

@BeforeEach
void setUp() {
    MockitoAnnotations.openMocks(this);
}

    @Test
    void registerUser_UsernameAlreadyExists_ReturnsBadRequest() throws NoSuchAlgorithmException {
        // Arrange
        Users user = new Users();
        user.setUsername("existingUser");
        when(userRepository.findByUsername("existingUser")).thenReturn(Optional.of(user));

        // Act
        ResponseEntity<String> response = userService.registerUser(user);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Username already exists", response.getBody());
        verify(userRepository, never()).save(any(Users.class));
    }

    @Test
    void registerUser_NewUser_SuccessfullyRegistered() throws NoSuchAlgorithmException {
        // Arrange
        Users user = new Users();
        user.setUsername("newUser");
        user.setPassword("password");
        when(userRepository.findByUsername("newUser")).thenReturn(Optional.empty());

        // Act
        ResponseEntity<String> response = userService.registerUser(user);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User registered successfully", response.getBody());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void getAllConsultants_UserExists_ReturnsConsultants() {
        // Arrange
        String username = "existingUser";
        Users user = new Users();
        user.setUsername(username);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        List<Users> consultants = new ArrayList<>();
        consultants.add(new Users("consultant1", "password", Role.Consultant));
        consultants.add(new Users("consultant2", "password", Role.Consultant));
        when(userRepository.findByRole(Role.Consultant)).thenReturn(consultants);

        // Act
        List<Users> result = userService.getAllConsultants(username);

        // Assert
        assertEquals(consultants, result);
    }

    @Test
    void getAllConsultants_UserDoesNotExist_ReturnsNull() {
        // Arrange
        String username = "nonExistingUser";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Act
        List<Users> result = userService.getAllConsultants(username);

        // Assert
        assertNull(result);
        verify(userRepository, never()).findByRole(Role.Consultant);
    }
}



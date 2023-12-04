package com.hritvik.LeanPlatformTechnologiesAssignment.service;

import com.hritvik.LeanPlatformTechnologiesAssignment.model.Users;
import com.hritvik.LeanPlatformTechnologiesAssignment.respository.IuserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
        user = new Users();
        user.setUsername("testUser");
        user.setPassword("password");
        user.setEmail("test@example.com");
    }

    @Test
    @DisplayName("Test registerUser when username taken taken then success")
    void testRegisterUserWhenUsernameNotTakenThenSuccess() throws NoSuchAlgorithmException {
        // Arrange
//        when(userRepository.findByUsername(user.getUsername())).thenReturn(null);
        when(userRepository.save(user)).thenReturn(user);

        // Act
        ResponseEntity<String> response = userService.registerUser(user);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User registered successfully", response.getBody());
        verify(userRepository).save(user);
    }

    @Test
    @DisplayName("Test registerUser when username taken then failure")
    void testRegisterUserWhenUsernameTakenThenFailure() throws NoSuchAlgorithmException {
        // Arrange
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        // Act
        ResponseEntity<String> response = userService.registerUser(user);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Username already exists", response.getBody());
        verify(userRepository, never()).save(user);
    }

    @Test
    @DisplayName("Test getAllConsultants then success")
    void testGetAllConsultantsThenSuccess() {
        // Arrange
        Users consultant = new Users();
        consultant.setRole("Consultant");
        List<Users> consultants = List.of(consultant);
        when(userRepository.findByRole("Consultant")).thenReturn(consultants);

        // Act
        List<Users> result = userService.getAllConsultants("anyUsername");

        // Assert
        assertEquals(consultants, result);
        verify(userRepository).findByRole("Consultant");
    }
}

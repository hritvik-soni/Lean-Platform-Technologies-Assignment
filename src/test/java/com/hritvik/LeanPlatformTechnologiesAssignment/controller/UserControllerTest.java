package com.hritvik.LeanPlatformTechnologiesAssignment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hritvik.LeanPlatformTechnologiesAssignment.model.Users;
import com.hritvik.LeanPlatformTechnologiesAssignment.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testRegisterUserWhenUserIsRegisteredThenReturnSuccess() throws Exception {
        Users user = new Users("testUser");
        String successMessage = "User registered successfully";
        Mockito.when(userService.registerUser(Mockito.any(Users.class)))
                .thenReturn(new ResponseEntity<>(successMessage, HttpStatus.OK));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(content().string(successMessage));
    }


    @Test
    public void testGetAllConsultantsWhenConsultantsAvailableThenReturnList() throws Exception {
        Users consultant = new Users("consultantUser");
        List<Users> consultants = Collections.singletonList(consultant);
        Mockito.when(userService.getAllConsultants(Mockito.anyString()))
                .thenReturn(consultants);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/consultants")
                .param("username", "consultantUser"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(consultants)));
    }

    @Test
    public void testGetAllConsultantsWhenNoConsultantsAvailableThenReturnNull() throws Exception {
        Mockito.when(userService.getAllConsultants(Mockito.anyString()))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/consultants")
                .param("username", "nonexistentUser"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(""));
    }
}

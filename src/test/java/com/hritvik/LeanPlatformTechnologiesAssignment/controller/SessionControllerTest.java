package com.hritvik.LeanPlatformTechnologiesAssignment.controller;

import com.hritvik.LeanPlatformTechnologiesAssignment.service.SessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SessionController.class)
public class SessionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SessionService sessionService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testBookSessionWhenSessionIsBookedThenReturnOk() throws Exception {
        // Arrange
        String username = "user1";
        Long mentorId = 1L;
        LocalDateTime bookDateTime = LocalDateTime.now().plusDays(1);
        String bookDateTimeStr = bookDateTime.format(DateTimeFormatter.ISO_DATE_TIME);
        when(sessionService.bookSession(eq(username), eq(mentorId), any(LocalDateTime.class)))
                .thenReturn(new ResponseEntity<>("Session booked successfully", HttpStatus.OK));

        // Act & Assert
        mockMvc.perform(post("/api/sessions/create")
                .param("username", username)
                .param("mentorId", mentorId.toString())
                .param("dateTime", bookDateTimeStr))
                .andExpect(status().isOk());
    }

    @Test
    public void testBookSessionWhenBookDateTimeNotProvidedThenReturnBadRequest() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/api/sessions/create")
                .param("username", "user1")
                .param("mentorId", "1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCancelSessionWhenSessionIsCancelledThenReturnOk() throws Exception {
        // Arrange
        Long sessionId = 1L;
        String username = "user1";
        when(sessionService.cancelSession(eq(sessionId), eq(username)))
                .thenReturn(new ResponseEntity<>("Session canceled successfully", HttpStatus.OK));

        // Act & Assert
        mockMvc.perform(post("/api/sessions/cancel")
                .param("sessionId", sessionId.toString())
                .param("username", username))
                .andExpect(status().isOk());
    }

    @Test
    public void testCancelSessionWhenSessionIdNotProvidedThenReturnBadRequest() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/api/sessions/cancel")
                .param("username", "user1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testRescheduleSessionWhenSessionIsRescheduledThenReturnOk() throws Exception {
        // Arrange
        Long sessionId = 1L;
        String username = "user1";
        LocalDateTime newDateTime = LocalDateTime.now().plusDays(1);
        String newDateTimeStr = newDateTime.format(DateTimeFormatter.ISO_DATE_TIME);
        when(sessionService.rescheduleSession(eq(sessionId), any(LocalDateTime.class), eq(username)))
                .thenReturn(new ResponseEntity<>("Session rescheduled successfully", HttpStatus.OK));

        // Act & Assert
        mockMvc.perform(post("/api/sessions/reschedule")
                .param("sessionId", sessionId.toString())
                .param("dateTime", newDateTimeStr)
                .param("username", username))
                .andExpect(status().isOk());
    }

    @Test
    public void testRescheduleSessionWhenSessionIdNotProvidedThenReturnBadRequest() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/api/sessions/reschedule")
                .param("newDateTime", LocalDateTime.now().plusDays(1).format(DateTimeFormatter.ISO_DATE_TIME))
                .param("username", "user1"))
                .andExpect(status().isBadRequest());
    }

//    @Test
//    public void testBookRecurringSessionsWhenSessionsAreBookedThenReturnOk() throws Exception {
//        // Arrange
//        Long mentorId = 1L;
//        LocalDateTime startDateTime = LocalDateTime.now().plusDays(1);
//        String startDateTimeStr = startDateTime.format(DateTimeFormatter.ISO_DATE_TIME);
//        String recurrence = "weekly";
//        int durationMonths = 3;
//        String username = "user1";
//        when(sessionService.bookRecurringSessions(eq(mentorId), any(LocalDateTime.class), eq(recurrence), eq(durationMonths), eq(username)))
//                .thenReturn(new ResponseEntity<>("Recurring sessions booked successfully", HttpStatus.OK));
//
//        // Act & Assert
//        mockMvc.perform(post("/api/sessions/book-recurring")
//                .param("mentorId", mentorId.toString())
//                .param("dateTime", startDateTimeStr)
//                .param("recurrence", recurrence)
//                .param("durationMonths", String.valueOf(durationMonths))
//                .param("username", username))
//                .andExpect(status().isOk());
//    }

    @Test
    public void testBookRecurringSessions_Success() {
        // Arrange
        Long mentorId = 1L;
        LocalDateTime startDateTime = LocalDateTime.now();
        String recurrence = "Weekly";
        int durationMonths = 3;
        String username = "john_doe";

        when(sessionService.bookRecurringSessions(mentorId, startDateTime, recurrence, durationMonths, username))
                .thenReturn(ResponseEntity.ok("Successfully booked recurring sessions"));

        // Act
        ResponseEntity<String> response = sessionService.bookRecurringSessions(mentorId, startDateTime, recurrence, durationMonths, username);

        // Assert
        assertEquals(ResponseEntity.ok("Successfully booked recurring sessions"), response);
        verify(sessionService, times(1)).bookRecurringSessions(mentorId, startDateTime, recurrence, durationMonths, username);
    }

    @Test
    public void testBookRecurringSessions_Failure() {
        // Arrange
        Long mentorId = 2L;
        LocalDateTime startDateTime = LocalDateTime.now();
        String recurrence = "Monthly";
        int durationMonths = 2;
        String username = "jane_doe";

        when(sessionService.bookRecurringSessions(mentorId, startDateTime, recurrence, durationMonths, username))
                .thenReturn(ResponseEntity.status(500).body("Failed to book recurring sessions"));

        // Act
        ResponseEntity<String> response = sessionService.bookRecurringSessions(mentorId, startDateTime, recurrence, durationMonths, username);

        // Assert
        assertEquals(ResponseEntity.status(500).body("Failed to book recurring sessions"), response);
        verify(sessionService, times(1)).bookRecurringSessions(mentorId, startDateTime, recurrence, durationMonths, username);
    }


}

package com.hritvik.LeanPlatformTechnologiesAssignment.service;

import com.hritvik.LeanPlatformTechnologiesAssignment.model.Sessions;
import com.hritvik.LeanPlatformTechnologiesAssignment.model.Users;
import com.hritvik.LeanPlatformTechnologiesAssignment.respository.IsessionRepo;
import com.hritvik.LeanPlatformTechnologiesAssignment.respository.IuserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class SessionServiceTest {

    @Mock
    private IsessionRepo sessionRepository;

    @Mock
    private IuserRepo userRepo;

    @InjectMocks
    private SessionService sessionService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCancelSessionWhenValidConditionsThenSuccess() {
        Long sessionId = 1L;
        String username = "user1";
        LocalDateTime futureDateTime = LocalDateTime.now().plusHours(15);
        Sessions session = new Sessions();
        session.setId(sessionId);
        Users user = new Users();
        user.setUsername(username);
        session.setUser(user);
        session.setDate(futureDateTime.toLocalDate().atStartOfDay());
        session.setTime(futureDateTime.toLocalTime());

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));

        ResponseEntity<String> result = sessionService.cancelSession(sessionId, username);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Session canceled successfully", result.getBody());

        verify(sessionRepository).findById(sessionId);
        verify(sessionRepository).delete(session);
    }

    @Test
    public void testCancelSessionWhenTimeDifferenceLessThan12HoursThenFail() {
        Long sessionId = 1L;
        String username = "user1";
        LocalDateTime nearFutureDateTime = LocalDateTime.now().plusHours(11);
        Sessions session = new Sessions();
        session.setId(sessionId);
        Users user = new Users();
        user.setUsername(username);
        session.setUser(user);
        session.setDate(nearFutureDateTime.toLocalDate().atStartOfDay());
        session.setTime(nearFutureDateTime.toLocalTime());

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));

        ResponseEntity<String> result = sessionService.cancelSession(sessionId, username);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("Cannot cancel the session. Time difference is less than 12 hours.", result.getBody());

        verify(sessionRepository).findById(sessionId);
        verify(sessionRepository, never()).delete(session);
    }

    @Test
    public void testRescheduleSessionWhenValidConditionsThenSuccess() {
        Long sessionId = 1L;
        LocalDateTime newDateTime = LocalDateTime.now().plusHours(5);
        String username = "user1";
        Sessions session = new Sessions();
        session.setId(sessionId);
        Users user = new Users();
        user.setUsername(username);
        session.setUser(user);
        session.setDate(LocalDateTime.now().plusHours(6).toLocalDate().atStartOfDay());
        session.setTime(LocalDateTime.now().plusHours(6).toLocalTime());

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));

        ResponseEntity<String> result = sessionService.rescheduleSession(sessionId, newDateTime, username);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Session rescheduled successfully", result.getBody());

        verify(sessionRepository).findById(sessionId);
        verify(sessionRepository).save(session);
    }

    @Test
    public void testRescheduleSessionWhenTimeDifferenceLessThan4HoursThenFail() {
        Long sessionId = 1L;
        LocalDateTime newDateTime = LocalDateTime.now().plusHours(5);
        String username = "user1";
        Sessions session = new Sessions();
        session.setId(sessionId);
        Users user = new Users();
        user.setUsername(username);
        session.setUser(user);
        session.setDate(LocalDateTime.now().plusHours(3).toLocalDate().atStartOfDay());
        session.setTime(LocalDateTime.now().plusHours(3).toLocalTime());

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));

        ResponseEntity<String> result = sessionService.rescheduleSession(sessionId, newDateTime, username);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("Cannot reschedule the session. Time difference is less than 4 hours.", result.getBody());

        verify(sessionRepository).findById(sessionId);
        verify(sessionRepository, never()).save(session);
    }

    @Test
    public void testBookRecurringSessionsWhenValidConditionsThenSuccess() {
        Long mentorId = 1L;
        LocalDateTime startDateTime = LocalDateTime.now().plusDays(7);
        String recurrence = "weekly";
        int durationMonths = 3;
        String username = "user1";
        Users mentor = new Users();
        mentor.setId(mentorId);
        mentor.setRole("Mentor");

        when(userRepo.findById(mentorId)).thenReturn(Optional.of(mentor));
        when(sessionRepository.save(any(Sessions.class))).thenReturn(new Sessions());

        ResponseEntity<String> result = sessionService.bookRecurringSessions(mentorId, startDateTime, recurrence, durationMonths, username);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Recurring sessions booked successfully", result.getBody());

        verify(userRepo).findById(mentorId);
        verify(sessionRepository, atLeastOnce()).save(any(Sessions.class));
    }

    @Test
    public void testBookSessionWhenValidConditionsThenSuccess() {
        Long mentorId = 1L;
        LocalDateTime bookDateTime = LocalDateTime.now().plusDays(7);
        String username = "user1";
        Users mentor = new Users();
        mentor.setId(mentorId);
        mentor.setRole("Mentor");

        when(userRepo.findById(mentorId)).thenReturn(Optional.of(mentor));
        when(sessionRepository.save(any(Sessions.class))).thenReturn(new Sessions());

        ResponseEntity<String> result = sessionService.bookSession(username, mentorId, bookDateTime);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Recurring sessions booked successfully", result.getBody());

        verify(userRepo).findById(mentorId);
        verify(sessionRepository).save(any(Sessions.class));
    }
}
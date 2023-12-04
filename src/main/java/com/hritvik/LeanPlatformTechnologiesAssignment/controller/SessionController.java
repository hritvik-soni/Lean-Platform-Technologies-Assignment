package com.hritvik.LeanPlatformTechnologiesAssignment.controller;


import com.hritvik.LeanPlatformTechnologiesAssignment.model.Users;
import com.hritvik.LeanPlatformTechnologiesAssignment.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/sessions")
public class SessionController {
    @Autowired
    private SessionService sessionService;

    @PostMapping("/create")
    public ResponseEntity<String> bookSession(@RequestParam ("username") String username,
                                              @RequestParam("mentorId") Long mentorId,
                                              @RequestParam ("dateTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime bookDateTime) {
        return sessionService.bookSession(username,mentorId,bookDateTime);
    }

    @PostMapping("/cancel")
    public ResponseEntity<String> cancelSession(@RequestParam("sessionId") Long sessionId,
                                                @RequestParam("username") String username) {
        return sessionService.cancelSession(sessionId, username);
    }


    @PostMapping("/reschedule")
    public ResponseEntity<String> rescheduleSession(@RequestParam("sessionId") Long sessionId,
                                                    @RequestParam("dateTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime newDateTime,
                                                    @RequestParam("username") String username) {
        return sessionService.rescheduleSession(sessionId, newDateTime, username);
    }

    @PostMapping("/book-recurring")
    public ResponseEntity<String> bookRecurringSessions(@RequestParam("mentorId") Long mentorId,
                                                        @RequestParam("dateTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDateTime,
                                                        @RequestParam ("recurrence")String recurrence,
                                                        @RequestParam ("duration")int durationMonths,
                                                        @RequestParam ("username")String username) {
        return sessionService.bookRecurringSessions(mentorId, startDateTime, recurrence, durationMonths, username);
    }
}

package com.hritvik.LeanPlatformTechnologiesAssignment.service;

import com.hritvik.LeanPlatformTechnologiesAssignment.model.Sessions;
import com.hritvik.LeanPlatformTechnologiesAssignment.model.Users;
import com.hritvik.LeanPlatformTechnologiesAssignment.respository.IsessionRepo;
import com.hritvik.LeanPlatformTechnologiesAssignment.respository.IuserRepo;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.Optional;

@Service

public class SessionService {

    @Autowired

    private IsessionRepo sessionRepository;

    @Autowired
    private IuserRepo userRepo;

    public ResponseEntity<String> cancelSession(Long sessionId, String username) {
        Optional<Sessions> optionalSession = sessionRepository.findById(sessionId);

        if (optionalSession.isPresent()) {
            Sessions session = optionalSession.get();

            // Check if the session belongs to the user requesting cancellation
            if (session.getUser().getUsername().equals(username)) {

                LocalDateTime currentTime = LocalDateTime.now();
                LocalDateTime sessionTime = LocalDateTime.of(LocalDate.from(session.getDate()), session.getTime());

                // Check if the time difference is greater than 12 hours
                long hoursDifference = ChronoUnit.HOURS.between(currentTime, sessionTime);

                if (hoursDifference > 12) {
                    // Perform the cancellation logic (you can update the status, delete the record, etc.)
                    sessionRepository.delete(session);
                    return new ResponseEntity<>("Session canceled successfully", HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("Cannot cancel the session. Time difference is less than 12 hours.", HttpStatus.BAD_REQUEST);
                }
            } else {
                return new ResponseEntity<>("Unauthorized access to cancel the session.", HttpStatus.UNAUTHORIZED);
            }
        } else {
            return new ResponseEntity<>("Session not found.", HttpStatus.NOT_FOUND);
        }
    }


    public ResponseEntity<String> rescheduleSession(Long sessionId, LocalDateTime newDateTime, String username) {
        Optional<Sessions> optionalSession = sessionRepository.findById(sessionId);

        if (optionalSession.isPresent()) {
            Sessions session = optionalSession.get();

            // Check if the session belongs to the user requesting reschedule
            if (session.getUser().getUsername().equals(username)) {

                LocalDateTime currentTime = LocalDateTime.now();
                LocalDateTime sessionTime = LocalDateTime.of(LocalDate.from(session.getDate()), session.getTime());

                // Check if the time difference is greater than 4 hours
                long hoursDifference = ChronoUnit.HOURS.between(currentTime, sessionTime);

                if (hoursDifference > 4) {
                    // Perform the rescheduling logic (update the session time)
                    session.setDate(newDateTime.toLocalDate().atStartOfDay());
                    session.setTime(newDateTime.toLocalTime());
                    sessionRepository.save(session);
                    return new ResponseEntity<>("Session rescheduled successfully", HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("Cannot reschedule the session. Time difference is less than 4 hours.", HttpStatus.BAD_REQUEST);
                }
            } else {
                return new ResponseEntity<>("Unauthorized access to reschedule the session.", HttpStatus.UNAUTHORIZED);
            }
        } else {
            return new ResponseEntity<>("Session not found.", HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<String> bookRecurringSessions(Long mentorId, LocalDateTime startDateTime, String recurrence, int durationMonths, String username) {

        if(durationMonths<1 || durationMonths>3){
            return new ResponseEntity<>("Cannot book recurring sessions for less than 1 month or more than 3 months.", HttpStatus.BAD_REQUEST);
        }


        Optional<Users> optionalMentor = userRepo.findById(mentorId);
        Optional<Users> user = userRepo.findByUsername(username);

        if (optionalMentor.isPresent()) {
            Users mentor = optionalMentor.get();
            if(user.isPresent() && Objects.equals(user.get().getId(), mentorId)){
                return  new ResponseEntity<>("Cannot book recurring sessions Invalid Id", HttpStatus.BAD_REQUEST);
            }
            if(user.isPresent() && user.get().getRole().toString().equals("Consultant")){
                return  new ResponseEntity<>("Cannot book recurring sessions for Consultant", HttpStatus.BAD_REQUEST);
            }


//             Check if the user requesting the booking is a client
            if (mentor.getRole().toString().equalsIgnoreCase("Client")) {
                return new ResponseEntity<>("Cannot book recurring sessions with a Client.", HttpStatus.BAD_REQUEST);
            }

            LocalDateTime currentDateTime = LocalDateTime.now();

            // Initialize the booking date
            LocalDateTime bookingDateTime = startDateTime;

            // Calculate the duration in weeks
            long weeks = Duration.between(currentDateTime, bookingDateTime).toDays() / 7;

            // Check if the booking date is in the future
            if (weeks >= 0) {
                // Book the initial session
                Sessions session = createSession(username, mentor, bookingDateTime);

                sessionRepository.save(session);

                // Book additional sessions based on the recurrence and duration
                for (int i = 1; i < durationMonths * 4; i++) {  // Assuming 4 weeks in a month
                    if (recurrence.equalsIgnoreCase("weekly")) {
                        bookingDateTime = bookingDateTime.plusWeeks(1);
                    } else if (recurrence.equalsIgnoreCase("bi-weekly")) {
                        bookingDateTime = bookingDateTime.plusWeeks(2);
                    }

                    // Check if the booking date is still within the specified duration
                    if (Duration.between(currentDateTime, bookingDateTime).toDays() / 7 < durationMonths * 4L) {
                        session = createSession(username, mentor, bookingDateTime);
                        sessionRepository.save(session);
                    }
                }

                return new ResponseEntity<>("Recurring sessions booked successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Cannot book recurring sessions in the past.", HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>("Mentor not found.", HttpStatus.NOT_FOUND);
        }
    }

    private Sessions createSession(String username, Users mentor, LocalDateTime bookingDateTime) {
        Sessions session= new Sessions();
        session.setUser(userRepo.findByUsername(username).orElse(null));
        session.setMentor(mentor);
        session.setDate(bookingDateTime.toLocalDate().atStartOfDay());
        session.setTime(bookingDateTime.toLocalTime());
        session.setBookedAt(LocalDateTime.now());
        return session;
    }

    public ResponseEntity<String> bookSession(String username, Long mentorId, LocalDateTime bookDateTime) {
        Optional<Users> optionalMentor = userRepo.findById(mentorId);
        Optional<Users> user = userRepo.findByUsername(username);

        if (optionalMentor.isPresent()) {
            Users mentor = optionalMentor.get();

            if(user.isPresent() && Objects.equals(user.get().getId(), mentorId)){
                return  new ResponseEntity<>("Cannot book recurring sessions Invalid Id", HttpStatus.BAD_REQUEST);
            }
            if(user.isPresent() && user.get().getRole().toString().equals("Consultant")){
                return  new ResponseEntity<>("Cannot book recurring sessions for Consultant", HttpStatus.BAD_REQUEST);
            }

//             Check if the user requesting the booking is a client
            if (mentor.getRole().toString().equalsIgnoreCase("Client")) {
                return new ResponseEntity<>("Cannot book recurring sessions with a client.", HttpStatus.BAD_REQUEST);
            }

            LocalDateTime currentDateTime = LocalDateTime.now();


            // Calculate the duration in weeks
            long weeks = Duration.between(currentDateTime, bookDateTime).toDays() / 7;

            if (weeks >= 0) {
                // Book the initial session
                Sessions session = createSession(username, mentor, bookDateTime);
                sessionRepository.save(session);
                return new ResponseEntity<>("Recurring sessions booked successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Cannot book recurring sessions in the past.", HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>("Mentor not found.", HttpStatus.NOT_FOUND);
        }
    }
}



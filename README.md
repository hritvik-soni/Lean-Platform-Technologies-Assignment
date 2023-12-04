# Sessions Management Backend APIs

## Overview

This repository contains the backend code for a web application that facilitates users in finding and booking sessions with consultants. The necessary APIs for user registration, consultant search, and session booking have been implemented. The application includes features such as canceling, rescheduling, and booking recurring sessions with a mentor based on user preferences.

## Features

### 1. Cancel Session

Users can cancel a booked session with a mentor. However, cancellation is only allowed if the time difference between the current time and the session time is greater than 12 hours.

- **Endpoint:** `/api/sessions/cancel`
- **Method:** `POST`
- **Parameters:**
  - `userId`: ID of the user canceling the session.
  - `meetingId`: ID of the session/meeting to be canceled.

### 2. Reschedule Session

Users can reschedule a booked session with a mentor, provided that the time difference between the current time and the session time is greater than 4 hours.

- **Endpoint:** `/api/sessions/reschedule`
- **Method:** `POST`
- **Parameters:**
  - `userId`: ID of the user rescheduling the session.
  - `meetingId`: ID of the session/meeting to be rescheduled.
  - `newDate`: New date for the session.
  - `newTime`: New time for the session.

### 3. API for Booking Recurring Sessions

Users can book sessions with a mentor for recurring intervals based on user-specified preferences.

- **Endpoint:** `/api/sessions/book-recurring`
- **Method:** `POST`
- **Parameters:**
  - `userId`: ID of the user booking recurring sessions.
  - `mentorId`: ID of the mentor.
  - `frequency`: Frequency of sessions (e.g., once per week).
  - `duration`: Duration of recurring sessions (e.g., 1 month to 3 months).

### 4. Tables

The application includes a table for storing meeting/session information.

- **Table Name:** `sessions`

  - `userId`: ID of the user.
  - `mentorId`: ID of the mentor.
  - `date`: Date of the session.
  - `time`: Time of the session.
  - `bookedAt`: Timestamp when the session was booked.

- **Table Name:** `users`
  - `userId`: ID of the user.
  - `username`: Name of the user.
  - `email`: Email of the user.
  - `password`: Password of the user.
  - `role`: role of the user( Client or Consultant ).

## Technical Requirements

- **Backend Language:** Java
- **Framework:** Spring Boot
- **Database:** In-memory database or a simple file-based storage system
- **Documentation:** Clear, readable, and well-documented code
- **Testing:** API functionality can be demonstrated using Postman (Postman collections file is also added in project file).

## Getting Started

1. Clone the repository.
2. Open the project in your preferred Java IDE.
3. Build and run the project.
4. Test the APIs using Postman or any API testing tool.

## API Usage

Refer to the API documentation for details on using each endpoint. The documentation can be found in the code comments or an external documentation file.

##

Happy coding!

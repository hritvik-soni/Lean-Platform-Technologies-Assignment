package com.hritvik.LeanPlatformTechnologiesAssignment.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty
    private String username;
    @Email
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role; // 'Client' or 'Consultant'


    public Users(String testUser) {
    }

    public Users(String consultant1, String password, Role role) {
    }
}

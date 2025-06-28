package com.masjidlink.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {
    private String token;
    private String type = "Bearer"; // Standard type for JWT
    private Long id;
    private String username;
    private String email;
    private String role; // Send back the user's role
}
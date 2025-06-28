package com.masjidlink.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginSuccessResponse {
    private Long id;
    private String username;
    private String email;
    private String role;
    // No token, as authentication is now cookie/session based
}
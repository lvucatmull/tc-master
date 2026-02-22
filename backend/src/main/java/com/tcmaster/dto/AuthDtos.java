package com.tcmaster.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class AuthDtos {

    public record RegisterRequest(
            @Email String email,
            @NotBlank String password,
            @NotBlank String name
    ) {}

    public record LoginRequest(
            @Email String email,
            @NotBlank String password
    ) {}

    public record AuthResponse(String accessToken) {}
}

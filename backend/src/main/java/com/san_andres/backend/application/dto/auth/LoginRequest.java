package com.san_andres.backend.application.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank(message = "Email is required.")
    @Email(message = "The email format is not valid.")
    private String login;

    @NotBlank(message = "Password is required.")
    private String password;
}
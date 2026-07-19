package com.san_andres.backend.auth.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank(message = "El usuario o correo es obligatorio")
    private String login;

    @NotBlank(message = "La contraseña es obligatoria")
    private String password;
}
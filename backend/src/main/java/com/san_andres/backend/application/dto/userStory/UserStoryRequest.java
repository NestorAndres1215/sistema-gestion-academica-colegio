package com.san_andres.backend.application.dto.userStory;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserStoryRequest {

    @NotBlank(message = "La acción es obligatoria")
    private String action;

    @NotBlank(message = "El detalle es obligatorio")
    private String detail;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El formato del correo no es válido")
    private String email;
}
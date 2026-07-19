package com.san_andres.backend.company.application.dto.request;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;

@Data
@ToString
public class CompanyRequest {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String name;

    @NotBlank(message = "El RUC es obligatorio")
    @Pattern(regexp = "^[0-9]{11}$", message = "El RUC debe tener exactamente 11 dígitos")
    private String ruc;

    @NotBlank(message = "La descripción es obligatoria")
    @Size(max = 500, message = "La descripción no debe exceder 500 caracteres")
    private String description;

    @NotBlank(message = "El número de teléfono es obligatorio")
    @Pattern(regexp = "^[0-9+\\- ]{7,15}$", message = "El número de teléfono no es válido")
    private String phone;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El formato del correo no es válido")
    private String email;

    @NotBlank(message = "La dirección es obligatoria")
    @Size(max = 200, message = "La dirección no debe exceder 200 caracteres")
    private String address;

    @NotBlank(message = "La ciudad es obligatoria")
    private String city;

    @NotBlank(message = "El país es obligatorio")
    private String country;

    @NotBlank(message = "El tipo de empresa es obligatorio")
    private String companyType;

    @NotNull(message = "La fecha de fundación es obligatoria")
    @Past(message = "La fecha de fundación debe ser en el pasado")
    private LocalDate foundingDate;

}
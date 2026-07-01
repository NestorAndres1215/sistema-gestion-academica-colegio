package com.san_andres.backend.application.dto.company;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;

@Data
@ToString
public class CompanyRequest {

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "The name must be between 2 and 100 characters")
    private String name;


    @NotBlank(message = "RUC is required")
    @Pattern(regexp = "^[0-9]{11}$", message = "The RUC must have exactly 11 digits")
    private String ruc;

    @NotBlank(message = "Description is required")
    @Size(max = 500, message = "The description must not exceed 500 characters")
    private String description;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[0-9+\\- ]{7,15}$", message = "Invalid phone number")
    private String phone;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Address is required")
    @Size(max = 200, message = "The address must not exceed 200 characters")
    private String address;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "Country is required")
    private String country;

    @NotBlank(message = "Company type is required")
    private String companyType;

    @NotNull(message = "Founding date is required")
    @Past(message = "The founding date must be in the past")
    private LocalDate foundingDate;
}
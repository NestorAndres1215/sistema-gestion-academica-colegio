package com.san_andres.backend.company.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Company {

    private Long id;
    private String code;
    private String name;
    private String logo;
    private String ruc;
    private String description;
    private String phone;
    private String email;
    private String address;
    private String city;
    private String country;
    private String companyType;
    private LocalDate foundingDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
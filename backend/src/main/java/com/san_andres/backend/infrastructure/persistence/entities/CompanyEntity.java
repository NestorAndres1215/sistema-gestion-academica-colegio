package com.san_andres.backend.infrastructure.persistence.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "company")
public class CompanyEntity {

    @Id
    @Column(length = 8)
    private String id;

    @Column(nullable = false, unique = true , length = 150)
    private String name;

    private String logo;

    @Column(unique = true, length = 11)
    private String ruc;
    private String description;

    @Column(length = 20)
    private String phone;

    @Column(unique = true, nullable = false)
    private String email;

    private String address;

    @Column(length = 100)
    private String city;

    @Column(length = 100)
    private String country;

    @Column(name = "company_type")
    private String companyType;

    @Column(name = "founding_date")
    private LocalDate foundingDate;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
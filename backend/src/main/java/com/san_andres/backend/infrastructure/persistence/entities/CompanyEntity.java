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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 10)
    private String code;

    @Column(nullable = false, unique = true , length = 150)
    private String name;

    private String logo;

    @Column(unique = true, length = 11)
    private String ruc;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 9,unique = true, nullable = false )
    private String phone;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(length = 100)
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
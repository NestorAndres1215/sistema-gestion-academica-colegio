package com.san_andres.backend.infrastructure.persistence.entities;

import com.san_andres.backend.domain.enums.Gender;
import com.san_andres.backend.domain.enums.UserStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "administrator")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "paternal_last_name")
    private String paternalLastName;

    @Column(name = "maternal_last_name")
    private String maternalLastName;

    @Column(unique = true, nullable = false)
    private String dni;

    @Column(unique = true, nullable = false, length = 9)
    private String phone;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    private String profile;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    private String nationality;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private UserStatus status;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity userEntity;

}

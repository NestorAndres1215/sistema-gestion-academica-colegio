package com.san_andres.backend.domain.models;

import com.san_andres.backend.domain.enums.Gender;
import com.san_andres.backend.domain.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Admin {

    private Long id;
    private String firstName;
    private String middleName;
    private String paternalLastName;
    private String maternalLastName;
    private String dni;
    private String phone;
    private LocalDate birthDate;
    private String profile;
    private Gender gender;
    private String nationality;
    private UserStatus status;
    private User user;
}

package com.san_andres.backend.application.dto.admin;

import com.san_andres.backend.domain.enums.Gender;
import com.san_andres.backend.domain.enums.UserStatus;
import lombok.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@Builder
public class AdminResponse {
    private Long id;
    private String email;
    private String username;
    private String firstName;
    private String middleName;
    private String paternalLastName;
    private String maternalLastName;
    private String dni;
    private String phone;
    private LocalDate birthDate;
    private int age;
    private String profile;
    private String gender;
    private String nationality;
    private String status;
    private String role;

}

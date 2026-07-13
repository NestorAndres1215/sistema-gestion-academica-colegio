package com.san_andres.backend.infrastructure.persistence.mapper;

import com.san_andres.backend.application.dto.admin.AdminResponse;
import com.san_andres.backend.domain.models.Admin;
import com.san_andres.backend.infrastructure.persistence.entities.AdminEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AdminMapper {

    private final UserMapper userMapper;

    public Admin toDomain(AdminEntity entity) {
        if (entity == null) return null;

        return Admin.builder()
                .id(entity.getId())
                .firstName(entity.getFirstName())
                .middleName(entity.getMiddleName())
                .paternalLastName(entity.getPaternalLastName())
                .maternalLastName(entity.getMaternalLastName())
                .dni(entity.getDni())
                .phone(entity.getPhone())
                .birthDate(entity.getBirthDate())
                .profile(entity.getProfile())
                .gender(entity.getGender())
                .nationality(entity.getNationality())
                .status(entity.getStatus())
                .user(userMapper.toDomain(entity.getUserEntity()))
                .build();
    }

    public AdminEntity toEntity(Admin domain) {
        if (domain == null) return null;

        return AdminEntity.builder()
                .id(domain.getId())
                .firstName(domain.getFirstName())
                .middleName(domain.getMiddleName())
                .paternalLastName(domain.getPaternalLastName())
                .maternalLastName(domain.getMaternalLastName())
                .dni(domain.getDni())
                .phone(domain.getPhone())
                .birthDate(domain.getBirthDate())
                .profile(domain.getProfile())
                .gender(domain.getGender())
                .nationality(domain.getNationality())
                .status(domain.getStatus())
                .userEntity(userMapper.toEntity(domain.getUser()))
                .build();
    }

    public AdminResponse toResponse(AdminEntity entity) {
        if (entity == null) return null;

        var user = entity.getUserEntity();

        return AdminResponse.builder()
                .id(entity.getId())
                .email(user != null ? user.getEmail() : null)
                .username(user != null ? user.getUsername() : null)
                .firstName(entity.getFirstName())
                .middleName(entity.getMiddleName())
                .paternalLastName(entity.getPaternalLastName())
                .maternalLastName(entity.getMaternalLastName())
                .dni(entity.getDni())
                .phone(entity.getPhone())
                .birthDate(entity.getBirthDate())
                .age(calculateAge(entity.getBirthDate()))
                .profile(entity.getProfile())
                .gender(entity.getGender())
                .nationality(entity.getNationality())
                .status(entity.getStatus())
                .role(extractRoleName(user != null ? user.getRoles() : null))
                .build();
    }

    private int calculateAge(LocalDate birthDate) {
        if (birthDate == null) return 0;
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    private String extractRoleName(List<?> roles) {
        if (roles == null || roles.isEmpty()) return null;

        Object role = roles.get(0);

        try {
            return (String) role.getClass()
                    .getMethod("getName")
                    .invoke(role);
        } catch (Exception e) {
            return null;
        }
    }
}
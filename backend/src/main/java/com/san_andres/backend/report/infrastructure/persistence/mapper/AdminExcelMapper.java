package com.san_andres.backend.report.infrastructure.persistence.mapper;

import com.san_andres.backend.admin.application.dto.request.AdminRequest;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class AdminExcelMapper {

    public AdminRequest toRequest(List<String> row) {

        AdminRequest request = new AdminRequest();

        request.setEmail(row.get(0));
        request.setUsername(row.get(1));
        request.setFirstName(row.get(2));
        request.setMiddleName(row.get(3));
        request.setPaternalLastName(row.get(4));
        request.setMaternalLastName(row.get(5));
        request.setDni(row.get(6));
        request.setPhone(row.get(7));
        request.setBirthDate(parseDate(row.get(8)));
        request.setGender(row.get(9));
        request.setNationality(row.get(10));
        request.setPassword(generatePassword(request.getPaternalLastName(), request.getDni()));
        return request;
    }

    private String generatePassword(String lastname, String dni) {
        return lastname.substring(0, 1).toUpperCase() + dni;
    }

    private LocalDate parseDate(String value) {

        if (value == null || value.isBlank()) {
            return null;
        }

        List<DateTimeFormatter> formats = List.of(
                DateTimeFormatter.ofPattern("M/d/yy"),
                DateTimeFormatter.ofPattern("M/d/yyyy"),
                DateTimeFormatter.ofPattern("dd/MM/yyyy"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        for (DateTimeFormatter formatter : formats) {

            try {
                return LocalDate.parse(value, formatter);
            } catch (Exception ignored) {

            }

        }

        throw new RuntimeException("Fecha inválida: " + value);
    }
}
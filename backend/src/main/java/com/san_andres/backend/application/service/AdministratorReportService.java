package com.san_andres.backend.application.service;

import com.san_andres.backend.application.dto.admin.AdminReportRequest;
import com.san_andres.backend.domain.port.repositories.AdminReportPort;
import com.san_andres.backend.domain.port.repositories.ExcelGeneratorPort;
import com.san_andres.backend.domain.port.repositories.PdfGeneratorPort;
import com.san_andres.backend.domain.port.usecases.AdminReportUseCase;
import com.san_andres.backend.infrastructure.persistence.projection.AdministratorReportProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class AdministratorReportService implements AdminReportUseCase {


    private final AdminReportPort administratorReportPort;
    private final ExcelGeneratorPort excelGeneratorPort;
    private final PdfGeneratorPort pdfGeneratorPort;

    @Override
    public byte[] generatePdf(AdminReportRequest request) {
        request.setUsername(false);

        List<AdministratorReportProjection> admins = administratorReportPort.findForReport(request.getStatusFilter());

        List<String> headers = new ArrayList<>();

        if(Boolean.TRUE.equals(request.getEmail()))
            headers.add("Correo");

        if(Boolean.TRUE.equals(request.getName()))
            headers.add("Nombre");

        if(Boolean.TRUE.equals(request.getLastName()))
            headers.add("Apellido ");

        if(Boolean.TRUE.equals(request.getPhone()))
            headers.add("Teléfono");

        if(Boolean.TRUE.equals(request.getDni()))
            headers.add("DNI");

        if(Boolean.TRUE.equals(request.getGender()))
            headers.add("Género");


        if(Boolean.TRUE.equals(request.getStatus()))
            headers.add("Estado");

        List<List<String>> rows =
                admins.stream()
                        .map(admin -> {

                            List<String> row = new ArrayList<>();

                            if(Boolean.TRUE.equals(request.getEmail()))
                                row.add(admin.getEmail());

                            if(Boolean.TRUE.equals(request.getName()))
                                row.add(admin.getFirstName());

                            if(Boolean.TRUE.equals(request.getLastName()))
                                row.add(admin.getPaternalLastName());

                            if(Boolean.TRUE.equals(request.getPhone()))
                                row.add(admin.getPhone());

                            if(Boolean.TRUE.equals(request.getDni()))
                                row.add(admin.getDni());

                            if(Boolean.TRUE.equals(request.getGender()))
                                row.add(admin.getGender());

                            if(Boolean.TRUE.equals(request.getStatus()))
                                row.add(admin.getStatus());

                            return row;

                        })
                        .toList();

        return pdfGeneratorPort.generateList(
                "Reporte de administradores",
                headers,
                rows
        );

    }

    @Override
    public byte[] generatePdfId(Long id) {

        List<AdministratorReportProjection> admins = administratorReportPort.findForReportId(id);

        List<String> headers = List.of(
                "Correo",
                "Nombre",
                "Apellido",
                "Teléfono",
                "DNI",
                "Género",
                "Estado"
        );

        List<List<String>> rows = admins.stream()
                .map(admin -> List.of(
                        admin.getEmail(),
                        admin.getFirstName(),
                        admin.getPaternalLastName(),
                        admin.getPhone(),
                        admin.getDni(),
                        admin.getGender(),
                        admin.getStatus()
                ))
                .toList();

        return pdfGeneratorPort.generateListId(
                "Reporte del administrador",
                headers,
                rows
        );
    }

    @Override
    public byte[] generateExcel(AdminReportRequest request) {
        request.setUsername(false);

        List<AdministratorReportProjection> admins = administratorReportPort.findForReport(request.getStatusFilter());
        System.out.println("Cantidad administradores: " + admins.size());

        List<String> headers = new ArrayList<>();

        if(Boolean.TRUE.equals(request.getEmail()))
            headers.add("Correo");

        if(Boolean.TRUE.equals(request.getName()))
            headers.add("Nombre");

        if(Boolean.TRUE.equals(request.getLastName()))
            headers.add("Apellido ");

        if(Boolean.TRUE.equals(request.getPhone()))
            headers.add("Teléfono");

        if(Boolean.TRUE.equals(request.getDni()))
            headers.add("DNI");


        if(Boolean.TRUE.equals(request.getGender()))
            headers.add("Género");

        if(Boolean.TRUE.equals(request.getStatus()))
            headers.add("Estado");

        List<List<String>> rows =
                admins.stream()
                        .map(admin -> {

                            List<String> row = new ArrayList<>();

                            if(Boolean.TRUE.equals(request.getEmail()))
                                row.add(admin.getEmail());

                            if(Boolean.TRUE.equals(request.getName()))
                                row.add(admin.getFirstName());

                            if(Boolean.TRUE.equals(request.getLastName()))
                                row.add(admin.getPaternalLastName());

                            if(Boolean.TRUE.equals(request.getPhone()))
                                row.add(admin.getPhone());

                            if(Boolean.TRUE.equals(request.getDni()))
                                row.add(admin.getDni());

                            if(Boolean.TRUE.equals(request.getGender()))
                                row.add(admin.getGender());

                            if(Boolean.TRUE.equals(request.getStatus()))
                                row.add(admin.getStatus());

                            return row;

                        })
                        .toList();

        return excelGeneratorPort.generateList(
                "Reporte de administradores",
                headers,
                rows
        );
    }


    @Override
    public List<Map<String, Object>> findForReport(AdminReportRequest request) {

        List<AdministratorReportProjection> admins = administratorReportPort.findForReport(request.getStatusFilter());

        List<Map<String, Object>> result = new ArrayList<>();

        for (AdministratorReportProjection admin : admins) {

            Map<String, Object> row = new LinkedHashMap<>();

            if (Boolean.TRUE.equals(request.getEmail())) {
                row.put("email", admin.getEmail());
            }
            if (Boolean.TRUE.equals(request.getUsername())) {
                row.put("username", admin.getUsername());
            }

            if (Boolean.TRUE.equals(request.getName())) {
                row.put("name", admin.getFirstName());
            }

            if (Boolean.TRUE.equals(request.getLastName())) {
                row.put("lastName", admin.getPaternalLastName() + " " + admin.getMaternalLastName());
            }

            if (Boolean.TRUE.equals(request.getPhone())) {
                row.put("phone", admin.getPhone());
            }

            if (Boolean.TRUE.equals(request.getDni())) {
                row.put("dni", admin.getDni());
            }

            if (Boolean.TRUE.equals(request.getGender())) {
                row.put("gender", admin.getGender());
            }

            if (Boolean.TRUE.equals(request.getStatus())) {
                row.put("status", admin.getStatus());
            }
            System.out.println("EMAIL: " + admin.getEmail());
            System.out.println("USERNAME: " + admin.getUsername());
            result.add(row);
        }

        return result;
    }
}
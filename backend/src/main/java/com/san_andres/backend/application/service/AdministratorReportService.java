package com.san_andres.backend.application.service;

import com.san_andres.backend.application.dto.admin.AdminReportRequest;
import com.san_andres.backend.domain.port.repositories.AdminReportPort;
import com.san_andres.backend.domain.port.repositories.PdfGeneratorPort;
import com.san_andres.backend.domain.port.usecases.AdminReportUseCase;
import com.san_andres.backend.infrastructure.persistence.projection.AdministratorReportProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class AdministratorReportService implements AdminReportUseCase {


    private final AdminReportPort administratorReportPort;

    private final PdfGeneratorPort pdfGeneratorPort;



    @Override
    public byte[] generatePdf(
            AdminReportRequest request
    ) {


        List<AdministratorReportProjection> admins = administratorReportPort.findForReport(request.getStatusFilter());
        System.out.println("Cantidad administradores: " + admins.size());

        List<String> headers =
                new ArrayList<>();


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

                            List<String> row =
                                    new ArrayList<>();


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
}
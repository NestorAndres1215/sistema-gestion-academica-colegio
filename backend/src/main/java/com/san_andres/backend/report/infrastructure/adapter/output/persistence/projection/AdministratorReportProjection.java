package com.san_andres.backend.report.infrastructure.adapter.output.persistence.projection;

public interface AdministratorReportProjection {

    String getEmail();

    String getUsername();

    String getFirstName();

    String getMiddleName();

    String getPaternalLastName();

    String getMaternalLastName();

    String getPhone();

    String getDni();

    String getGender();

    String getStatus();
}
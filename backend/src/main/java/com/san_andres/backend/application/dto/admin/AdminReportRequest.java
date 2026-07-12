package com.san_andres.backend.application.dto.admin;

import lombok.Data;

@Data
public class AdminReportRequest {

    private Boolean email;
    private Boolean name;
    private Boolean lastName;
    private Boolean phone;
    private Boolean dni;
    private Boolean gender;
    private Boolean status;
    private String statusFilter;
}

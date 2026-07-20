package com.san_andres.backend.file.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UploadResponse {

    private  String fileUrl;
    private  String fileName;
    private  long fileSize;

}

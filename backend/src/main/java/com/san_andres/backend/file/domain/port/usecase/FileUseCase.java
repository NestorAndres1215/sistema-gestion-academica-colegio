package com.san_andres.backend.file.domain.port.usecase;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;

public interface FileUseCase {

    String storeFile(MultipartFile file, String folder);

    Resource loadAsResource(String nameFile) throws MalformedURLException;

    void deleteFile(String nameFile) throws IOException;

    Path uploadFile(String nameFile);
}
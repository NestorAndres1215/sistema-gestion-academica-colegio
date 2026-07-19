package com.san_andres.backend.application.service;

import com.san_andres.backend.shared.exception.ResourceNotFoundException;
import com.san_andres.backend.domain.port.usecases.FileUseCase;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Value;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
@RequiredArgsConstructor
public class FileService implements FileUseCase {

    @Value("${storage.location}")
    private String storageLocation;

    private Path storagePath;

    @PostConstruct
    public void init() throws IOException {
        this.storagePath = Paths.get(storageLocation).toAbsolutePath().normalize();
        Files.createDirectories(this.storagePath);
    }

    @Override
    public String storeFile(MultipartFile file, String folder) {

        String nameFile = StringUtils.cleanPath(file.getOriginalFilename());

        if (nameFile.contains("..")) {
            throw new ResourceNotFoundException("Nombre de archivo no válido");
        }

        try {
            Path folderPath = storagePath.resolve(folder).normalize();
            Files.createDirectories(folderPath);

            Path destination = folderPath.resolve(nameFile);

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destination, StandardCopyOption.REPLACE_EXISTING);
            }

        } catch (IOException e) {
            throw new ResourceNotFoundException("Error al guardar el archivo");
        }

        // Retorna: company/icono.webp
        return folder + "/" + nameFile;
    }

    @Override
    public Resource loadAsResource(String nameFile) throws MalformedURLException {

        Path file = uploadFile(nameFile);
        Resource resource = new UrlResource(file.toUri());

        if (resource.exists() && resource.isReadable()) {
            return resource;
        } else {
            throw new ResourceNotFoundException("Error al guardar el archivo");
        }
    }

    @Override
    public void deleteFile(String nameFile) throws IOException {

        if (nameFile.startsWith("http")) {
            nameFile = nameFile.substring(nameFile.indexOf("/assets/") + 8);
        }

        Path file = uploadFile(nameFile);

        if (Files.exists(file)) {
            FileSystemUtils.deleteRecursively(file);
        }
    }

    @Override
    public Path uploadFile(String nameFile) {
        return storagePath.resolve(nameFile).normalize();
    }
}
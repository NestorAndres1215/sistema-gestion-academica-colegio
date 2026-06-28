package com.san_andres.backend.infrastructure.controllers;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class ResourceController {


    @GetMapping("/assets/{folder}/{filename:.+}")
    public ResponseEntity<Resource> getAsset(
            @PathVariable String folder,
            @PathVariable String filename) throws MalformedURLException {


        Path file = Paths.get("assets")
                .resolve(folder)
                .resolve(filename)
                .normalize();

        Resource resource = new UrlResource(file.toUri());

        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(resource);


    }
}
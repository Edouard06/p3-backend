package com.openclassrooms.springsecurityauth.service;

import com.openclassrooms.springsecurityauth.configuration.ImageProperties;
import com.openclassrooms.springsecurityauth.exceptions.StorageException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class StorageService {
    private final Path rootLocation;
    private final String baseUrl;

    public StorageService(ImageProperties imageProperties) {
        this.rootLocation = Paths.get(imageProperties.getImageDir());
        this.baseUrl = imageProperties.getBaseUrl();
    }

    public String store(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file.");
            }
            String originalFileName = file.getOriginalFilename();
            String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            String storedFileName = UUID.randomUUID() + fileExtension;

            try (InputStream is = file.getInputStream()) {
                Files.copy(is, this.rootLocation.resolve(storedFileName));
            }

            return baseUrl + storedFileName;
        } catch (IOException e) {
            throw new StorageException("Failed to store file.", e);
        }
    }
}

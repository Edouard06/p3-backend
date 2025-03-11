package com.openclassrooms.springsecurityauth.service;

import com.openclassrooms.springsecurityauth.configuration.ImageProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageProperties imageProperties;

    public String storeImage(MultipartFile file) {
        if (file.isEmpty()) {
            throw new RuntimeException("File is empty");
        }
        try {
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

            Path targetLocation = Paths.get(imageProperties.getImageDir()).resolve(fileName);

            Files.createDirectories(targetLocation.getParent());

            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return imageProperties.getBaseUrl() + fileName;
        } catch (IOException ex) {
            throw new RuntimeException("Error while storing the image: " + file.getOriginalFilename(), ex);
        }
    }
}

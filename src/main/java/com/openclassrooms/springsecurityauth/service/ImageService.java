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
            throw new RuntimeException("Le fichier est vide");
        }
        try {
            // Générer un nom unique pour le fichier
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

            // Construire le chemin complet de destination
            Path targetLocation = Paths.get(imageProperties.getImageDir()).resolve(fileName);

            // Créer le dossier si nécessaire
            Files.createDirectories(targetLocation.getParent());

            // Copier le fichier sur le disque
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            // Retourner l'URL finale de l'image (baseUrl + fileName)
            return imageProperties.getBaseUrl() + fileName;
        } catch (IOException ex) {
            throw new RuntimeException("Erreur lors de l'enregistrement de l'image " + file.getOriginalFilename(), ex);
        }
    }
}

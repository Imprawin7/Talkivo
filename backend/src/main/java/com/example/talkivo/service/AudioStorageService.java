package com.example.talkivo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * Handles writing generated audio bytes to a short-lived storage location
 * and returning a URL the frontend can play/download from.
 *
 * Swap this out for S3/Cloud Storage in production; keep audio short-lived
 * per the project's security guidelines (don't retain it longer than needed).
 */
@Service
public class AudioStorageService {

    @Value("${app.audio.storage-dir}")
    private String storageDir;

    public String save(byte[] audioBytes, String extension) {
        try {
            Path dir = Paths.get(storageDir);
            Files.createDirectories(dir);

            String filename = UUID.randomUUID() + "." + extension;
            Path filePath = dir.resolve(filename);
            Files.write(filePath, audioBytes);

            return "/audio/" + filename;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store generated audio", e);
        }
    }
}

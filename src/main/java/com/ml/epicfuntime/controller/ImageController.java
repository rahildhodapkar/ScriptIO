package com.ml.epicfuntime.controller;

import com.ml.epicfuntime.model.ImageDataDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

@RestController
public class ImageController {
    @PostMapping("/canvas")
    public ResponseEntity<String> processImage(@RequestBody ImageDataDTO imageDataDTO) throws IOException {
        String base64image = imageDataDTO.getImage().split(",")[1];
        byte[] imageBytes = Base64.getDecoder().decode(base64image);
        Path tempFile = Files.createTempFile("image", ".png");
        try (OutputStream os = new FileOutputStream(tempFile.toFile())) {
            os.write(imageBytes);
        }
        String jsonFilePath = "scenic-lane-401400-17efc3a4fb60.json";
        ProcessBuilder processBuilder = new ProcessBuilder("python", "gvision.py", "-i", tempFile.toString(), "-c", jsonFilePath);
        processBuilder.directory(new File("pyscripts/scripts"));
        Process process = processBuilder.start();
        StringBuilder output = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        int i = 0;
        while ((line = reader.readLine()) != null) {
            if (i == 0) {
                i++;
                continue;
            }
            output.append(line).append("\n");
        }

        int exitCode;
        try {
            exitCode = process.waitFor();
        } catch (InterruptedException e) {
            return ResponseEntity.badRequest().body("Python script execution failed");
        }

        if (exitCode != 0) {
            return ResponseEntity.badRequest().body("Python script execution failed");
        }

        Files.delete(tempFile);

        return ResponseEntity.ok(output.toString());
    }
}

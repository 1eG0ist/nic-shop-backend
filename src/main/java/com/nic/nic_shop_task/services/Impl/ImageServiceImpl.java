package com.nic.nic_shop_task.services.Impl;

import com.nic.nic_shop_task.services.ImageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ImageServiceImpl implements ImageService {

    @Value("${file.files-dir}")
    private String filesDir;
    @Value("${file.files-sep}")
    private String filesSep;

    private void saveFile(MultipartFile file, String filePath) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(file.getBytes());
        }
    }

    @Override
    public ResponseEntity<String> uploadFile(MultipartFile file, String category) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty");
        }

        String path = filesDir + category + filesSep
                + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"))
                + UUID.randomUUID().toString().substring(0, 10)
                + ".jpg";

        try {
            saveFile(file, path);
            return ResponseEntity.ok(path);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Failed to upload file: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<InputStreamResource> downloadFile(String filePath) {
        try {
            File file = Paths.get(filePath).toFile();
            if (!file.exists()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            InputStreamResource resource = new InputStreamResource(Files.newInputStream(file.toPath()));

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName());
            if (filePath.contains(".png")) {
                headers.setContentType(MediaType.IMAGE_PNG);
            }

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(file.length())
                    .body(resource);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Override
    public ResponseEntity<?> deleteFiles(List<String> filesToDelete) {
        List<File> successfullyDeletedFiles = new ArrayList<>();

        try {
            for (String filePath : filesToDelete) {
                File file = Paths.get(filePath).toFile();
                if (file.exists() && file.isFile()) {
                    Files.deleteIfExists(file.toPath());
                    successfullyDeletedFiles.add(file);
                }
            }
            return ResponseEntity.ok("Files deleted successfully.");
        } catch (Exception e) {
            rollbackFiles(successfullyDeletedFiles);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete files: " + e.getMessage());
        }
    }

    private void rollbackFiles(List<File> filesToRestore) {
        for (File file : filesToRestore) {
            try {
                Files.createFile(file.toPath());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}

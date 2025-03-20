package com.nic.nic_shop_task.controllers;

import com.nic.nic_shop_task.services.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/images")
public class ImageController {
    private final ImageService imageService;

    @GetMapping
    public ResponseEntity<?> downloadFile(@RequestParam("path") String path) {
        try {
            InputStreamResource resource = imageService.downloadFile(path);
            if (resource == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File not found");
            }

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + path.substring(path.lastIndexOf("/") + 1));

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(resource);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to download file: " + e.getMessage());
        }
    }

    /*
     * now only 2 category exists - products, categories
     */
    @PostMapping("/{category}")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file, @PathVariable("category") String category) {
        try {
            String filePath = imageService.uploadFile(file, category);
            return ResponseEntity.ok(filePath);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file: " + e.getMessage());
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteFiles(@RequestBody List<String> filesToDelete) {
        try {
            imageService.deleteFiles(filesToDelete);
            return ResponseEntity.ok("Files deleted successfully: ");
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Failed to delete files: " + e.getMessage());
        }
    }
}

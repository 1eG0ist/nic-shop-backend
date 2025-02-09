package com.nic.nic_shop_task.controllers;

import com.nic.nic_shop_task.services.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/images")
public class ImageController {
    private final ImageService imageService;

    @GetMapping()
    public ResponseEntity<InputStreamResource> downloadFile(@RequestParam("path") String path) {
        System.out.println(path);
        return imageService.downloadFile(path);
    }

    @PostMapping()
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file, String category) {
        return imageService.uploadFile(file, category);
    }

    @DeleteMapping()
    public ResponseEntity<?> deleteFiles(@RequestBody List<String> filesToDelete) {
        return imageService.deleteFiles(filesToDelete);
    }
}

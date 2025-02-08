package com.nic.nic_shop_task.controllers;

import com.nic.nic_shop_task.services.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/images")
public class ImageController {
    private final ImageService imageService;

    @GetMapping()
    public ResponseEntity<InputStreamResource> downloadFile(@RequestParam("path") String path) {
        return imageService.downloadFile(path);
    }
}

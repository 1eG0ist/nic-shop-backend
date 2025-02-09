package com.nic.nic_shop_task.services;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageService {
    ResponseEntity<InputStreamResource> downloadFile(String filePath);
    ResponseEntity<String> uploadFile(MultipartFile file, String category);
    ResponseEntity<?> deleteFiles(List<String> filesToDelete);

}

package com.nic.nic_shop_task.services;

import org.springframework.core.io.InputStreamResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ImageService {
    InputStreamResource downloadFile(String filePath) throws IOException;
    String uploadFile(MultipartFile file, String category) throws IOException;
    void deleteFiles(List<String> filesToDelete) throws IOException;
}

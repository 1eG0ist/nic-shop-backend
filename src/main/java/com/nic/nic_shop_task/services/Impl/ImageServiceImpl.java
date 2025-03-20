package com.nic.nic_shop_task.services.Impl;

import com.nic.nic_shop_task.services.ImageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
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
    public String uploadFile(MultipartFile file, String category) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("File is empty");
        }

        String path = filesDir + category + filesSep
                + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"))
                + UUID.randomUUID().toString().substring(0, 10)
                + ".jpg";

        saveFile(file, path);
        return path;
    }

    @Override
    public InputStreamResource downloadFile(String filePath) throws IOException {
        File file = Paths.get(filePath).toFile();
        if (!file.exists()) {
            return null;
        }
        return new InputStreamResource(Files.newInputStream(file.toPath()));
    }

    public void deleteFiles(List<String> filesToDelete) throws IOException {
        List<File> successfullyDeletedFiles = new ArrayList<>();

        try {
            for (String filePath : filesToDelete) {
                File file = Paths.get(filePath).toFile();
                if (file.exists() && file.isFile()) {
                    Files.deleteIfExists(file.toPath());
                    successfullyDeletedFiles.add(file);
                }
            }
        } catch (IOException e) {
            rollbackFiles(successfullyDeletedFiles);
            throw e;
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

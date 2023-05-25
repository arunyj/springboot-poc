package com.elixr.training.service.impl;

import com.elixr.training.service.FileStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    private static final String ALLOWED_FILE_TYPE = "text/plain";
    @Value("${file.upload.folder}")
    private String uploadFolder;

    @Override
    public void save(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new RuntimeException("Please select a file to upload.");
        }

        if(!Objects.equals(file.getContentType(), ALLOWED_FILE_TYPE)) {
            throw new RuntimeException("Only .txt files are allowed to upload.");
        }
        // Get the file and save it in specified location
        byte[] bytes = file.getBytes();
        Path path = Paths.get(uploadFolder + file.getOriginalFilename());
        Files.write(path, bytes);
    }

}

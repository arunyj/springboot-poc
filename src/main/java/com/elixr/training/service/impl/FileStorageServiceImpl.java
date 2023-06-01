package com.elixr.training.service.impl;

import com.elixr.training.model.FileInfo;
import com.elixr.training.repository.FileRepository;
import com.elixr.training.service.FileStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class FileStorageServiceImpl implements FileStorageService {

    private static final String ALLOWED_FILE_TYPE = "text/plain";
    @Value("${file.upload.folder}")
    private String uploadFolder;

    @Autowired
    FileRepository fileRepository;

    @Override
    public FileInfo save(File file) throws IOException {
//        if (file.isEmpty()) {
//            log.error("Input file is empty. Please select a file to upload. ");
//            throw new RuntimeException("Please select a file to upload.");
//        }
//
//        if(!Objects.equals(file.getContentType(), ALLOWED_FILE_TYPE)) {
//            log.error("Only .txt files are allowed to upload.");
//            throw new RuntimeException("Only .txt files are allowed to upload.");
//        }
        // Get the file and save it in specified location
        String fileName = file.getName();
        Path path = Paths.get(uploadFolder + fileName);
        OutputStream out = new FileOutputStream(path.toFile());
        out.close();
        log.info("File validation success");
        //Save file info to DB
        FileInfo fileInfo = new FileInfo(UUID.randomUUID(),"Test", fileName, path.toString(), new Date());
        return fileRepository.save(fileInfo);
    }

    @Override
    public FileInfo get(String id) throws FileNotFoundException {
        Optional<FileInfo> optional = fileRepository.findById(id.toString());

        return optional.map(fileInfo-> fileInfo)
                .orElseThrow(() -> new FileNotFoundException());
    }

}

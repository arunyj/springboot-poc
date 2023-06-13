package com.elixr.training.service.impl;

import brave.Tracer;
import com.elixr.training.exception.FileInfoNotFoundException;
import com.elixr.training.exception.InvalidInputException;
import com.elixr.training.model.FileInfo;
import com.elixr.training.repository.FileRepository;
import com.elixr.training.service.FileStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class FileStorageServiceImpl implements FileStorageService {

    private static final String ALLOWED_FILE_TYPE = "txt";
    @Value("${file.upload.folder}")
    private String uploadFolder;

    @Value("${message.error.invalid.extension}")
    private String invalidExtensionMessage;

    @Value("${message.error.invalid.file}")
    private String invalidFileMessage;

    @Value("${message.error.invalid.username}")
    private String invalidUserNameMessage;

    @Value("${message.error.file.info.not.found}")
    private String fileInfoNotFoundMessage;

    @Value("${message.error.invalid.id}")
    private String fileIdInvalidMessage;


    @Autowired
    private Tracer tracer;

    @Autowired
    FileRepository fileRepository;

    @Override
    public FileInfo save(File file, String userName) throws InvalidInputException {

        //Validations
        validateFileInfo(file, userName);

        // Get the file and save it in specified location
        String fileName = file.getName();
        Path path = Paths.get(uploadFolder + fileName);
        try {
            OutputStream out = new FileOutputStream(path.toFile());
            out.close();
        } catch (IOException ex) {
            log.error(invalidFileMessage, ex.getMessage());
            throw new InvalidInputException(invalidFileMessage);
        }
        log.info("File uploaded to the specified location");
        //Save file info to DB
        FileInfo fileInfo = new FileInfo(UUID.randomUUID(),userName, fileName, path.toString(), new Date());
        return fileRepository.save(fileInfo);
    }

    private void validateFileInfo(File file, String userName) throws InvalidInputException {

        if (file == null) {
            throw new InvalidInputException(invalidFileMessage);
        }

        String fileName = file.getName();
        if(!getFileExtension(fileName).equals(ALLOWED_FILE_TYPE)) {
            log.error(invalidExtensionMessage);
            throw new InvalidInputException(String.format(invalidExtensionMessage, fileName));
        }

        if(!StringUtils.hasText(userName)) {
            log.error(invalidUserNameMessage);
            throw new InvalidInputException(String.format(invalidUserNameMessage));
        }
    }

    @Override
    public FileInfo get(String id) throws FileInfoNotFoundException, InvalidInputException {
        UUID uuid = converToUUID(id);
        return fileRepository.findByFileId(uuid).orElseThrow(() -> new FileInfoNotFoundException(fileInfoNotFoundMessage));
    }

    private static String getFileExtension(String fullName) {
        int dotIndex = fullName.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fullName.substring(dotIndex + 1);
    }

    private UUID converToUUID(String id) throws InvalidInputException {
        UUID uuid;
        try {
            uuid = UUID.fromString(id);
        } catch (IllegalArgumentException exception) {
            throw new InvalidInputException(fileIdInvalidMessage);
        }
        return uuid;
    }

}

package com.elixr.training.service;
import com.elixr.training.exception.FileInfoNotFoundException;
import com.elixr.training.exception.InvalidInputException;
import com.elixr.training.model.FileInfo;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

public interface FileStorageService {
    public FileInfo save(File file, String userName) throws InvalidInputException;

    FileInfo get(String id) throws FileInfoNotFoundException, InvalidInputException;

}

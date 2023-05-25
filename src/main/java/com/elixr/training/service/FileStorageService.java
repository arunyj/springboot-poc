package com.elixr.training.service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileStorageService {
    public void save(MultipartFile file) throws IOException;
}

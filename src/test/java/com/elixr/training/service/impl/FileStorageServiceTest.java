package com.elixr.training.service.impl;

import com.elixr.training.repository.FileRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class FileStorageServiceTest {
    @Mock
    FileRepository fileRepository;

    @InjectMocks
    FileStorageServiceImpl fileStorageService;

    @Test

}

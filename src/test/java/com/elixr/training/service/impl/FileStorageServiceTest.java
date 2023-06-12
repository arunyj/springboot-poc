package com.elixr.training.service.impl;

import com.elixr.training.exception.FileInfoNotFoundException;
import com.elixr.training.exception.InvalidInputException;
import com.elixr.training.model.FileInfo;
import com.elixr.training.repository.FileRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FileStorageServiceTest {
    @Mock
    FileRepository fileRepository;

    @InjectMocks
    FileStorageServiceImpl fileStorageService;

    private static final String USER_NAME = "Test User";
    private static final String FILE_NAME = "test.txt";
    private static final String FILE_URL = "C://test//" + FILE_NAME;

    private static final String CONTENT_TYPE = "text/plain";
    private static final String FILE_CONTENT = "test";
    private UUID uuid = UUID.randomUUID();
    private static final String ERROR_MESSAGE_FILE_NOT_FOUND = "Requested file not found";

    private static final String ERROR_MESSAGE_INVALID_INPUT = "Invalid Input";

    File file = new File(FILE_URL);
    FileInfo fileInfo = new FileInfo(uuid, USER_NAME, FILE_NAME, FILE_URL, new Date());


    @Test
    void testGetFileSuccess() throws InvalidInputException, FileInfoNotFoundException {
        Optional<FileInfo> optionalFileInfo = Optional.of(fileInfo);
        when(fileRepository.findByFileId(uuid)).thenReturn(optionalFileInfo);
        FileInfo fileInfo = fileStorageService.get(uuid.toString());
        assertEquals(fileInfo.getFileName(), FILE_NAME);
        assertEquals(fileInfo.getFileId(), uuid);
        assertEquals(fileInfo.getUser(), USER_NAME);
        assertEquals(fileInfo.getUrl(), FILE_URL);
    }

    @Test
    void testGetFileNotFoundError()  {
        when(fileRepository.findByFileId(uuid)).thenReturn(Optional.empty());
        FileInfoNotFoundException exception = assertThrows(FileInfoNotFoundException.class, ()-> fileStorageService.get(uuid.toString()));
//        assertEquals( ERROR_MESSAGE_FILE_NOT_FOUND, exception.getMsg());
    }

    @Test
    void testGetFileInvalidInputError()  {
        InvalidInputException exception = assertThrows(InvalidInputException.class, ()-> fileStorageService.get("123"));
//        assertEquals( ERROR_MESSAGE_FILE_NOT_FOUND, exception.getMsg());
    }

    @Test
    void testFileUploadSuccess() throws InvalidInputException {
        Mockito.when(fileRepository.save(any(FileInfo.class))).thenReturn(fileInfo);
        FileInfo fileInfoReceived = fileStorageService.save(file, USER_NAME);
        assertEquals( fileInfoReceived.getFileName(), fileInfo.getFileName());
        assertEquals(fileInfoReceived.getFileId(), fileInfo.getFileId());
    }

    @Test
    void testFileUploadWithoutUserName() {
        assertThrows(InvalidInputException.class, () -> fileStorageService.save(file, ""));
    }

}

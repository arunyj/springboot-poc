package com.elixr.training.service.impl;

import com.elixr.training.exception.FileInfoNotFoundException;
import com.elixr.training.exception.InvalidInputException;
import com.elixr.training.model.FileInfo;
import com.elixr.training.repository.FileRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    private UUID uuid = UUID.randomUUID();
    private FileInfo fileToReturn = new FileInfo(uuid, USER_NAME, FILE_NAME, FILE_URL, new Date());
    private static final String ERROR_MESSAGE_FILE_NOT_FOUND = "Requested file not found";

    private static final String ERROR_MESSAGE_INVALID_INPUT = "Invalid Input";

    @Test
    void testGetFileSuccess() throws InvalidInputException, FileInfoNotFoundException {
        Optional<FileInfo> optionalFileInfo = Optional.of(fileToReturn);
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
    void testGetInvalidInputError()  {
        InvalidInputException exception = assertThrows(InvalidInputException.class, ()-> fileStorageService.get("123"));
//        assertEquals( ERROR_MESSAGE_FILE_NOT_FOUND, exception.getMsg());
    }

}

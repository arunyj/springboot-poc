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
import org.springframework.test.util.ReflectionTestUtils;

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
    private FileRepository fileRepository;

    @InjectMocks
    private FileStorageServiceImpl fileStorageService;

    private static final String USER_NAME = "Test User";
    private static final String FILE_NAME = "test.txt";

    private static final String UPLOAD_FOLDER = "C://test//";
    private static final String FILE_URL =  UPLOAD_FOLDER + FILE_NAME;
    private static final String CONTENT_TYPE = "text/plain";
    private static final String FILE_CONTENT = "test";
    private UUID uuid = UUID.randomUUID();
    private static final String ERROR_MESSAGE_FILE_NOT_FOUND = "Requested file not found";
    private static final String ERROR_MESSAGE_INVALID_FILE_ID = "Invalid Input";

    private static final String ERROR_MESSAGE_INVALID_FILE = "Invalid file";

    private final String ERROR_MESSAGE_INVALID_USER_NAME = "Provide valid username";

    private  final String ERROR_MESSAGE_INVALID_FILE_EXTENSION = "Only .txt files are allowed to upload";

    private File file = new File(FILE_URL);
    private FileInfo fileInfo = new FileInfo(uuid, USER_NAME, FILE_NAME, FILE_URL, new Date());

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
        ReflectionTestUtils.setField(fileStorageService , "fileInfoNotFoundMessage", ERROR_MESSAGE_FILE_NOT_FOUND);
        FileInfoNotFoundException exception = assertThrows(FileInfoNotFoundException.class, ()-> fileStorageService.get(uuid.toString()));
        assertEquals( ERROR_MESSAGE_FILE_NOT_FOUND, exception.getMsg());
    }

    @Test
    void testGetFileInvalidInputError()  {
        ReflectionTestUtils.setField(fileStorageService , "fileIdInvalidMessage", ERROR_MESSAGE_INVALID_FILE_ID);
        InvalidInputException exception = assertThrows(InvalidInputException.class, ()-> fileStorageService.get("123"));
        assertEquals( ERROR_MESSAGE_INVALID_FILE_ID, exception.getMsg());
    }

    @Test
    void testFileUploadSuccess() throws InvalidInputException {
        Mockito.when(fileRepository.save(any(FileInfo.class))).thenReturn(fileInfo);
        ReflectionTestUtils.setField(fileStorageService , "uploadFolder", UPLOAD_FOLDER);
        FileInfo fileInfoReceived = fileStorageService.save(file, USER_NAME);
        assertEquals( fileInfoReceived.getFileName(), fileInfo.getFileName());
        assertEquals(fileInfoReceived.getFileId(), fileInfo.getFileId());
    }

    @Test
    void testFileUploadWithoutUserName() {
        ReflectionTestUtils.setField(fileStorageService , "invalidUserNameMessage", ERROR_MESSAGE_INVALID_USER_NAME);
        InvalidInputException exception = assertThrows(InvalidInputException.class, () -> fileStorageService.save(file, ""));
        assertEquals( ERROR_MESSAGE_INVALID_USER_NAME, exception.getMsg());
    }

    @Test
    void testFileUploadFileExtensionValidation() {
        File file = new File( "C://test//test.jpg" );
        ReflectionTestUtils.setField(fileStorageService , "invalidExtensionMessage", ERROR_MESSAGE_INVALID_FILE_EXTENSION);
        InvalidInputException exception = assertThrows(InvalidInputException.class, () -> fileStorageService.save(file, USER_NAME));
        assertEquals( ERROR_MESSAGE_INVALID_FILE_EXTENSION, exception.getMsg());
    }

    @Test
    void testFileUploadToNonExistingPath() {
        ReflectionTestUtils.setField(fileStorageService , "uploadFolder", "C://test//non-exist//");
        ReflectionTestUtils.setField(fileStorageService , "invalidFileMessage", ERROR_MESSAGE_INVALID_FILE);
        InvalidInputException exception = assertThrows(InvalidInputException.class, () -> fileStorageService.save(file, USER_NAME));
        assertEquals( ERROR_MESSAGE_INVALID_FILE, exception.getMsg());
    }

    @Test
    void testFileUploadWithoutFile() {
        ReflectionTestUtils.setField(fileStorageService , "invalidFileMessage", ERROR_MESSAGE_INVALID_FILE);
        InvalidInputException exception = assertThrows(InvalidInputException.class, () -> fileStorageService.save(null, USER_NAME));
        assertEquals( ERROR_MESSAGE_INVALID_FILE, exception.getMsg());
    }
}

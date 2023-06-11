package com.elixr.training.controller;

import com.elixr.training.exception.FileInfoNotFoundException;
import com.elixr.training.exception.InvalidInputException;
import com.elixr.training.model.FileInfo;
import com.elixr.training.service.impl.FileStorageServiceImpl;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Date;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@WebMvcTest(controllers = FilesController.class)
public class FilesControllerTest {

    private static final String API_URL_FILE_UPLOAD = "/file/upload";

    private static final String API_URL_GET_FILE = "/file/{id}";
    private static final String FILE_ID = "36b8f84d-df4e-4d49-b662-bcde71a8764f";
    private static final String USER_NAME = "Arun";
    private static final String FILE_NAME = "test.txt";
    private static final String INVALID_FILE = "File is empty! Please choose a file";
    private static final String CONTENT_TYPE = "text/plain";
    private static final String FILE_CONTENT = "test";
    private static final String SUCCESS = "SUCCESS";
    private static final String FAILURE = "FAILURE";
    private static final String MESSAGE_FILE_NOT_FOUND = "File id not found";
    private static final String MESSAGE_INVALID_USERNAME = "Invalid username";
    private static final String MESSAGE_FILE_INFO_NOT_FOUND= "File info not found.";
    private MockMultipartFile mockMultipartFile = new MockMultipartFile("file", FILE_NAME, CONTENT_TYPE, FILE_CONTENT.getBytes());

    private UUID fileId = UUID.fromString(FILE_ID);
    private String traceId = UUID.randomUUID().toString();

    FileInfo fileToReturn = new FileInfo(fileId, USER_NAME, FILE_NAME, "C://test//" + FILE_NAME, new Date());


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    FileStorageServiceImpl storageService;

    @MockBean
    TracingServiceImpl tracingService;

    @Test
    void uploadFileSuccess() throws Exception {
        Mockito.when(storageService.save(any(), any())).thenReturn(fileToReturn);
        Mockito.when(tracingService.getTraceId()).thenReturn(traceId);
        mockMvc.perform(MockMvcRequestBuilders.multipart(API_URL_FILE_UPLOAD)
                        .file(mockMultipartFile)
                        .param("userName", USER_NAME))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode", Matchers.is(HttpStatus.OK.value())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is(SUCCESS)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.traceId", Matchers.is(traceId)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.fileId", Matchers.is(FILE_ID)));
    }

    @Test
    void uploadFileError() throws Exception {
        Mockito.when(tracingService.getTraceId()).thenReturn(traceId);
        Mockito.when(storageService.save(any(), anyString())).thenThrow(new InvalidInputException(INVALID_FILE));
        mockMvc.perform(MockMvcRequestBuilders.multipart(API_URL_FILE_UPLOAD)
                        .file(mockMultipartFile)
                        .param("userName", USER_NAME))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode", Matchers.is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is(FAILURE)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is(INVALID_FILE)));
    }

    @Test
    void getFileInfoTest() throws Exception {
        Mockito.when(storageService.get(fileId.toString())).thenReturn(fileToReturn);
        Mockito.when(tracingService.getTraceId()).thenReturn(traceId);
        mockMvc.perform(MockMvcRequestBuilders.get(API_URL_GET_FILE, fileId)).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode", Matchers.is(HttpStatus.OK.value())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is(SUCCESS)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.fileId", Matchers.is(fileId.toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.fileName", Matchers.is(FILE_NAME)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.user", Matchers.is(USER_NAME)));
    }

    @Test
    void getFileInfoErrorTest() throws Exception {
        Mockito.when(storageService.get(fileId.toString())).thenThrow(new FileInfoNotFoundException(MESSAGE_FILE_INFO_NOT_FOUND));
        Mockito.when(tracingService.getTraceId()).thenReturn(traceId);
        mockMvc.perform(MockMvcRequestBuilders.get(API_URL_GET_FILE, fileId)).andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode", Matchers.is(HttpStatus.NOT_FOUND.value())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is(FAILURE)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is(MESSAGE_FILE_INFO_NOT_FOUND)));
    }
}

package com.elixr.training.controller;

import com.elixr.training.model.FileInfo;
import com.elixr.training.service.impl.FileStorageServiceImpl;
import com.elixr.training.service.impl.TracingServiceImpl;
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

@WebMvcTest(controllers = FilesController.class)
public class FilesControllerTest {

    private static final String UPLOAD_API_URL = "/file/upload";
    private static final String FILE_ID = "36b8f84d-df4e-4d49-b662-bcde71a8764f";
    private static final String USER_NAME = "rkattayil";
    private static final String FILE_NAME = "test.txt";
    private static final String INVALID_FILE = "File is empty! Please choose a file";
    private static final String CONTENT_TYPE = "text/plain";
    private static final String UPLOAD_URL = "/file/upload";
    private static final String GET_FILE_URI = "/file/{id}";
    private static final String FILE_CONTENT = "test";
    private static final String SUCCESS = "SUCCESS";
    private static final String FAILURE = "FAILED";
    private static final String FILE_NOT_FOUND="File id not found";
    private static final String INVALID_USERNAME = "Invalid username";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    FileStorageServiceImpl storageService;

    @MockBean
    TracingServiceImpl tracingService;

    @Test
    void uploadFileSuccess() throws Exception {
        UUID fileId = UUID.fromString(FILE_ID);
        String traceId = UUID.randomUUID().toString();
        FileInfo fileToReturn = new FileInfo(fileId, USER_NAME, FILE_NAME, "C://test//" + FILE_NAME, new Date());
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", FILE_NAME, CONTENT_TYPE, FILE_CONTENT.getBytes());
        Mockito.when(storageService.save(any(), any())).thenReturn(fileToReturn);
        Mockito.when(tracingService.getTraceId()).thenReturn(traceId);
        mockMvc.perform(MockMvcRequestBuilders.multipart(UPLOAD_API_URL)
                        .file(mockMultipartFile)
                        .param("userName", USER_NAME))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode", Matchers.is(HttpStatus.OK.value())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is(SUCCESS)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.fileId", Matchers.is(FILE_ID)));
    }
}

package com.elixr.training.controller;

import com.elixr.training.constants.ResponseStatus;
import com.elixr.training.response.ResponseData;
import com.elixr.training.exception.FileInfoNotFoundException;
import com.elixr.training.exception.InvalidInputException;
import com.elixr.training.model.FileInfo;
import com.elixr.training.service.TracingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.elixr.training.service.FileStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;

@Slf4j
@RestController
@RequestMapping("/file")
public class FilesController {

    @Value("${message.error.invalid.file}")
    private String invalidFileMessage;

    @Value("${message.success.file.upload}")
    private String uploadSuccessMsg;

    @Value("${message.success.file.retrieve}")
    private String getFileSuccessMsg;

    @Autowired
    TracingService tracingService;

    @Autowired
    FileStorageService storageService;

    @PostMapping("/upload")
    public ResponseEntity<ResponseData> uploadFile(@RequestParam("file") MultipartFile multipartFile, @RequestParam("userName") String userName) throws InvalidInputException {
        log.info("File upload request received.");
        if(multipartFile.isEmpty()) {
            throw new InvalidInputException(invalidFileMessage);
        }
        File file = new File(multipartFile.getOriginalFilename());
        FileInfo fileInfo = storageService.save(file, userName);
        String  message = String.format(uploadSuccessMsg, multipartFile.getOriginalFilename());
        log.info(message);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseData(HttpStatus.OK.value(), message, ResponseStatus.SUCCESS, tracingService.getTraceId(), fileInfo));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseData> uploadFile(@PathVariable("id") String id) throws FileInfoNotFoundException, InvalidInputException {
        log.info("File get request received.");
        FileInfo fileInfo = storageService.get(id);
        log.info(getFileSuccessMsg);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseData(HttpStatus.OK.value(), getFileSuccessMsg, ResponseStatus.SUCCESS, tracingService.getTraceId(), fileInfo ));
    }
}

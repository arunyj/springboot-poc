package com.elixr.training.exception.handler;

import com.elixr.training.dto.ResponseMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @Value("${spring.servlet.multipart.max-file-size}")
    private String maxFileUploadSize;

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<ResponseMessage> handleMultipartExceptionException(MultipartException exc) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("Please select a file to upload."));
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ResponseMessage> handleMaxSizeException(MaxUploadSizeExceededException exc) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("File too large. Please upload file size lesser than " +  maxFileUploadSize + "."));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseMessage> handleException(Exception exc) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("Error occurred! " + exc.getMessage()));
    }
}

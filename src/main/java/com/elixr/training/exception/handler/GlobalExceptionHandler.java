package com.elixr.training.exception.handler;

import com.elixr.training.dto.Response;
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
    public ResponseEntity<Response> handleMultipartExceptionException(MultipartException exc) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Please select a file to upload."));
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<Response> handleMaxSizeException(MaxUploadSizeExceededException exc) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("File too large. Please upload file size lesser than " +  maxFileUploadSize + "."));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response> handleException(Exception exc) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Error occurred! " + exc.getMessage()));
    }
}

package com.elixr.training.exception.handler;

import com.elixr.training.constants.ResponseStatus;
import com.elixr.training.dto.ResponseData;
import com.elixr.training.service.TracingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.UUID;

@ControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    private TracingService tracingService;

//    @ExceptionHandler(MultipartException.class)
//    public ResponseEntity<ResponseData> handleMultipartExceptionException(MultipartException exc) {
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseData("Please select a file to upload."));
//    }
//
//    @ExceptionHandler(MaxUploadSizeExceededException.class)
//    public ResponseEntity<ResponseData> handleMaxSizeException(MaxUploadSizeExceededException exc) {
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseData("File too large. Please upload file size lesser than " +  maxFileUploadSize + "."));
//    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseData> handleException(Exception exc) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseData(HttpStatus.BAD_REQUEST.value(), "Error occurred! " + exc.getMessage(), ResponseStatus.FAILURE, tracingService.getTraceId(), null));
    }
}

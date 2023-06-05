package com.elixr.training.exception.handler;

import com.elixr.training.constants.ResponseStatus;
import com.elixr.training.dto.ResponseData;
import com.elixr.training.exception.FileInfoNotFoundException;
import com.elixr.training.exception.InvalidInputException;
import com.elixr.training.service.TracingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class FileInfoExceptionHandler {

    @Autowired
    private TracingService tracingService;

    @ExceptionHandler(FileInfoNotFoundException.class)
    public ResponseEntity<ResponseData> handleFileInfoNotFoundException(FileInfoNotFoundException exc) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseData(HttpStatus.NOT_FOUND.value(), exc.getMsg(), ResponseStatus.FAILURE, tracingService.getTraceId()));
    }

    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<ResponseData> handleInvalidInputException(InvalidInputException exc) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseData(HttpStatus.BAD_REQUEST.value(), exc.getMsg(), ResponseStatus.FAILURE, tracingService.getTraceId()));
    }

}

package com.elixr.training.exception.handler;

import com.elixr.training.constants.ResponseStatus;
import com.elixr.training.dto.ResponseData;
import com.elixr.training.exception.FileInfoNotFoundException;
import com.elixr.training.exception.InvalidInputException;
import com.elixr.training.service.TracingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

@Slf4j
@ControllerAdvice
public class FileInfoExceptionHandler {

    @Value("${message.error.mandatory.param}")
    private String missingParamErrorMsg;


    @Value("${message.error.missing.file}")
    private String missingFileErrorMsg;

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

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ResponseData> mandatoryRequestParamException(MissingServletRequestParameterException exc) {
        log.error(exc.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseData(HttpStatus.BAD_REQUEST.value(),
                String.format(missingParamErrorMsg, exc.getParameterName()) , ResponseStatus.FAILURE, tracingService.getTraceId()));
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<ResponseData> mandatoryRequestPartException(MissingServletRequestPartException exc) {
        log.error(exc.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseData(HttpStatus.BAD_REQUEST.value(),
                String.format(missingParamErrorMsg, exc.getRequestPartName()) , ResponseStatus.FAILURE, tracingService.getTraceId()));
    }

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<ResponseData> notMultipartRequestException(MultipartException exc) {
        log.error(exc.getMessage() + " - "+ missingFileErrorMsg);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseData(HttpStatus.BAD_REQUEST.value(), missingFileErrorMsg, ResponseStatus.FAILURE, tracingService.getTraceId()));
    }


}

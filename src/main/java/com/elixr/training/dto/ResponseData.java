package com.elixr.training.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.UUID;

@Data
@AllArgsConstructor
public class ResponseData {
    private String message;
    private String status;
    private UUID traceId;
    private Object data;
}

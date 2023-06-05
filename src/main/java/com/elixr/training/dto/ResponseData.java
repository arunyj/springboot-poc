package com.elixr.training.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.UUID;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class ResponseData {
    @NonNull
    private int statusCode;
    @NonNull
    private String message;
    @NonNull
    private String status;
    @NonNull
    private String traceId;
    private Object data;
}

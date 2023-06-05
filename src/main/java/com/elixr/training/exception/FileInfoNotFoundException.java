package com.elixr.training.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FileInfoNotFoundException extends Exception {
    private String msg;
}
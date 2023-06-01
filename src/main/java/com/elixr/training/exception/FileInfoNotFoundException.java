package com.elixr.training.exception;

public class FileInfoNotFoundException extends Exception {


    public  FileInfoNotFoundException() {
        super("File info not found in the database");
    }
}
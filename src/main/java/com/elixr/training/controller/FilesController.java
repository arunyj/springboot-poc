package com.elixr.training.controller;

import com.elixr.training.constants.ResponseStatus;
import com.elixr.training.dto.ResponseData;
import com.elixr.training.model.FileInfo;
import org.springframework.beans.factory.annotation.Autowired;
import com.elixr.training.service.FileStorageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("file")
public class FilesController {

    @Autowired
    FileStorageService storageService;

    @PostMapping("/upload")
    public ResponseEntity<ResponseData> uploadFile(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        File file = new File(multipartFile.getOriginalFilename());

        FileInfo fileInfo = storageService.save(file);
        String  message = "Uploaded the file successfully: " + multipartFile.getOriginalFilename();
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseData(message, ResponseStatus.SUCCESS, UUID.randomUUID(), fileInfo));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseData> uploadFile(@PathVariable("id") String id) throws IOException {
        FileInfo fileInfo = storageService.get(id);
        String  message = "File info retrieved successfully";
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseData(message, ResponseStatus.SUCCESS, UUID.randomUUID(), fileInfo ));
    }
}

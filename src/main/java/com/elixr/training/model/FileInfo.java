package com.elixr.training.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.UUID;

@Data
@Document("files")
@AllArgsConstructor
public class FileInfo {
    @Id
    private UUID fileId;
    private String userName;
    private String fileName;
    private String url;
    private Date uploadedAt;
}

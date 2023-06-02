package com.elixr.training.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.UUID;

@Data
@Document("files")
@AllArgsConstructor
@RequiredArgsConstructor
public class FileInfo {
    @Id
    private UUID fileId;
    @NonNull
    private String userName;
    @NonNull
    private String fileName;
    @NonNull
    private String url;
    @NonNull
    private Date uploadedAt;
}

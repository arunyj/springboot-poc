package com.elixr.training.model;

import lombok.AllArgsConstructor;
import lombok.Data;
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
    @Field("userName")
    private String user;
    private String fileName;
    private String url;
    private Date uploadedAt;
}

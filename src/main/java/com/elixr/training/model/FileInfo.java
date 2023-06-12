package com.elixr.training.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.UUID;

@Document("files")
@AllArgsConstructor
@Getter
@Setter
public class FileInfo {
    @Id
    private UUID fileId;
    @Field("userName")
    private String user;
    private String fileName;
    private String url;
    private Date uploadedAt;
}

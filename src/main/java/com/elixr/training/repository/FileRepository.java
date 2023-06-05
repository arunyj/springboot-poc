package com.elixr.training.repository;

import com.elixr.training.model.FileInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;
import java.util.UUID;

public interface FileRepository extends MongoRepository<FileInfo, String> {
    Optional<FileInfo> findByFileId(UUID id);
}

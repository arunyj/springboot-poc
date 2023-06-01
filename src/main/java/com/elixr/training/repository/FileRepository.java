package com.elixr.training.repository;

import com.elixr.training.model.FileInfo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FileRepository extends MongoRepository<FileInfo, String> {

}

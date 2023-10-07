package com.ml.epicfuntime.repository;

import com.ml.epicfuntime.model.Input;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface InputRepository extends MongoRepository<Input, String> {
}

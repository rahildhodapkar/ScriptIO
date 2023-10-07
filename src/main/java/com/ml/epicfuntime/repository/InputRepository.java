package com.ml.epicfuntime.repository;

import com.ml.epicfuntime.model.Input;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InputRepository extends MongoRepository<Input, String> {
}

package com.ml.epicfuntime.repository;

import com.ml.epicfuntime.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
}

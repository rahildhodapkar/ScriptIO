package com.ml.epicfuntime.repository;

import com.ml.epicfuntime.model.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RoleRepository extends MongoRepository<Role, String> {
    Optional<Role> findByUsername(String username);
}

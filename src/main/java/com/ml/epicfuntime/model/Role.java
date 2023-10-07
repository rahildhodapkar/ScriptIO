package com.ml.epicfuntime.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "roles")
@Data
public class Role {
    @Id
    private String id;

    private String username;
    private String role;

    public Role(String username, String role) {
        this.username = username;
        this.role = role;
    }
}

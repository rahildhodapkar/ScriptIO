package com.ml.epicfuntime.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "inputs")
@Data
public class Input {
    @Id
    private String id;

    private String email;
    private String input;
}

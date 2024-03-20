package com.example.courseWork.DTO.gameDTO;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;

public class FieldDTO {
    private int id;

    private String key;

    private String type;

    private String label;

    private String description;

    public FieldDTO(String key, String type, String label, String description) {
        this.key = key;
        this.type = type;
        this.label = label;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public FieldDTO() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

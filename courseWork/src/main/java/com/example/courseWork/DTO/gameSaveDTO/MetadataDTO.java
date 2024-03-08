package com.example.courseWork.DTO.gameSaveDTO;

import jakarta.persistence.Column;

public class MetadataDTO {
    private String type;

    private String value;

    private String label;

    private String description;

    public MetadataDTO(String type, String value, String label, String description) {
        this.type = type;
        this.value = value;
        this.label = label;
        this.description = description;
    }

    public MetadataDTO() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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

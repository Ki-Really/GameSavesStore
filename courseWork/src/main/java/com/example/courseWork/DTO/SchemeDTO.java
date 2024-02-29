package com.example.courseWork.DTO;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;

import java.util.ArrayList;
import java.util.List;

public class SchemeDTO {
    /*@NotEmpty(message ="Путь не должен быть пустым!")*/
    private String filename;
    private List<FieldDTO> fields;

    public SchemeDTO(String filename, List<FieldDTO> fields) {
        this.filename = filename;
        this.fields = fields;
    }

    public SchemeDTO() {
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public List<FieldDTO> getFields() {
        return fields;
    }

    public void setFields(List<FieldDTO> fields) {
        this.fields = fields;
    }
}

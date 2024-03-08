package com.example.courseWork.DTO.gameDTO;

import java.util.List;

public class SchemeDTO {
    /*@NotEmpty(message ="Путь не должен быть пустым!")*/
    private String filename;
    private List<FieldDTO> fields;

    public SchemeDTO(String filename, List<FieldDTO> fields) {
        this.filename = filename;
        this.fields = fields;
    }

    public SchemeDTO(String filename) {
        this.filename = filename;
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

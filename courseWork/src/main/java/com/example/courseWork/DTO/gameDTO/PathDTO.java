package com.example.courseWork.DTO.gameDTO;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;

public class PathDTO {
    private int id;

    private String path;

    public PathDTO() {
    }

    public PathDTO(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

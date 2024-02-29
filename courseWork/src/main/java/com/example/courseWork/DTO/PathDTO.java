package com.example.courseWork.DTO;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;

public class PathDTO {
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
}

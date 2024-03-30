package com.example.courseWork.DTO.gameDTO;

public class GameStateParameterTypeDTO {
    private int id;
    private String type;


    public GameStateParameterTypeDTO(int id, String type) {
        this.id = id;
        this.type = type;
    }

    public GameStateParameterTypeDTO() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

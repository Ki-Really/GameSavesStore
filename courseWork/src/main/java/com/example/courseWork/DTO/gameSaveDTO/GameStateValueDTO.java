package com.example.courseWork.DTO.gameSaveDTO;

public class GameStateValueDTO {
    private int id;
    private int gameStateParameterId;

    private String value;
    private String label;
    private String description;



    public GameStateValueDTO(int gameStateParameterId, String value) {
        this.gameStateParameterId = gameStateParameterId;
        this.value = value;

    }
    public GameStateValueDTO() {
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getGameStateParameterId() {
        return gameStateParameterId;
    }

    public void setGameStateParameterId(int gameStateParameterId) {
        this.gameStateParameterId = gameStateParameterId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

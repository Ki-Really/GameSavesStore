package com.example.courseWork.DTO.gameSaveDTO;

public class GameStateValueDTO {
    private int id;
    private int gameStateParameterId;

    private String value;


    public GameStateValueDTO(int gameStateParameterId, String value) {
        this.gameStateParameterId = gameStateParameterId;
        this.value = value;

    }
    public GameStateValueDTO() {
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

package com.example.courseWork.DTO.gameDTO;

import java.util.List;

public class SchemeDTO {
    /*@NotEmpty(message ="Путь не должен быть пустым!")*/
    private int id;
    private String filename;
    private List<GameStateParameterDTO> gameStateParameters;

    public SchemeDTO(String filename, List<GameStateParameterDTO> gameStateParameters) {
        this.filename = filename;
        this.gameStateParameters = gameStateParameters;
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

    public List<GameStateParameterDTO> getGameStateParameters() {
        return gameStateParameters;
    }

    public void setGameStateParameters(List<GameStateParameterDTO> gameStateParameters) {
        this.gameStateParameters = gameStateParameters;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

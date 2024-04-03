package com.example.courseWork.DTO.gameDTO;

import java.util.List;

public class SchemeResponseDTO {
    private int id;
    private String filename;
    private List<GameStateParameterResponseDTO> gameStateParameters;

    public SchemeResponseDTO( String filename, List<GameStateParameterResponseDTO> gameStateParameters) {

        this.filename = filename;
        this.gameStateParameters = gameStateParameters;
    }

    public SchemeResponseDTO() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public List<GameStateParameterResponseDTO> getGameStateParameters() {
        return gameStateParameters;
    }

    public void setGameStateParameters(List<GameStateParameterResponseDTO> gameStateParameters) {
        this.gameStateParameters = gameStateParameters;
    }
}

package com.example.courseWork.DTO.commonParameterDTO;

import com.example.courseWork.DTO.gameDTO.GameStateParameterTypeDTO;

public class CommonParameterDTO {
    private int id;
    private GameStateParameterTypeDTO type;
    private String label;
    private String description;

    public CommonParameterDTO(int id, GameStateParameterTypeDTO gameStateParameterTypeDTO, String label, String description) {
        this.id = id;
        this.type = gameStateParameterTypeDTO;
        this.label = label;
        this.description = description;
    }

    public CommonParameterDTO() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public GameStateParameterTypeDTO getGameStateParameterTypeDTO() {
        return type;
    }

    public void setGameStateParameterTypeDTO(GameStateParameterTypeDTO gameStateParameterTypeDTO) {
        this.type = gameStateParameterTypeDTO;
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

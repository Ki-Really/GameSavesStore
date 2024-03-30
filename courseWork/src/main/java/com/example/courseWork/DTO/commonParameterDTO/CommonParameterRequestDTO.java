package com.example.courseWork.DTO.commonParameterDTO;

public class CommonParameterRequestDTO {
    private int gameStateParameterTypeId;
    private String label;
    private String description;

    public CommonParameterRequestDTO(int gameStateParameterTypeId, String label, String description) {
        this.gameStateParameterTypeId = gameStateParameterTypeId;
        this.label = label;
        this.description = description;
    }

    public CommonParameterRequestDTO() {
    }

    public int getGameStateParameterTypeId() {
        return gameStateParameterTypeId;
    }

    public void setGameStateParameterTypeId(int gameStateParameterTypeId) {
        this.gameStateParameterTypeId = gameStateParameterTypeId;
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

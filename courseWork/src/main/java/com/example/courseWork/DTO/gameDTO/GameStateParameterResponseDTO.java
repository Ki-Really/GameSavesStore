package com.example.courseWork.DTO.gameDTO;

import com.example.courseWork.DTO.commonParameterDTO.CommonParameterDTO;

public class GameStateParameterResponseDTO {
    private int id;

    private String key;

    private String type;

    private CommonParameterDTO commonParameter;

    private String label;

    private String description;

    public GameStateParameterResponseDTO( String key, String type,  String label, String description) {
        this.key = key;
        this.type = type;
        this.label = label;
        this.description = description;
    }

    public GameStateParameterResponseDTO() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public CommonParameterDTO getCommonParameter() {
        return commonParameter;
    }

    public void setCommonParameter(CommonParameterDTO commonParameterDTO) {
        this.commonParameter = commonParameterDTO;
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

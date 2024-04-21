package com.example.courseWork.DTO.graphicCommonDTO;

import com.example.courseWork.DTO.commonParameterDTO.CommonParameterDTO;

public class GraphicCommonDTO {
    private int id;
    private String visualType;
    private CommonParameterDTO commonParameter;

    public GraphicCommonDTO(int id, String visualType, CommonParameterDTO commonParameterDTO) {
        this.id = id;
        this.visualType = visualType;
        this.commonParameter = commonParameterDTO;
    }

    public GraphicCommonDTO() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVisualType() {
        return visualType;
    }

    public void setVisualType(String visualType) {
        this.visualType = visualType;
    }

    public CommonParameterDTO getCommonParameter() {
        return commonParameter;
    }

    public void setCommonParameter(CommonParameterDTO commonParameterDTO) {
        this.commonParameter = commonParameterDTO;
    }
}

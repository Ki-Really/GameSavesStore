package com.example.courseWork.DTO.graphicCommonDTO;

public class GraphicCommonRequestDTO {
    private String visualType;
    private int commonParameterId;

    public GraphicCommonRequestDTO(String visualType, int commonParameterId) {
        this.visualType = visualType;
        this.commonParameterId = commonParameterId;
    }

    public GraphicCommonRequestDTO() {
    }

    public String getVisualType() {
        return visualType;
    }

    public void setVisualType(String visualType) {
        this.visualType = visualType;
    }

    public int getCommonParameterId() {
        return commonParameterId;
    }

    public void setCommonParameterId(int commonParameterId) {
        this.commonParameterId = commonParameterId;
    }
}

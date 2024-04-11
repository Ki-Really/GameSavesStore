package com.example.courseWork.DTO.graphicCommonDTO;

public class GraphicCommonDTO {
    private int id;
    private String visualType;
    private int commonParameterId;

    public GraphicCommonDTO(int id, String visualType, int commonParameterId) {
        this.id = id;
        this.visualType = visualType;
        this.commonParameterId = commonParameterId;
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

    public int getCommonParameterId() {
        return commonParameterId;
    }

    public void setCommonParameterId(int commonParameterId) {
        this.commonParameterId = commonParameterId;
    }
}

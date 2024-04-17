package com.example.courseWork.DTO.graphicCommonDataDTO;

import com.example.courseWork.DTO.commonParameterDTO.CommonParameterDTO;

import java.util.List;

public class GraphicCommonPieChartGenderResponseDataDTO {
    private int id;

    private String visualType;
    private CommonParameterDTO commonParameter;
    private List<CommonPieChartDataDTO> data;

    public GraphicCommonPieChartGenderResponseDataDTO(int id, String visualType, CommonParameterDTO commonParameterDTO, List<CommonPieChartDataDTO> commonPieChartDataDTOList) {
        this.id = id;
        this.visualType = visualType;
        this.commonParameter = commonParameterDTO;
        this.data = commonPieChartDataDTOList;
    }

    public GraphicCommonPieChartGenderResponseDataDTO() {
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

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

    public List<CommonPieChartDataDTO> getData() {
        return data;
    }

    public void setData(List<CommonPieChartDataDTO> commonPieChartDataDTOList) {
        this.data = commonPieChartDataDTOList;
    }
}

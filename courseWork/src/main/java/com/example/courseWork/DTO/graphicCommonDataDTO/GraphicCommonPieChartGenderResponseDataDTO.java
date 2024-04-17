package com.example.courseWork.DTO.graphicCommonDataDTO;

import com.example.courseWork.DTO.commonParameterDTO.CommonParameterDTO;

import java.util.List;

public class GraphicCommonPieChartGenderResponseDataDTO {
    private String visualType;
    private CommonParameterDTO commonParameterDTO;
    private List<CommonPieChartDataDTO> commonPieChartDataDTOList;

    public GraphicCommonPieChartGenderResponseDataDTO(String visualType, CommonParameterDTO commonParameterDTO, List<CommonPieChartDataDTO> commonPieChartDataDTOList) {
        this.visualType = visualType;
        this.commonParameterDTO = commonParameterDTO;
        this.commonPieChartDataDTOList = commonPieChartDataDTOList;
    }

    public GraphicCommonPieChartGenderResponseDataDTO() {
    }

    public String getVisualType() {
        return visualType;
    }

    public void setVisualType(String visualType) {
        this.visualType = visualType;
    }

    public CommonParameterDTO getCommonParameterDTO() {
        return commonParameterDTO;
    }

    public void setCommonParameterDTO(CommonParameterDTO commonParameterDTO) {
        this.commonParameterDTO = commonParameterDTO;
    }

    public List<CommonPieChartDataDTO> getCommonPieChartDataDTOList() {
        return commonPieChartDataDTOList;
    }

    public void setCommonPieChartDataDTOList(List<CommonPieChartDataDTO> commonPieChartDataDTOList) {
        this.commonPieChartDataDTOList = commonPieChartDataDTOList;
    }
}

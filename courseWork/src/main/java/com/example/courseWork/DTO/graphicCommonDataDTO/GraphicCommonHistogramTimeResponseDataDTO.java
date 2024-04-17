package com.example.courseWork.DTO.graphicCommonDataDTO;

import com.example.courseWork.DTO.commonParameterDTO.CommonParameterDTO;

import java.util.List;

public class GraphicCommonHistogramTimeResponseDataDTO {
    private int id;

    private String visualType;
    private CommonParameterDTO commonParameter;
    private List<CommonHistogramDataDTO> data;

    public GraphicCommonHistogramTimeResponseDataDTO(int id, String visualType, CommonParameterDTO commonParameterDTO, List<CommonHistogramDataDTO> commonHistogramDataDTOList) {
        this.id = id;
        this.visualType = visualType;
        this.commonParameter = commonParameterDTO;
        this.data = commonHistogramDataDTOList;
    }

    public GraphicCommonHistogramTimeResponseDataDTO() {
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

    public List<CommonHistogramDataDTO> getData() {
        return data;
    }

    public void setData(List<CommonHistogramDataDTO> commonHistogramDataDTOList) {
        this.data = commonHistogramDataDTOList;
    }
}

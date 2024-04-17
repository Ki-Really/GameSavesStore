package com.example.courseWork.DTO.graphicCommonDataDTO;

import com.example.courseWork.DTO.commonParameterDTO.CommonParameterDTO;

import java.util.List;

public class GraphicCommonHistogramTimeResponseDataDTO {
    private String visualType;
    private CommonParameterDTO commonParameterDTO;
    private List<CommonHistogramDataDTO> commonHistogramDataDTOList;

    public GraphicCommonHistogramTimeResponseDataDTO(String visualType, CommonParameterDTO commonParameterDTO, List<CommonHistogramDataDTO> commonHistogramDataDTOList) {
        this.visualType = visualType;
        this.commonParameterDTO = commonParameterDTO;
        this.commonHistogramDataDTOList = commonHistogramDataDTOList;
    }

    public GraphicCommonHistogramTimeResponseDataDTO() {
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

    public List<CommonHistogramDataDTO> getCommonHistogramDataDTOList() {
        return commonHistogramDataDTOList;
    }

    public void setCommonHistogramDataDTOList(List<CommonHistogramDataDTO> commonHistogramDataDTOList) {
        this.commonHistogramDataDTOList = commonHistogramDataDTOList;
    }
}

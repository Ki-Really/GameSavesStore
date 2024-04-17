package com.example.courseWork.DTO.graphicCommonDataDTO;

public class CommonPieChartDataDTO {
    private int percentage;
    private String label;

    public CommonPieChartDataDTO(int percentage, String label) {
        this.percentage = percentage;
        this.label = label;
    }


    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}

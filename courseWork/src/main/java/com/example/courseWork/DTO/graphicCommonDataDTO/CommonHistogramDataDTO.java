package com.example.courseWork.DTO.graphicCommonDataDTO;

public class CommonHistogramDataDTO {
    private int min;
    private int max;
    private int height;

    public CommonHistogramDataDTO(int min, int max, int height) {
        this.min = min;
        this.max = max;
        this.height = height;
    }

    public CommonHistogramDataDTO() {
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}

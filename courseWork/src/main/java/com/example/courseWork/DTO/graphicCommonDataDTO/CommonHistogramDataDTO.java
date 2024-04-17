package com.example.courseWork.DTO.graphicCommonDataDTO;

public class CommonHistogramDataDTO {
    private double min;
    private double max;
    private int height;

    public CommonHistogramDataDTO(double min, double max, int height) {
        this.min = min;
        this.max = max;
        this.height = height;
    }

    public CommonHistogramDataDTO() {
    }

    public double getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public double getMax() {
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

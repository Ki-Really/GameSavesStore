package com.example.courseWork.DTO;

import jakarta.persistence.Column;

public class ExtractionPipelineDTO {

    private String type;
    private String inputFilename;
    private String outputFilename;

    public ExtractionPipelineDTO(String type, String inputFilename, String outputFilename) {
        this.type = type;
        this.inputFilename = inputFilename;
        this.outputFilename = outputFilename;
    }

    public ExtractionPipelineDTO() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getInputFilename() {
        return inputFilename;
    }

    public void setInputFilename(String inputFilename) {
        this.inputFilename = inputFilename;
    }

    public String getOutputFilename() {
        return outputFilename;
    }

    public void setOutputFilename(String outputFilename) {
        this.outputFilename = outputFilename;
    }
}

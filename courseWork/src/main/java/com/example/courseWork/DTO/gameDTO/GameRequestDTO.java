package com.example.courseWork.DTO.gameDTO;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class GameRequestDTO {

    @NotEmpty(message="Name of a game should not be empty!")
    @NotNull(message = "Name of a game should not be null!")
    private String name;
    private String description;
    private List<PathDTO> paths;
    private List<ExtractionPipelineDTO> extractionPipeline;
    private SchemeDTO schema;
    private String filename;
    private List<GameStateParameterDTO> gameStateParameters;

    public GameRequestDTO(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public GameRequestDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<PathDTO> getPaths() {
        return paths;
    }

    public void setPaths(List<PathDTO> paths) {
        this.paths = paths;
    }

    public List<ExtractionPipelineDTO> getExtractionPipeline() {
        return extractionPipeline;
    }

    public void setExtractionPipeline(List<ExtractionPipelineDTO> extractionPipeline) {
        this.extractionPipeline = extractionPipeline;
    }

    public SchemeDTO getSchema() {
        return schema;
    }

    public void setSchema(SchemeDTO schema) {
        this.schema = schema;
    }
}


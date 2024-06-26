package com.example.courseWork.DTO.gameDTO;

import java.util.List;

public class GameResponseDTO {
    private int id;
    private String name;
    private String description;
    private List<PathDTO> paths;
    private List<ExtractionPipelineDTO> extractionPipeline;
    private SchemeResponseDTO schema;
    private String imageUrl;

    public GameResponseDTO(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public GameResponseDTO() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public SchemeResponseDTO getSchema() {
        return schema;
    }

    public void setSchema(SchemeResponseDTO schema) {
        this.schema = schema;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

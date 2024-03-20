package com.example.courseWork.DTO.gameDTO;

import java.util.List;

public class GameRequestDTO {
    private String name;
    private String description;
    private List<PathDTO> paths;
    private List<ExtractionPipelineDTO> extractionPipeline;
    private SchemeDTO schema;



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




/*

name: string: // game name
        description: string;
        paths: [{path: string}]; //массив путей
        extractionPipeline: [{   // шаги при извлечении метаданных
        type: "sav-to-json" | "extract-from-json" // Будет приходить что то одно из этого
        }];
        schema: {           //список полей из Json которые надо извлечь
        filename: string;
        fields: [{ //схема для того, чтобы определять какие метаданные извлекать для определенной игры. //Отдельная таблица для fields с айдишниками к таблице со схемами.
        key: string;
        type: string;
        label: string;
        description: string;
        }]
        }
        image: Blob;*/

package com.example.courseWork.DTO;

import com.example.courseWork.models.ExtractionPipeline;
import com.example.courseWork.models.Path;
import jakarta.persistence.Lob;

import java.util.ArrayList;

public class GameAddRequestDTO {
    private String name;
    private String description;
    private ArrayList<PathDTO> paths;
    private ArrayList<ExtractionPipelineDTO> extractionPipelines;
    private SchemeDTO schema;



    public GameAddRequestDTO(String name, String description, ArrayList<PathDTO> paths, ArrayList<ExtractionPipelineDTO> extractionPipelines, SchemeDTO schema) {
        this.name = name;
        this.description = description;
        this.paths = paths;
        this.extractionPipelines = extractionPipelines;
        this.schema = schema;

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

    public ArrayList<PathDTO> getPaths() {
        return paths;
    }

    public void setPaths(ArrayList<PathDTO> paths) {
        this.paths = paths;
    }

    public ArrayList<ExtractionPipelineDTO> getExtractionPipelines() {
        return extractionPipelines;
    }

    public void setExtractionPipelines(ArrayList<ExtractionPipelineDTO> extractionPipelines) {
        this.extractionPipelines = extractionPipelines;
    }

    public SchemeDTO getScheme() {
        return schema;
    }

    public void setScheme(SchemeDTO schema) {
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

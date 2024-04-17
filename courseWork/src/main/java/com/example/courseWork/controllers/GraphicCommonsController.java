package com.example.courseWork.controllers;

import com.example.courseWork.DTO.entityDTO.EntitiesResponseDTO;
import com.example.courseWork.DTO.graphicCommonDTO.GraphicCommonDTO;
import com.example.courseWork.DTO.graphicCommonDTO.GraphicCommonRequestDTO;
import com.example.courseWork.DTO.graphicCommonDTO.GraphicCommonsRequestDTO;
import com.example.courseWork.DTO.graphicCommonDataDTO.GraphicCommonHistogramTimeResponseDataDTO;
import com.example.courseWork.DTO.graphicCommonDataDTO.GraphicCommonPieChartGenderResponseDataDTO;
import com.example.courseWork.services.graphicCommonServices.GraphicCommonsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class GraphicCommonsController {
    private final GraphicCommonsService graphicCommonsService;

    @Autowired
    public GraphicCommonsController(GraphicCommonsService graphicCommonsService) {
        this.graphicCommonsService = graphicCommonsService;
    }
    @PostMapping("/graphic/common")
    private ResponseEntity<GraphicCommonDTO> addCommonGraphic(@RequestBody GraphicCommonRequestDTO graphicCommonRequestDTO) throws JsonProcessingException {
        GraphicCommonDTO graphicCommonDTO = graphicCommonsService.save(graphicCommonRequestDTO);
        return ResponseEntity.ok(graphicCommonDTO);
    }
    @PatchMapping("/graphic/common/{id}")
    private ResponseEntity<GraphicCommonDTO> updateCommonGraphic(
            @PathVariable(name = "id") int id,
            @RequestBody GraphicCommonRequestDTO graphicCommonRequestDTO){
        GraphicCommonDTO graphicCommonDTO = graphicCommonsService.update(id,graphicCommonRequestDTO);
        return ResponseEntity.ok(graphicCommonDTO);
    }

    @DeleteMapping("/graphic/common/{id}")
    private ResponseEntity<HttpStatus> deleteCommonGraphic(@PathVariable(name = "id") int id) {
        graphicCommonsService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/graphic/common/{id}")
    private ResponseEntity<GraphicCommonDTO> getCommonGraphic(@PathVariable(name = "id") int id) {
        GraphicCommonDTO graphicCommonDTO = graphicCommonsService.findById(id);
        return ResponseEntity.ok(graphicCommonDTO);
    }

    @GetMapping("/graphic/common")
    private ResponseEntity<EntitiesResponseDTO<GraphicCommonDTO>> findCommonParameters(
            @RequestParam(value = "searchQuery",required = false) String searchQuery,
            @RequestParam(value = "pageSize") Integer pageSize,
            @RequestParam(value = "pageNumber") Integer pageNumber){

        GraphicCommonsRequestDTO graphicCommonsRequestDTO = new GraphicCommonsRequestDTO(
                searchQuery, pageSize, pageNumber
        );

        EntitiesResponseDTO<GraphicCommonDTO> graphicCommonResponseDTO =
                graphicCommonsService.findAll(graphicCommonsRequestDTO);

        return ResponseEntity.ok(graphicCommonResponseDTO);
    }

    @GetMapping("/graphic-data/histogram/common/{id}")
    private ResponseEntity<GraphicCommonHistogramTimeResponseDataDTO> getHistogramData(@PathVariable(name = "id") int id){
        GraphicCommonHistogramTimeResponseDataDTO graphicCommonDataDTO = graphicCommonsService.getHistogramTimeData(id);
        return ResponseEntity.ok(graphicCommonDataDTO);
    }

    @GetMapping("/graphic-data/pie_chart/common/{id}")
    private ResponseEntity<GraphicCommonPieChartGenderResponseDataDTO> getPieChartData(@PathVariable(name = "id") int id){
        GraphicCommonPieChartGenderResponseDataDTO graphicCommonDataDTO = graphicCommonsService.getPieChartGenderData(id);
        return ResponseEntity.ok(graphicCommonDataDTO);
    }
}

package com.example.courseWork.controllers;

import com.example.courseWork.DTO.commonParameterDTO.CommonParameterDTO;
import com.example.courseWork.DTO.commonParameterDTO.CommonParametersRequestDTO;
import com.example.courseWork.DTO.entityDTO.EntitiesResponseDTO;
import com.example.courseWork.DTO.graphicCommonDTO.GraphicCommonDTO;
import com.example.courseWork.DTO.graphicCommonDTO.GraphicCommonRequestDTO;
import com.example.courseWork.DTO.graphicCommonDTO.GraphicCommonsRequestDTO;
import com.example.courseWork.services.graphicCommonServices.GraphicCommonsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/graphic/common")
public class GraphicCommonsController {
    private final GraphicCommonsService graphicCommonsService;

    @Autowired
    public GraphicCommonsController(GraphicCommonsService graphicCommonsService) {
        this.graphicCommonsService = graphicCommonsService;
    }
    @PostMapping
    private ResponseEntity<GraphicCommonDTO> addCommonGraphic(@RequestBody GraphicCommonRequestDTO graphicCommonRequestDTO) throws JsonProcessingException {
        GraphicCommonDTO graphicCommonDTO = graphicCommonsService.save(graphicCommonRequestDTO);
        return ResponseEntity.ok(graphicCommonDTO);
    }
    @PatchMapping("/{id}")
    private ResponseEntity<GraphicCommonDTO> updateCommonGraphic(
            @PathVariable(name = "id") int id,
            @RequestBody GraphicCommonRequestDTO graphicCommonRequestDTO){
        GraphicCommonDTO graphicCommonDTO = graphicCommonsService.update(id,graphicCommonRequestDTO);
        return ResponseEntity.ok(graphicCommonDTO);
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<HttpStatus> deleteCommonGraphic(@PathVariable(name = "id") int id) {
        graphicCommonsService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping
    private ResponseEntity<EntitiesResponseDTO<GraphicCommonDTO>> findCommonParameters(
            @RequestParam(value = "searchQuery",required = false) String searchQuery,
            @RequestParam(value = "pageSize") Integer pageSize,
            @RequestParam(value = "pageNumber") Integer pageNumber){
        GraphicCommonsRequestDTO graphicCommonsRequestDTO = new GraphicCommonsRequestDTO(
                searchQuery, pageSize, pageNumber
        );
        //EntitiesResponseDTO<GraphicCommonDTO> graphicCommonResponseDTO = graphicCommonsService.findAll(graphicCommonsRequestDTO);

        return ResponseEntity.ok(null);
    }

}

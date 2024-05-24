package com.example.courseWork.controllers;

import com.example.courseWork.DTO.entityDTO.EntitiesResponseDTO;
import com.example.courseWork.DTO.graphicCommonDTO.GraphicCommonDTO;
import com.example.courseWork.DTO.graphicCommonDTO.GraphicCommonRequestDTO;
import com.example.courseWork.DTO.graphicCommonDTO.GraphicCommonsRequestDTO;
import com.example.courseWork.DTO.graphicCommonDataDTO.GraphicDataResponse;
import com.example.courseWork.services.graphicCommonServices.GraphicCommonsService;
import com.example.courseWork.util.exceptions.graphicCommonException.GraphicCommonBadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping
public class GraphicCommonsController {
    private final GraphicCommonsService graphicCommonsService;

    @Autowired
    public GraphicCommonsController(GraphicCommonsService graphicCommonsService) {
        this.graphicCommonsService = graphicCommonsService;
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/graphic/common")
    private ResponseEntity<GraphicCommonDTO> addCommonGraphic(@RequestBody GraphicCommonRequestDTO graphicCommonRequestDTO, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            List<FieldError> errors = bindingResult.getFieldErrors();
            List<String> stringErrors = new LinkedList<>();
            for(FieldError error : errors){
                stringErrors.add(error.getField() + " - " + error.getDefaultMessage()+";");
            }
            throw new GraphicCommonBadRequestException("Graphic adding failed!", stringErrors);
        }
        GraphicCommonDTO graphicCommonDTO = graphicCommonsService.save(graphicCommonRequestDTO);
        return ResponseEntity.ok(graphicCommonDTO);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/graphic/common/{id}")
    private ResponseEntity<GraphicCommonDTO> updateCommonGraphic(
            @PathVariable(name = "id") int id,
            @RequestBody GraphicCommonRequestDTO graphicCommonRequestDTO){
        GraphicCommonDTO graphicCommonDTO = graphicCommonsService.update(id,graphicCommonRequestDTO);
        return ResponseEntity.ok(graphicCommonDTO);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/graphic/common/{id}")
    private ResponseEntity<HttpStatus> deleteCommonGraphic(@PathVariable(name = "id") int id) {
        graphicCommonsService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/graphic/common/{id}")
    private ResponseEntity<GraphicCommonDTO> getCommonGraphic(@PathVariable(name = "id") int id) {
        GraphicCommonDTO graphicCommonDTO = graphicCommonsService.findById(id);
        return ResponseEntity.ok(graphicCommonDTO);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
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

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/graphic-data/common/{id}")
    private ResponseEntity<GraphicDataResponse> getGraphicData(@PathVariable(name = "id") int id) throws Exception {
       GraphicDataResponse graphicDataResponse;
       if(graphicCommonsService.findVisualTypeById(id).equals("histogram")){
           graphicDataResponse = graphicCommonsService.getHistogramTimeData(id);
       }else if(graphicCommonsService.findVisualTypeById(id).equals("pie_chart")){
           graphicDataResponse = graphicCommonsService.getPieChartGenderData(id);
       }else{
           throw new Exception("Internal");
       }
        return ResponseEntity.ok(graphicDataResponse);
    }
}

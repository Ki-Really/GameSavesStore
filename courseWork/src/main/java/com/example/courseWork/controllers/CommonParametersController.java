package com.example.courseWork.controllers;

import com.example.courseWork.DTO.commonParameterDTO.CommonParameterDTO;
import com.example.courseWork.DTO.commonParameterDTO.CommonParameterRequestDTO;
import com.example.courseWork.DTO.commonParameterDTO.CommonParametersRequestDTO;
import com.example.courseWork.DTO.entityDTO.EntitiesResponseDTO;
import com.example.courseWork.models.commonParameters.CommonParameter;
import com.example.courseWork.services.commonParameterServices.CommonParametersService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/common-parameters")
public class CommonParametersController {
    private final ObjectMapper objectMapper;
    private final CommonParametersService commonParametersService;
    @Autowired
    public CommonParametersController(ObjectMapper objectMapper, CommonParametersService commonParametersService) {
        this.objectMapper = objectMapper;
        this.commonParametersService = commonParametersService;
    }

    @PostMapping
    private ResponseEntity<CommonParameterDTO> addCommonParameter(@RequestParam("commonParameterData") String commonParameterData) throws JsonProcessingException {
        CommonParameterRequestDTO commonParameterRequestDTO = objectMapper.readValue(commonParameterData, CommonParameterRequestDTO.class);
        CommonParameter commonParameter = commonParametersService.save(commonParameterRequestDTO);
        CommonParameterDTO commonParameterDTO = commonParametersService.constructCommonParameterDTO(commonParameter);

        return ResponseEntity.ok(commonParameterDTO);
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<HttpStatus> deleteCommonParameter(@PathVariable(name = "id") int id){
        commonParametersService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    private ResponseEntity<CommonParameterDTO> updateCommonParameter(@RequestParam("commonParameterData") String commonParameterData,
                                                                     @PathVariable(name ="id") int id) throws JsonProcessingException {
        CommonParameterRequestDTO commonParameterRequestDTO = objectMapper.readValue(commonParameterData, CommonParameterRequestDTO.class);
        CommonParameter commonParameter = commonParametersService.update(commonParameterRequestDTO,id);

        CommonParameterDTO commonParameterDTO = commonParametersService.constructCommonParameterDTO(commonParameter);
        return ResponseEntity.ok(commonParameterDTO);
    }

    @GetMapping
    private ResponseEntity<EntitiesResponseDTO<CommonParameterDTO>> findCommonParameters(
            @RequestParam(value = "searchQuery",required = false) String searchQuery,
            @RequestParam(value = "pageSize") Integer pageSize,
            @RequestParam(value = "pageNumber") Integer pageNumber){
        CommonParametersRequestDTO commonParametersRequestDTO = new CommonParametersRequestDTO(
                searchQuery, pageSize, pageNumber
        );
        EntitiesResponseDTO<CommonParameterDTO> commonParametersResponseDTO = commonParametersService.findAll(commonParametersRequestDTO);

        return ResponseEntity.ok(commonParametersResponseDTO);
    }
}

package com.example.courseWork.controllers;

import com.example.courseWork.DTO.commonParameterDTO.CommonParameterDTO;
import com.example.courseWork.DTO.commonParameterDTO.CommonParameterRequestDTO;
import com.example.courseWork.DTO.commonParameterDTO.CommonParametersRequestDTO;
import com.example.courseWork.DTO.entityDTO.EntitiesResponseDTO;
import com.example.courseWork.models.commonParameters.CommonParameter;
import com.example.courseWork.services.commonParameterServices.CommonParametersService;
import com.example.courseWork.util.exceptions.commonParameterException.CommonParameterBadRequestException;
import com.example.courseWork.util.exceptions.commonParameterException.CommonParameterDeleteException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/common-parameters")
public class CommonParametersController {
    private final ObjectMapper objectMapper;
    private final CommonParametersService commonParametersService;
    private final Validator validator;

    @Autowired
    public CommonParametersController(ObjectMapper objectMapper, CommonParametersService commonParametersService, Validator validator) {
        this.objectMapper = objectMapper;
        this.commonParametersService = commonParametersService;
        this.validator = validator;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    private ResponseEntity<CommonParameterDTO> addCommonParameter(@RequestParam("commonParameterData")
                                                                  String commonParameterData) {

        CommonParameterRequestDTO commonParameterRequestDTO;
        try {
            commonParameterRequestDTO = objectMapper.readValue(commonParameterData, CommonParameterRequestDTO.class);
            Set<ConstraintViolation<CommonParameterRequestDTO>> violations = validator.validate(commonParameterRequestDTO);
            BindingResult bindingResult = new BeanPropertyBindingResult(commonParameterRequestDTO, "commonParameterRequestDTO");

            for (ConstraintViolation<CommonParameterRequestDTO> violation : violations) {
                String field = violation.getPropertyPath().toString();
                bindingResult.rejectValue(field,"",violation.getMessage());
            }

            if(bindingResult.hasErrors()){
                List<FieldError> errors = bindingResult.getFieldErrors();
                List<String> stringErrors = new LinkedList<>();
                for(FieldError error : errors){
                    stringErrors.add(error.getField() + " - " + error.getDefaultMessage()+";");
                }
                throw new CommonParameterBadRequestException("Common parameter type adding failed!", stringErrors);
            }
            CommonParameter commonParameter = commonParametersService.save(commonParameterRequestDTO);
            CommonParameterDTO commonParameterDTO = commonParametersService.constructCommonParameterDTO(commonParameter);
            return ResponseEntity.ok(commonParameterDTO);
        } catch (JsonProcessingException e){
            throw new RuntimeException(e);
        }
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    private ResponseEntity<HttpStatus> deleteCommonParameter(@PathVariable(name = "id") int id){
        commonParametersService.delete(id);
        if(commonParametersService.findById(id) == null){
            return ResponseEntity.ok(HttpStatus.OK);
        }else{
            throw new CommonParameterDeleteException("Common parameter was not deleted!");
        }
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/{id}")
    private ResponseEntity<CommonParameterDTO> updateCommonParameter(@RequestParam("commonParameterData") String commonParameterData,
                                                                     @PathVariable(name ="id") int id){
        CommonParameterRequestDTO commonParameterRequestDTO;

        try {
            commonParameterRequestDTO = objectMapper.readValue(commonParameterData, CommonParameterRequestDTO.class);
            Set<ConstraintViolation<CommonParameterRequestDTO>> violations = validator.validate(commonParameterRequestDTO);
            BindingResult bindingResult = new BeanPropertyBindingResult(commonParameterRequestDTO, "commonParameterRequestDTO");

            for (ConstraintViolation<CommonParameterRequestDTO> violation : violations) {
                String field = violation.getPropertyPath().toString();
                bindingResult.rejectValue(field,"",violation.getMessage());
            }
            if(bindingResult.hasErrors()){
                List<FieldError> errors = bindingResult.getFieldErrors();
                List<String> stringErrors = new LinkedList<>();
                for(FieldError error : errors){
                    stringErrors.add(error.getField() + " - " + error.getDefaultMessage()+";");
                }
                throw new CommonParameterBadRequestException("Common parameter type updating failed!", stringErrors);
            }

            CommonParameter commonParameter = commonParametersService.update(commonParameterRequestDTO,id);
            CommonParameterDTO commonParameterDTO = commonParametersService.constructCommonParameterDTO(commonParameter);
            return ResponseEntity.ok(commonParameterDTO);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
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

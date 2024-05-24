package com.example.courseWork.controllers;

import com.example.courseWork.DTO.entityDTO.EntitiesResponseDTO;
import com.example.courseWork.DTO.gameDTO.GameStateParameterTypeDTO;
import com.example.courseWork.DTO.gameStateParameterTypeDTO.GameStateParameterTypesRequestDTO;
import com.example.courseWork.services.gameServices.GameStateParameterTypesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/game-state-parameter-types")
public class GameStateParameterTypesController {
    private final GameStateParameterTypesService gameStateParameterTypesService;
    @Autowired
    public GameStateParameterTypesController(GameStateParameterTypesService gameStateParameterTypesService) {
        this.gameStateParameterTypesService = gameStateParameterTypesService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    private ResponseEntity<EntitiesResponseDTO<GameStateParameterTypeDTO>> findGameStateParameterTypes(
            @RequestParam(value = "searchQuery") String searchQuery,
            @RequestParam(value = "pageSize") Integer pageSize,
            @RequestParam(value = "pageNumber") Integer pageNumber){
        GameStateParameterTypesRequestDTO gameStateParameterTypesRequestDTO = new GameStateParameterTypesRequestDTO(
                searchQuery, pageSize, pageNumber
        );
        EntitiesResponseDTO<GameStateParameterTypeDTO> gameStateParameterTypesResponseDTO = gameStateParameterTypesService.findAll(gameStateParameterTypesRequestDTO);
        return ResponseEntity.ok(gameStateParameterTypesResponseDTO);
    }
}

package com.example.courseWork.controllers;

import com.example.courseWork.DTO.sharedSaveDTO.GameStateSharesResponseDTO;
import com.example.courseWork.DTO.sharedSaveDTO.ShareWithDTO;
import com.example.courseWork.models.sharedSave.SharedSave;
import com.example.courseWork.DTO.sharedSaveDTO.GameStateShareResponseDTO;
import com.example.courseWork.services.gameStateSharesServices.GameStateSharesService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/game-state-shares")
public class SharedSavesController{
    private final ObjectMapper objectMapper;
    private final GameStateSharesService gameStateSharesService;

    @Autowired
    public SharedSavesController(ObjectMapper objectMapper, GameStateSharesService gameStateSharesService) {
        this.objectMapper = objectMapper;
        this.gameStateSharesService = gameStateSharesService;
    }

    @PostMapping
    private ResponseEntity<GameStateShareResponseDTO> addGameStateShare(@RequestParam("gameStateSharedData") String gameStateSharedData) throws JsonProcessingException {
        ShareWithDTO shareWithDTO = objectMapper.readValue(gameStateSharedData, ShareWithDTO.class);

        SharedSave sharedSave = gameStateSharesService.save(shareWithDTO);

        GameStateShareResponseDTO gameStateShareResponseDTO = gameStateSharesService.constructResponseDTO(sharedSave);
        return ResponseEntity.ok(gameStateShareResponseDTO);
    }

    @DeleteMapping("{id}")
    private ResponseEntity<HttpStatus> deleteStateShare(@PathVariable("id") int id){
        gameStateSharesService.deleteById(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("{id}")
    private ResponseEntity<GameStateSharesResponseDTO> getSharedSaves(@PathVariable("id") int id){
        GameStateSharesResponseDTO gameStateSharesResponseDTO = gameStateSharesService.getGameStateShares(id);
        return ResponseEntity.ok(gameStateSharesResponseDTO);
    }
}

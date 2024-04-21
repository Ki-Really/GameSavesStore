package com.example.courseWork.controllers;

import com.example.courseWork.DTO.entityDTO.EntitiesResponseDTO;
import com.example.courseWork.DTO.gameSaveDTO.GameStateRequestDTO;
import com.example.courseWork.DTO.gameSaveDTO.GameStateDTO;
import com.example.courseWork.DTO.gameSaveDTO.GameStatesRequestDTO;
import com.example.courseWork.models.gameSaveModel.GameState;
import com.example.courseWork.services.gameStateServices.GameStatesService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@RestController
@RequestMapping("/game-saves")
public class GameStatesController {
    private final ObjectMapper objectMapper;
    private final GameStatesService gameStatesService;

    @Autowired
    public GameStatesController(ObjectMapper objectMapper, GameStatesService gameStatesService) {
        this.objectMapper = objectMapper;
        this.gameStatesService = gameStatesService;
    }

    @PostMapping
    private ResponseEntity<GameStateDTO> addGameState(@RequestPart("archive") MultipartFile file,
                                                     @RequestParam("gameStateData") String gameStatesData, Principal principal) throws JsonProcessingException {
        GameStateRequestDTO addGameStateDTO = objectMapper.readValue(gameStatesData, GameStateRequestDTO.class);

        int gameStateId = gameStatesService.save(addGameStateDTO,file,principal);

        GameState gameState = gameStatesService.findById(gameStateId);
        GameStateDTO gameStateDTO = gameStatesService.constructGameStateDTO(gameState);

        return ResponseEntity.ok(gameStateDTO);
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<HttpStatus> deleteGameState(@PathVariable(name ="id") int id,Principal principal) throws IOException {
        gameStatesService.deleteById(id,principal);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    private ResponseEntity<GameStateDTO> updateGameState(@RequestPart("archive") MultipartFile file,
                                               @RequestParam("gameStateData") String gameStateData,
                                               @PathVariable(name ="id") int id,Principal principal) throws IOException {
        GameStateRequestDTO gameStateRequestDTO = objectMapper.readValue(gameStateData, GameStateRequestDTO.class);
        gameStatesService.update(gameStateRequestDTO,file,id,principal);

        GameState gameState = gameStatesService.findById(id);
        GameStateDTO gameStateDTO = gameStatesService.constructGameStateDTO(gameState);

        return ResponseEntity.ok(gameStateDTO);
    }

    @GetMapping("/{id}")
    private ResponseEntity<GameStateDTO> findGameStateById(@PathVariable(name ="id") int id){
        GameState gameState = gameStatesService.findById(id);

        GameStateDTO gameStateDTO = gameStatesService.constructGameStateDTO(gameState);
        return ResponseEntity.ok(gameStateDTO);
    }

    @GetMapping
    private ResponseEntity<EntitiesResponseDTO> findGameStates(
            @RequestParam(value = "searchQuery") String searchQuery,
            @RequestParam(value = "pageSize") Integer pageSize,
            @RequestParam(value = "pageNumber") Integer pageNumber,
            Principal principal){
        GameStatesRequestDTO gameStatesRequestDTO = new GameStatesRequestDTO(
                searchQuery, pageSize, pageNumber);
        EntitiesResponseDTO<GameStateDTO> gameStatesDTO = gameStatesService.findAll(gameStatesRequestDTO,principal);

        return ResponseEntity.ok(gameStatesDTO);
    }

    @GetMapping("/public")
    private ResponseEntity<EntitiesResponseDTO<GameStateDTO>> findPublicGameStates(
            @RequestParam(value = "searchQuery",required = false) String searchQuery,
            @RequestParam(value = "pageSize") Integer pageSize,
            @RequestParam(value = "pageNumber") Integer pageNumber,
            @RequestParam(value = "searchGameId",required = false) Integer searchGameId) {
        GameStatesRequestDTO gameStatesRequestDTO = new GameStatesRequestDTO(
                searchQuery, pageSize, pageNumber,
                searchGameId);
        EntitiesResponseDTO<GameStateDTO> gameStatesDTO = gameStatesService.findAllPublic(gameStatesRequestDTO);

        return ResponseEntity.ok(gameStatesDTO);
    }


    @GetMapping("/received-game-state-shares")
    private ResponseEntity<EntitiesResponseDTO<GameStateDTO>> findReceivedGameStates(
            @RequestParam(value = "searchQuery") String searchQuery,
            @RequestParam(value = "pageSize") Integer pageSize,
            @RequestParam(value = "pageNumber") Integer pageNumber,Principal principal){
        GameStatesRequestDTO gameStatesRequestDTO = new GameStatesRequestDTO(
                searchQuery, pageSize, pageNumber);
        EntitiesResponseDTO<GameStateDTO> gameStatesDTO = gameStatesService.findReceivedGameStates(gameStatesRequestDTO,principal);

        return ResponseEntity.ok(gameStatesDTO);
    }



}

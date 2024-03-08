package com.example.courseWork.controllers;

import com.example.courseWork.DTO.gameDTO.GameDTO;
import com.example.courseWork.DTO.gameDTO.GameResponseDTO;
import com.example.courseWork.DTO.gameDTO.GamesRequestDTO;
import com.example.courseWork.DTO.gameDTO.GamesResponseDTO;
import com.example.courseWork.DTO.gameSaveDTO.AddGameSaveDTO;
import com.example.courseWork.DTO.gameSaveDTO.GameSaveDTO;
import com.example.courseWork.models.gameModel.Game;
import com.example.courseWork.models.gameSaveModel.GameSave;
import com.example.courseWork.services.gameSaveServices.GameSavesService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/game-saves")
public class GameSavesController {
    private final ObjectMapper objectMapper;
    private final GameSavesService gameSavesService;

    @Autowired
    public GameSavesController(ObjectMapper objectMapper, GameSavesService gameSavesService) {
        this.objectMapper = objectMapper;
        this.gameSavesService = gameSavesService;
    }

    @PostMapping
    private ResponseEntity<GameSaveDTO> saveGameSaving(@RequestPart("archive") MultipartFile file,
                                                       @RequestParam @Valid String gameSavesData) throws JsonProcessingException {
        AddGameSaveDTO addGameSaveDTO = objectMapper.readValue(gameSavesData, AddGameSaveDTO.class);
        gameSavesService.save(addGameSaveDTO,file);
        GameSave gameSave = gameSavesService.findByName(addGameSaveDTO.getName());
        GameSaveDTO gameSaveDTO = new GameSaveDTO();

        return ResponseEntity.ok(gameSaveDTO);
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<HttpStatus> deleteGameSave(@PathVariable(name ="id") int id) throws IOException {


        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    private ResponseEntity<HttpStatus> updateGameSave(@PathVariable(name ="id") int id) throws IOException {


        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/{id}")
    private ResponseEntity findGameById(@PathVariable(name ="id") int id){

        return null;
    }

    @GetMapping
    private ResponseEntity findGames(){


        return null;
    }

}

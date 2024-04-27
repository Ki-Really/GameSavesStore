package com.example.courseWork.controllers;

import com.example.courseWork.DTO.entityDTO.EntitiesResponseDTO;
import com.example.courseWork.DTO.gameDTO.*;
import com.example.courseWork.models.gameModel.Game;
import com.example.courseWork.services.gameServices.GamesService;
import com.example.courseWork.util.exceptions.gameException.GameBadRequestException;
import com.example.courseWork.util.exceptions.gameException.GameNotFoundException;
import com.example.courseWork.util.validators.gameValidator.UniqueGameNameValidator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/games")
public class GamesController {
    private final ObjectMapper objectMapper;
    private final GamesService gamesService;
    private final UniqueGameNameValidator uniqueGameNameValidator;

    @Autowired
    public GamesController(ObjectMapper objectMapper, GamesService gamesService, UniqueGameNameValidator uniqueGameNameValidator) {
        this.objectMapper = objectMapper;
        this.gamesService = gamesService;
        this.uniqueGameNameValidator = uniqueGameNameValidator;
    }

    @PostMapping
    private ResponseEntity<GameDTO> addGame(@RequestPart("image") MultipartFile file,
                                            @RequestParam("gameData") String gameData, BindingResult bindingResult) {

        uniqueGameNameValidator.validate(gameData,bindingResult);

        if(bindingResult.hasErrors()){
            List<FieldError> errors = bindingResult.getFieldErrors();
            List<String> stringErrors = new LinkedList<>();
            for(FieldError error : errors){
                stringErrors.add(error.getField() + " - " + error.getDefaultMessage()+";");
            }
            throw new GameBadRequestException("Game adding failed!", stringErrors);
        }
        GameRequestDTO gameRequestDTO;
        try {
            gameRequestDTO = objectMapper.readValue(gameData, GameRequestDTO.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        gamesService.save(gameRequestDTO,file);
        Game game = gamesService.findByName(gameRequestDTO.getName());
        GameDTO gameDTO = new GameDTO(game.getId(),game.getName());
        return ResponseEntity.ok(gameDTO);
    }

    @PatchMapping("/{id}")
    private ResponseEntity<GameDTO> updateGame(@RequestPart(value = "image", required = false) MultipartFile file,
                                               @RequestParam("gameData") String gameData,
                                               @PathVariable(name ="id") int id, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            List<FieldError> errors = bindingResult.getFieldErrors();
            List<String> stringErrors = new LinkedList<>();
            for(FieldError error : errors){
                stringErrors.add(error.getField() + " - " + error.getDefaultMessage()+";");
            }
            throw new GameBadRequestException("Game updating failed!", stringErrors);
        }
        GameRequestDTO gameRequestDTO;
        try {
            gameRequestDTO = objectMapper.readValue(gameData, GameRequestDTO.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        gamesService.update(gameRequestDTO,file,id);

        Game game = gamesService.findByName(gameRequestDTO.getName());
        GameDTO gameDTO = new GameDTO(game.getId(),game.getName());
        return ResponseEntity.ok(gameDTO);
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<HttpStatus> deleteGame(@PathVariable(name ="id") int id) {
        gamesService.deleteById(id);
        if(gamesService.findOne(id)== null){
            return ResponseEntity.ok(HttpStatus.OK);
        }else{
            throw new GameNotFoundException("Game not found!");
        }
    }

    @GetMapping
    private ResponseEntity<EntitiesResponseDTO<GameResponseDTO>> findGames(
        @RequestParam(value = "searchQuery") String searchQuery,
        @RequestParam(value = "pageSize") Integer pageSize,
        @RequestParam(value = "pageNumber") Integer pageNumber){
        GamesRequestDTO gamesRequestDTO = new GamesRequestDTO(
            searchQuery, pageSize, pageNumber
        );
        EntitiesResponseDTO<GameResponseDTO> gamesResponseDTO = gamesService.findAll(gamesRequestDTO);

        return ResponseEntity.ok(gamesResponseDTO);
    }

    @GetMapping("/{id}")
    private ResponseEntity<GameResponseDTO> findGameById(@PathVariable(name ="id") int id){
        Game game = gamesService.findOne(id);

        GameResponseDTO gameResponseDTO = gamesService.constructGame(game);
        return ResponseEntity.ok(gameResponseDTO);
    }
}





/*
{
        "name":"name",
        "description":"description",
        "paths":[{
        "path":"path"
        }],
        "extractionPipeline": [{
        "type":"sav-to-json"
        }],
        "schema": {
        "filename":"filename",
        "fields":[{
        "key":"key",
        "type":"type",
        "label":"label",
        "description":"description"
        }]
        }
        }*/

package com.example.courseWork.controllers;

import com.example.courseWork.DTO.ExtractionPipelineDTO;
import com.example.courseWork.DTO.GameAddRequestDTO;
import com.example.courseWork.DTO.GameDTO;
import com.example.courseWork.DTO.PathDTO;
import com.example.courseWork.models.*;
import com.example.courseWork.services.GamesService;
import com.example.courseWork.services.ImagesService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/games")
public class GamesController {
    private final ObjectMapper objectMapper;
    private final GamesService gamesService;
    @Autowired
    public GamesController(ObjectMapper objectMapper, GamesService gamesService) {
        this.objectMapper = objectMapper;
        this.gamesService = gamesService;
    }

    @PostMapping
    private ResponseEntity<GameDTO> addGame(@RequestPart("image") MultipartFile file,
                                            @RequestParam("gameData") String gameData) throws IOException {
        GameAddRequestDTO gameAddRequestDTO = objectMapper.readValue(gameData, GameAddRequestDTO.class);
        gamesService.save(gameAddRequestDTO,file);
        Game game = gamesService.findByName(gameAddRequestDTO.getName());
        GameDTO gameDTO = new GameDTO(game.getId(),game.getName());

        return ResponseEntity.ok(gameDTO);
    }

    @GetMapping("/{id}")
    private ResponseEntity<GameAddRequestDTO> findGameById(@PathVariable(name ="id") int id){
        System.out.println("Here");
        Game game = gamesService.findOne(id);

        GameAddRequestDTO gameAddRequestDTO = gamesService.constructGame(game);
        return ResponseEntity.ok(gameAddRequestDTO);
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

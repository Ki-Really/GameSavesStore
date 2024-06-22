package com.example.courseWork.controllers;

import com.example.courseWork.DTO.entityDTO.EntitiesResponseDTO;
import com.example.courseWork.DTO.gameDTO.*;
import com.example.courseWork.models.gameModel.Game;
import com.example.courseWork.services.gameServices.GamesService;
import com.example.courseWork.util.exceptions.gameException.GameBadRequestException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.util.*;

@RestController
@RequestMapping("/games")
public class GamesController {
    private final ObjectMapper objectMapper;
    private final GamesService gamesService;

    private final Validator validator;

    @Autowired
    public GamesController(ObjectMapper objectMapper, GamesService gamesService, Validator validator) {
        this.objectMapper = objectMapper;
        this.gamesService = gamesService;
        this.validator = validator;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<GameDTO> addGame(@RequestPart("image") MultipartFile file,
                                            @RequestParam("gameData") String gameData) {
        GameRequestDTO gameRequestDTO;
        try {
            gameRequestDTO = objectMapper.readValue(gameData, GameRequestDTO.class);
            Set<ConstraintViolation<GameRequestDTO>> violations = validator.validate(gameRequestDTO);
            BindingResult bindingResult = new BeanPropertyBindingResult(gameRequestDTO, "gameRequestDTO");

            for (ConstraintViolation<GameRequestDTO> violation : violations) {
                String field = violation.getPropertyPath().toString();
                bindingResult.rejectValue(field,"",violation.getMessage());
            }

            if(bindingResult.hasErrors()){
                List<FieldError> errors = bindingResult.getFieldErrors();
                List<String> stringErrors = new LinkedList<>();
                for(FieldError error : errors){
                    stringErrors.add(error.getField() + " - " + error.getDefaultMessage()+";");
                }
                throw new GameBadRequestException("Game adding failed!", stringErrors);
            }

            gamesService.save(gameRequestDTO,file);
            Game game = gamesService.findByName(gameRequestDTO.getName());
            GameDTO gameDTO = new GameDTO(game.getId(),game.getName());
            return ResponseEntity.ok(gameDTO);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<GameDTO> updateGame(@RequestPart(value = "image", required = false) MultipartFile file,
                                               @RequestParam("gameData") String gameData,
                                               @PathVariable(name ="id") int id) {
        GameRequestDTO gameRequestDTO;
        try {
            gameRequestDTO = objectMapper.readValue(gameData, GameRequestDTO.class);
            Set<ConstraintViolation<GameRequestDTO>> violations = validator.validate(gameRequestDTO);
            BindingResult bindingResult = new BeanPropertyBindingResult(gameRequestDTO, "gameRequestDTO");

            for (ConstraintViolation<GameRequestDTO> violation : violations) {
                String field = violation.getPropertyPath().toString();
                bindingResult.rejectValue(field,"", violation.getMessage());
            }
            if(bindingResult.hasErrors()){
                List<FieldError> errors = bindingResult.getFieldErrors();
                List<String> stringErrors = new LinkedList<>();
                for(FieldError error : errors){
                    stringErrors.add(error.getField() + " - " + error.getDefaultMessage()+";");
                }
                throw new GameBadRequestException("Game updating failed!", stringErrors);
            }

            gamesService.update(gameRequestDTO,file,id);
            Game game = gamesService.findByName(gameRequestDTO.getName());
            GameDTO gameDTO = new GameDTO(game.getId(),game.getName());
            return ResponseEntity.ok(gameDTO);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteGame(@PathVariable(name ="id") int id) {
        gamesService.deleteById(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity<EntitiesResponseDTO<GameResponseDTO>> findGames(
        @RequestParam(value = "searchQuery") String searchQuery,
        @RequestParam(value = "pageSize") Integer pageSize,
        @RequestParam(value = "pageNumber") Integer pageNumber){
        GamesRequestDTO gamesRequestDTO = new GamesRequestDTO(
            searchQuery, pageSize, pageNumber
        );
        EntitiesResponseDTO<GameResponseDTO> gamesResponseDTO = gamesService.findAll(gamesRequestDTO);

        return ResponseEntity.ok(gamesResponseDTO);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public ResponseEntity<GameResponseDTO> findGameById(@PathVariable(name ="id") int id){
        Game game = gamesService.findOne(id);

        GameResponseDTO gameResponseDTO = gamesService.constructGame(game);
        return ResponseEntity.ok(gameResponseDTO);
    }
}

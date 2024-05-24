package com.example.courseWork.controllers;

import com.example.courseWork.DTO.entityDTO.EntitiesResponseDTO;
import com.example.courseWork.DTO.sharedSaveDTO.ShareWithDTO;
import com.example.courseWork.models.sharedSave.SharedSave;
import com.example.courseWork.DTO.sharedSaveDTO.GameStateShareResponseDTO;
import com.example.courseWork.services.gameStateSharesServices.GameStateSharesService;
import com.example.courseWork.util.exceptions.gameException.GameBadRequestException;
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
@RequestMapping("/game-state-shares")
public class SharedSavesController{

    private final GameStateSharesService gameStateSharesService;

    @Autowired
    public SharedSavesController( GameStateSharesService gameStateSharesService) {

        this.gameStateSharesService = gameStateSharesService;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping
    private ResponseEntity<GameStateShareResponseDTO> addGameStateShare(@RequestBody ShareWithDTO shareWithDTO,
                                                                        BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            List<FieldError> errors = bindingResult.getFieldErrors();
            List<String> stringErrors = new LinkedList<>();
            for(FieldError error : errors){
                stringErrors.add(error.getField() + " - " + error.getDefaultMessage()+";");
            }
            throw new GameBadRequestException("Shared save adding failed!", stringErrors);
        }
        SharedSave sharedSave = gameStateSharesService.save(shareWithDTO);

        GameStateShareResponseDTO gameStateShareResponseDTO = gameStateSharesService.constructResponseDTO(sharedSave);
        return ResponseEntity.ok(gameStateShareResponseDTO);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("{id}")
    private ResponseEntity<HttpStatus> deleteStateShare(@PathVariable("id") int id){
        gameStateSharesService.deleteById(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("{id}")
    private ResponseEntity<EntitiesResponseDTO<GameStateShareResponseDTO>> getSharedSaves(@PathVariable("id") int id){
        EntitiesResponseDTO<GameStateShareResponseDTO> gameStateSharesResponseDTO = gameStateSharesService.getGameStateShares(id);
        return ResponseEntity.ok(gameStateSharesResponseDTO);
    }

}

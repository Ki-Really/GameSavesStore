package com.example.courseWork.controllers;

import com.example.courseWork.DTO.GameAddRequestDTO;
import com.example.courseWork.DTO.GameDTO;
import com.example.courseWork.models.Game;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/games")
public class GamesController {
    @PostMapping
    private ResponseEntity<HttpStatus> addGame(@RequestPart("image") MultipartFile file,
                                               @RequestParam("gameData") String gameData){


        System.out.println(gameData);

        System.out.println(file.getName());
        System.out.println(file.getSize());
        return new ResponseEntity<>(HttpStatus.CREATED);
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

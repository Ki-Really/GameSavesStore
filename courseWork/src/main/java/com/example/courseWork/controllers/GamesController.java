package com.example.courseWork.controllers;

import com.example.courseWork.DTO.gameDTO.*;
import com.example.courseWork.models.gameModel.Game;
import com.example.courseWork.services.gameServices.GamesService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.minio.errors.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

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
                                            @RequestParam("gameData") String gameData) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        GameAddRequestDTO gameAddRequestDTO = objectMapper.readValue(gameData, GameAddRequestDTO.class);
        gamesService.save(gameAddRequestDTO,file);
        Game game = gamesService.findByName(gameAddRequestDTO.getName());
        GameDTO gameDTO = new GameDTO(game.getId(),game.getName());

        return ResponseEntity.ok(gameDTO);
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<HttpStatus> deleteGame(@PathVariable(name ="id") int id) throws IOException {
        gamesService.deleteById(id);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    private ResponseEntity<HttpStatus> updateGame(@PathVariable(name ="id") int id) throws IOException {

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping
    private ResponseEntity<GamesResponseDTO> findGames(
        @RequestParam(value = "searchQuery") String searchQuery,
        @RequestParam(value = "pageSize") Integer pageSize,
        @RequestParam(value = "pageNumber") Integer pageNumber
    ){
        GamesRequestDTO gamesRequestDTO = new GamesRequestDTO(
            searchQuery, pageSize, pageNumber
        );
        GamesResponseDTO gamesResponseDTO = gamesService.findAll(gamesRequestDTO);

        return ResponseEntity.ok(gamesResponseDTO);
    }

    @GetMapping("/{id}")
    private ResponseEntity<GameResponseDTO> findGameById(@PathVariable(name ="id") int id){
        Game game = gamesService.findOne(id);

        GameResponseDTO gameResponseDTO = gamesService.constructGame(game);
        return ResponseEntity.ok(gameResponseDTO);
    }

    @GetMapping("/image/{id}")
    private ResponseEntity<byte[]> getBytesById(
        @PathVariable(name ="id") int id,
        HttpServletResponse response
    ){
        Game game = gamesService.findOne(id);


        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.IMAGE_PNG);
        responseHeaders.setContentDisposition(ContentDisposition.inline().build());

        return new ResponseEntity<byte[]>(
            game.getImage().getBytes(),
            responseHeaders,
            HttpStatus.CREATED
        );
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

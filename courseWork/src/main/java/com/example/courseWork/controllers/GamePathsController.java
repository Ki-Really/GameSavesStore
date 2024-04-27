package com.example.courseWork.controllers;

import com.example.courseWork.DTO.entityDTO.EntitiesResponseDTO;
import com.example.courseWork.DTO.gamePathDTO.GamePathDTO;
import com.example.courseWork.DTO.gamePathDTO.GamePathsRequestDTO;
import com.example.courseWork.services.gameServices.PathsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/game-paths")
public class GamePathsController {
    private final PathsService pathsService;
    @Autowired
    public GamePathsController(PathsService pathsService) {
        this.pathsService = pathsService;
    }

    @GetMapping
    private ResponseEntity<EntitiesResponseDTO<GamePathDTO>> findGamePaths(
            @RequestParam(value = "searchQuery") String searchQuery,
            @RequestParam(value = "pageSize") Integer pageSize,
            @RequestParam(value = "pageNumber") Integer pageNumber){
        GamePathsRequestDTO gamePathsRequestDTO = new GamePathsRequestDTO(
                searchQuery, pageSize, pageNumber
        );
        EntitiesResponseDTO<GamePathDTO> gamePathsResponseDTO = pathsService.findPaths(gamePathsRequestDTO);
        return ResponseEntity.ok(gamePathsResponseDTO);
    }
}

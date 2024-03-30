package com.example.courseWork.controllers;

import com.example.courseWork.DTO.gamePathDTO.GamePathsRequestDTO;
import com.example.courseWork.DTO.gamePathDTO.GamePathsResponseDTO;
import com.example.courseWork.services.gameServices.GamesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/game-paths")
public class GamePathsController {
    private final GamesService gamesService;

    public GamePathsController(GamesService gamesService) {
        this.gamesService = gamesService;
    }

    @GetMapping
    private ResponseEntity<GamePathsResponseDTO> findGames(
            @RequestParam(value = "searchQuery") String searchQuery,
            @RequestParam(value = "pageSize") Integer pageSize,
            @RequestParam(value = "pageNumber") Integer pageNumber){
        GamePathsRequestDTO gamePathsRequestDTO = new GamePathsRequestDTO(
                searchQuery, pageSize, pageNumber
        );
        GamePathsResponseDTO gamePathsResponseDTO = gamesService.findPaths(gamePathsRequestDTO);

        return ResponseEntity.ok(gamePathsResponseDTO);
    }
}

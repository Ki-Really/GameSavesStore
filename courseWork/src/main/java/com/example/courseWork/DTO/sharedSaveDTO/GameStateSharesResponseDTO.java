package com.example.courseWork.DTO.sharedSaveDTO;

import java.util.List;

public class GameStateSharesResponseDTO {
    private List<GameStateShareResponseDTO> gameStateShares;

    public GameStateSharesResponseDTO(List<GameStateShareResponseDTO> gameStateShares) {
        this.gameStateShares = gameStateShares;
    }

    public GameStateSharesResponseDTO() {
    }

    public List<GameStateShareResponseDTO> getGameStateShares() {
        return gameStateShares;
    }

    public void setGameStateShares(List<GameStateShareResponseDTO> gameStateShares) {
        this.gameStateShares = gameStateShares;
    }
}

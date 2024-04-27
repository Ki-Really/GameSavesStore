package com.example.courseWork.DTO.sharedSaveDTO;

import jakarta.validation.constraints.NotNull;

public class ShareWithDTO {
    @NotNull(message = "Game state id should not be null!")
    private int gameStateId;

    @NotNull(message = "Person's id should not be null!")
    private int shareWithId;

    public ShareWithDTO(int gameStateId, int shareWithId) {
        this.gameStateId = gameStateId;
        this.shareWithId = shareWithId;
    }

    public ShareWithDTO() {
    }

    public int getGameStateId() {
        return gameStateId;
    }

    public void setGameStateId(int gameStateId) {
        this.gameStateId = gameStateId;
    }

    public int getShareWithId() {
        return shareWithId;
    }

    public void setShareWithId(int shareWithId) {
        this.shareWithId = shareWithId;
    }
}

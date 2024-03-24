package com.example.courseWork.DTO.gameSaveDTO;

import java.util.List;

public class GameStateRequestDTO {
    private String name;
    private int gameId;
    private String localPath;
    private boolean isPublic;
    private List<GameStateValueDTO> gameStateValues;

    public GameStateRequestDTO(String name, int gameId, String localPath, boolean isPublic) {
        this.name = name;
        this.gameId = gameId;
        this.localPath = localPath;
        this.isPublic = isPublic;
    }

    public GameStateRequestDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }


    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public List<GameStateValueDTO> getGameStateValues() {
        return gameStateValues;
    }

    public void setGameStateValues(List<GameStateValueDTO> gameStateValues) {
        this.gameStateValues = gameStateValues;
    }
}

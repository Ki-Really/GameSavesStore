package com.example.courseWork.DTO.gameSaveDTO;

import java.util.List;

public class AddGameSaveDTO {
    private String name;
    private int gameId;
    private String localPath;
    private List<MetadataDTO> metadata;

    public AddGameSaveDTO(String name, int gameId, String localPath) {
        this.name = name;
        this.gameId = gameId;
        this.localPath = localPath;
    }

    public AddGameSaveDTO() {
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

    public List<MetadataDTO> getMetadata() {
        return metadata;
    }

    public void setMetadata(List<MetadataDTO> metadata) {
        this.metadata = metadata;
    }
}

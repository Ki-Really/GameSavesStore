package com.example.courseWork.DTO.gameSaveDTO;


import java.util.List;

public class GameStateDTO {
    private int id;
    private String name;
    private int gameId;
    private String gameIconUrl;
    private boolean isPublic;
    private String localPath;
    private List<GameStateValueDTO> gameStateValues;
    private String archiveUrl;
    private long sizeInBytes;

    public GameStateDTO(int id, String name, int gameId, String gameIconUrl, boolean isPublic, String localPath, List<GameStateValueDTO> gameStateValues, String archiveUrl, long sizeInBytes) {
        this.id = id;
        this.name = name;
        this.gameId = gameId;
        this.gameIconUrl = gameIconUrl;
        this.isPublic = isPublic;
        this.localPath = localPath;
        this.gameStateValues = gameStateValues;
        this.archiveUrl = archiveUrl;
        this.sizeInBytes = sizeInBytes;
    }

    public GameStateDTO() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getGameIconUrl() {
        return gameIconUrl;
    }

    public void setGameIconUrl(String gameIconUrl) {
        this.gameIconUrl = gameIconUrl;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public List<GameStateValueDTO> getGameStateValues() {
        return gameStateValues;
    }

    public void setGameStateValues(List<GameStateValueDTO> gameStateValues) {
        this.gameStateValues = gameStateValues;
    }

    public String getArchiveUrl() {
        return archiveUrl;
    }

    public void setArchiveUrl(String archiveUrl) {
        this.archiveUrl = archiveUrl;
    }

    public long getSizeInBytes() {
        return sizeInBytes;
    }

    public void setSizeInBytes(long sizeInBytes) {
        this.sizeInBytes = sizeInBytes;
    }

    public boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }
}

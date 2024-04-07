package com.example.courseWork.DTO.gameSaveDTO;


import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;

public class GameStateDTO {
    private int id;
    private String name;
    private int gameId;
    private String gameName;
    private String gameIconUrl;
    private boolean isPublic;
    private String localPath;
    private List<GameStateValueDTO> gameStateValues;
    private String archiveUrl;
    private long sizeInBytes;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime uploadedAt;

    public GameStateDTO(int id, String name, int gameId, String gameName, String gameIconUrl, boolean isPublic, String localPath, List<GameStateValueDTO> gameStateValues, String archiveUrl, long sizeInBytes, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime uploadedAt) {
        this.id = id;
        this.name = name;
        this.gameId = gameId;
        this.gameName = gameName;
        this.gameIconUrl = gameIconUrl;
        this.isPublic = isPublic;
        this.localPath = localPath;
        this.gameStateValues = gameStateValues;
        this.archiveUrl = archiveUrl;
        this.sizeInBytes = sizeInBytes;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.uploadedAt = uploadedAt;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }
}

package com.example.courseWork.DTO.gamePathDTO;

public class GamePathDTO {
    private int id;
    private String path;
    private int gameId;
    private String gameName;
    private String gameIconUrl;

    public GamePathDTO(int id, String path, int gameId, String gameName, String gameIconUrl) {
        this.id = id;
        this.path = path;
        this.gameId = gameId;
        this.gameName = gameName;
        this.gameIconUrl = gameIconUrl;
    }

    public GamePathDTO() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getGameIconUrl() {
        return gameIconUrl;
    }

    public void setGameIconUrl(String gameIconUrl) {
        this.gameIconUrl = gameIconUrl;
    }
}

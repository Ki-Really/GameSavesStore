package com.example.courseWork.DTO.gameDTO;

import org.springframework.stereotype.Component;

@Component
public class GameDTO {
    private int id;
    private String gameName;


    public GameDTO(int id, String gameName) {
        this.id = id;
        this.gameName = gameName;

    }

    public GameDTO() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }
}

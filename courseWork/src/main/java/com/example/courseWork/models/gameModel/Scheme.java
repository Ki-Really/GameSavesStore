package com.example.courseWork.models.gameModel;

import jakarta.persistence.*;
import org.checkerframework.common.aliasing.qual.Unique;

import java.util.List;

@Entity
@Table(name = "Scheme")
public class Scheme {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
   // @NotEmpty(message ="Путь не должен быть пустым!")
    @Column(name = "filename")
    private String filename;

    @Unique
    @OneToOne
    @JoinColumn(name ="fk_game_id",referencedColumnName = "id")
    private Game game;

    @OneToMany(mappedBy="scheme", cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REMOVE,CascadeType.DETACH}, orphanRemoval = true)
    private List<GameStateParameter> gameStateParameters;


    public Scheme(String filename) {
        this.filename = filename;
    }

    public Scheme() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public List<GameStateParameter> getGameStateParameters() {
        return gameStateParameters;
    }

    public void setGameStateParameters(List<GameStateParameter> gameStateParameters) {
        this.gameStateParameters = gameStateParameters;
    }
}

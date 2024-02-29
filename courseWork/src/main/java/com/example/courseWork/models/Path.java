package com.example.courseWork.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

@Entity
@Table(name="Path")
public class Path {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotEmpty(message ="Путь не должен быть пустым!")
    @Column(name = "name")
    private String path;

    @ManyToOne
    @JoinColumn(name ="fk_game_id",referencedColumnName = "id")
    private Game game;



    public Path() {
    }

    public Path(String path) {
        this.path = path;
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

    public void setPath(String name) {
        this.path = name;
    }
    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }
}

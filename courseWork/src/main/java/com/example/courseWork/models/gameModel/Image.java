package com.example.courseWork.models.gameModel;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

@Entity
@Table(name="Image")
public class Image {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty(message = "Game name should not be empty!")
    @Column(name = "name")
    private String name;

    @OneToOne
    @JoinColumn(name ="fk_game_id",referencedColumnName = "id")
    private Game game;

    public Image(String name) {
        this.name = name;
    }

    public Image() {
    }

    private void addGameToImage(){

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

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

}

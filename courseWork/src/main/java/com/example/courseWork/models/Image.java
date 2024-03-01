package com.example.courseWork.models;

import jakarta.persistence.*;

@Entity
@Table(name="Image")
public class Image {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name="image",columnDefinition="bytea")
    /*@Lob*/
    private byte[] image;
    @OneToOne
    @JoinColumn(name ="fk_game_id",referencedColumnName = "id")
    private Game game;

    public Image(byte[] image) {
        this.image = image;
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

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

}

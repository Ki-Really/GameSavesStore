package com.example.courseWork.models;

import jakarta.persistence.*;

@Entity
@Table(name="Image")
public class Image {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name="bytes",columnDefinition="bytes")
    /*@Lob*/
    private byte[] bytes;
    @OneToOne
    @JoinColumn(name ="fk_game_id",referencedColumnName = "id")
    private Game game;

    public Image(byte[] bytes) {
        this.bytes = bytes;
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

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] image) {
        this.bytes = image;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

}

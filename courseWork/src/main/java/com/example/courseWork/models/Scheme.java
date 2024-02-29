package com.example.courseWork.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

import java.util.ArrayList;

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

    @OneToOne
    @JoinColumn(name ="fk_game_id",referencedColumnName = "id")
    private Game game;

    @OneToMany(mappedBy="scheme")
    private ArrayList<Field> fields;


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

    public ArrayList<Field> getFields() {
        return fields;
    }

    public void setFields(ArrayList<Field> fields) {
        this.fields = fields;
    }
}

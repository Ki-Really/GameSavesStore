package com.example.courseWork.models.gameModel;

import com.example.courseWork.models.gameSaveModel.GameState;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

@Entity
@Table(name="Game")
public class Game {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotEmpty(message ="Название игры не должно быть пустым!")
    @Size(min = 2, max = 50, message = "Название игры должно быть от 2 до 50 символов в длину!")
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    @Size(min = 2, max = 255, message = "Описание игры должно быть от 2 до 50 символов в длину!")
    private String description;

    @OneToMany(mappedBy = "game", cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REMOVE,CascadeType.DETACH}, orphanRemoval = true)
    private List<Path> paths;

    @OneToMany(mappedBy = "game", cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REMOVE,CascadeType.DETACH}, orphanRemoval = true)
    private List<ExtractionPipeline> extractionPipelines;

    @OneToOne(mappedBy = "game", cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REMOVE,CascadeType.DETACH}, orphanRemoval = true)
    private Scheme scheme;

    @OneToOne(mappedBy="game", cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REMOVE,CascadeType.DETACH})
    private Image image;

    @OneToMany(mappedBy = "game", cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REMOVE,CascadeType.DETACH})
    private List<GameState> gameStates;

    public Game(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Game(){}

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Path> getPaths() {
        return paths;
    }

    public void setPaths(List<Path> paths) {
        this.paths = paths;
    }

    public List<ExtractionPipeline> getExtractionPipelines() {
        return extractionPipelines;
    }

    public void setExtractionPipelines(List<ExtractionPipeline> extractionPipelines) {
        this.extractionPipelines = extractionPipelines;
    }

    public Scheme getScheme() {
        return scheme;
    }

    public void setScheme(Scheme scheme) {
        this.scheme = scheme;
    }

    public void addSchemesToGame(Scheme scheme){
        scheme.setGame(this);
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public List<GameState> getGameStates() {
        return gameStates;
    }

    public void setGameStates(List<GameState> gameStates) {
        this.gameStates = gameStates;
    }
}

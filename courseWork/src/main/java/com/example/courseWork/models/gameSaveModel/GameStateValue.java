package com.example.courseWork.models.gameSaveModel;

import com.example.courseWork.models.gameModel.GameStateParameter;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

@Entity
@Table(name= "game_state_value")
public class GameStateValue {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotEmpty(message = "Value should not be empty!")
    @Column(name="value")
    private String value;
    @ManyToOne
    @JoinColumn(name="fk_game_state_parameter_id", referencedColumnName = "id")
    private GameStateParameter gameStateParameter;

    @ManyToOne
    @JoinColumn(name="fk_game_state_id", referencedColumnName = "id")
    private GameState gameState;

    public GameStateValue(String value, GameStateParameter gameStateParameter) {
        this.value = value;
        this.gameStateParameter = gameStateParameter;
    }

    public GameStateValue() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public GameStateParameter getGameStateParameter() {
        return gameStateParameter;
    }

    public void setGameStateParameter(GameStateParameter gameStateParameter) {
        this.gameStateParameter = gameStateParameter;
    }
}

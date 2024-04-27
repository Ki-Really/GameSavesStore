package com.example.courseWork.models.gameModel;

import com.example.courseWork.models.commonParameters.CommonParameter;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

@Entity
@Table(name="game_state_parameter_type")
public class GameStateParameterType {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotEmpty(message = "Type should not be empty!")
    @Column(name="type")
    private String type;
    @OneToMany(mappedBy = "gameStateParameterType")
    private List<GameStateParameter> gameStateParameters;
    @OneToMany(mappedBy = "gameStateParameterType")
    private List<CommonParameter> commonParameters;

    public GameStateParameterType() {
    }

    public GameStateParameterType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<GameStateParameter> getGameStateParameters() {
        return gameStateParameters;
    }

    public void setGameStateParameters(List<GameStateParameter> gameStateParameters) {
        this.gameStateParameters = gameStateParameters;
    }

    public List<CommonParameter> getCommonParameters() {
        return commonParameters;
    }

    public void setCommonParameters(List<CommonParameter> commonParameters) {
        this.commonParameters = commonParameters;
    }
}

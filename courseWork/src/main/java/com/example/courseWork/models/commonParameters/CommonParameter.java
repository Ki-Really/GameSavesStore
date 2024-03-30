package com.example.courseWork.models.commonParameters;

import com.example.courseWork.models.gameModel.GameStateParameter;
import com.example.courseWork.models.gameModel.GameStateParameterType;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name="common_parameter")
public class CommonParameter {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="label")
    private String label;
    @Column(name="description")
    private String description;
    @ManyToOne(cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    @JoinColumn(name = "fk_game_state_parameter_type",referencedColumnName = "id")
    private GameStateParameterType gameStateParameterType;

    @OneToMany(mappedBy = "commonParameter")
    private List<GameStateParameter> gameStateParameters;

    public CommonParameter(String label, String description, GameStateParameterType gameStateParameterType, List<GameStateParameter> gameStateParameters) {
        this.label = label;
        this.description = description;
        this.gameStateParameterType = gameStateParameterType;
        this.gameStateParameters = gameStateParameters;
    }

    public CommonParameter() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public GameStateParameterType getGameStateParameterType() {
        return gameStateParameterType;
    }

    public void setGameStateParameterType(GameStateParameterType gameStateParameterType) {
        this.gameStateParameterType = gameStateParameterType;
    }

    public List<GameStateParameter> getGameStateParameters() {
        return gameStateParameters;
    }

    public void setGameStateParameters(List<GameStateParameter> gameStateParameters) {
        this.gameStateParameters = gameStateParameters;
    }
}
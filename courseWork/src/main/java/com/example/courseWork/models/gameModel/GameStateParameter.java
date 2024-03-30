package com.example.courseWork.models.gameModel;

import com.example.courseWork.models.commonParameters.CommonParameter;
import com.example.courseWork.models.gameSaveModel.GameStateValue;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

@Entity
@Table(name ="game_state_parameter")
public class GameStateParameter {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotEmpty(message ="Название игры не должно быть пустым!")
    @Column(name = "key")
    private String key;
    @NotEmpty(message ="Название игры не должно быть пустым!")
    @Column(name = "label")
    private String label;
    @NotEmpty(message ="Название игры не должно быть пустым!")
    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name ="fk_scheme_id",referencedColumnName = "id")
    private Scheme scheme;
    @ManyToOne
    @JoinColumn(name = "fk_game_state_parameter_type_id", referencedColumnName = "id")
    private GameStateParameterType gameStateParameterType;

    @OneToMany(mappedBy = "gameStateParameter")
    private List<GameStateValue> gameStateValues;
    @ManyToOne(cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REMOVE,CascadeType.DETACH})
    @JoinColumn(name = "fk_common_parameter_id",referencedColumnName = "id")
    private CommonParameter commonParameter;

    public GameStateParameter(String key, String label, String description) {
        this.key = key;
        this.label = label;
        this.description = description;
    }

    public GameStateParameter() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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

    public Scheme getScheme() {
        return scheme;
    }

    public void setScheme(Scheme scheme) {
        this.scheme = scheme;
    }
    public GameStateParameterType getGameStateParameterType() {
        return gameStateParameterType;
    }

    public void setGameStateParameterType(GameStateParameterType gameStateParameterType) {
        this.gameStateParameterType = gameStateParameterType;
    }

    public List<GameStateValue> getGameStateValues() {
        return gameStateValues;
    }

    public void setGameStateValues(List<GameStateValue> gameStateValues) {
        this.gameStateValues = gameStateValues;
    }

    public CommonParameter getCommonParameter() {
        return commonParameter;
    }

    public void setCommonParameter(CommonParameter commonParameter) {
        this.commonParameter = commonParameter;
    }
}

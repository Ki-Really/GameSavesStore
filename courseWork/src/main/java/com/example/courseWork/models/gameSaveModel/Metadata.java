package com.example.courseWork.models.gameSaveModel;

import jakarta.persistence.*;

@Entity
@Table(name="metadata")
public class Metadata {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "type")
    private String type;
    @Column(name = "value")
    private String value;
    @Column(name = "label")
    private String label;
    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name="fk_game_save_id", referencedColumnName = "id")
    private GameSave gameSave;

    public Metadata(String type, String value, String label, String description) {
        this.type = type;
        this.value = value;
        this.label = label;
        this.description = description;
    }

    public Metadata() {
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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

    public GameSave getGameSave() {
        return gameSave;
    }

    public void setGameSave(GameSave gameSave) {
        this.gameSave = gameSave;
    }
}

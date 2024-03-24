package com.example.courseWork.models.gameModel;

import jakarta.persistence.*;

@Entity
@Table(name ="extraction_pipeline")
public class ExtractionPipeline {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "type")
    private String type;
    @Column(name = "input_filename")
    private String inputFilename;
    @Column(name = "output_filename")
    private String outputFilename;

    @ManyToOne
    @JoinColumn(name ="fk_game_id",referencedColumnName = "id")
    private Game game;

    public ExtractionPipeline( String type, String inputFilename, String outputFilename) {
        this.type = type;
        this.inputFilename = inputFilename;
        this.outputFilename = outputFilename;
    }

    public ExtractionPipeline() {
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

    public String getInputFilename() {
        return inputFilename;
    }

    public void setInputFilename(String inputFilename) {
        this.inputFilename = inputFilename;
    }

    public String getOutputFilename() {
        return outputFilename;
    }

    public void setOutputFilename(String outputFilename) {
        this.outputFilename = outputFilename;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

}

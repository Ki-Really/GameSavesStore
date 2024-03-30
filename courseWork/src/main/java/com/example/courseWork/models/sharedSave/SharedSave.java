package com.example.courseWork.models.sharedSave;

import com.example.courseWork.models.authModel.Person;
import com.example.courseWork.models.gameSaveModel.GameState;
import jakarta.persistence.*;

@Entity
@Table(name="shared_save")
public class SharedSave {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name ="fk_shared_with_id",referencedColumnName = "id")
    private Person person;

    @ManyToOne
    @JoinColumn(name="fk_game_state_id",referencedColumnName = "id")
    private GameState gameState;

    public SharedSave(Person person, GameState gameState) {
        this.person = person;
        this.gameState = gameState;
    }

    public SharedSave() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }
}

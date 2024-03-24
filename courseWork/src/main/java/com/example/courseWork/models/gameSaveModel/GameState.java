package com.example.courseWork.models.gameSaveModel;

import com.example.courseWork.models.authModel.Person;
import com.example.courseWork.models.gameModel.Game;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name= "game_state")
public class GameState {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "name")
    private String name;
    @Column(name = "local_path")
    private String localPath;
    @Column(name = "archive_name")
    private String archiveName;
    @Column(name = "size_in_bytes")
    private long sizeInBytes;
    @Column(name = "is_public")
    private boolean isPublic;
    @ManyToOne
    @JoinColumn(name ="fk_person_id",referencedColumnName = "id")
    private Person person;
    @ManyToOne
    @JoinColumn(name = "fk_game_id",referencedColumnName = "id")
    private Game game;
    @OneToMany(mappedBy="gameState",cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REMOVE,CascadeType.DETACH},orphanRemoval = true)
    private List<GameStateValue> gameStateValues;
    @Column(name="created_at")
    private LocalDateTime createdAt;
    @Column(name="updated_at")
    private LocalDateTime updatedAt;
    @Column(name="uploaded_at")
    private LocalDateTime uploadedAt;

    public GameState(String name, String localPath, String archiveName, int sizeInBytes) {
        this.name = name;
        this.localPath = localPath;
        this.archiveName = archiveName;
        this.sizeInBytes = sizeInBytes;
    }

    public GameState() {
    }

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

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public String getArchiveName() {
        return archiveName;
    }

    public void setArchiveName(String archiveName) {
        this.archiveName = archiveName;
    }

    public long getSizeInBytes() {
        return sizeInBytes;
    }

    public void setSizeInBytes(long sizeInBytes) {
        this.sizeInBytes = sizeInBytes;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public List<GameStateValue> getGameStateValues() {
        return gameStateValues;
    }


    public void setGameStateValues(List<GameStateValue> gameStateValues) {
        this.gameStateValues = gameStateValues;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }
}

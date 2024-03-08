package com.example.courseWork.models.gameSaveModel;

import com.example.courseWork.models.authModel.Person;
import com.example.courseWork.models.gameModel.Game;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name="game_save")
public class GameSave {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "name")
    private String name;
    @Column(name = "local_path")
    private String localPath;
    @Column(name = "archive_url")
    private String archiveUrl;
    @Column(name = "size_in_bytes")
    private long sizeInBytes;

    @ManyToOne
    @JoinColumn(name ="fk_person_id",referencedColumnName = "id")
    private Person person;

    @ManyToOne
    @JoinColumn(name = "fk_game_id",referencedColumnName = "id")
    private Game game;

    @OneToMany(mappedBy="gameSave")
    private List<Metadata> metadataList;

    public GameSave(String name, String localPath, String archiveUrl, int sizeInBytes) {
        this.name = name;
        this.localPath = localPath;
        this.archiveUrl = archiveUrl;
        this.sizeInBytes = sizeInBytes;
    }

    public GameSave() {
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

    public String getArchiveUrl() {
        return archiveUrl;
    }

    public void setArchiveUrl(String archiveUrl) {
        this.archiveUrl = archiveUrl;
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

    public List<Metadata> getMetadataList() {
        return metadataList;
    }

    public void setMetadataList(List<Metadata> metadataList) {
        this.metadataList = metadataList;
    }
}

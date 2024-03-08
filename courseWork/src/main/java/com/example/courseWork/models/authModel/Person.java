package com.example.courseWork.models.authModel;

import com.example.courseWork.models.gameSaveModel.GameSave;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

@Entity
@Table(name = "Person")
public class Person {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty(message ="Имя пользователя не должно быть пустым!")
    @Size(min = 2, max = 50,message = "Имя пользователя должно быть от 2 до 50 символов в длину!")
    @Column(name = "username")
    private String username;


    @NotEmpty(message ="Поле Email не должно быть пустым!")
    @Size(min = 2, max = 50,message = "Поле Email должно быть от 2 до 50 символов в длину!")
    @Email
    @Column(name = "email")
    private String email;

    @NotEmpty(message ="Поле пароль не должно быть пустым!")
    /*@Size(min = 3, max = 50, message = "Поле пароль должно быть от 3 до 50 символов в длину!")*/
    @Column(name = "password")
    private String password;

    @ManyToOne
    @JoinColumn(name ="fk_role",referencedColumnName = "id")
    private Role role;
    @OneToOne(mappedBy = "person")
    private PasswordRecoveryTokenEntity passwordRecoveryTokenEntity;

    @OneToMany(mappedBy = "person")
    private List<GameSave> gameSaves;
    public Person() {
    }
    public Person(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role){
        this.role = role;
    }

    public PasswordRecoveryTokenEntity getPasswordRecoveryTokenEntity() {
        return passwordRecoveryTokenEntity;
    }

    public void setPasswordRecoveryTokenEntity(PasswordRecoveryTokenEntity passwordRecoveryTokenEntity) {
        this.passwordRecoveryTokenEntity = passwordRecoveryTokenEntity;
    }

    public List<GameSave> getGameSaves() {
        return gameSaves;
    }

    public void setGameSaves(List<GameSave> gameSaves) {
        this.gameSaves = gameSaves;
    }
}

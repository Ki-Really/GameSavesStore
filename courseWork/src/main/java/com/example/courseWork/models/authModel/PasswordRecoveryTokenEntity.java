package com.example.courseWork.models.authModel;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

@Entity
@Table(name = "passwordrecoverytoken")
public class PasswordRecoveryTokenEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotEmpty(message ="Поле с токеном не должно быть пустым!")
    @Column(name ="token")
    private String token;
    @OneToOne
    @JoinColumn(name ="fk_person_id",referencedColumnName = "id")
    private Person person;

    public PasswordRecoveryTokenEntity(String token, Person person) {
        this.token = token;
        this.person = person;
    }
    public PasswordRecoveryTokenEntity(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}

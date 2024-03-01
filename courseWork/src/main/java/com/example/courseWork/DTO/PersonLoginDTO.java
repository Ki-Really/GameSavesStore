package com.example.courseWork.DTO;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class PersonLoginDTO {
    @NotEmpty(message ="Имя пользователя не должно быть пустым!")
    @Size(min = 2, max = 50,message = "Имя пользователя должно быть от 2 до 50 символов в длину!")

    private String username;

    @NotEmpty(message ="Поле пароль не должно быть пустым!")
    /*@Size(min = 3, max = 50, message = "Поле пароль должно быть от 3 до 50 символов в длину!")*/

    private String password;

    public PersonLoginDTO() {
    }

    public PersonLoginDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

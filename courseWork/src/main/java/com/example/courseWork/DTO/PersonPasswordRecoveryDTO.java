package com.example.courseWork.DTO;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class PersonPasswordRecoveryDTO {
    @NotEmpty(message ="Поле Email не должно быть пустым!")
    //@Size(min = 2, max = 50,message = "Поле Email должно быть от 2 до 50 символов в длину!")
    @Email
    private String email;

    public PersonPasswordRecoveryDTO(String email) {
        this.email = email;
    }

    public PersonPasswordRecoveryDTO() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

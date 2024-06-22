package com.example.courseWork.DTO.authDTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public class PersonPasswordRecoveryDTO {
    @NotEmpty(message ="Поле Email не должно быть пустым!")
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

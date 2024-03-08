package com.example.courseWork.DTO.authDTO;

public class PersonAuthChangePasswordDTO {
    private String password;
    private String repeatedPassword;

    public PersonAuthChangePasswordDTO(String password, String repeatedPassword) {
        this.password = password;
        this.repeatedPassword = repeatedPassword;
    }
    public  PersonAuthChangePasswordDTO(){}

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRepeatedPassword() {
        return repeatedPassword;
    }

    public void setRepeatedPassword(String repeatedPassword) {
        this.repeatedPassword = repeatedPassword;
    }
}

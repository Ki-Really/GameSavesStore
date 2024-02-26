package com.example.courseWork.DTO;

public class PersonChangePasswordDTO {
    private String token;
    private String password;
    private String repeatedPassword;

    public PersonChangePasswordDTO(String token, String password, String repeatedPassword) {
        this.token = token;
        this.password = password;
        this.repeatedPassword = repeatedPassword;
    }
    public  PersonChangePasswordDTO(){}

    public String getToken() {
        return token;
    }

    public void setToken(String tokenToChangePassword) {
        this.token = tokenToChangePassword;
    }

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

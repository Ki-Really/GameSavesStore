package com.example.courseWork.DTO.authDTO;

public class PersonAuthChangePasswordDTO {
    private String oldPassword;
    private String password;
    private String repeatedPassword;

    public PersonAuthChangePasswordDTO(String password, String repeatedPassword, String oldPassword) {
        this.password = password;
        this.repeatedPassword = repeatedPassword;
        this.oldPassword = oldPassword;
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
    public void setOldPassword(String oldPassword){
        this.oldPassword = oldPassword;
    }
    public String getOldPassword(){
        return oldPassword;
    }
}

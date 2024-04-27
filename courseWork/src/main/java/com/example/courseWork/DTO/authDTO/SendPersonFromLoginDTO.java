package com.example.courseWork.DTO.authDTO;


public class SendPersonFromLoginDTO {

    private String username;
    private String email;


    private String role;

    public SendPersonFromLoginDTO(String username, String email, String role) {
        this.username = username;
        this.email = email;
        this.role = role;
    }
    public SendPersonFromLoginDTO(String username){
        this.username = username;
    }
    public SendPersonFromLoginDTO() {
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



    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}

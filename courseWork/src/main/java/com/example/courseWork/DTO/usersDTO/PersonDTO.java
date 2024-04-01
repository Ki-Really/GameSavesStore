package com.example.courseWork.DTO.usersDTO;

import com.example.courseWork.models.authModel.Role;

public class PersonDTO {
    private int id;
    private String username;
    private String email;
    private String role;
    private boolean isBlocked;

    public PersonDTO(int id, String username, String email, String role, boolean isBlocked) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
        this.isBlocked = isBlocked;
    }

    public PersonDTO() {
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean getIsBlocked() {
        return isBlocked;
    }

    public void setIsBlocked(boolean isBlocked) {
        this.isBlocked = isBlocked;
    }
}


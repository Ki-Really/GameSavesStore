package com.example.courseWork.controllers;

import com.example.courseWork.DTO.usersDTO.PersonDTO;
import com.example.courseWork.models.authModel.Person;
import com.example.courseWork.services.authServices.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
@RequestMapping("/users")
public class UsersController {
    private final PeopleService peopleService;
    @Autowired
    public UsersController(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @DeleteMapping("/{id}/block")
    private ResponseEntity<PersonDTO> blockUser(@PathVariable(name ="id") int id) {
        Person person = peopleService.findPersonById(id);
        PersonDTO personDTO = new PersonDTO();
        personDTO.setId(person.getId());
        personDTO.setUsername(person.getUsername());
        personDTO.setEmail(person.getEmail());
        personDTO.setRole(person.getRole());
        personDTO.setBlocked(true);
        peopleService.block(id);
        return ResponseEntity.ok(personDTO);
    }

    @DeleteMapping("/{id}/unblock")
    private ResponseEntity<PersonDTO> unblockUser(@PathVariable(name ="id") int id) {
        //??????????????
        return null;
    }
}

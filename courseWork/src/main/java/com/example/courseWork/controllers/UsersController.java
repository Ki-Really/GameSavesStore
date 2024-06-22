package com.example.courseWork.controllers;

import com.example.courseWork.DTO.entityDTO.EntitiesResponseDTO;
import com.example.courseWork.DTO.usersDTO.PeopleRequestDTO;
import com.example.courseWork.DTO.usersDTO.PersonDTO;
import com.example.courseWork.models.authModel.Person;
import com.example.courseWork.services.authServices.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;


@RestController
@RequestMapping("/users")
public class UsersController {

    private final PeopleService peopleService;
    @Autowired
    public UsersController(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/{id}/block")
    public ResponseEntity<PersonDTO> blockUser(@PathVariable(name ="id") int id) {
        Person person = peopleService.findPersonById(id);
        peopleService.blockUser(id);
        PersonDTO personDTO = new PersonDTO();
        personDTO.setId(person.getId());
        personDTO.setUsername(person.getUsername());
        personDTO.setEmail(person.getEmail());
        personDTO.setRole(person.getRole().getName());
        personDTO.setIsBlocked(person.getIsBlocked());
        return ResponseEntity.ok(personDTO);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/{id}/unblock")
    public ResponseEntity<PersonDTO> unblockUser(@PathVariable(name ="id") int id) {
        Person person = peopleService.findPersonById(id);
        peopleService.unblockUser(id);
        PersonDTO personDTO = new PersonDTO();
        personDTO.setId(person.getId());
        personDTO.setUsername(person.getUsername());
        personDTO.setEmail(person.getEmail());
        personDTO.setRole(person.getRole().getName());
        personDTO.setIsBlocked(person.getIsBlocked());
        return ResponseEntity.ok(personDTO);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<EntitiesResponseDTO<PersonDTO>> getUsers(@RequestParam(value = "searchQuery") String searchQuery,
                                                         @RequestParam(value = "pageSize") Integer pageSize,
                                                         @RequestParam(value = "pageNumber") Integer pageNumber, Principal principal){
        PeopleRequestDTO peopleRequestDTO = new PeopleRequestDTO(
                searchQuery, pageSize, pageNumber
        );
        EntitiesResponseDTO<PersonDTO> peopleDTO = peopleService.findAll(peopleRequestDTO,principal);
        return ResponseEntity.ok(peopleDTO);
    }
}

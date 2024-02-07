package com.example.courseWork.controllers;

import com.example.courseWork.models.Person;
import com.example.courseWork.services.PeopleService;
import com.example.courseWork.util.PersonErrorResponse;
import com.example.courseWork.util.PersonNotCreatedException;
import com.example.courseWork.util.PersonNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/people")
public class PeopleController {
    private final PeopleService peopleService;

    @Autowired
    public PeopleController(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @GetMapping
    private List<Person> getPeople(){
        return peopleService.findAll();
    }

    @GetMapping("/{id}")
    private Person getPerson(@PathVariable int id){
        return peopleService.findOne(id);
    }

    @PostMapping
    private ResponseEntity<HttpStatus> create(@RequestBody @Valid Person person,
                                              BindingResult bindingResult){
        if(bindingResult.hasErrors()){

            StringBuilder errorMsg = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for(FieldError error : errors){
                errorMsg.append(error.getField())
                        .append(" - ")
                        .append(error.getDefaultMessage())
                        .append(";");
            }
            throw new PersonNotCreatedException(errorMsg.toString());
        }
        peopleService.save(person);
        return ResponseEntity.ok(HttpStatus.OK);

    }

    @ExceptionHandler
    private ResponseEntity<PersonErrorResponse> handleException(PersonNotFoundException e){
        PersonErrorResponse response = new PersonErrorResponse("Person with this id was not found!",
                System.currentTimeMillis());

        return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<PersonErrorResponse> handleException(PersonNotCreatedException e){
        PersonErrorResponse response = new PersonErrorResponse(e.getMessage(),
                System.currentTimeMillis());

        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }
}

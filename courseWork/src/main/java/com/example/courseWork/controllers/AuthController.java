package com.example.courseWork.controllers;

import com.example.courseWork.DTO.PersonLoginDTO;
import com.example.courseWork.DTO.PersonPasswordRecoveryDTO;
import com.example.courseWork.models.MailStructure;
import com.example.courseWork.models.Person;
import com.example.courseWork.services.MailService;
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
import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final PeopleService peopleService;
    private final MailService mailService;

    @Autowired
    public AuthController(PeopleService peopleService, MailService mailService) {
        this.peopleService = peopleService;
        this.mailService = mailService;
    }

    /*@GetMapping
    private List<Person> getPeople(){
        return peopleService.findAll();
    }

    @GetMapping("/{id}")
    private Person getPerson(@PathVariable int id){
        return peopleService.findOne(id);
    }*/

    @PostMapping("/registration")
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

    @PostMapping("/login")
    private ResponseEntity<Person> login(@RequestBody @Valid PersonLoginDTO personLoginDTO,
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
        Person person = peopleService.checkCredentials(personLoginDTO.getUsername(), personLoginDTO.getPassword());
        System.out.println(person);
        return ResponseEntity.ok(person);

    }

    @PostMapping("/recover-password")
    private ResponseEntity<HttpStatus> recoverPassword(@RequestBody @Valid PersonPasswordRecoveryDTO personPasswordRecoveryDTO){
        String email = personPasswordRecoveryDTO.getEmail();
        System.out.println(email);
        UUID uuid = UUID.randomUUID();
        String generatedToken = uuid.toString();
        String emailText = "<a>cloud-saves://reset-password?token="+ generatedToken + "</a>";
        System.out.println(emailText);
        MailStructure mailStructure = new MailStructure("Password Recovery",emailText);
        mailService.sendMail(email,mailStructure);
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

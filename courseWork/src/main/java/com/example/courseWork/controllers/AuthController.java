package com.example.courseWork.controllers;

import com.example.courseWork.DTO.PersonLoginDTO;
import com.example.courseWork.DTO.PersonPasswordRecoveryDTO;
import com.example.courseWork.models.MailStructure;
import com.example.courseWork.models.Person;
import com.example.courseWork.security.PersonDetails;
import com.example.courseWork.services.MailService;
import com.example.courseWork.services.PeopleService;
import com.example.courseWork.util.PersonErrorResponse;
import com.example.courseWork.util.PersonNotCreatedException;
import com.example.courseWork.util.PersonNotFoundException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
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
                                              BindingResult bindingResult, HttpServletRequest request) throws ServletException {
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

        request.login(personLoginDTO.getUsername(), personLoginDTO.getPassword());
        Person person = peopleService.findOne(personLoginDTO.getUsername());
        System.out.println(person);
        return ResponseEntity.ok(person);

    }

    @GetMapping("/me")
    @ResponseBody
    public ResponseEntity<Person> currentUser(Principal principal) {
        Person person = peopleService.findOne(principal.getName());
        return ResponseEntity.ok(person);
    }

    @PostMapping("/recover-password")
    private ResponseEntity<HttpStatus> recoverPassword(@RequestBody @Valid PersonPasswordRecoveryDTO personPasswordRecoveryDTO){
        String email = personPasswordRecoveryDTO.getEmail();
        System.out.println(email);
        UUID uuid = UUID.randomUUID();
        String generatedToken = uuid.toString();
        String emailText = "cloud-saves://reset-password?token="+ generatedToken;
        System.out.println(emailText);
        MailStructure mailStructure = new MailStructure("Password Recovery", "<a href="+emailText+">" + emailText + "</a>");

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

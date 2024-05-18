package com.example.courseWork.controllers;

import com.example.courseWork.DTO.authDTO.*;
import com.example.courseWork.models.authModel.PasswordRecoveryTokenEntity;
import com.example.courseWork.models.authModel.Person;
import com.example.courseWork.services.authServices.PasswordRecoveryTokenService;
import com.example.courseWork.services.authServices.PeopleService;
import com.example.courseWork.util.exceptions.personException.LoginFailedException;
import com.example.courseWork.util.exceptions.personException.PasswordsNotMatchException;
import com.example.courseWork.util.exceptions.personException.PersonBadCredentialsException;
import com.example.courseWork.util.exceptions.personException.PersonNotFoundException;
import com.example.courseWork.util.validators.personValidator.UniqueEmailValidator;
import com.example.courseWork.util.validators.personValidator.UniqueUsernameValidator;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final PeopleService peopleService;
    private final PasswordRecoveryTokenService passwordRecoveryTokenService;
    private final UniqueUsernameValidator uniqueUsernameValidator;
    private final UniqueEmailValidator uniqueEmailValidator;

    @Autowired
    public AuthController(PeopleService peopleService, PasswordRecoveryTokenService passwordRecoveryTokenService,
                          UniqueUsernameValidator uniqueUsernameValidator, UniqueEmailValidator uniqueEmailValidator){
        this.peopleService = peopleService;
        this.passwordRecoveryTokenService = passwordRecoveryTokenService;
        this.uniqueUsernameValidator = uniqueUsernameValidator;
        this.uniqueEmailValidator = uniqueEmailValidator;
    }

    @GetMapping("/redirect")
    private void redirect(@RequestParam(value = "url") String redirectTo, HttpServletResponse response) throws IOException {
        response.sendRedirect(redirectTo);
    }

    @PostMapping("/registration")
    private ResponseEntity<HttpStatus> create(@RequestBody @Valid Person person, BindingResult bindingResult){
        uniqueUsernameValidator.validate(person,bindingResult);
        uniqueEmailValidator.validate(person,bindingResult);

        if(bindingResult.hasErrors()){
            List<FieldError> errors = bindingResult.getFieldErrors();
            List<String> stringErrors = new LinkedList<>();
            for(FieldError error : errors){
                stringErrors.add(error.getField() +" - " + error.getDefaultMessage()+";");
            }
            throw new PersonBadCredentialsException("Registration failed", stringErrors);
        }
        peopleService.save(person);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/login")
    private ResponseEntity<SendPersonFromLoginDTO> login(
        @RequestBody @Valid PersonLoginDTO personLoginDTO,
        BindingResult bindingResult,
        HttpServletRequest request) {
        if(bindingResult.hasErrors()){
            List<String> stringErrors = new LinkedList<>();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for(FieldError error : errors){
                stringErrors.add(error.getField() +" - " + error.getDefaultMessage()+";");
            }
            throw new PersonBadCredentialsException("Bad credentials",stringErrors);
        }
        try{
            request.login(personLoginDTO.getUsername(), personLoginDTO.getPassword());
        }catch(ServletException e){
            throw new LoginFailedException("Login failed. Invalid username or password!");
        }
        Person person = peopleService.findOne(personLoginDTO.getUsername());
        if(person == null){
            throw new PersonNotFoundException("User not found!");
        }
        SendPersonFromLoginDTO sendPersonFromLoginDTO = new SendPersonFromLoginDTO(person.getUsername(),
                person.getEmail(),person.getRole().getName());
        return ResponseEntity.ok(sendPersonFromLoginDTO);
    }

    @GetMapping("/me")
    @ResponseBody
    public ResponseEntity<SendPersonFromLoginDTO> currentUser(Principal principal) {
        Person person = peopleService.findOne(principal.getName());
        SendPersonFromLoginDTO sendPersonFromLoginDTO = new SendPersonFromLoginDTO(person.getUsername(),
                person.getEmail(), person.getRole().getName());
        return ResponseEntity.ok(sendPersonFromLoginDTO);
    }

    @PostMapping("/recover-password")
    private ResponseEntity<HttpStatus> recoverPassword(@RequestBody @Valid PersonPasswordRecoveryDTO personPasswordRecoveryDTO){
        String email = personPasswordRecoveryDTO.getEmail();
        peopleService.sendMailForChangingPasswordUnauthorized(email);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/change-password")
    private ResponseEntity<HttpStatus> changePassword(@RequestBody @Valid PersonChangePasswordDTO personChangePasswordDTO){
        if(passwordRecoveryTokenService.findByToken(personChangePasswordDTO.getToken())!= null){
            PasswordRecoveryTokenEntity passwordRecoveryTokenEntity = passwordRecoveryTokenService.findByToken(personChangePasswordDTO.getToken());
            Person personToChangePassword = peopleService.findPersonById(passwordRecoveryTokenEntity.getPerson().getId());
            if(personChangePasswordDTO.getPassword().equals(personChangePasswordDTO.getRepeatedPassword())){
                peopleService.updatePassword(personToChangePassword.getId(), personChangePasswordDTO.getPassword());
                passwordRecoveryTokenService.remove(passwordRecoveryTokenEntity);
                return ResponseEntity.ok(HttpStatus.OK);
            }else{
                throw new PasswordsNotMatchException("Passwords not equal!");
            }
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/auth-change-password")
    private ResponseEntity<HttpStatus> changePasswordAuth(@RequestBody @Valid PersonAuthChangePasswordDTO personAuthChangePasswordDTO, Principal principal){
        Person person = peopleService.findOne(principal.getName());
        peopleService.changePasswordForAuthenticated(personAuthChangePasswordDTO,person);
        return ResponseEntity.ok(HttpStatus.OK);
    }

   /* @ExceptionHandler
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
    }*/
}

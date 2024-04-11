package com.example.courseWork.controllers;

import com.example.courseWork.DTO.authDTO.*;
import com.example.courseWork.models.authModel.MailStructure;
import com.example.courseWork.models.authModel.PasswordRecoveryTokenEntity;
import com.example.courseWork.models.authModel.Person;
import com.example.courseWork.services.authServices.MailService;
import com.example.courseWork.services.authServices.PasswordRecoveryTokenService;
import com.example.courseWork.services.authServices.PeopleService;
import com.example.courseWork.util.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
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
    private final PasswordRecoveryTokenService passwordRecoveryTokenService;

    @Autowired
    public AuthController(PeopleService peopleService, MailService mailService, PasswordRecoveryTokenService passwordRecoveryTokenService) {
        this.peopleService = peopleService;
        this.mailService = mailService;
        this.passwordRecoveryTokenService = passwordRecoveryTokenService;
    }

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

    /*@PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid PersonLoginDTO personLoginDTO) {
        try {
            // Попытка аутентификации пользователя
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(personLoginDTO.getUsername(), personLoginDTO.getPassword())
            );

            // Установка аутентификации в контекст безопасности
            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetails userDetails = personDetailsService.loadUserByUsername(personLoginDTO.getUsername());

            return ResponseEntity.ok("Login successful!");
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed");
        }
    }*/
  /*  @ExceptionHandler(LoginFailedException.class)
    public ResponseEntity<String> handleLoginFailedException(LoginFailedException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }*/

    @PostMapping("/login")
    private ResponseEntity<SendPersonFromLoginDTO> login(
        @RequestBody @Valid PersonLoginDTO personLoginDTO,
        BindingResult bindingResult,
        HttpServletRequest request
    ) {
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
        Person person = peopleService.findOne(personLoginDTO.getUsername());
        if(person == null){
            throw new PersonNotFoundException("Пользователь не был найден.");
        }
        try{
            request.login(personLoginDTO.getUsername(), personLoginDTO.getPassword());
        }catch(ServletException e){
            throw new LoginFailedException("Login failed. Invalid username or password.");
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
        UUID uuid = UUID.randomUUID();

        String generatedToken = uuid.toString();
        String emailText = "https://cloud-saves://reset-password?token="+ generatedToken;
        String emailHtmlContent = "<html><body>"
                + "<h1>Password Recovery</h1>"
                + "<p>You can paste this link to the search bar!</p>"
                + "<p><a href=\"" + emailText + "\">" + emailText + "</a></p>"
                + "</body></html>";
        MailStructure mailStructure = new MailStructure("Password Recovery", emailHtmlContent);
        mailService.sendMail(email,mailStructure);

        Person person = peopleService.findPersonByEmail(email);
        PasswordRecoveryTokenEntity passwordRecoveryTokenEntity = new PasswordRecoveryTokenEntity();
        passwordRecoveryTokenEntity.setToken(generatedToken);
        passwordRecoveryTokenEntity.setPerson(person);
        passwordRecoveryTokenService.save(passwordRecoveryTokenEntity);

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
                return ResponseEntity.badRequest().build();
            }
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/auth-change-password")
    private ResponseEntity<HttpStatus> changePasswordAuth(@RequestBody @Valid PersonAuthChangePasswordDTO personAuthChangePasswordDTO, Principal principal){
        Person person = peopleService.findOne(principal.getName());
        if(personAuthChangePasswordDTO.getPassword().equals(personAuthChangePasswordDTO.getRepeatedPassword())){
            peopleService.updatePassword(person.getId(), personAuthChangePasswordDTO.getPassword());
            return ResponseEntity.ok(HttpStatus.OK);
        }else{
            return ResponseEntity.badRequest().build();
        }
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

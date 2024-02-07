package com.example.courseWork.util;

import com.example.courseWork.models.Person;
import com.example.courseWork.services.PersonDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class PersonValidator implements Validator {
    private PersonDetailsService personDetailsService;

    @Autowired
    public PersonValidator(PersonDetailsService personDetailsService) {
        this.personDetailsService = personDetailsService;
    }


    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;
        try{
            personDetailsService.loadUserByUsername(person.getUsername());
        }catch(UsernameNotFoundException ignored){
            return; //Все ок, пользователь с таким именем не найден!
        }
        errors.rejectValue("username","",
                "Person with this username already exists!");
    }

}

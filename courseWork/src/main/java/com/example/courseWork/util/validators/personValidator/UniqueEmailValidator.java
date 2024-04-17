package com.example.courseWork.util.validators.personValidator;

import com.example.courseWork.models.authModel.Person;
import com.example.courseWork.services.authServices.PeopleService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UniqueEmailValidator implements Validator {
    private final PeopleService peopleService;

    public UniqueEmailValidator(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person)target;
        if(peopleService.findPersonByEmail(person.getEmail())!=null){
            errors.rejectValue("email","","Person with this email already exists!");
        }
    }
}

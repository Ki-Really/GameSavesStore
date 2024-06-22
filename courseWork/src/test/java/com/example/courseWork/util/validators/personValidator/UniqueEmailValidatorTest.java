package com.example.courseWork.util.validators.personValidator;

import com.example.courseWork.models.authModel.Person;
import com.example.courseWork.services.authServices.PeopleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


class UniqueEmailValidatorTest {
    @Mock
    private PeopleService peopleService;

    @InjectMocks
    private UniqueEmailValidator uniqueEmailValidator;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testValidate_PersonWithExistingEmail() {

        Person person = new Person();
        person.setEmail("existing@example.com");

        when(peopleService.checkPersonPresentByEmail(person.getEmail())).thenReturn(person);

        Errors errors = new BeanPropertyBindingResult(person, "person");
        uniqueEmailValidator.validate(person, errors);

        assertTrue(errors.hasErrors());
        assertEquals("Person with this email already exists!",
                errors.getFieldError("email").getDefaultMessage());
    }

    @Test
    public void testValidate_PersonWithNonExistingEmail() {

        Person person = new Person();
        person.setEmail("new@example.com");

        when(peopleService.checkPersonPresentByEmail(person.getEmail())).thenReturn(null);

        Errors errors = new BeanPropertyBindingResult(person, "person");
        uniqueEmailValidator.validate(person, errors);

        assertFalse(errors.hasErrors());
    }
}
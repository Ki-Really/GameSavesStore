package com.example.courseWork.services;

import com.example.courseWork.models.Person;
import com.example.courseWork.repositories.PeopleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService{

    private final PeopleRepository peopleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationService(PeopleRepository peopleRepository, PasswordEncoder passwordEncoder) {
        this.peopleRepository = peopleRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @Transactional
    public void register(Person person){
        String encodedPassword = passwordEncoder.encode(person.getPassword());
        person.setPassword(encodedPassword);
        person.setRole("ROLE_ADMIN");
        peopleRepository.save(person);
    }
}

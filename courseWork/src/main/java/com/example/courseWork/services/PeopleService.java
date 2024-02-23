package com.example.courseWork.services;

import com.example.courseWork.models.Person;
import com.example.courseWork.repositories.PeopleRepository;
import com.example.courseWork.util.PersonNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PeopleService {
    private final PeopleRepository peopleRepository;
    private final PasswordEncoder passwordEncoder;
    private final RolesService rolesService;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository, PasswordEncoder passwordEncoder, RolesService rolesService) {
        this.peopleRepository = peopleRepository;
        this.passwordEncoder = passwordEncoder;
        this.rolesService = rolesService;
    }

    @Transactional
    public void save(Person person){
        String encodedPassword = passwordEncoder.encode(person.getPassword());
        person.setPassword(encodedPassword);
        person.setRole(rolesService.assignRole("ROLE_USER"));
        peopleRepository.save(person);
    }

    public Person checkCredentials(String username,String password){
        Optional<Person> person = peopleRepository.findByUsername(username);
        String encodedPassword = passwordEncoder.encode(password);

        if(person.isPresent() && passwordEncoder.matches(password,person.get().getPassword())) {
            System.out.println(encodedPassword);
            System.out.println(person.get().getPassword());
            return person.orElse(null);
        }
        return null;
    }

    public List<Person> findAll()
    {
        List<Person> people = peopleRepository.findAll();
        return people;
    }

    public Person findOne(int id)
    {
        Optional<Person> person = peopleRepository.findById(id);
        return person.orElseThrow(PersonNotFoundException::new);
    }

    public Person findOne(String username) {
        Optional<Person> person = peopleRepository.findByUsername(username);
        return person.orElseThrow(PersonNotFoundException::new);
    }
}

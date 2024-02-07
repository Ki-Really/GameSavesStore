package com.example.courseWork.services;

import com.example.courseWork.models.Person;
import com.example.courseWork.repositories.PeopleRepository;
import com.example.courseWork.util.PersonNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PeopleService {
    private final PeopleRepository peopleRepository;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    @Transactional
    public void save(Person person){
        peopleRepository.save(person);
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
}

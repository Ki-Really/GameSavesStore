package com.example.courseWork.services.authServices;

import com.example.courseWork.DTO.entityDTO.EntitiesResponseDTO;
import com.example.courseWork.DTO.usersDTO.PeopleRequestDTO;
import com.example.courseWork.DTO.usersDTO.PersonDTO;
import com.example.courseWork.models.authModel.Person;
import com.example.courseWork.repositories.authRepositories.PeopleRepository;
import com.example.courseWork.util.PersonNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
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
        person.setIsBlocked(false);
        peopleRepository.save(person);
    }

    @Transactional
    public void blockUser(int id) {
        Optional<Person> optionalPerson = peopleRepository.findById(id);
        if(optionalPerson.isPresent()){
            Person person = optionalPerson.get();
            person.setIsBlocked(true);
            peopleRepository.save(person);
        }
    }

    @Transactional
    public void unblockUser(int id) {
        Optional<Person> optionalPerson = peopleRepository.findById(id);
        if(optionalPerson.isPresent()) {
            Person person = optionalPerson.get();
            person.setIsBlocked(false);
            peopleRepository.save(person);
        }

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

    public Person findPersonByEmail(String email){
        Optional<Person> person = peopleRepository.findByEmail(email);
        return person.orElse(null);
    }

    public Person findPersonById(int id){
        Optional<Person> person = peopleRepository.findById(id);
        return person.orElse(null);
    }
    @Transactional
    public void updatePassword(int id,String password){
        Optional<Person> person = peopleRepository.findById(id);
        person.ifPresent(value -> {
            String encodedPassword = passwordEncoder.encode(password);
            value.setPassword(encodedPassword);
            peopleRepository.save(person.get());
        });
    }

    public EntitiesResponseDTO<PersonDTO> findAll(PeopleRequestDTO peopleRequestDTO, Principal principal){
        Page<Person> page;
        if(peopleRequestDTO.getSearchQuery()!=null && !peopleRequestDTO.getSearchQuery().isEmpty()){
             page = peopleRepository.findByUsernameContaining(peopleRequestDTO.getSearchQuery(), PageRequest.of(
                    peopleRequestDTO.getPageNumber() - 1,
                    peopleRequestDTO.getPageSize(),
                    Sort.by(Sort.Direction.DESC, "id")
            ));
        }else{
             page = peopleRepository.findAll(PageRequest.of(
                    peopleRequestDTO.getPageNumber() - 1,
                    peopleRequestDTO.getPageSize(),
                    Sort.by(Sort.Direction.DESC, "id")
            ));
        }
        List<Person> filteredPeople = page.getContent().stream()
                .filter(person -> !person.getUsername().equals(principal.getName()))
                .toList();

        EntitiesResponseDTO<PersonDTO> peopleDTO = new EntitiesResponseDTO<>();

        peopleDTO.setItems(filteredPeople.stream().map(
                this::constructPersonDTO
        ).toList());
        peopleDTO.setTotalCount(filteredPeople.size());

        return peopleDTO;
    }
    private PersonDTO constructPersonDTO(Person person){
        PersonDTO personDTO = new PersonDTO();
        personDTO.setId(person.getId());
        personDTO.setUsername(person.getUsername());
        personDTO.setEmail(person.getEmail());
        personDTO.setRole(person.getRole().getName());
        personDTO.setIsBlocked(person.getIsBlocked());
        return personDTO;
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

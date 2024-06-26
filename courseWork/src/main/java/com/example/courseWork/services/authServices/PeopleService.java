package com.example.courseWork.services.authServices;

import com.example.courseWork.DTO.authDTO.PersonAuthChangePasswordDTO;
import com.example.courseWork.DTO.entityDTO.EntitiesResponseDTO;
import com.example.courseWork.DTO.usersDTO.PeopleRequestDTO;
import com.example.courseWork.DTO.usersDTO.PersonDTO;
import com.example.courseWork.models.authModel.MailStructure;
import com.example.courseWork.models.authModel.PasswordRecoveryTokenEntity;
import com.example.courseWork.models.authModel.Person;
import com.example.courseWork.repositories.authRepositories.PeopleRepository;
import com.example.courseWork.util.exceptions.personException.PasswordsNotMatchException;
import com.example.courseWork.util.exceptions.personException.PersonNotFoundException;
import com.example.courseWork.util.exceptions.personException.WrongPasswordException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@Transactional
public class PeopleService {
    private final PeopleRepository peopleRepository;
    private final PasswordEncoder passwordEncoder;
    private final RolesService rolesService;
    private final MailService mailService;
    private final PasswordRecoveryTokenService passwordRecoveryTokenService;
    private final TokenCleanupScheduler tokenCleanupScheduler;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository, PasswordEncoder passwordEncoder, RolesService rolesService, MailService mailService, PasswordRecoveryTokenService passwordRecoveryTokenService, TokenCleanupScheduler tokenCleanupScheduler) {
        this.peopleRepository = peopleRepository;
        this.passwordEncoder = passwordEncoder;
        this.rolesService = rolesService;
        this.mailService = mailService;
        this.passwordRecoveryTokenService = passwordRecoveryTokenService;
        this.tokenCleanupScheduler = tokenCleanupScheduler;
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

    public void changePasswordForAuthenticated(PersonAuthChangePasswordDTO personAuthChangePasswordDTO, Person person) {
        if (passwordEncoder.matches(personAuthChangePasswordDTO.getOldPassword(),person.getPassword())){
            if (personAuthChangePasswordDTO.getPassword().equals(personAuthChangePasswordDTO.getRepeatedPassword())){
                updatePassword(person.getId(), personAuthChangePasswordDTO.getPassword());
            }else{
                throw new PasswordsNotMatchException("Passwords are not equals!");
            }
        }else{
            throw new WrongPasswordException("Wrong password!");
        }
    }

    @Transactional
    public void sendMailForChangingPasswordUnauthorized(String email){
        if(findPersonByEmail(email) != null){
            UUID uuid = UUID.randomUUID();
            String generatedToken = uuid.toString();
            String emailText = "cloud-saves://reset-password?token="+ generatedToken;
            String emailHtmlContent = "<html><body>"
                    + "<h1>Password Recovery</h1>"
                    + "<p>You can paste this link to the search bar!</p>"
                    + "<p><a href=\"" + "http://localhost:8080/auth/redirect?url="+ emailText + "\">" + emailText + "</a></p>"
                    + "</body></html>";
            MailStructure mailStructure = new MailStructure("Password Recovery", emailHtmlContent);
            mailService.sendMail(email,mailStructure);
            Person person = findPersonByEmail(email);
            PasswordRecoveryTokenEntity passwordRecoveryTokenEntity = new PasswordRecoveryTokenEntity();
            passwordRecoveryTokenEntity.setToken(generatedToken);
            passwordRecoveryTokenEntity.setPerson(person);
            passwordRecoveryTokenEntity.setExpiryDate(LocalDateTime.now().plusMinutes(1));
            passwordRecoveryTokenService.save(passwordRecoveryTokenEntity);
            tokenCleanupScheduler.cleanupExpiredTokens();
        }
    }

    public Person findPersonByEmail(String email){
        Optional<Person> person = peopleRepository.findByEmail(email);
        return person.orElseThrow(() -> new PersonNotFoundException("Person not found with this email:" + email));
    }

    public Person checkPersonPresentByEmail(String email){
        Optional<Person> person = peopleRepository.findByEmail(email);
        return person.orElse(null);
    }

    public Person findPersonById(int id){
        Optional<Person> person = peopleRepository.findById(id);
        return person.orElseThrow(() -> new PersonNotFoundException("Person not found with this id:" + id));
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

    public Person findOne(String username) {
        Optional<Person> person = peopleRepository.findByUsername(username);
        return person.orElseThrow(() -> new PersonNotFoundException("Person not found with username: " + username));
    }

}

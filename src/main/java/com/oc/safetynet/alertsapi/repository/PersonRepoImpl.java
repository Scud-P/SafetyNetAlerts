package com.oc.safetynet.alertsapi.repository;

import com.oc.safetynet.alertsapi.model.Data;
import com.oc.safetynet.alertsapi.model.Person;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class PersonRepoImpl implements PersonRepo {

    private static final Logger logger = LoggerFactory.getLogger(PersonRepoImpl.class);

    private List<Person> persons = new ArrayList<>();

    private final DataRepository dataRepository;

    @Autowired
    public PersonRepoImpl(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    @PostConstruct
    private void loadAllPersons() {
        try {
            Data data = dataRepository.readData();
            persons = data.getPersons();
            if (persons != null) {
                logger.info("Loaded List of persons from data {}.", persons);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read data from the repository", e);
        }
    }

    @Override
    public List<Person> getAllPersons() {
        return persons;
    }

    @Override
    public Person addPersonToList(Person person) {
            if (person == null) {
                persons = new ArrayList<>();
            }
            if (persons.contains(person)) {
                logger.error("Person with firstName {} and lastName {} already in DB", person.getFirstName(), person.getLastName());
            } else {
                persons.add(person);
                logger.info("Person added: {}", person);
                logger.info("New List of persons: {}", persons);
                return person;
            }
        return null;
    }

    @Override
    public void deletePersonFromList(String firstName, String lastName) {
            Optional<Person> personToRemove = persons.stream()
                    .filter(person -> person.getFirstName().equals(firstName) && person.getLastName().equals(lastName))
                    .findFirst();
            if (personToRemove.isPresent()) {
                Person removedPerson = personToRemove.get();
                persons.remove(removedPerson);
                logger.info("Person removed: {}", removedPerson);
                logger.info("New List of persons: {}", persons);

            } else {
                logger.error("Person not found : First Name: {}, Last Name: {}", firstName, lastName);
            }
    }

    @Override
    public Person updatePerson(Person person) {
            Optional<Person> foundPerson = persons.stream()
                    .filter(currentPerson ->
                            currentPerson.getFirstName().equals(person.getFirstName()) &&
                                    currentPerson.getLastName().equals(person.getLastName()))
                    .findFirst();

            if (foundPerson.isPresent()) {
                foundPerson.ifPresent(existingPerson -> {
                    existingPerson.setFirstName(person.getFirstName());
                    existingPerson.setLastName(person.getLastName());
                    existingPerson.setAddress(person.getAddress());
                    existingPerson.setPhone(person.getPhone());
                    existingPerson.setEmail(person.getEmail());
                    existingPerson.setZip(person.getZip());
                    existingPerson.setCity(person.getCity());
                });

                logger.info("Person modified: {}", person);
                logger.info("New List of persons: {}", persons);
                return person;
            } else {
                logger.error("Person not found for: {} {}", person.getFirstName(), person.getLastName());
            }
        return null;
    }

    @Override
    public List<String> findEmailsByCity(String city) {
        List<String> emails = persons.stream()
                .filter(person -> person.getCity().equalsIgnoreCase(city))
                .map(Person::getEmail)
                .distinct()
                .toList();
        logger.info("Emails for City {}: {}", city, emails);
        return emails;
    }

    @Override
    public List<Person> findAllByFirstNameAndLastName(String firstName, String lastName) {
        return persons.stream()
                .filter(person -> person.getFirstName().equalsIgnoreCase(firstName) && person.getLastName().equalsIgnoreCase(lastName)).toList();
    }

    @Override
    public List<Person> findPersonsByAddresses(List<String> addresses) {
        return persons.stream()
                .filter(person -> addresses.contains(person.getAddress()))
                .toList();
    }

    @Override
    public List<Person> findPersonsByAddress(String address) {
        return persons.stream()
                .filter(person -> address.contains(person.getAddress()))
                .toList();
    }

}

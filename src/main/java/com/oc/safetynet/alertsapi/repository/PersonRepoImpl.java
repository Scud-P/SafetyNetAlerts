package com.oc.safetynet.alertsapi.repository;

import com.oc.safetynet.alertsapi.model.Data;
import com.oc.safetynet.alertsapi.model.FireStation;
import com.oc.safetynet.alertsapi.model.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
public class PersonRepoImpl implements PersonRepo {

    private static final Logger logger = LoggerFactory.getLogger(PersonRepoImpl.class);

    private final DataRepository dataRepository;

    @Autowired
    public PersonRepoImpl(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    @Override
    public List<Person> getAllPersons() {
        try {
            Data data = dataRepository.readData();
            List<Person> persons = data.getPersons();
            if (persons == null) {
                return Collections.emptyList();
            }
            return persons;
        } catch (IOException e) {
            throw new RuntimeException("Failed to read data from the repository", e);
        }
    }

    @Override
    public Person addPersonToList(Person person) {
        try {
            Data data = dataRepository.readData();
            List<Person> persons = data.getPersons();

            if(persons == null) {
                persons = new ArrayList<>();
            }
            if (persons.contains(person)) {
                logger.error("Person with firstName {} and lastName {} already in DB", person.getFirstName(), person.getLastName());
            } else {
                persons.add(person);
                data.setPersons(persons);
                logger.info("Person added: {}", person);
                logger.info("New List of persons: {}", data.getPersons());
                dataRepository.writeData(data);
                return person;
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read data from the repository", e);
        }
        return null;
    }

    @Override
    public void deletePersonFromList(String firstName, String lastName) {
        try {
            Data data = dataRepository.readData();
            List<Person> persons = data.getPersons();
            Optional<Person> personToRemove = persons.stream()
                    .filter(person -> person.getFirstName().equals(firstName) && person.getLastName().equals(lastName))
                    .findFirst();
            if (personToRemove.isPresent()) {
                Person removedPerson = personToRemove.get();
                persons.remove(removedPerson);
                logger.info("Person removed: {}", removedPerson);
                logger.info("New List of persons: {}", persons);
                data.setPersons(persons);
                dataRepository.writeData(data);
            } else {
                logger.error("Person not found : First Name: {}, Last Name: {}", firstName, lastName);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read data from the repository", e);
        }
    }

    @Override
    public Person updatePerson(Person person) {
        try {
            Data data = dataRepository.readData();
            List<Person> currentPersons = data.getPersons();

            Optional<Person> foundPerson = currentPersons.stream()
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

                data.setPersons(currentPersons);

                logger.info("Person modified: {}", person);
                logger.info("New List of persons: {}", data.getPersons());

                dataRepository.writeData(data);
                return person;
            } else {
                logger.error("Person not found for: {} {}", person.getFirstName(), person.getLastName());
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to read data from the repository", e);
        }
        return null;
    }

    @Override
    public List<String> findEmailsByCity(String city) {
        try {
            Data data = dataRepository.readData();
            List<Person> persons = data.getPersons();
            return persons.stream()
                    .filter(person -> person.getCity().equalsIgnoreCase(city))
                    .map(Person::getEmail)
                    .distinct()
                    .toList();

        } catch (IOException e) {
            throw new RuntimeException("Failed to read data from the repository", e);
        }
    }

    @Override
    public List<Person> findAllByFirstNameAndLastName(String firstName, String lastName) {

        try {
            Data data = dataRepository.readData();
            return data.getPersons().stream()
                    .filter(person -> person.getFirstName().equalsIgnoreCase(firstName) && person.getLastName().equalsIgnoreCase(lastName)).toList();
        } catch (IOException e) {
            throw new RuntimeException("Failed to read data from the repository", e);
        }
    }
    @Override
    public List<Person> findPersonsByAddresses(List<String> addresses) {

        try {
            Data data = dataRepository.readData();
            return data.getPersons().stream()
                    .filter(person -> addresses.contains(person.getAddress()))
                    .toList();

        } catch (IOException e) {
            throw new RuntimeException("Failed to read data from the repository", e);
        }
    }

    @Override
    public List<Person> findPersonsByAddress(String address) {
        try {
            Data data = dataRepository.readData();
            return data.getPersons().stream()
                    .filter(person -> address.contains(person.getAddress()))
                    .toList();

        } catch (IOException e) {
            throw new RuntimeException("Failed to read data from the repository", e);
        }
    }

}

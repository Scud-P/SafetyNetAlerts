package com.oc.safetynet.alertsapi.repository;

import com.oc.safetynet.alertsapi.model.Person;

import java.util.List;

public interface PersonRepo {
    List<Person> getAllPersons();
    void addPersonToList(Person person);

    void deletePersonFromList(String firstName, String lastName);

    void updatePerson(Person person);

    List<String> findEmailsByCity(String City);

    List<Person> findAllByFirstNameAndLastName(String firstName, String lastName);

}

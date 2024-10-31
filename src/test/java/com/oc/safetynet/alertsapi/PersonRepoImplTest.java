package com.oc.safetynet.alertsapi;

import com.oc.safetynet.alertsapi.model.Data;
import com.oc.safetynet.alertsapi.model.Person;
import com.oc.safetynet.alertsapi.service.JsonReaderService;
import com.oc.safetynet.alertsapi.repository.PersonRepoImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class PersonRepoImplTest {

    @Mock
    private JsonReaderService jsonReaderService;

    @InjectMocks
    private PersonRepoImpl personRepo;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getAllPersons_ShouldReturnListOfPersons() throws IOException {
        List<Person> mockPersons = new ArrayList<>();
        when(jsonReaderService.readData()).thenReturn(new Data());
        List<Person> result = personRepo.getAllPersons();
        assertEquals(mockPersons, result);
    }

    @Test
    public void addPersonToList_ShouldAddPersonToList() {
        Person person = new Person("John", "Doe", "123 Main St", "123-456-7890", "john@example.com", "12345", "City");
        personRepo.addPersonToList(person);

        List<Person> updatedPersons = personRepo.getAllPersons();

        assertEquals(1, updatedPersons.size());
        assertEquals(person, updatedPersons.get(0));
    }

    @Test
    public void deletePersonFromList_shouldDeletePersonFromList() {
        Person person = new Person("John", "Doe", "123 Main St", "123-456-7890", "john@example.com", "12345", "City");
        Person person2 = new Person("Jane", "Doe", "123 Main St", "123-456-7890", "john@example.com", "12345", "City");

        personRepo.addPersonToList(person);
        personRepo.addPersonToList(person2);

        personRepo.deletePersonFromList(person.getFirstName(), person.getLastName());

        List<Person> updatedPersons = personRepo.getAllPersons();

        assertEquals(1, updatedPersons.size());
        assertEquals(person2, updatedPersons.get(0));
    }

    @Test
    public void updatePerson_shouldUpdateAPerson() {

        Person person = new Person("John", "Doe", "123 Main St", "City", "00000", "12345", "john@example.com");
        Person person2 = new Person("Jane", "Doe", "123 Main St", "City", "00000", "12345", "jane@example.com");

        personRepo.addPersonToList(person);
        personRepo.addPersonToList(person2);

        Person person3 = new Person("John", "Doe", "456 Main St", "City", "00000", "12345", "jane@example.com");

        personRepo.updatePerson(person3);

        List<Person> updatedPersons = personRepo.getAllPersons();

        assertEquals(2, updatedPersons.size());
        assertEquals("456 Main St", updatedPersons.get(0).getAddress());
    }

    @Test
    public void findEmailsByCityTest() {
        Person person = new Person("John", "Doe", "123 Main St", "City", "00000", "12345", "john@example.com");
        Person person2 = new Person("Jane", "Doe", "123 Main St", "City", "00000", "12345", "jane@example.com");

        personRepo.addPersonToList(person);
        personRepo.addPersonToList(person2);

        String city = "city";
        List<String> result = personRepo.findEmailsByCity(city);

        assertEquals(2, result.size());
        assertEquals(person.getEmail(), result.get(0));
    }

    @Test
    public void findAllByFirstNameAndLastName() throws IOException {
        Person person = new Person("John", "Doe", "123 Main St", "City", "00000", "12345", "john@example.com");
        Person person2 = new Person("John", "Doe", "456 Main St", "City", "00000", "12345", "jane@example.com");

        personRepo.addPersonToList(person);
        personRepo.addPersonToList(person2);

        String firstName = "John";
        String lastName = "Doe";

        List<Person> result = personRepo.findAllByFirstNameAndLastName(firstName, lastName);

        assertEquals(2, result.size());
        assertEquals(person.getAddress(), result.get(0).getAddress());
        assertEquals(person2.getAddress(), result.get(1).getAddress());
    }

    @Test
    public void findPersonsByAddresses() throws IOException {
        Person person = new Person("John", "Doe", "123 Main St", "City", "00000", "12345", "john@example.com");
        Person person2 = new Person("Jane", "Doe", "123 Main St", "City", "00000", "12345", "jane@example.com");
        Person person3 = new Person("John", "Smith", "456 Main St", "City", "00000", "12345", "john@example.com");
        Person person4 = new Person("Jane", "Smith", "456 Main St", "City", "00000", "12345", "jane@example.com");

        personRepo.addPersonToList(person);
        personRepo.addPersonToList(person2);
        personRepo.addPersonToList(person3);
        personRepo.addPersonToList(person4);

        String address1 = "123 Main St";
        String address2 = "456 Main St";

        List<String> addresses = List.of(
                address1,
                address2
        );

        List<Person> result = personRepo.findPersonsByAddresses(addresses);

        assertEquals(4, result.size());
        assertEquals(person.getZip(), result.get(0).getZip());
        assertEquals(person2.getAddress(), result.get(1).getAddress());
    }

    @Test
    public void findPersonsByAddress_shouldReturnAListOfPersonsLivingAtThisAddress() throws IOException {

        Person person = new Person("John", "Doe", "123 Main St", "City", "00000", "12345", "john@example.com");
        Person person2 = new Person("Jane", "Doe", "123 Main St", "City", "00000", "12345", "jane@example.com");
        Person person3 = new Person("John", "Smith", "456 Main St", "City", "00000", "12345", "john@example.com");
        Person person4 = new Person("Jane", "Smith", "456 Main St", "City", "00000", "12345", "jane@example.com");

        personRepo.addPersonToList(person);
        personRepo.addPersonToList(person2);
        personRepo.addPersonToList(person3);
        personRepo.addPersonToList(person4);

        String address1 = "123 Main St";

        List<Person> result = personRepo.findPersonsByAddress(address1);

        assertEquals(2, result.size());
        assertEquals(person, result.get(0));
    }

}


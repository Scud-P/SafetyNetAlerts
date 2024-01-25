package com.oc.safetynet.alertsapi;

import com.oc.safetynet.alertsapi.model.Data;
import com.oc.safetynet.alertsapi.model.Person;
import com.oc.safetynet.alertsapi.repository.DataRepository;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class PersonRepoImplTest {

    @Mock
    private DataRepository dataRepository;

    @InjectMocks
    private PersonRepoImpl personRepo;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getAllPersons_ShouldReturnListOfPersons() throws IOException {
        List<Person> mockPersons = new ArrayList<>();
        when(dataRepository.readData()).thenReturn(new Data());
        List<Person> result = personRepo.getAllPersons();
        assertEquals(mockPersons, result);
    }

    @Test
    public void addPersonToList_ShouldAddPersonToList() throws IOException {
        Person person = new Person("John", "Doe", "123 Main St", "123-456-7890", "john@example.com", "12345", "City");

        Data mockData = new Data();

        when(dataRepository.readData()).thenReturn(mockData);

        personRepo.addPersonToList(person);

        assertEquals(1, mockData.getPersons().size());
        assertEquals(person, mockData.getPersons().get(0));

        verify(dataRepository, times(1)).writeData(mockData);
    }

    @Test
    public void deletePersonFromList_shouldDeletePersonFromList() throws IOException {
        Person person = new Person("John", "Doe", "123 Main St", "123-456-7890", "john@example.com", "12345", "City");
        Person person2 = new Person("Jane", "Doe", "123 Main St", "123-456-7890", "john@example.com", "12345", "City");

        List<Person> persons = new ArrayList<>();
        persons.add(person);
        persons.add(person2);

        Data mockData = new Data();
        mockData.setPersons(persons);

        when(dataRepository.readData()).thenReturn(mockData);

        personRepo.deletePersonFromList(person.getFirstName(), person.getLastName());

        assertEquals(1, mockData.getPersons().size());
        assertEquals(person2, mockData.getPersons().get(0));
    }

    @Test
    public void getAllPersons_IOExceptionThrown_ShouldThrowRuntimeException() throws IOException {
        when(dataRepository.readData()).thenThrow(new IOException("Mocked IOException"));
        assertThrows(RuntimeException.class, () -> personRepo.getAllPersons());
    }

    @Test
    public void updatePerson_shouldUpdateAPerson() throws IOException {

        Person person = new Person("John", "Doe", "123 Main St", "City", "00000", "12345", "john@example.com");
        Person person2 = new Person("Jane", "Doe", "123 Main St", "City", "00000", "12345", "jane@example.com");

        List<Person> persons = new ArrayList<>();
        persons.add(person);
        persons.add(person2);

        Data mockData = new Data();
        mockData.setPersons(persons);

        when(dataRepository.readData()).thenReturn(mockData);

        Person person3 = new Person("John", "Doe", "456 Main St", "City", "00000", "12345", "jane@example.com");

        personRepo.updatePerson(person3);

        assertEquals(2, mockData.getPersons().size());
        assertEquals("456 Main St", mockData.getPersons().get(0).getAddress());
    }

    @Test
    public void findEmailsByCityTest() throws IOException {
        Person person = new Person("John", "Doe", "123 Main St", "City", "00000", "12345", "john@example.com");
        Person person2 = new Person("Jane", "Doe", "123 Main St", "City", "00000", "12345", "jane@example.com");

        List<Person> persons = new ArrayList<>();
        persons.add(person);
        persons.add(person2);

        Data mockData = new Data();
        mockData.setPersons(persons);

        when(dataRepository.readData()).thenReturn(mockData);

        String city = "city";

        List<String> result = personRepo.findEmailsByCity(city);

        assertEquals(2, result.size());
        assertEquals(person.getEmail(), result.get(0));
    }

    @Test
    public void findAllByFirstNameAndLastName() throws IOException {
        Person person = new Person("John", "Doe", "123 Main St", "City", "00000", "12345", "john@example.com");
        Person person2 = new Person("John", "Doe", "456 Main St", "City", "00000", "12345", "jane@example.com");

        List<Person> persons = new ArrayList<>();
        persons.add(person);
        persons.add(person2);

        Data mockData = new Data();
        mockData.setPersons(persons);

        when(dataRepository.readData()).thenReturn(mockData);

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

        List<Person> persons = new ArrayList<>();
        persons.add(person);
        persons.add(person2);
        persons.add(person3);
        persons.add(person4);

        Data mockData = new Data();
        mockData.setPersons(persons);

        when(dataRepository.readData()).thenReturn(mockData);

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

        List<Person> persons = new ArrayList<>();
        persons.add(person);
        persons.add(person2);
        persons.add(person3);
        persons.add(person4);

        Data mockData = new Data();
        mockData.setPersons(persons);

        when(dataRepository.readData()).thenReturn(mockData);

        String address1 = "123 Main St";

        List<Person> result = personRepo.findPersonsByAddress(address1);

        assertEquals(2, result.size());
        assertEquals(person, result.get(0));
    }

}


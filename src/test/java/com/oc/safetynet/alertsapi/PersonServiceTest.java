package com.oc.safetynet.alertsapi;

import com.oc.safetynet.alertsapi.controller.FireStationController;
import com.oc.safetynet.alertsapi.controller.MedicalRecordController;
import com.oc.safetynet.alertsapi.controller.PersonController;
import com.oc.safetynet.alertsapi.model.Data;
import com.oc.safetynet.alertsapi.model.MedicalRecord;
import com.oc.safetynet.alertsapi.model.Person;
import com.oc.safetynet.alertsapi.model.dto.*;
import com.oc.safetynet.alertsapi.repository.FireStationRepoImpl;
import com.oc.safetynet.alertsapi.repository.MedicalRecordRepoImpl;
import com.oc.safetynet.alertsapi.repository.PersonRepoImpl;
import com.oc.safetynet.alertsapi.service.PersonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@WebMvcTest(PersonService.class)
@ComponentScan(basePackages = "com.oc.safetynet.alertsapi")

public class PersonServiceTest {

    @Autowired
    private PersonService personService;

    @MockBean
    private PersonController personController;

    @MockBean
    private PersonRepoImpl personRepoImpl;

    @MockBean
    private FireStationRepoImpl fireStationRepoImpl;

    @MockBean
    private MedicalRecordRepoImpl medicalRecordRepoImpl;

    @MockBean
    private MedicalRecordController medicalRecordController;

    @MockBean
    private FireStationController fireStationController;


    @Test
    public void testFindPersonInfoListDTO() {

        String address1 = "test address 1";
        String address2 = "test address 2";

        List<String> allergiesJohnDoe = new ArrayList<>();
        allergiesJohnDoe.add("Capitalism");
        allergiesJohnDoe.add("Pop Music");
        List<String> medicationsJohnDoe = new ArrayList<>();
        medicationsJohnDoe.add("Reggae");
        medicationsJohnDoe.add("Women");


        List<String> allergiesJohnSmith = new ArrayList<>();
        allergiesJohnSmith.add("Shellfish");
        allergiesJohnSmith.add("Gluten");
        List<String> medicationsJohnSmith = new ArrayList<>();
        medicationsJohnSmith.add("Prozac 30mg");
        medicationsJohnSmith.add("Viagra 10mg");

        List<Person> persons = List.of(
                new Person("Bob", "Ross", address1, "test city", "test zip", "test phone", "test email"),
                new Person("Bob", "Ross", address2, "test city", "test zip", "test phone", "test email")
        );

        List<MedicalRecord> medicalRecords = List.of(
                new MedicalRecord("Bob", "Ross", "01/01/2000", medicationsJohnDoe, allergiesJohnDoe),
                new MedicalRecord("Bob", "Ross", "01/01/2020", medicationsJohnSmith, allergiesJohnSmith)
        );

        when(personRepoImpl.findAllByFirstNameAndLastName(any(), any())).thenReturn(persons);
        when(medicalRecordRepoImpl.findAllByFirstNameAndLastName(any(), any())).thenReturn(medicalRecords);

        List<PersonInfoDTO> result = personService.findPersonInfoListDTO("Bob", "Ross");

        verify(personRepoImpl, times(1)).findAllByFirstNameAndLastName("Bob", "Ross");
        verify(medicalRecordRepoImpl, times(1)).findAllByFirstNameAndLastName("Bob", "Ross");
    }

    @Test
    public void testFindPhonesByStation() {

        int station = 1;

        List<String> addresses = List.of("address1", "address2");

        List<Person> persons = List.of(
                new Person("Bob", "Ross", "address1", "test city", "test zip", "test phone1", "test email"),
                new Person("Bob", "Ross", "address2", "test city", "test zip", "test phone2", "test email")
        );

        when(fireStationRepoImpl.findAddressesByStation(station)).thenReturn(addresses);
        when(personRepoImpl.findPersonsByAddresses(addresses)).thenReturn(persons);

        List<String> expectedPhones = List.of("test phone1", "test phone2");

        List<String> result = personService.findPhonesByStation(station);

        assertEquals(expectedPhones, result);
    }

    @Test
    public void findHomesByAddressTest() {

        int station = 1;

        List<String> addresses = List.of("address1", "address2");

        List<Person> personsAtAddress1 = new ArrayList<>();
        List<Person> personsAtAddress2 = new ArrayList<>();

        Person person1 = new Person("John", "Doe", "address1", "test city", "test zip", "test phone1", "test email");
        Person person2 = new Person("Jane", "Doe", "address1", "test city", "test zip", "test phone2", "test email");
        Person person3 = new Person("John", "Smith", "address1", "test city", "test zip", "test phone1", "test email");
        Person person4 = new Person("Jane", "Smith", "address1", "test city", "test zip", "test phone2", "test email");

        personsAtAddress1.add(person1);
        personsAtAddress1.add(person2);
        personsAtAddress2.add(person3);
        personsAtAddress2.add(person4);


        List<String> allergiesJohnDoe = new ArrayList<>();
        allergiesJohnDoe.add("Capitalism");
        allergiesJohnDoe.add("Pop Music");
        List<String> medicationsJohnDoe = new ArrayList<>();
        medicationsJohnDoe.add("Reggae");
        medicationsJohnDoe.add("Women");


        List<String> allergiesJohnSmith = new ArrayList<>();
        allergiesJohnSmith.add("Shellfish");
        allergiesJohnSmith.add("Gluten");
        List<String> medicationsJohnSmith = new ArrayList<>();
        medicationsJohnSmith.add("Prozac 30mg");
        medicationsJohnSmith.add("Viagra 10mg");

        List<String> allergiesJaneDoe = new ArrayList<>();
        allergiesJaneDoe.add("Capitalism");
        allergiesJaneDoe.add("Pop Music");
        List<String> medicationsJaneDoe = new ArrayList<>();
        medicationsJaneDoe.add("Reggae");
        medicationsJaneDoe.add("Women");


        List<String> allergiesJaneSmith = new ArrayList<>();
        allergiesJaneSmith.add("Shellfish");
        allergiesJaneSmith.add("Gluten");
        List<String> medicationsJaneSmith = new ArrayList<>();
        medicationsJaneSmith.add("Prozac 30mg");
        medicationsJaneSmith.add("Viagra 10mg");

        MedicalRecord medicalRecordJohnDoe = new MedicalRecord("John", "Doe", "01/01/2000", medicationsJohnDoe, allergiesJohnDoe);
        MedicalRecord medicalRecordJaneDoe = new MedicalRecord("Jane", "Doe", "01/01/2020", medicationsJaneDoe, allergiesJaneDoe);
        MedicalRecord medicalRecordJohnSmith = new MedicalRecord("John", "Smith", "01/01/2000", medicationsJohnSmith, allergiesJohnSmith);
        MedicalRecord medicalRecordJaneSmith = new MedicalRecord("Jane", "Smith", "01/01/2020", medicationsJaneSmith, allergiesJaneSmith);

        when(fireStationRepoImpl.findAddressesByStation(station)).thenReturn(addresses);
        when(personRepoImpl.findPersonsByAddresses(Collections.singletonList("address1"))).thenReturn(personsAtAddress1);
        when(personRepoImpl.findPersonsByAddresses(Collections.singletonList("address2"))).thenReturn(personsAtAddress2);

        when(medicalRecordRepoImpl.findByFirstNameAndLastName("John", "Doe")).thenReturn(medicalRecordJohnDoe);
        when(medicalRecordRepoImpl.findByFirstNameAndLastName("Jane", "Doe")).thenReturn(medicalRecordJaneDoe);
        when(medicalRecordRepoImpl.findByFirstNameAndLastName("John", "Smith")).thenReturn(medicalRecordJohnSmith);
        when(medicalRecordRepoImpl.findByFirstNameAndLastName("Jane", "Smith")).thenReturn(medicalRecordJaneSmith);


        List<Person> persons = List.of(person1, person2, person3, person4);
        List<MedicalRecord> medicalRecords = List.of(medicalRecordJohnDoe, medicalRecordJaneDoe, medicalRecordJohnSmith, medicalRecordJaneSmith);

        Data mockData = new Data();
        mockData.setPersons(persons);
        mockData.setMedicalrecords(medicalRecords);


        List<HomeDTO> result = personService.findHomesByAddresses(station);

        assertEquals(2, result.size());
        assertEquals("address1", result.get(0).getAddress());
        assertEquals(2, result.get(0).getFamilyMembers().size());
        assertEquals("address2", result.get(1).getAddress());
        assertEquals(2, result.get(1).getFamilyMembers().size());
    }

    @Test
    public void findPersonsAtAddressTest() {

        String address = "address1";

        Person person1 = new Person("John", "Doe", "address1", "test city", "test zip", "test phone1", "test email");
        Person person2 = new Person("Jane", "Doe", "address1", "test city", "test zip", "test phone2", "test email");

        List<Person> persons = new ArrayList<>();
        persons.add(person1);
        persons.add(person2);

        List<String> allergiesJohnDoe = new ArrayList<>();
        allergiesJohnDoe.add("Capitalism");
        allergiesJohnDoe.add("Pop Music");
        List<String> medicationsJohnDoe = new ArrayList<>();
        medicationsJohnDoe.add("Reggae");
        medicationsJohnDoe.add("Women");

        List<String> allergiesJaneDoe = new ArrayList<>();
        allergiesJaneDoe.add("Capitalism");
        allergiesJaneDoe.add("Pop Music");
        List<String> medicationsJaneDoe = new ArrayList<>();
        medicationsJaneDoe.add("Reggae");
        medicationsJaneDoe.add("Women");

        MedicalRecord medicalRecordJohnDoe = new MedicalRecord("John", "Doe", "01/01/2000", medicationsJohnDoe, allergiesJohnDoe);
        MedicalRecord medicalRecordJaneDoe = new MedicalRecord("Jane", "Doe", "01/01/2020", medicationsJaneDoe, allergiesJaneDoe);

        when(personRepoImpl.findPersonsByAddress(address)).thenReturn(persons);
        when(medicalRecordRepoImpl.findByFirstNameAndLastName("John", "Doe")).thenReturn(medicalRecordJohnDoe);
        when(medicalRecordRepoImpl.findByFirstNameAndLastName("Jane", "Doe")).thenReturn(medicalRecordJaneDoe);

        int station = 1;

        when(fireStationRepoImpl.findStationByAddress(address)).thenReturn(station);

        PersonFireWithStationNumberDTO expectedDTO = new PersonFireWithStationNumberDTO();
        expectedDTO.setStation(station);

        List<PersonFireDTO> personFireDTOS = persons.stream()
                .map(person -> {
                    MedicalRecord medicalRecord = medicalRecordRepoImpl.findByFirstNameAndLastName(person.getFirstName(), person.getLastName());
                    int age = medicalRecordRepoImpl.calculateAge(medicalRecord);
                    return new PersonFireDTO(person, medicalRecord, age);
                })
                .toList();
        expectedDTO.setPersonFireDTOs(personFireDTOS);

        PersonFireWithStationNumberDTO resultDTO = personService.findPersonsAtAddress(address);

        assertEquals(expectedDTO.getStation(), resultDTO.getStation());
        assertEquals(expectedDTO.getPersonFireDTOs().size(), resultDTO.getPersonFireDTOs().size());
    }

    @Test
    public void findMinorsAtAddressTest() {

        String address = "address1";

        Person person1 = new Person("John", "Doe", "address1", "test city", "test zip", "test phone1", "test email");
        Person person2 = new Person("Jane", "Doe", "address1", "test city", "test zip", "test phone2", "test email");

        List<Person> persons = new ArrayList<>();
        persons.add(person1);
        persons.add(person2);

        List<String> allergiesJohnDoe = new ArrayList<>();
        allergiesJohnDoe.add("Capitalism");
        allergiesJohnDoe.add("Pop Music");
        List<String> medicationsJohnDoe = new ArrayList<>();
        medicationsJohnDoe.add("Reggae");
        medicationsJohnDoe.add("Women");

        List<String> allergiesJaneDoe = new ArrayList<>();
        allergiesJaneDoe.add("Capitalism");
        allergiesJaneDoe.add("Pop Music");
        List<String> medicationsJaneDoe = new ArrayList<>();
        medicationsJaneDoe.add("Reggae");
        medicationsJaneDoe.add("Women");

        MedicalRecord medicalRecordJohnDoe = new MedicalRecord("John", "Doe", "01/01/2000", medicationsJohnDoe, allergiesJohnDoe);
        MedicalRecord medicalRecordJaneDoe = new MedicalRecord("Jane", "Doe", "01/01/2020", medicationsJaneDoe, allergiesJaneDoe);

        when(personRepoImpl.findPersonsByAddress(address)).thenReturn(persons);

        String johnFirstName = "John";
        String janeFirstName = "Jane";
        String johnLastName = "Doe";
        String janeLastName = "Doe";

        when(medicalRecordRepoImpl.findAllByFirstNameAndLastName(johnFirstName, johnLastName))
                .thenReturn(Collections.singletonList(medicalRecordJohnDoe));
        when(medicalRecordRepoImpl.findAllByFirstNameAndLastName(janeFirstName, janeLastName))
                .thenReturn(Collections.singletonList(medicalRecordJaneDoe));
        when(medicalRecordRepoImpl.findMinors(Collections.singletonList(medicalRecordJohnDoe)))
                .thenReturn(Collections.emptyList());
        when(medicalRecordRepoImpl.findMinors(Collections.singletonList(medicalRecordJaneDoe)))
                .thenReturn(Collections.singletonList(medicalRecordJaneDoe));

        List<ChildDTO> expectedMinors = new ArrayList<>();
        expectedMinors.add(new ChildDTO(person2, medicalRecordJaneDoe));

        List<ChildDTO> result = personService.findMinorsAtAddress(address);

        assertEquals(expectedMinors.size(), result.size());
    }

    @Test
    public void findPersonsByStationTest() {

        int station = 1;
        List<String> addresses = List.of("address1", "address2");

        when(fireStationRepoImpl.findAddressesByStation(station)).thenReturn(addresses);

        Person person1 = new Person("John", "Doe", "address1", "test city", "test zip", "test phone1", "test email");
        Person person2 = new Person("Jane", "Doe", "address2", "test city", "test zip", "test phone2", "test email");

        List<Person> persons = new ArrayList<>();
        persons.add(person1);
        persons.add(person2);

        List<String> allergiesJohnDoe = new ArrayList<>();
        allergiesJohnDoe.add("Capitalism");
        allergiesJohnDoe.add("Pop Music");
        List<String> medicationsJohnDoe = new ArrayList<>();
        medicationsJohnDoe.add("Reggae");
        medicationsJohnDoe.add("Women");

        List<String> allergiesJaneDoe = new ArrayList<>();
        allergiesJaneDoe.add("Capitalism");
        allergiesJaneDoe.add("Pop Music");
        List<String> medicationsJaneDoe = new ArrayList<>();
        medicationsJaneDoe.add("Reggae");
        medicationsJaneDoe.add("Women");

        MedicalRecord medicalRecordJohnDoe = new MedicalRecord("John", "Doe", "01/01/2000", medicationsJohnDoe, allergiesJohnDoe);
        MedicalRecord medicalRecordJaneDoe = new MedicalRecord("Jane", "Doe", "01/01/2020", medicationsJaneDoe, allergiesJaneDoe);

        List<MedicalRecord> medicalRecords = List.of(medicalRecordJohnDoe, medicalRecordJaneDoe);

        when(personRepoImpl.findPersonsByAddresses(addresses)).thenReturn(persons);

        when(medicalRecordRepoImpl.findByFirstNameAndLastName("John", "Doe")).thenReturn(medicalRecordJohnDoe);
        when(medicalRecordRepoImpl.findByFirstNameAndLastName("Jane", "Doe")).thenReturn(medicalRecordJaneDoe);

        PersonDTO personDTO1 = new PersonDTO(person1.getFirstName(), person1.getLastName(), person1.getAddress(), person1.getPhone());
        PersonDTO personDTO2 = new PersonDTO(person2.getFirstName(), person2.getLastName(), person2.getAddress(), person2.getPhone());

        List<PersonDTO> personDTOs = List.of(personDTO1, personDTO2);

        when(medicalRecordRepoImpl.countMajors(medicalRecords)).thenReturn(1);
        when(medicalRecordRepoImpl.countMinors(medicalRecords)).thenReturn(1);

        PersonWithCountDTO result = personService.findPersonsByStation(station);

        assertEquals(2, result.getPersonDTOs().size());
        assertEquals(1, result.getMinorCount());
        assertEquals(1, result.getMajorCount());
    }
}

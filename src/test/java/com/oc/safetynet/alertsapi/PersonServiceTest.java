package com.oc.safetynet.alertsapi;

import com.oc.safetynet.alertsapi.controller.FireStationController;
import com.oc.safetynet.alertsapi.controller.MedicalRecordController;
import com.oc.safetynet.alertsapi.controller.PersonController;
import com.oc.safetynet.alertsapi.model.MedicalRecord;
import com.oc.safetynet.alertsapi.model.Person;
import com.oc.safetynet.alertsapi.model.dto.ChildDTO;
import com.oc.safetynet.alertsapi.model.dto.FamilyMemberDTO;
import com.oc.safetynet.alertsapi.model.dto.HomeDTO;
import com.oc.safetynet.alertsapi.repository.FireStationRepository;
import com.oc.safetynet.alertsapi.repository.MedicalRecordRepository;
import com.oc.safetynet.alertsapi.repository.PersonRepository;
import com.oc.safetynet.alertsapi.service.FireStationService;
import com.oc.safetynet.alertsapi.service.MedicalRecordService;
import com.oc.safetynet.alertsapi.service.PersonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@WebMvcTest(PersonService.class)
public class PersonServiceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PersonService personService;

    @MockBean
    private PersonRepository personRepository;

    @MockBean
    private PersonController personController;

    @MockBean
    private MedicalRecordController medicalRecordController;

    @MockBean
    private MedicalRecordService medicalRecordService;

    @MockBean
    private FireStationController fireStationController;

    @MockBean
    private FireStationService fireStationService;

    @MockBean
    private FireStationRepository fireStationRepository;

    @MockBean
    private MedicalRecordRepository medicalRecordRepository;


    @Test
    public void testFindMinorsAtAddress () {

        LocalDate now = LocalDate.now();
        LocalDate eighteenYearsAgo = now.minusYears(18);

        String address = "test address";

        List<Person> personsAtAddress = List.of(
                new Person(1L, "John", "Doe", "address", "test city", "test zip", "test phone", "test email"),
                new Person(2L, "John", "Smith", "address", "test city", "test zip", "test phone", "test email")
        );

        when(personRepository.findByAddress(address)).thenReturn(personsAtAddress);

        List<ChildDTO> childDTOS = new ArrayList<>();

        LocalDate johnDoeDate = now.minusYears(40);
        LocalDate johnSmithDate = now.minusYears(8);

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

        List<MedicalRecord> medicalRecords = List.of(
                new MedicalRecord(1L, "John", "Doe", johnDoeDate, allergiesJohnDoe, medicationsJohnDoe),
                new MedicalRecord(2L, "John", "Smith", johnSmithDate, allergiesJohnSmith, medicationsJohnSmith)
        );

        for(Person person : personsAtAddress) {
            when(medicalRecordRepository.findByFirstNameAndLastNameAndBirthDateAfter(person.getFirstName(), person.getLastName(), eighteenYearsAgo)).thenReturn(medicalRecords);
            for (MedicalRecord medicalRecord : medicalRecords) {
                if(isSamePerson(medicalRecord, person)) {
                    int age = calculateAge(medicalRecord.getBirthdate());
                    ChildDTO childDTO = new ChildDTO();
                    childDTO.setFirstName(person.getFirstName());
                    childDTO.setLastName(person.getLastName());
                    childDTO.setAge(age);

                    List<Person> familyMembers = new ArrayList<>(personsAtAddress);
                    familyMembers.removeIf(p -> isSamePerson(medicalRecord, p));
                    childDTO.setFamilyMembers(familyMembers);
                    childDTOS.add(childDTO);
                }
            }
        }
        personService.findMinorsAtAddress(address);

        verify(medicalRecordRepository, times(2)).findByFirstNameAndLastNameAndBirthDateAfter(anyString(), anyString(), any());
        verify(personRepository, times(1)).findByAddress(address);
    }

    @Test
    public void testFindPhonesByStation() {

        int station = 1;

        List<String> addresses = List.of("address1, address2");
        List<String> phones = List.of("phone1", "phone2", "phone3", "phone4");

        when(fireStationRepository.findAddressesByStation(station)).thenReturn(addresses);
        when(personRepository.findPhoneByAddresses(addresses)).thenReturn(phones);

        List<String> results = personService.findPhonesByStation(station);

        assertNotNull(results);
        assertThat(results).hasSize(4);

        verify(fireStationRepository, times(1)).findAddressesByStation(station);
        verify(personRepository, times(1)).findPhoneByAddresses(addresses);

    }

    @Test
    public void testGetAllPersons() {

        List<Person> persons = List.of(
                new Person(1L, "John", "Doe", "address", "test city", "test zip", "test phone", "test email"),
                new Person(1L, "John", "Smith", "address", "test city", "test zip", "test phone", "test email")
        );

        when(personRepository.findAll()).thenReturn(persons);

        List<Person> result = personService.getAllPersons();

        assertNotNull(result);
        assertThat(result).hasSize(2);

        verify(personRepository, times(1)).findAll();

    }

    @Test
    public void testCalculateAge() {
        LocalDate now = LocalDate.now();
        LocalDate birthDate;
        birthDate = now.minusYears(20);
        int age = (int) ChronoUnit.YEARS.between(birthDate, now);
        int actualAge = personService.calculateAge(birthDate);
        assertEquals(actualAge, age);
    }

    @Test
    public void testAddPersonNoExistingPerson() {
        Person personToAdd = new Person();

        personToAdd.setFirstName("John");
        personToAdd.setLastName("Doe");
        personToAdd.setAddress("test address");
        personToAdd.setCity("test city");
        personToAdd.setZip("test zip");
        personToAdd.setPhone("test phone");
        personToAdd.setEmail("test email");
        when(personRepository.getByFirstNameAndLastName(anyString(), anyString())).thenReturn(null);

        personService.addPerson(personToAdd);

        verify(personRepository, times(1)).getByFirstNameAndLastName("John", "Doe");
        verify(personRepository, times(1)).save(personToAdd);
    }

    @Test
    public void testAddPersonExistingPerson() {
        Person personToAdd = new Person();

        personToAdd.setFirstName("John");
        personToAdd.setLastName("Doe");
        personToAdd.setAddress("test address");
        personToAdd.setCity("test city");
        personToAdd.setZip("test zip");
        personToAdd.setPhone("test phone");
        personToAdd.setEmail("test email");

        when(personRepository.getByFirstNameAndLastName(anyString(), anyString())).thenReturn(personToAdd);

        assertThrows(IllegalArgumentException.class, () -> {
            personService.addPerson(personToAdd);
        });

        verify(personRepository, times(1)).getByFirstNameAndLastName("John", "Doe");
        verify(personRepository, times(0)).save(personToAdd);
    }

    @Test
    public void testAddPersonSpecifiedID() {
        Person personToAdd = new Person();

        personToAdd.setId(1);
        personToAdd.setFirstName("John");
        personToAdd.setLastName("Doe");
        personToAdd.setAddress("test address");
        personToAdd.setCity("test city");
        personToAdd.setZip("test zip");
        personToAdd.setPhone("test phone");
        personToAdd.setEmail("test email");

        when(personRepository.getByFirstNameAndLastName(anyString(), anyString())).thenReturn(personToAdd);

        assertThrows(IllegalArgumentException.class, () -> {
            personService.addPerson(personToAdd);
        });

        verify(personRepository, times(1)).getByFirstNameAndLastName("John", "Doe");
        verify(personRepository, times(0)).save(personToAdd);
    }


    @Test
    public void testFindHomesByStation() {
        int station = 1;

        String address1 = "test address 1";
        String address2 = "test address 2";

        List<String> addresses = new ArrayList<>();
        addresses.add(address1);
        addresses.add(address2);

        when(fireStationRepository.findAddressesByStation(station)).thenReturn(addresses);

        List<Person> personsAtAddress1 = new ArrayList<>();
        List<Person> personsAtAddress2 = new ArrayList<>();


        Person johnDoe = new Person(1L, "John", "Doe", address1, "test city", "test zip", "test phone", "test email");
        Person bobDoe = new Person(2L, "Bob", "Doe", address1, "test city", "test zip", "test phone", "test email");

        personsAtAddress1.add(johnDoe);
        personsAtAddress1.add(bobDoe);


        Person johnSmith = new Person(3L, "John", "Smith", address2, "test city", "test zip", "test phone", "test email");
        Person bobSmith = new Person(4L, "Bob", "Smith", address2, "test city", "test zip", "test phone", "test email");

        personsAtAddress2.add(johnSmith);
        personsAtAddress2.add(bobSmith);


        when(personRepository.findByAddress(address1)).thenReturn(personsAtAddress1);
        when(personRepository.findByAddress(address2)).thenReturn(personsAtAddress2);


        System.out.println("Persons at address 1: " + personsAtAddress1);
        System.out.println("Persons at address 2: " + personsAtAddress2);

        LocalDate now = LocalDate.now();
        LocalDate johnBirthDate1 = now.minusYears(50);
        LocalDate johnBirthDate2 = now.minusYears(50);
        LocalDate bobBirthdate1 = now.minusYears(5);
        LocalDate bobBirthdate2 = now.minusYears(5);

        List<String> allergiesJohn1 = Arrays.asList("allergy1", "allergy2");
        List<String> medicationsJohn1 = Arrays.asList("medication1", "medication2");
        List<String> allergiesJohn2 = Arrays.asList("allergy1", "allergy2");
        List<String> medicationsJohn2 = Arrays.asList("medication1", "medication2");
        List<String> allergiesBob1 = Arrays.asList("allergy1", "allergy2");
        List<String> medicationsBob1 = Arrays.asList("medication1", "medication2");
        List<String> allergiesBob2 = Arrays.asList("allergy1", "allergy2");
        List<String> medicationsBob2 = Arrays.asList("medication1", "medication2");

        when(medicalRecordRepository.findByFirstNameAndLastName("John", "Doe")).thenReturn(new MedicalRecord(1L, "John", "Doe", johnBirthDate1, allergiesJohn1, medicationsJohn1));
        when(medicalRecordRepository.findByFirstNameAndLastName("Bob", "Doe")).thenReturn(new MedicalRecord(2L, "Bob", "Doe", bobBirthdate1, allergiesBob1, medicationsBob1));
        when(medicalRecordRepository.findByFirstNameAndLastName("John", "Smith")).thenReturn(new MedicalRecord(3L, "John", "Smith", johnBirthDate2, allergiesJohn2, medicationsJohn2));
        when(medicalRecordRepository.findByFirstNameAndLastName("Bob", "Smith")).thenReturn(new MedicalRecord(4L, "Bob", "Smith", bobBirthdate2, allergiesBob2, medicationsBob2));

        int johnSmithAge = calculateAge(johnBirthDate1);
        int bobSmithAge = calculateAge(bobBirthdate1);
        int johnDoeAge = calculateAge(johnBirthDate2);
        int bobDoeAge = calculateAge(bobBirthdate2);

        FamilyMemberDTO johnDoeDTO = new FamilyMemberDTO("John", "Doe", "test phone", johnDoeAge, allergiesJohn1, medicationsJohn1);
        FamilyMemberDTO bobDoeDTO = new FamilyMemberDTO("Bob", "Doe", "test phone", bobDoeAge, allergiesBob1, medicationsBob1);

        List<FamilyMemberDTO> doeMemberDTOS = new ArrayList<>();
        doeMemberDTOS.add(johnDoeDTO);
        doeMemberDTOS.add(bobDoeDTO);

        FamilyMemberDTO johnSmithDTO = new FamilyMemberDTO("John", "Smith", "test phone", johnSmithAge, allergiesJohn2, medicationsJohn2);
        FamilyMemberDTO bobSmithDTO = new FamilyMemberDTO("Bob", "Smith", "test phone", bobSmithAge, allergiesBob2, medicationsBob2);

        List<FamilyMemberDTO> smithMemberDTOS = new ArrayList<>();
        smithMemberDTOS.add(johnSmithDTO);
        smithMemberDTOS.add(bobSmithDTO);

        HomeDTO doeHomeDTO = new HomeDTO();
        doeHomeDTO.setAddress(address1);
        doeHomeDTO.setFamilyMembers(doeMemberDTOS);

        HomeDTO smithHomeDTO = new HomeDTO();
        smithHomeDTO.setAddress(address2);
        smithHomeDTO.setFamilyMembers(smithMemberDTOS);

        List<HomeDTO> expectedHomeDTOs = new ArrayList<>();
        expectedHomeDTOs.add(doeHomeDTO);
        expectedHomeDTOs.add(smithHomeDTO);

        List<HomeDTO> result = personService.findHomesByStation(station);

        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
        verify(medicalRecordRepository, times(4)).findByFirstNameAndLastName(anyString(), anyString());
        verify(personRepository, times(2)).findByAddress(anyString());
    }

    private int calculateAge(LocalDate birthdate) {
        return Period.between(birthdate, LocalDate.now()).getYears();
    }

    private boolean isSamePerson(MedicalRecord medicalRecord, Person person) {
        return medicalRecord.getFirstName().equals(person.getFirstName()) && medicalRecord.getLastName().equals(person.getLastName());
    }

}

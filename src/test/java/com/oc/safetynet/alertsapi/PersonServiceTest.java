package com.oc.safetynet.alertsapi;

import com.oc.safetynet.alertsapi.controller.FireStationController;
import com.oc.safetynet.alertsapi.controller.MedicalRecordController;
import com.oc.safetynet.alertsapi.controller.PersonController;
import com.oc.safetynet.alertsapi.model.MedicalRecord;
import com.oc.safetynet.alertsapi.model.Person;
import com.oc.safetynet.alertsapi.model.dto.*;
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
    public void testGetPersonByFirstNameAndLastName() {

        Person person = new Person(1L, "John", "Doe", "address", "test city", "test zip", "test phone", "test email");

        String firstName = "John";
        String lastName = "Doe";

        when(personRepository.getByFirstNameAndLastName(firstName, lastName)).thenReturn(person);

        Person foundPerson = personService.getPersonByFirstNameAndLastName(firstName, lastName);

        verify(personRepository, times(1)).getByFirstNameAndLastName(firstName, lastName);
        assertNotNull(foundPerson);
        assertEquals("address", foundPerson.getAddress());
        assertEquals("test phone", foundPerson.getPhone());
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
    public void updatePersonTestExistingPerson() {

        Person personToUpdate = new Person(1L, "John", "Doe", "address", "test city", "test zip", "test phone", "test email");

        when(personRepository.findByFirstNameAndLastName(anyString(), anyString())).thenReturn(personToUpdate);

        personToUpdate.setAddress("updated address");
        personToUpdate.setCity("updated city");
        personToUpdate.setZip("updated zip");
        personToUpdate.setPhone("updated phone");
        personToUpdate.setEmail("updated email");

        personService.updatePerson(personToUpdate);

        verify(personRepository, times(1)).findByFirstNameAndLastName(anyString(), anyString());
        verify(personRepository, times(1)).save(personToUpdate);
    }

    @Test
    public void updatePersonTestNoExistingPerson() {

        Person personToUpdate = new Person(1L, "John", "Doe", "address", "test city", "test zip", "test phone", "test email");

        when(personRepository.findByFirstNameAndLastName(anyString(), anyString())).thenReturn(null);

        personToUpdate.setAddress("updated address");
        personToUpdate.setCity("updated city");
        personToUpdate.setZip("updated zip");
        personToUpdate.setPhone("updated phone");
        personToUpdate.setEmail("updated email");

        assertThrows(NullPointerException.class, () -> {
            personService.updatePerson(personToUpdate);
        });

        verify(personRepository, times(1)).findByFirstNameAndLastName(anyString(), anyString());
        verify(personRepository, times(0)).save(personToUpdate);
    }

    @Test
    public void deleteExistingPersonTest() {

        Person personToDelete = new Person(1L, "John", "Doe", "address", "test city", "test zip", "test phone", "test email");
        when(personRepository.getByFirstNameAndLastName(anyString(), anyString())).thenReturn(personToDelete);
        personService.deletePerson(personToDelete);

        verify(personRepository, times(1)).getByFirstNameAndLastName(anyString(), anyString());
        verify(personRepository, times(1)).deleteByFirstNameAndLastName(anyString(),anyString());

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

    @Test
    public void findPersonInfoListDTOTest() {

        LocalDate now = LocalDate.now();

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
                new MedicalRecord(1L, "Bob", "Ross", johnDoeDate, medicationsJohnDoe, allergiesJohnDoe),
                new MedicalRecord(2L, "Bob", "Ross", johnSmithDate, medicationsJohnSmith, allergiesJohnSmith)
        );

        String address1 = "test address 1";
        String address2 = "test address 2";

        List<Person> persons = new ArrayList<>();

        Person bobRoss1 = new Person(1L, "Bob", "Ross", address1, "test city", "test zip", "test phone", "test email");
        Person bobRoss2 = new Person(2L, "Bob", "Ross", address2, "test city", "test zip", "test phone", "test email");

        persons.add(bobRoss1);
        persons.add(bobRoss2);

        when(medicalRecordRepository.findAllByFirstNameAndLastName("Bob", "Ross")).thenReturn(medicalRecords);
        when(personRepository.findPersonByFirstNameAndLastName("Bob", "Ross")).thenReturn(persons);

        List<PersonInfoDTO> personInfoDTOS = personService.findPersonInfoListDTO("Bob", "Ross");

        assertNotNull(personInfoDTOS);
        assertEquals(2, personInfoDTOS.size());

        PersonInfoDTO personInfoDTO1 = personInfoDTOS.get(0);
        assertEquals("Bob", personInfoDTO1.getFirstName());
        assertEquals("Ross", personInfoDTO1.getLastName());
        assertEquals(address1, personInfoDTO1.getAddress());
        assertEquals("test email", personInfoDTO1.getEmail());
        assertEquals(40, personInfoDTO1.getAge());
        assertEquals(allergiesJohnDoe, personInfoDTO1.getAllergies());
        assertEquals(medicationsJohnDoe, personInfoDTO1.getMedications());

        PersonInfoDTO personInfoDTO2 = personInfoDTOS.get(1);
        assertEquals("Bob", personInfoDTO2.getFirstName());
        assertEquals("Ross", personInfoDTO2.getLastName());
        assertEquals(address2, personInfoDTO2.getAddress());
        assertEquals("test email", personInfoDTO2.getEmail());
        assertEquals(8, personInfoDTO2.getAge());
        assertEquals(allergiesJohnSmith, personInfoDTO2.getAllergies());
        assertEquals(medicationsJohnSmith, personInfoDTO2.getMedications());

        verify(medicalRecordRepository, times(1)).findAllByFirstNameAndLastName("Bob", "Ross");
        verify(personRepository, times(1)).findPersonByFirstNameAndLastName("Bob", "Ross");
    }

    @Test
    public void testPersonFireWithStationNumberDTO() {

        // A RETRAVAILLER

        String address1 = "test address 1";

        List<Person> personsAtAddress1 = new ArrayList<>();

        Person johnDoe = new Person(1L, "John", "Doe", address1, "test city", "test zip", "test phone", "test email");
        Person bobDoe = new Person(2L, "Bob", "Doe", address1, "test city", "test zip", "test phone", "test email");
        personsAtAddress1.add(johnDoe);
        personsAtAddress1.add(bobDoe);


        LocalDate now = LocalDate.now();

        LocalDate johnDoeDate = now.minusYears(40);
        LocalDate bobDoeDate = now.minusYears(8);
        LocalDate johnSmithDate = now.minusYears(40);
        LocalDate bobSmithDate = now.minusYears(8);

        List<String> allergiesJohnDoe = new ArrayList<>();
        allergiesJohnDoe.add("Capitalism");
        allergiesJohnDoe.add("Pop Music");
        List<String> medicationsJohnDoe = new ArrayList<>();
        medicationsJohnDoe.add("Reggae");
        medicationsJohnDoe.add("Women");

        List<String> allergiesBobDoe = new ArrayList<>();
        allergiesBobDoe.add("Shellfish");
        allergiesBobDoe.add("Gluten");
        List<String> medicationsBobDoe = new ArrayList<>();
        medicationsBobDoe.add("Prozac 30mg");
        medicationsBobDoe.add("Viagra 10mg");

        List<String> allergiesJohnSmith = new ArrayList<>();
        allergiesJohnSmith.add("Capitalism");
        allergiesJohnSmith.add("Pop Music");
        List<String> medicationsJohnSmith = new ArrayList<>();
        medicationsJohnSmith.add("Reggae");
        medicationsJohnSmith.add("Women");

        List<String> allergiesBobSmith = new ArrayList<>();
        allergiesBobSmith.add("Shellfish");
        allergiesBobSmith.add("Gluten");
        List<String> medicationsBobSmith = new ArrayList<>();
        medicationsBobSmith.add("Prozac 30mg");
        medicationsBobSmith.add("Viagra 10mg");

        List<MedicalRecord> medicalRecords = new ArrayList<>();

        MedicalRecord medicalRecordJohnDoe = new MedicalRecord(1L, "John", "Doe", johnDoeDate, medicationsJohnDoe, allergiesJohnDoe);
        MedicalRecord medicalRecordBobDoe =  new MedicalRecord(2L, "Bob", "Doe", bobDoeDate, medicationsBobDoe, allergiesBobDoe);
        MedicalRecord medicalRecordJohnSmith = new MedicalRecord(3L, "John", "Smith", johnSmithDate, medicationsJohnSmith, allergiesJohnSmith);
        MedicalRecord medicalRecordBobSmith =  new MedicalRecord(4L, "Bob", "Smith", bobSmithDate, medicationsBobSmith, allergiesBobSmith);

        medicalRecords.add(medicalRecordJohnDoe);
        medicalRecords.add(medicalRecordBobDoe);
        medicalRecords.add(medicalRecordJohnSmith);
        medicalRecords.add(medicalRecordBobSmith);

        when(personRepository.findByAddress(address1)).thenReturn(personsAtAddress1);
        for (MedicalRecord medicalRecord : medicalRecords) {
            when(medicalRecordRepository.findByFirstNameAndLastName(anyString(), anyString())).thenReturn(medicalRecord);
        }

        PersonFireWithStationNumberDTO result = personService.findPersonsAtAddress(address1);

        assertNotNull(result);
        verify(personRepository, times(1)).findByAddress(address1);
        verify(medicalRecordRepository, times(2)).findByFirstNameAndLastName(anyString(), anyString());

    }
    @Test
    public void findPersonsByStationTest() {

        int station = 1;

        String address1 = "test address 1";
        String address2 = "test address 2";

        List<String> addresses = List.of(
                address1, address2
        );


        List<Person> personsAtAddresses = new ArrayList<>();


        Person johnDoe = new Person(1L, "John", "Doe", address1, "test city", "test zip", "test phone", "test email");
        Person bobDoe = new Person(2L, "Bob", "Doe", address1, "test city", "test zip", "test phone", "test email");
        personsAtAddresses.add(johnDoe);
        personsAtAddresses.add(bobDoe);

        Person johnSmith = new Person(3L, "John", "Smith", address2, "test city", "test zip", "test phone", "test email");
        Person bobSmith = new Person(4L, "Bob", "Smith", address2, "test city", "test zip", "test phone", "test email");
        personsAtAddresses.add(johnSmith);
        personsAtAddresses.add(bobSmith);

        LocalDate now = LocalDate.now();

        LocalDate johnDoeDate = now.minusYears(40);
        LocalDate bobDoeDate = now.minusYears(8);
        LocalDate johnSmithDate = now.minusYears(40);
        LocalDate bobSmithDate = now.minusYears(8);

        List<String> allergiesJohnDoe = new ArrayList<>();
        allergiesJohnDoe.add("Capitalism");
        allergiesJohnDoe.add("Pop Music");
        List<String> medicationsJohnDoe = new ArrayList<>();
        medicationsJohnDoe.add("Reggae");
        medicationsJohnDoe.add("Women");

        List<String> allergiesBobDoe = new ArrayList<>();
        allergiesBobDoe.add("Shellfish");
        allergiesBobDoe.add("Gluten");
        List<String> medicationsBobDoe = new ArrayList<>();
        medicationsBobDoe.add("Prozac 30mg");
        medicationsBobDoe.add("Viagra 10mg");

        List<String> allergiesJohnSmith = new ArrayList<>();
        allergiesJohnSmith.add("Capitalism");
        allergiesJohnSmith.add("Pop Music");
        List<String> medicationsJohnSmith = new ArrayList<>();
        medicationsJohnSmith.add("Reggae");
        medicationsJohnSmith.add("Women");

        List<String> allergiesBobSmith = new ArrayList<>();
        allergiesBobSmith.add("Shellfish");
        allergiesBobSmith.add("Gluten");
        List<String> medicationsBobSmith = new ArrayList<>();
        medicationsBobSmith.add("Prozac 30mg");
        medicationsBobSmith.add("Viagra 10mg");

        List<MedicalRecord> medicalRecords = new ArrayList<>();

        MedicalRecord medicalRecordJohnDoe = new MedicalRecord(1L, "John", "Doe", johnDoeDate, medicationsJohnDoe, allergiesJohnDoe);
        MedicalRecord medicalRecordBobDoe =  new MedicalRecord(2L, "Bob", "Doe", bobDoeDate, medicationsBobDoe, allergiesBobDoe);
        MedicalRecord medicalRecordJohnSmith = new MedicalRecord(3L, "John", "Smith", johnSmithDate, medicationsJohnSmith, allergiesJohnSmith);
        MedicalRecord medicalRecordBobSmith =  new MedicalRecord(4L, "Bob", "Smith", bobSmithDate, medicationsBobSmith, allergiesBobSmith);

        medicalRecords.add(medicalRecordJohnDoe);
        medicalRecords.add(medicalRecordBobDoe);
        medicalRecords.add(medicalRecordJohnSmith);
        medicalRecords.add(medicalRecordBobSmith);

        when(fireStationRepository.findAddressesByStation(station)).thenReturn(addresses);
        when(personRepository.findByAddresses(addresses)).thenReturn(personsAtAddresses);

        List<PersonDTO> personDTOs = new ArrayList<>();

        for (int i = 0; i < personsAtAddresses.size(); i++) {
            Person person = personsAtAddresses.get(i);
            when(medicalRecordRepository.findByFirstNameAndLastName(person.getFirstName(), person.getLastName()))
                    .thenReturn(medicalRecords.get(i));
            PersonDTO personDTO = new PersonDTO(person.getFirstName(), person.getLastName(), person.getAddress(), person.getPhone());
            personDTOs.add(personDTO);
        }

        int majorCount = 2;
        int minorCount = 2;

        when(medicalRecordService.countMajorsForMedicalRecords(medicalRecords)).thenReturn(majorCount);
        when(medicalRecordService.countMinorsForMedicalRecords(medicalRecords)).thenReturn(minorCount);

        PersonWithCountDTO personWithCountDTO = new PersonWithCountDTO();
        personWithCountDTO.setPersonDTOs(personDTOs);
        personWithCountDTO.setMinorCount(minorCount);
        personWithCountDTO.setMajorCount(majorCount);

        PersonWithCountDTO result = personService.findPersonsByStation(station);

        verify(fireStationRepository, times(1)).findAddressesByStation(station);
        verify(personRepository, times(1)).findByAddresses(addresses);
        verify(medicalRecordRepository, times(4)).findByFirstNameAndLastName(anyString(), anyString());

        assertEquals(result.getMajorCount(), 2);
        assertEquals(result.getMinorCount(), 2);
        assertNotNull(result.getPersonDTOs());
        assertEquals(result.getPersonDTOs().size(), 4);

    }

    @Test
    public void testFindEmailsByCity() {

        List<String> emails = List.of("email1", "email2", "email3");
        String city = "Biot";

        when(personRepository.findEmailsByCity(anyString())).thenReturn(emails);

        List<String> resultEmailList = personService.findEmailsByCity(city);

        assertNotNull(resultEmailList);
        assertEquals(3, resultEmailList.size());
        assertThat(resultEmailList).containsExactlyInAnyOrder("email1", "email2", "email3");
    }


    private int calculateAge(LocalDate birthdate) {
        return Period.between(birthdate, LocalDate.now()).getYears();
    }

    private boolean isSamePerson(MedicalRecord medicalRecord, Person person) {
        return medicalRecord.getFirstName().equals(person.getFirstName()) && medicalRecord.getLastName().equals(person.getLastName());
    }

}

package com.oc.safetynet.alertsapi.service;

import com.oc.safetynet.alertsapi.model.FireStation;
import com.oc.safetynet.alertsapi.model.MedicalRecord;
import com.oc.safetynet.alertsapi.model.Person;
import com.oc.safetynet.alertsapi.model.dto.*;
import com.oc.safetynet.alertsapi.repository.FireStationRepository;
import com.oc.safetynet.alertsapi.repository.MedicalRecordRepository;
import com.oc.safetynet.alertsapi.repository.PersonRepository;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;


@Service
@Data
public class PersonService {

    private static final Logger logger = LoggerFactory.getLogger(PersonService.class);

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private FireStationRepository fireStationRepository;

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    public List<Person> getAllPersons() {
        return personRepository.findAll();
    }

    public Person savePerson(Person person) {
        return personRepository.save(person);
    }

    public List<Person> saveAllPersons(List<Person> persons) {
        return personRepository.saveAll(persons);
    }

    public List<Person> getPersonsByStation(int station) {
        List<FireStation> fireStations = fireStationRepository.findByStation(station);
        List<Person> persons = new ArrayList<>();
        if (fireStations != null) {
            for (FireStation fireStation : fireStations) {
                persons.addAll(personRepository.findAll().stream()
                        .filter(person -> person.getAddress().equals(fireStation.getAddress())).toList());
            }
        }
        logger.info("Content of Persons : {}", persons);
        return persons;
    }

    public List<String> findPhonesByStation(int station) {
        List<String> addresses = fireStationRepository.findAddressesByStation(station);
        return addresses.stream()
                .map(address -> personRepository.findPhoneByAddress(address))
                .flatMap(List::stream)
                .distinct()
                .toList();
    }

    public PersonFireWithStationNumberDTO findPersonsAtAddress(String address) {
        List<MedicalRecord> medicalRecords = medicalRecordRepository.findAll(); // Try to target better
        List<Person> persons = medicalRecords.stream()
                .flatMap(medicalRecord -> findMatchingPersons(medicalRecord, address).stream())
                .distinct()
                .toList();

        logger.info("Persons: {}", persons);

        List<PersonFireDTO> personsFireDTO = persons.stream()
                .map(person -> {
                    PersonFireDTO personFireDTO = new PersonFireDTO();
                    personFireDTO.setFirstName(person.getFirstName());
                    personFireDTO.setLastName(person.getLastName());
                    personFireDTO.setPhone(person.getPhone());

                    MedicalRecord matchingMedicalRecord = findMatchingMedicalRecord(person, medicalRecords);
                    logger.info("Matching medical record: {}", matchingMedicalRecord);


                    int age = calculateAge(matchingMedicalRecord.getBirthdate());
                    personFireDTO.setAge(age);

                    List<String> allergies = matchingMedicalRecord.getAllergies();
                    List<String> medications = matchingMedicalRecord.getMedications();
                    personFireDTO.setAllergies(allergies);
                    personFireDTO.setMedications(medications);

                    return personFireDTO;

                })
                .toList();

        PersonFireWithStationNumberDTO personFireWithStationNumberDTO = new PersonFireWithStationNumberDTO();
        personFireWithStationNumberDTO.setPersonFireDTOs(personsFireDTO);

        int station = fireStationRepository.findStationNumberByAddress(address);
        personFireWithStationNumberDTO.setStation(station);

        return personFireWithStationNumberDTO;
    }

    public List<ChildDTO> findMinorsAtAddress(String address) {
        LocalDate now = LocalDate.now();
        LocalDate eighteenYearsAgo = now.minusYears(18);

        List<MedicalRecord> minorsRecord = medicalRecordRepository.findByBirthdateAfter(eighteenYearsAgo);
        List<Person> minorsAtAddress = minorsRecord.stream()
                .flatMap(medicalRecord -> findMatchingPersons(medicalRecord, address).stream())
                .distinct().toList();

        List<Person> familyMembers = personRepository.findByAddress(address);
        familyMembers.removeIf(minorsAtAddress::contains);

        List<ChildDTO> minorsAtAddressDTO = minorsAtAddress.stream()
                .map(person -> {
                    ChildDTO childDTO = new ChildDTO();
                    childDTO.setFirstName(person.getFirstName());
                    childDTO.setLastName(person.getLastName());
                    childDTO.setFamilyMembers(familyMembers);

                    MedicalRecord matchingMedicalRecord = findMatchingMedicalRecord(person, minorsRecord);
                    int age = calculateAge(matchingMedicalRecord.getBirthdate());
                    childDTO.setAge(age);

                    return childDTO;
                })
                .toList();

        return minorsAtAddressDTO;
    }

    public PersonWithCountDTO findPersonsByStation(int station) {
        List<String> addresses = fireStationRepository.findAddressesByStation(station);

        List<PersonDTO> personDTOs = addresses.stream()
                .flatMap(address -> personRepository.findByAddress(address).stream())
                .map(person -> {
                    PersonDTO personDTO = new PersonDTO();
                    personDTO.setFirstName(person.getFirstName());
                    personDTO.setLastName(person.getLastName());
                    personDTO.setPhone(person.getPhone());
                    personDTO.setAddress(person.getAddress());
                    return personDTO;
                })
                .toList();

        List<LocalDate> birthDates = addresses.stream()
                .flatMap(address -> personRepository.findByAddress(address).stream())
                .flatMap(person -> medicalRecordRepository
                        .findBirthdateByFirstNameAndLastName(person.getFirstName(), person.getLastName()).stream())
                .toList();

        LocalDate now = LocalDate.now();

        long minorCount = birthDates.stream()
                .filter(birthDate -> Period.between(birthDate, now).getYears() < 18)
                .count();

        long majorCount = birthDates.size() - minorCount;

        PersonWithCountDTO personWithCountDTO = new PersonWithCountDTO();
        personWithCountDTO.setPersonDTOs(personDTOs);
        personWithCountDTO.setMinorCount((int) minorCount);
        personWithCountDTO.setMajorCount((int) majorCount);

        return personWithCountDTO;
    }

    public List <LocalDate> findBirthDates (String firstName, String lastName) {
        return medicalRecordRepository.findBirthdateByFirstNameAndLastName(firstName, lastName);
    }

    private boolean isSamePerson(MedicalRecord medicalRecord, Person person) {
        return medicalRecord.getFirstName().equals(person.getFirstName()) && medicalRecord.getLastName().equals(person.getLastName());
    }

    private List<Person> findMatchingPersons(MedicalRecord medicalRecord, String address) {
        return personRepository.findAll().stream()
                .filter(person -> isSamePerson(medicalRecord, person) && person.getAddress().equals(address))
                .collect(Collectors.toList());
    }

    private MedicalRecord findMatchingMedicalRecord(Person person, List<MedicalRecord> medicalRecords) {
        return medicalRecords.stream()
                .filter(medicalRecord -> isSamePerson(medicalRecord, person))
                .findFirst()
                .orElseThrow();
    }

    private int calculateAge(LocalDate birthdate) {
        LocalDate now = LocalDate.now();
        return (int) ChronoUnit.YEARS.between(birthdate, now);
    }

}
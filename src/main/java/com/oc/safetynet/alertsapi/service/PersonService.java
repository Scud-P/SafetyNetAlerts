package com.oc.safetynet.alertsapi.service;

import com.oc.safetynet.alertsapi.exception.PersonNotFoundException;
import com.oc.safetynet.alertsapi.model.Data;
import com.oc.safetynet.alertsapi.model.MedicalRecord;
import com.oc.safetynet.alertsapi.model.Person;
import com.oc.safetynet.alertsapi.model.dto.*;
import com.oc.safetynet.alertsapi.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class PersonService {

    private static final Logger logger = LoggerFactory.getLogger(PersonService.class);

    private static Data data;

    private PersonRepository personRepository;

    private FireStationRepository fireStationRepository;

    private MedicalRecordRepository medicalRecordRepository;

    private MedicalRecordService medicalRecordService;

    @Autowired
    private PersonRepoImpl personRepoImpl;

    @Autowired
    private MedicalRecordRepoImpl medicalRecordRepoImpl;

    @Autowired
    private FireStationRepoImpl fireStationRepoImpl;

    public List<Person> getAllPersons() {
        return data.getPersons();
    }
    public List<Person> saveAllPersons(List<Person> persons) {
        logger.debug("List of persons saved: {}", persons);
        return personRepository.saveAll(persons);
    }

    public List<String> findPhonesByStation(int station) {
        List<String> addresses = fireStationRepository.findAddressesByStation(station);
        logger.debug("List of addresses corresponding to FireStation number {}: {}", station, addresses);
        List<String> phones = personRepository.findPhoneByAddresses(addresses);
        logger.info("List of phone numbers found for addresses {} covered by station {}: {}", addresses, station,phones);
        return personRepository.findPhoneByAddresses(addresses);
    }

//    public List<PersonInfoDTO> findPersonInfoListDTO(String firstName, String lastName) {
//        List<MedicalRecord> medicalRecords = medicalRecordRepository.findAllByFirstNameAndLastName(firstName, lastName);
//        logger.debug("Medical Record(s) found: {}", medicalRecords);
//        List<Person> persons = personRepository.findPersonByFirstNameAndLastName(firstName, lastName);
//        logger.debug("Person(s) found: {}", persons);
//
//        List<PersonInfoDTO> personInfoDTOS = new ArrayList<>();
//
//        for(int i = 0; i < medicalRecords.size() && i < persons.size(); i++) {
//            MedicalRecord medicalRecord = medicalRecords.get(i);
//            Person person = persons.get(i);
//
//            PersonInfoDTO personInfoDTO = new PersonInfoDTO();
//
//            personInfoDTO.setFirstName(person.getFirstName());
//            personInfoDTO.setLastName(person.getLastName());
//            personInfoDTO.setAddress(person.getAddress());
//            personInfoDTO.setEmail(person.getEmail());
//
//            int age = calculateAge(medicalRecord.getBirthdate());
//            personInfoDTO.setAge(age);
//            personInfoDTO.setMedications(medicalRecord.getMedications());
//            personInfoDTO.setAllergies(medicalRecord.getAllergies());
//
//            personInfoDTOS.add(personInfoDTO);
//        }
//        logger.info("Information found for person(s): {}", personInfoDTOS.toString());
//        return personInfoDTOS;
//    }

    public List<PersonInfoDTO> findPersonInfoListDTO(String firstName, String lastName) {

        List<Person> persons = personRepoImpl.findAllByFirstNameAndLastName(firstName, lastName);
        List<MedicalRecord> medicalRecords = medicalRecordRepoImpl.findAllByFirstNameAndLastName(firstName, lastName);

        return persons.stream()
                .flatMap(person -> medicalRecords.stream()
                        .filter(medicalRecord ->
                                person.getFirstName().equals(medicalRecord.getFirstName()) &&
                                        person.getLastName().equals(medicalRecord.getLastName()))
                                        .map(medicalRecord -> new PersonInfoDTO(person, medicalRecord)))
                        .toList();
    }


    public List<HomeDTO> findHomesByAddresses(int station) {
        List<String> addresses = fireStationRepoImpl.findAddressesByStation(station);
        return addresses.stream()
                .map(address -> {
                    List<Person> persons = personRepoImpl.findPersonsByAddresses(Collections.singletonList(address));
                    List<FamilyMemberDTO> familyMemberDTOS = persons.stream()
                            .map(person -> {
                                MedicalRecord medicalRecord = medicalRecordRepoImpl.findByFirstNameAndLastName(person.getFirstName(), person.getLastName());
                                return new FamilyMemberDTO(person, medicalRecord);
                            })
                            .toList();
                    return new HomeDTO(address, familyMemberDTOS);
                })
                .toList();
    }





//    public PersonFireWithStationNumberDTO findPersonsAtAddress(String address) {
//
//        List<Person> persons = personRepository.findByAddress(address);
//        logger.debug("Persons at address {}: {}", address, persons);
//        List<PersonFireDTO> personFireDTOS = new ArrayList<>();
//
//        for(Person person : persons) {
//            MedicalRecord medicalRecord = medicalRecordRepository.findByFirstNameAndLastName(person.getFirstName(), person.getLastName());
//            PersonFireDTO personFireDTO = new PersonFireDTO();
//            personFireDTO.setFirstName(person.getFirstName());
//            personFireDTO.setLastName(person.getLastName());
//            personFireDTO.setPhone(person.getPhone());
//            personFireDTO.setAllergies(medicalRecord.getAllergies());
//            personFireDTO.setMedications(medicalRecord.getMedications());
//            int age = calculateAge(medicalRecord.getBirthdate());
//            personFireDTO.setAge(age);
//            personFireDTOS.add(personFireDTO);
//
//        }
//        PersonFireWithStationNumberDTO personFireWithStationNumberDTO = new PersonFireWithStationNumberDTO();
//
//        int station = fireStationRepository.findStationNumberByAddress(address);
//        personFireWithStationNumberDTO.setPersonFireDTOs(personFireDTOS);
//        personFireWithStationNumberDTO.setStation(station);
//
//        logger.info("Persons at address {}: {}", address, personFireWithStationNumberDTO.toString());
//        return personFireWithStationNumberDTO;
//}

//    public List<ChildDTO> findMinorsAtAddress(String address) {
//        LocalDate now = LocalDate.now();
//        LocalDate eighteenYearsAgo = now.minusYears(18);
//
//        List<Person> personsAtAddress = personRepository.findByAddress(address);
//        logger.debug("Persons found at address {}: {}", address, personsAtAddress);
//        List<ChildDTO> childDTOS = new ArrayList<>();
//
//
//        for (Person personAtAddress : personsAtAddress) {
//            List<MedicalRecord> medicalRecords = medicalRecordRepository.findByFirstNameAndLastNameAndBirthDateAfter(
//                    personAtAddress.getFirstName(),
//                    personAtAddress.getLastName(),
//                    eighteenYearsAgo);
//
//            for (MedicalRecord medicalRecord : medicalRecords) {
//                if (isSamePerson(medicalRecord, personAtAddress)) {
//                    int age = calculateAge(medicalRecord.getBirthdate());
//                    ChildDTO childDTO = new ChildDTO();
//                    childDTO.setFirstName(personAtAddress.getFirstName());
//                    childDTO.setLastName(personAtAddress.getLastName());
//                    childDTO.setAge(age);
//
//                    List<Person> familyMembers = new ArrayList<>(personsAtAddress);
//                    familyMembers.removeIf(p -> isSamePerson(medicalRecord, p));
//                    childDTO.setFamilyMembers(familyMembers);
//                    childDTOS.add(childDTO);
//                }
//            }
//        }
//        logger.info("Minors found at address {}: {}", address, childDTOS.toString());
//        return childDTOS;
//    }


    public PersonWithCountDTO findPersonsByStation(int station) {

        List<String> addresses = fireStationRepository.findAddressesByStation(station);
        logger.debug("Addresses covered by station number {}: {}", station, addresses);
        List<Person> persons = personRepository.findByAddresses(addresses);
        logger.debug("Persons found for addresses {}: {}", addresses, persons);
        List<MedicalRecord> medicalRecords = new ArrayList<>();
        List<PersonDTO> personDTOs = new ArrayList<>();


        for (Person person : persons) {
            MedicalRecord medicalRecord = medicalRecordRepository.findByFirstNameAndLastName(person.getFirstName(), person.getLastName());
            medicalRecords.add(medicalRecord);
            PersonDTO personDTO = new PersonDTO(person.getFirstName(), person.getLastName(), person.getAddress(), person.getPhone());
            personDTOs.add(personDTO);
        }

        int majorCount = medicalRecordService.countMajorsForMedicalRecords(medicalRecords);
        logger.debug("{} majors have been found", majorCount);
        int minorCount = medicalRecordService.countMinorsForMedicalRecords(medicalRecords);
        logger.debug("{} minors have been found", minorCount);


        PersonWithCountDTO personWithCountDTO = new PersonWithCountDTO();
        personWithCountDTO.setPersonDTOs(personDTOs);
        personWithCountDTO.setMinorCount(minorCount);
        personWithCountDTO.setMajorCount(majorCount);

        logger.info("Persons covered by station number {}: {}", station, personWithCountDTO.toString());
        return personWithCountDTO;
    }


    private boolean isSamePerson(MedicalRecord medicalRecord, Person person) {
        return medicalRecord.getFirstName().equals(person.getFirstName()) && medicalRecord.getLastName().equals(person.getLastName());
    }

    public int calculateAge(LocalDate birthdate) {
        LocalDate now = LocalDate.now();
        return (int) ChronoUnit.YEARS.between(birthdate, now);
    }

    public List<String> findEmailsByCity(String city) {
        List<String> emails = personRepository.findEmailsByCity(city);
        logger.info("Emails for City {}: {}", city, emails);
        return emails;
    }

    public List<Person> addPerson (Person person){
        List<Person> persons = data.getPersons();
        persons.add(person);
        return persons;
    }

    @Transactional
    public void deletePerson(Person person) {

        if(person == null) {
            logger.error("Invalid input: Person is null");
            throw new IllegalArgumentException("Invalid input: medicalRecord is null");
        }

        Person personToDelete = personRepository.getByFirstNameAndLastName(person.getFirstName(), person.getLastName());

        if(personToDelete == null) {
            logger.error("No person found for: {} {}", person.getFirstName(), person.getLastName());
            throw new PersonNotFoundException(
                    String.format("No person was found for: %s %s",
                            person.getFirstName(), person.getLastName()));
        }
            String firstName = person.getFirstName();
            String lastName = person.getLastName();
            logger.info("Person deleted: {} {}", firstName, lastName);
            personRepository.deleteByFirstNameAndLastName(person.getFirstName(), person.getLastName());
    }

    public Person updatePerson (Person person) {

        if (person == null) {
            logger.error("Invalid input: Person is null");
            throw new IllegalArgumentException("Invalid input: medicalRecord is null");
        }

            Person personToUpdate = personRepository.findByFirstNameAndLastName(person.getFirstName(), person.getLastName());

        if(personToUpdate == null) {
            logger.error("No person found for: {} {}", person.getFirstName(), person.getLastName());
            throw new PersonNotFoundException(
                    String.format("No person was found for: %s %s",
                            person.getFirstName(), person.getLastName()));
        }

            logger.info("Person before update: {}", personToUpdate);

            personToUpdate.setAddress(person.getAddress());
            personToUpdate.setCity(person.getCity());
            personToUpdate.setZip(person.getZip());
            personToUpdate.setPhone(person.getPhone());
            personToUpdate.setEmail(person.getEmail());

            logger.info("Person after updating: {}", personToUpdate);
            return personRepository.save(personToUpdate);
    }

    public Person getPersonByFirstNameAndLastName(String firstName, String lastName) {
        return personRepository.getByFirstNameAndLastName(firstName, lastName);
    }
}
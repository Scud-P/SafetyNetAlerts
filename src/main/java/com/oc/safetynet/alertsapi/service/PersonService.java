package com.oc.safetynet.alertsapi.service;

import com.oc.safetynet.alertsapi.model.MedicalRecord;
import com.oc.safetynet.alertsapi.model.Person;
import com.oc.safetynet.alertsapi.model.dto.*;
import com.oc.safetynet.alertsapi.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PersonService {

    private static final Logger logger = LoggerFactory.getLogger(PersonService.class);

    @Autowired
    private PersonRepoImpl personRepoImpl;

    @Autowired
    private MedicalRecordRepoImpl medicalRecordRepoImpl;

    @Autowired
    private FireStationRepoImpl fireStationRepoImpl;

    public List<PersonInfoDTO> findPersonInfoListDTO(String firstName, String lastName) {

        List<Person> persons = personRepoImpl.findAllByFirstNameAndLastName(firstName, lastName);
        logger.debug("List of persons found for {} {}: {}", firstName, lastName, persons);
        List<MedicalRecord> medicalRecords = medicalRecordRepoImpl.findAllByFirstNameAndLastName(firstName, lastName);
        logger.debug("List of Medical Records found for {} {}: {}", firstName, lastName, medicalRecords);


        List<PersonInfoDTO> findPersonInfoListDTO = persons.stream()
                .flatMap(person -> medicalRecords.stream()
                        .filter(medicalRecord -> isSamePerson(person, medicalRecord))
                        .map(medicalRecord -> new PersonInfoDTO(person, medicalRecord)))
                .toList();
        logger.info("Information found for person(s): {}", findPersonInfoListDTO.toString());
        return findPersonInfoListDTO;
    }


    public List<HomeDTO> findHomesByAddresses(int station) {
        List<String> addresses = fireStationRepoImpl.findAddressesByStation(station);
        logger.debug("List of addresses covered by Fire Station number {}: {}", station, addresses);

        List<HomeDTO> homeDTOS = addresses.stream()
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

        logger.info("List of households covered by Fire Station number {}: {}", station, homeDTOS.toString());
        return homeDTOS;
    }

    public List<String> findPhonesByStation(int station) {
        List<String> addresses = fireStationRepoImpl.findAddressesByStation(station);
        logger.debug("List of addresses corresponding to FireStation number {}: {}", station, addresses);
        List<String> phones = personRepoImpl.findPersonsByAddresses(addresses).stream()
                .map(Person::getPhone)
                .distinct()
                .toList();
        logger.info("List of phone numbers found for addresses {} covered by station {}: {}", addresses, station, phones);
        return phones;
    }

    public PersonFireWithStationNumberDTO findPersonsAtAddress(String address) {

        List<Person> persons = personRepoImpl.findPersonsByAddress(address);
        logger.debug("Persons at address {}: {}", address, persons);
        List<PersonFireDTO> personFireDTOS = persons.stream()
                .map(person -> {
                    MedicalRecord medicalRecord = medicalRecordRepoImpl.findByFirstNameAndLastName(person.getFirstName(), person.getLastName());
                    int age = medicalRecordRepoImpl.calculateAge(medicalRecord);
                    return new PersonFireDTO(person, medicalRecord, age);
                })
                .toList();

        int station = fireStationRepoImpl.findStationByAddress(address);
        PersonFireWithStationNumberDTO personFireWithStationNumberDTO = new PersonFireWithStationNumberDTO();
        personFireWithStationNumberDTO.setPersonFireDTOs(personFireDTOS);
        personFireWithStationNumberDTO.setStation(station);

        logger.info("Persons at address {}: {}", address, personFireWithStationNumberDTO.toString());
        return personFireWithStationNumberDTO;
    }

    public List<ChildDTO> findMinorsAtAddress(String address) {
        List<Person> personsAtAddress = personRepoImpl.findPersonsByAddress(address);
        logger.debug("Persons found at address {}: {}", address, personsAtAddress);

        List<ChildDTO> minors = new ArrayList<>();

        for (Person person : personsAtAddress) {
            String firstName = person.getFirstName();
            String lastName = person.getLastName();
            List<MedicalRecord> medicalRecords = medicalRecordRepoImpl.findAllByFirstNameAndLastName(firstName, lastName);
            List<MedicalRecord> minorsRecords = medicalRecordRepoImpl.findMinors(medicalRecords);

            for (MedicalRecord minorRecord : minorsRecords) {
                ChildDTO minor = new ChildDTO(person, minorRecord);
                List<Person> familyMembers = new ArrayList<>(personsAtAddress);
                familyMembers.removeIf(p -> isSamePerson(p, minorRecord));
                minor.setFamilyMembers(familyMembers);
                minors.add(minor);
            }
        }
        logger.info("Minors found at address {}: {}", address, minors.toString());
        return minors;
    }


    public PersonWithCountDTO findPersonsByStation(int station) {

        List<String> addresses = fireStationRepoImpl.findAddressesByStation(station);
        logger.debug("Addresses covered by station number {}: {}", station, addresses);
        List<Person> persons = personRepoImpl.findPersonsByAddresses(addresses);
        logger.debug("Persons found for addresses {}: {}", addresses, persons);

        List<MedicalRecord> medicalRecords = persons.stream()
                .map(person -> medicalRecordRepoImpl.findByFirstNameAndLastName(person.getFirstName(), person.getLastName()))
                .toList();

        List<PersonDTO> personDTOs = persons.stream()
                .map(person -> new PersonDTO(person.getFirstName(), person.getLastName(), person.getAddress(), person.getPhone()))
                .toList();

        int majorCount = medicalRecordRepoImpl.countMajors(medicalRecords);
        logger.debug("{} majors have been found", majorCount);
        int minorCount = medicalRecordRepoImpl.countMinors(medicalRecords);
        logger.debug("{} minors have been found", minorCount);

        PersonWithCountDTO personWithCountDTO = new PersonWithCountDTO();
        personWithCountDTO.setPersonDTOs(personDTOs);
        personWithCountDTO.setMinorCount(minorCount);
        personWithCountDTO.setMajorCount(majorCount);

        logger.info("Persons covered by station number {}: {}", station, personWithCountDTO.toString());
        return personWithCountDTO;
    }

    public boolean isSamePerson(Person person, MedicalRecord medicalRecord) {
        return person.getFirstName().equalsIgnoreCase(medicalRecord.getFirstName()) &&
                person.getLastName().equalsIgnoreCase(medicalRecord.getLastName());
    }
}
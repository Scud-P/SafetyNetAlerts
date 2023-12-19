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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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

    @Autowired MedicalRecordService medicalRecordService;

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
        return personRepository.findPhoneByAddresses(addresses);
    }

    public List<PersonInfoDTO> findPersonInfoListDTO(String firstName, String lastName) {
        List<MedicalRecord> medicalRecords = medicalRecordRepository.findAllByFirstNameAndLastName(firstName, lastName);
        logger.info("Medical Record(s) found: {}", medicalRecords);
        List<Person> persons = personRepository.findPersonByFirstNameAndLastName(firstName, lastName);
        logger.info("Person(s) found: {}", persons);

        List<PersonInfoDTO> personInfoDTOS = new ArrayList<>();

        for(int i = 0; i < medicalRecords.size() && i < persons.size(); i++) {
            MedicalRecord medicalRecord = medicalRecords.get(i);
            Person person = persons.get(i);

            PersonInfoDTO personInfoDTO = new PersonInfoDTO();

            personInfoDTO.setFirstName(person.getFirstName());
            personInfoDTO.setLastName(person.getLastName());
            personInfoDTO.setAddress(person.getAddress());
            personInfoDTO.setEmail(person.getEmail());

            int age = calculateAge(medicalRecord.getBirthdate());
            personInfoDTO.setAge(age);
            personInfoDTO.setMedications(medicalRecord.getMedications());
            personInfoDTO.setAllergies(medicalRecord.getAllergies());

            personInfoDTOS.add(personInfoDTO);
        }
        return personInfoDTOS;
    }



    public List<HomeDTO> findHomesByStation(int station) {
        List<String> addresses = fireStationRepository.findAddressesByStation(station);
        List<HomeDTO> homeDTOS = new ArrayList<>();

        for (String address : addresses) {
            List<Person> persons = personRepository.findByAddress(address);
            List<FamilyMemberDTO> familyMemberDTOS = new ArrayList<>();

            for (Person person : persons) {
                MedicalRecord medicalRecord = medicalRecordRepository.findByFirstNameAndLastName(person.getFirstName(), person.getLastName());
                FamilyMemberDTO familyMemberDTO = new FamilyMemberDTO();
                familyMemberDTO.setFirstName(person.getFirstName());
                familyMemberDTO.setLastName(person.getLastName());
                familyMemberDTO.setPhone(person.getPhone());
                int age = calculateAge(medicalRecord.getBirthdate());
                familyMemberDTO.setAge(age);
                familyMemberDTO.setAllergies(medicalRecord.getAllergies());
                familyMemberDTO.setMedications(medicalRecord.getMedications());
                familyMemberDTOS.add(familyMemberDTO);

                HomeDTO homeDTO = new HomeDTO();
                homeDTO.setAddress(address);
                homeDTO.setFamilyMembers(familyMemberDTOS);
                homeDTOS.add(homeDTO);
            }
        }
        return homeDTOS;
    }



    public PersonFireWithStationNumberDTO findPersonsAtAddress(String address) {

        List<Person> persons = personRepository.findByAddress(address);
        logger.info("Persons: {}", persons);
        List<PersonFireDTO> personFireDTOS = new ArrayList<>();

        for(Person person : persons) {
            MedicalRecord medicalRecord = medicalRecordRepository.findByFirstNameAndLastName(person.getFirstName(), person.getLastName());
            PersonFireDTO personFireDTO = new PersonFireDTO();
            personFireDTO.setFirstName(person.getFirstName());
            personFireDTO.setLastName(person.getLastName());
            personFireDTO.setPhone(person.getPhone());
            personFireDTO.setAllergies(medicalRecord.getAllergies());
            personFireDTO.setMedications(medicalRecord.getMedications());
            int age = calculateAge(medicalRecord.getBirthdate());
            personFireDTO.setAge(age);
            personFireDTOS.add(personFireDTO);

        }
        PersonFireWithStationNumberDTO personFireWithStationNumberDTO = new PersonFireWithStationNumberDTO();

        int station = fireStationRepository.findStationNumberByAddress(address);
        personFireWithStationNumberDTO.setPersonFireDTOs(personFireDTOS);
        personFireWithStationNumberDTO.setStation(station);

        return personFireWithStationNumberDTO;
}

    public List<ChildDTO> findMinorsAtAddress(String address) {
        LocalDate now = LocalDate.now();
        LocalDate eighteenYearsAgo = now.minusYears(18);

        List<Person> personsAtAddress = personRepository.findByAddress(address);
        List<ChildDTO> childDTOS = new ArrayList<>();


        for (Person personAtAddress : personsAtAddress) {
            List<MedicalRecord> medicalRecords = medicalRecordRepository.findByFirstNameAndLastNameAndBirthDateAfter(
                    personAtAddress.getFirstName(),
                    personAtAddress.getLastName(),
                    eighteenYearsAgo);

            for (MedicalRecord medicalRecord : medicalRecords) {
                if (isSamePerson(medicalRecord, personAtAddress)) {
                    int age = calculateAge(medicalRecord.getBirthdate());
                    ChildDTO childDTO = new ChildDTO();
                    childDTO.setFirstName(personAtAddress.getFirstName());
                    childDTO.setLastName(personAtAddress.getLastName());
                    childDTO.setAge(age);

                    List<Person> familyMembers = new ArrayList<>(personsAtAddress);
                    familyMembers.removeIf(p -> isSamePerson(medicalRecord, p));
                    childDTO.setFamilyMembers(familyMembers);
                    childDTOS.add(childDTO);
                }
            }
        }
        return childDTOS;
    }


    public PersonWithCountDTO findPersonsByStation(int station) {

        List<String> addresses = fireStationRepository.findAddressesByStation(station);
        List<Person> persons = personRepository.findByAddresses(addresses);
        List<MedicalRecord> medicalRecords = new ArrayList<>();
        List<PersonDTO> personDTOs = new ArrayList<>();


        for (Person person : persons) {
            MedicalRecord medicalRecord = medicalRecordRepository.findByFirstNameAndLastName(person.getFirstName(), person.getLastName());
            medicalRecords.add(medicalRecord);
            PersonDTO personDTO = new PersonDTO(person.getFirstName(), person.getLastName(), person.getAddress(), person.getPhone());
            personDTOs.add(personDTO);
        }

        int majorCount = medicalRecordService.countMajorsForMedicalRecords(medicalRecords);
        int minorCount = medicalRecordService.countMinorsForMedicalRecords(medicalRecords);

        PersonWithCountDTO personWithCountDTO = new PersonWithCountDTO();
        personWithCountDTO.setPersonDTOs(personDTOs);
        personWithCountDTO.setMinorCount(minorCount);
        personWithCountDTO.setMajorCount(majorCount);

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

    public List<String> findEmailsByCity(String city) {
        List<String> emails = personRepository.findEmailsByCity(city).stream()
                        .distinct()
                .toList();
        logger.info("Emails for City {}: {}", city, emails);
        return emails;
    }

    public Person addPerson (Person person){
        // appel repo si la personne existe deja en fonction firstName and lastName si la personne est null on continue sinon on bloque
        // clé primaire composite

        Person personToAdd = personRepository.getByFirstNameAndLastName(person.getFirstName(), person.getLastName());
        if(person.getId()!= 0) {
            throw new IllegalArgumentException("ID is automatically incremented");
        }
        if (personToAdd != null) {
            throw new IllegalArgumentException("Person with the same first name and last name already exists");
        } else {
            person.setFirstName(person.getFirstName());
            person.setLastName(person.getLastName());
            person.setAddress(person.getAddress());
            person.setCity(person.getCity());
            person.setZip(person.getZip());
            person.setPhone(person.getPhone());
            person.setEmail(person.getEmail());

            logger.info("Person added: {}", person);

            return personRepository.save(person);
        }
    }

    @Transactional
    public void deletePerson(Person person) {

        if(person != null) {
            String firstName = person.getFirstName();
            String lastName = person.getLastName();
            logger.info("Person deleted: {} {}", firstName, lastName);
            personRepository.deleteByFirstNameAndLastName(person.getFirstName(), person.getLastName());
        } else {
            logger.warn("Person wasn't found");
        }

    }

    public Person updatePerson (Person person) {

        if (person != null) {
            Person personToUpdate = personRepository.findByFirstNameAndLastName(person.getFirstName(), person.getLastName());
            logger.info("Person before update: {}", personToUpdate);

            personToUpdate.setAddress(person.getAddress());
            personToUpdate.setCity(person.getCity());
            personToUpdate.setZip(person.getZip());
            personToUpdate.setPhone(person.getPhone());
            personToUpdate.setEmail(person.getEmail());

            logger.info("Person after updating: {}", personToUpdate);
            return personRepository.save(personToUpdate);

        } else {
            logger.warn("Person wasn't found");
            return null;
        }
    }

    public Person getPersonByFirstNameAndLastName(String firstName, String lastName) {
        return personRepository.getByFirstNameAndLastName(firstName, lastName);

    }
}
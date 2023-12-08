package com.oc.safetynet.alertsapi.service;

import com.oc.safetynet.alertsapi.model.FireStation;
import com.oc.safetynet.alertsapi.model.MedicalRecord;
import com.oc.safetynet.alertsapi.model.Person;
import com.oc.safetynet.alertsapi.model.dto.PersonsWithMinorCount;
import com.oc.safetynet.alertsapi.repository.FireStationRepository;
import com.oc.safetynet.alertsapi.repository.MedicalRecordRepository;
import com.oc.safetynet.alertsapi.repository.PersonRepository;
import com.oc.safetynet.alertsapi.view.ConsoleView;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

    public List<String> getPhoneNumbersByStation(int station) {
        List<Person> persons = getPersonsByStation(station);
        if(persons != null) {
            return persons.stream()
                    .map(Person::getPhone)
                    .distinct()
                    .collect(Collectors.toList());
        }
        return null;
    }

    public List<Person> determineMinors(List<Person> persons) {
        List<MedicalRecord> medicalRecords = medicalRecordRepository.findAll();
        LocalDate now = LocalDate.now();
        LocalDate majorityDate = now.minusYears(18);

        return persons.stream()
                .filter(person -> medicalRecords.stream()
                        .anyMatch(medicalRecord ->
                                person.getFirstName().equals(medicalRecord.getFirstName()) &&
                                        person.getLastName().equals(medicalRecord.getLastName()) &&
                                        medicalRecord.getBirthdate().isAfter(majorityDate)))
                .collect(Collectors.toList());
    }

    public List<Person> setAge(List<Person> persons) {
        List<MedicalRecord> medicalRecords = medicalRecordRepository.findAll();
        LocalDate now = LocalDate.now();

        return persons.stream()
                .peek(person -> {
                    Optional<MedicalRecord> matchingMedicalRecord = medicalRecords.stream()
                            .filter(medicalRecord ->
                                    person.getFirstName().equals(medicalRecord.getFirstName()) &&
                                    person.getLastName().equals(medicalRecord.getLastName()))
                            .findFirst();
                    matchingMedicalRecord.ifPresent(medicalRecord ->  {
                        LocalDate birthdate = medicalRecord.getBirthdate();
                        int age = Period.between(birthdate, now).getYears();
                        person.setAge(age);
                    });
                })
                .collect(Collectors.toList());
    }

    public List<Person> getPersonsByStationWithMinors (int station) {
        List<Person> personsByStation = getPersonsByStation(station);
        List<Person> minors = determineMinors(personsByStation);
        personsByStation.forEach(person -> person.setMinor(minors.contains(person)));
        logger.info("Content of Persons by Station with Minors : {}", personsByStation);
        return personsByStation;
    }

    public PersonsWithMinorCount getPersonsByStationWithMinorsAndCount (int station) {
        List<Person> personsByStation = getPersonsByStationWithMinors(station);
        List<Person> minors = determineMinors(personsByStation);

        long minorsCount = minors.stream().filter(Person::isMinor).count();
        long majorsCount = personsByStation.size() - minorsCount;

        PersonsWithMinorCount personsWithMinorCount = new PersonsWithMinorCount();

        personsWithMinorCount.setPersons(personsByStation);
        personsWithMinorCount.setMinorsCount((int)minorsCount);
        personsWithMinorCount.setMajorsCount((int)majorsCount);

        logger.info("Content of Persons with Minors and MinorsCount : {}", personsWithMinorCount);
        return personsWithMinorCount;
    }

    public List<Person> getMinorsByAddress(String address) {
        List<Person> persons = personRepository.findByAddress(address);
        List<Person> personsWithAge = setAge(persons);
        List<Person> minors = determineMinors(personsWithAge);
        minors.forEach(person -> person.setMinor(minors.contains(person)));
        return minors;
    }

}
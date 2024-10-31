package com.oc.safetynet.alertsapi.repository;

import com.oc.safetynet.alertsapi.model.Data;
import com.oc.safetynet.alertsapi.model.MedicalRecord;
import com.oc.safetynet.alertsapi.service.JsonReaderService;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class MedicalRecordRepoImpl implements MedicalRecordRepo {

    private static final Logger logger = LoggerFactory.getLogger(MedicalRecordRepoImpl.class);

    private List<MedicalRecord> medicalRecords = new ArrayList<>();

    private final Data data;

    @Autowired
    public MedicalRecordRepoImpl(JsonReaderService jsonReaderService, Data data) {
        this.data = jsonReaderService.readData();
    }

    @PostConstruct
    private void loadAllMedicalRecords() {
        medicalRecords = data.getMedicalrecords();
        if (medicalRecords != null) {
            logger.info("Loaded List of medical records from data {}.", medicalRecords);
        }
    }


    @Override
    public List<MedicalRecord> getAllMedicalRecords() {
        return medicalRecords;
    }

    @Override
    public MedicalRecord addMedicalRecordToList(MedicalRecord medicalRecord) {
        boolean isDuplicate = medicalRecords.stream()
                .anyMatch(existingRecord ->
                        existingRecord.getFirstName().equals(medicalRecord.getFirstName()) &&
                                existingRecord.getLastName().equals(medicalRecord.getLastName()));
        if (isDuplicate) {
            logger.error("MedicalRecord with first name {} and last name {} already exists in the database", medicalRecord.getFirstName(), medicalRecord.getLastName());
        } else {
            medicalRecords.add(medicalRecord);
            logger.info("Medical Record added: {}", medicalRecord);
            logger.info("New List of Medical Records: {}", medicalRecords);
            return medicalRecord;
        }
        return null;
    }

    @Override
    public void deleteMedicalRecordFromList(String firstName, String lastName) {
        Optional<MedicalRecord> removedMedicalRecord = medicalRecords.stream()
                .filter(medicalRecord -> medicalRecord.getFirstName().equals(firstName) && medicalRecord.getLastName().equals(lastName))
                .findFirst();

        if (removedMedicalRecord.isPresent()) {
            removedMedicalRecord.ifPresent(medicalRecord -> logger.info("Medical Record removed for: {} {}", medicalRecord.getFirstName(), medicalRecord.getLastName()));

            medicalRecords.removeIf(medicalRecord -> medicalRecord.getFirstName().equals(firstName) && medicalRecord.getLastName().equals(lastName));
        } else {
            logger.error("Medical Record not found for: {} {}", firstName, lastName);
        }
    }

    @Override
    public MedicalRecord updateMedicalRecord(MedicalRecord medicalRecord) {
        Optional<MedicalRecord> foundMedicalRecord = medicalRecords.stream()
                .filter(currentMedicalRecord ->
                        currentMedicalRecord.getFirstName().equals(medicalRecord.getFirstName()) &&
                                currentMedicalRecord.getLastName().equals(medicalRecord.getLastName()))
                .findFirst();

        if (foundMedicalRecord.isPresent()) {
            foundMedicalRecord.ifPresent(existingRecord -> {
                existingRecord.setFirstName(medicalRecord.getFirstName());
                existingRecord.setLastName(medicalRecord.getLastName());
                existingRecord.setBirthdate(medicalRecord.getBirthdate());
                existingRecord.setAllergies(medicalRecord.getAllergies());
                existingRecord.setMedications(medicalRecord.getMedications());
            });
            logger.info("Medical Record modified: {}", medicalRecord);
            logger.info("New List of Medical Records: {}", medicalRecords);
            return medicalRecord;

        } else {
            logger.error("Medical Record not found for: {} {}", medicalRecord.getFirstName(), medicalRecord.getLastName());
        }
        return null;
    }

    @Override
    public List<MedicalRecord> findAllByFirstNameAndLastName(String firstName, String lastName) {
        return medicalRecords.stream()
                .filter(medicalRecord -> medicalRecord.getFirstName().equalsIgnoreCase(firstName) && medicalRecord.getLastName().equalsIgnoreCase(lastName))
                .toList();
    }

    @Override
    public MedicalRecord findByFirstNameAndLastName(String firstName, String lastName) {
        return medicalRecords.stream()
                .filter(medicalRecord -> medicalRecord.getFirstName().equalsIgnoreCase(firstName) && medicalRecord.getLastName().equalsIgnoreCase(lastName))
                .findFirst()
                .orElse(null);
    }

    @Override
    public int countMinors(List<MedicalRecord> medicalRecords) {
        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        long minorCount = medicalRecords.stream()
                .map(MedicalRecord::getBirthdate)
                .map(dateString -> LocalDate.parse(dateString, formatter))
                .map(birthDate -> Period.between(birthDate, now).getYears())
                .filter(age -> age < 18)
                .count();
        return (int) minorCount;
    }

    @Override
    public int countMajors(List<MedicalRecord> medicalRecords) {
        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        long majorCount = medicalRecords.stream()
                .map(MedicalRecord::getBirthdate)
                .map(dateString -> LocalDate.parse(dateString, formatter))
                .map(birthDate -> Period.between(birthDate, now).getYears())
                .filter(age -> age > 18)
                .count();
        return (int) majorCount;
    }

    @Override
    public List<MedicalRecord> findMinors(List<MedicalRecord> medicalRecords) {
        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        return medicalRecords.stream()
                .filter(medicalRecord -> {
                    LocalDate birthDate = LocalDate.parse(medicalRecord.getBirthdate(), formatter);
                    int age = Period.between(birthDate, now).getYears();
                    return age < 18;
                })
                .toList();
    }

    @Override
    public int calculateAge(MedicalRecord medicalRecord) {
        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate birthDate = LocalDate.parse(medicalRecord.getBirthdate(), formatter);
        return Period.between(birthDate, now).getYears();
    }

}

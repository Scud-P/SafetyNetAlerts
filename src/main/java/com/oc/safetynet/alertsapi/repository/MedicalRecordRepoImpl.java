package com.oc.safetynet.alertsapi.repository;

import com.oc.safetynet.alertsapi.model.Data;
import com.oc.safetynet.alertsapi.model.MedicalRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Repository
public class MedicalRecordRepoImpl implements MedicalRecordRepo {

    private static final Logger logger = LoggerFactory.getLogger(MedicalRecordRepoImpl.class);

    private final DataRepository dataRepository;

    @Autowired
    public MedicalRecordRepoImpl(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }


    @Override
    public List<MedicalRecord> getAllMedicalRecords()  {
        try {
            Data data = dataRepository.readData();
            return data.getMedicalrecords();

        } catch (IOException e) {
        throw new RuntimeException("Failed to read data from the repository", e);
        }
    }

    @Override
    public MedicalRecord addMedicalRecordToList(MedicalRecord medicalRecord) {
        try {
            Data data = dataRepository.readData();
            List<MedicalRecord> medicalRecords = getAllMedicalRecords();

            boolean isDuplicate = medicalRecords.stream()
                    .anyMatch(existingRecord ->
                            existingRecord.getFirstName().equals(medicalRecord.getFirstName()) &&
                                    existingRecord.getLastName().equals(medicalRecord.getLastName()));
            if (isDuplicate) {
                logger.error("MedicalRecord with first name {} and last name {} already exists in the database", medicalRecord.getFirstName(), medicalRecord.getLastName());
            } else {
                medicalRecords.add(medicalRecord);
                data.setMedicalrecords(medicalRecords);
                logger.info("Medical Record added: {}", medicalRecord);
                logger.info("New List of Medical Records: {}", data.getMedicalrecords());
                dataRepository.writeData(data);
                return medicalRecord;
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read data from the repository", e);
        }
        return null;
    }

    @Override
    public void deleteMedicalRecordFromList(String firstName, String lastName) {
        try {
            Data data = dataRepository.readData();
            List<MedicalRecord> medicalRecords = getAllMedicalRecords();

            Optional<MedicalRecord> removedMedicalRecord = medicalRecords.stream()
                    .filter(medicalRecord -> medicalRecord.getFirstName().equals(firstName) && medicalRecord.getLastName().equals(lastName))
                    .findFirst();

            if (removedMedicalRecord.isPresent()) {
                removedMedicalRecord.ifPresent(medicalRecord -> logger.info("Medical Record removed for: {} {}", medicalRecord.getFirstName(), medicalRecord.getLastName()));

                medicalRecords.removeIf(medicalRecord -> medicalRecord.getFirstName().equals(firstName) && medicalRecord.getLastName().equals(lastName));
                data.setMedicalrecords(medicalRecords);
                logger.info("New List of Medical Records: {}", data.getMedicalrecords());
                dataRepository.writeData(data);
            } else {
                logger.error("Medical Record not found for: {} {}", firstName, lastName);
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to read data from the repository", e);
        }
    }

    @Override
    public MedicalRecord updateMedicalRecord(MedicalRecord medicalRecord) {
        try {
            Data data = dataRepository.readData();
            List<MedicalRecord> currentMedicalRecords = getAllMedicalRecords();

            Optional<MedicalRecord> foundMedicalRecord = currentMedicalRecords.stream()
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

                data.setMedicalrecords(currentMedicalRecords);
                logger.info("Medical Record modified: {}", medicalRecord);
                logger.info("New List of Medical Records: {}", data.getMedicalrecords());
                dataRepository.writeData(data);
                return medicalRecord;

            } else {
                logger.error("Medical Record not found for: {} {}", medicalRecord.getFirstName(), medicalRecord.getLastName());
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to read data from the repository", e);
        }
        return null;
    }

    @Override
    public List<MedicalRecord> findAllByFirstNameAndLastName(String firstName, String lastName) {
            return getAllMedicalRecords().stream()
                    .filter(medicalRecord -> medicalRecord.getFirstName().equalsIgnoreCase(firstName) && medicalRecord.getLastName().equalsIgnoreCase(lastName))
                    .toList();
    }

    @Override
    public MedicalRecord findByFirstNameAndLastName(String firstName, String lastName) {
            return getAllMedicalRecords().stream()
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
                    int age = Period.between(birthDate,now).getYears();
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

package com.oc.safetynet.alertsapi.repository;

import com.oc.safetynet.alertsapi.model.Data;
import com.oc.safetynet.alertsapi.model.MedicalRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
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
    public List<MedicalRecord> getAllMedicalRecords() throws IOException {
        Data data = dataRepository.readData();
        return data.getMedicalrecords();
    }

    @Override
    public MedicalRecord addMedicalRecordToList(MedicalRecord medicalRecord) {
        try {
            Data data = dataRepository.readData();
            List<MedicalRecord> medicalRecords = data.getMedicalrecords();

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
            List<MedicalRecord> medicalRecords = data.getMedicalrecords();

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
            List<MedicalRecord> currentMedicalRecords = data.getMedicalrecords();

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
        try {
            Data data = dataRepository.readData();
            return data.getMedicalrecords().stream()
                    .filter(medicalRecord -> medicalRecord.getFirstName().equalsIgnoreCase(firstName) && medicalRecord.getLastName().equalsIgnoreCase(lastName))
                    .toList();

        } catch (IOException e) {
            throw new RuntimeException("Failed to read data from the repository", e);
        }
    }

    @Override
    public MedicalRecord findByFirstNameAndLastName(String firstName, String lastName) {
        try {
            Data data = dataRepository.readData();
            return data.getMedicalrecords().stream()
                    .filter(medicalRecord -> medicalRecord.getFirstName().equalsIgnoreCase(firstName) && medicalRecord.getLastName().equalsIgnoreCase(lastName))
                    .findFirst()
                    .orElse(null);

        } catch (IOException e) {
            throw new RuntimeException("Failed to read data from the repository", e);
        }
    }
}

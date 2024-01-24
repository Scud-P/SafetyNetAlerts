package com.oc.safetynet.alertsapi.service;

import com.oc.safetynet.alertsapi.exception.MedicalRecordNotFoundException;
import com.oc.safetynet.alertsapi.model.MedicalRecord;
import com.oc.safetynet.alertsapi.repository.MedicalRecordRepository;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;


@Service
public class MedicalRecordService {
    private static final Logger logger = LoggerFactory.getLogger(MedicalRecordService.class);

    private MedicalRecordRepository medicalRecordRepository;

    public List<MedicalRecord> getAllMedicalRecords() {
        return medicalRecordRepository.findAll();
    }

    public MedicalRecord addMedicalRecord (MedicalRecord medicalRecord){

        MedicalRecord medicalRecordToAdd = medicalRecordRepository.getByFirstNameAndLastName(medicalRecord.getFirstName(), medicalRecord.getLastName());

        if(medicalRecordToAdd != null) {
            logger.error("Medical Record with the same first name and last name already exists");
            throw new IllegalArgumentException("Medical Record with the same first name and last name already exists");
        }

        medicalRecord.setFirstName(medicalRecord.getFirstName());
        medicalRecord.setLastName(medicalRecord.getLastName());
        medicalRecord.setBirthdate(medicalRecord.getBirthdate());
        medicalRecord.setAllergies(medicalRecord.getAllergies());
        medicalRecord.setMedications(medicalRecord.getMedications());

        logger.info("Medical Record added: {}", medicalRecord);

        return medicalRecordRepository.save(medicalRecord);
    }

    @Transactional
    public void deleteMedicalRecord(MedicalRecord medicalRecord) {

        if(medicalRecord == null) {
            logger.error("Invalid input: medicalRecord is null");
            throw new IllegalArgumentException("Invalid input: medicalRecord is null");
        }

        MedicalRecord medicalRecordToDelete = medicalRecordRepository.findByFirstNameAndLastName(medicalRecord.getFirstName(), medicalRecord.getLastName());

        if(medicalRecordToDelete == null) {
            logger.error("No medical record was found for person {} {}", medicalRecord.getFirstName(), medicalRecord.getLastName());
            throw new MedicalRecordNotFoundException(
                    String.format("No medical record was found for person %s %s",
                            medicalRecord.getFirstName(), medicalRecord.getLastName()));
        }
            String recordFirstName = medicalRecord.getFirstName();
            String recordLastName = medicalRecord.getLastName();
            logger.info("Medical Record deleted for {} {}", recordFirstName, recordLastName);
            medicalRecordRepository.deleteByFirstNameAndLastName(medicalRecord.getFirstName(), medicalRecord.getLastName());
    }

    public MedicalRecord updateMedicalRecord(MedicalRecord medicalRecord) {
        if (medicalRecord == null) {
            logger.error("Invalid input: medicalRecord is null");
            throw new IllegalArgumentException("Invalid input: medicalRecord is null");
        }

        MedicalRecord medicalRecordToUpdate = medicalRecordRepository.findByFirstNameAndLastName(
                medicalRecord.getFirstName(), medicalRecord.getLastName());

        if (medicalRecordToUpdate == null) {
            logger.error("No medical record was found for person {} {}", medicalRecord.getFirstName(), medicalRecord.getLastName());
            throw new MedicalRecordNotFoundException(
                    String.format("No medical record was found for person %s %s",
                            medicalRecord.getFirstName(), medicalRecord.getLastName()));
        }

        logger.info("Medical Record before update: {}", medicalRecordToUpdate);
        medicalRecordToUpdate.setBirthdate(medicalRecord.getBirthdate());
        medicalRecordToUpdate.setAllergies(medicalRecord.getAllergies());
        medicalRecordToUpdate.setMedications(medicalRecord.getMedications());
        logger.info("Medical Record after update: {}", medicalRecord);

        return medicalRecordRepository.save(medicalRecordToUpdate);
    }

    public List<MedicalRecord> saveAllMedicalRecords(List<MedicalRecord> medicalRecords) {
        return medicalRecordRepository.saveAll(medicalRecords);
    }

    public int countMajorsForMedicalRecords(List<MedicalRecord> medicalRecords) {
        LocalDate now = LocalDate.now();
        LocalDate birthdate = now.minusYears(18);
        return medicalRecordRepository.countMajorsInList(birthdate, medicalRecords);
    }

    public int countMinorsForMedicalRecords(List<MedicalRecord> medicalRecords) {
        LocalDate now = LocalDate.now();
        LocalDate birthdate = now.minusYears(18);
        return medicalRecordRepository.countMinorsInList(birthdate, medicalRecords);
    }
}

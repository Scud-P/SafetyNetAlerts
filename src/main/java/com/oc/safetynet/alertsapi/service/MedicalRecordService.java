package com.oc.safetynet.alertsapi.service;

import com.oc.safetynet.alertsapi.model.MedicalRecord;
import com.oc.safetynet.alertsapi.model.Person;
import com.oc.safetynet.alertsapi.repository.MedicalRecordRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;


@Service
public class MedicalRecordService {
    private static final Logger logger = LoggerFactory.getLogger(PersonService.class);

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    public List<MedicalRecord> getAllMedicalRecords() {
        return medicalRecordRepository.findAll();
    }

    public MedicalRecord addMedicalRecord (MedicalRecord medicalRecord){
        if(medicalRecord.getId()!= 0) {
            throw new IllegalArgumentException("ID is automatically incremented");
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
        if(medicalRecord != null) {
            String recordFirstName = medicalRecord.getFirstName();
            String recordLastName = medicalRecord.getLastName();

            logger.info("Medical Record deleted for {} {}", recordFirstName, recordLastName);
            medicalRecordRepository.deleteByFirstNameAndLastName(medicalRecord.getFirstName(), medicalRecord.getLastName());
        } else {
            logger.warn("Medical Record wasn't found");
        }

    }

    public MedicalRecord updateMedicalRecord(MedicalRecord medicalRecord) {

        if(medicalRecord != null) {

            MedicalRecord medicalRecordToUpdate = medicalRecordRepository.findByFirstNameAndLastName(medicalRecord.getFirstName(), medicalRecord.getLastName());
            logger.info("Medical Record before update: {}", medicalRecordToUpdate);
            medicalRecordToUpdate.setBirthdate(medicalRecord.getBirthdate());
            medicalRecordToUpdate.setAllergies(medicalRecord.getAllergies());
            medicalRecordToUpdate.setMedications(medicalRecord.getMedications());
            logger.info("Medical Record after update: {}", medicalRecord);
            return medicalRecordRepository.save(medicalRecordToUpdate);

        } else {
            logger.warn("No medical record was found for that person");
            return null;
        }
    }

    public MedicalRecord saveMedicalRecord(MedicalRecord medicalRecord) {
        return medicalRecordRepository.save(medicalRecord);
    }

    public List<MedicalRecord> saveAllMedicalRecords(List<MedicalRecord> medicalRecords) {
        return medicalRecordRepository.saveAll(medicalRecords);
    }

    public List<MedicalRecord> findMedicalRecordBornAfter(LocalDate date) {
        return medicalRecordRepository.findByBirthdateAfter(date);
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

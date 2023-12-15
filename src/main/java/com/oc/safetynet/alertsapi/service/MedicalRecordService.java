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
        String recordFirstName = medicalRecord.getFirstName();
        String recordLastName = medicalRecord.getLastName();
        logger.info("Medical Record deleted for {} {}", recordFirstName, recordLastName);
        medicalRecordRepository.deleteByFirstNameAndLastName(medicalRecord.getFirstName(), medicalRecord.getLastName());
    }

    public MedicalRecord updateMedicalRecord(MedicalRecord medicalRecord) {

        MedicalRecord medicalRecordToUpdate = medicalRecordRepository.findByFirstNameAndLastName(medicalRecord.getFirstName(), medicalRecord.getLastName());

        logger.info("Medical Record before update: {}", medicalRecordToUpdate);

        medicalRecord.setBirthdate(medicalRecord.getBirthdate());
        medicalRecord.setAllergies(medicalRecord.getAllergies());
        medicalRecord.setMedications(medicalRecord.getMedications());

        logger.info("Medical Record after update: {}", medicalRecord);

        return medicalRecordRepository.save(medicalRecord);

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

}

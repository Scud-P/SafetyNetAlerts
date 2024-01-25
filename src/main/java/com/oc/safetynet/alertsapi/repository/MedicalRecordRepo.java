package com.oc.safetynet.alertsapi.repository;

import com.oc.safetynet.alertsapi.model.MedicalRecord;

import java.io.IOException;
import java.util.List;

public interface MedicalRecordRepo {

    List<MedicalRecord> getAllMedicalRecords() throws IOException;

    MedicalRecord addMedicalRecordToList(MedicalRecord medicalRecord) throws IOException;

    void deleteMedicalRecordFromList(String firstName, String lastName);

    MedicalRecord updateMedicalRecord(MedicalRecord medicalRecord);

    List<MedicalRecord> findAllByFirstNameAndLastName(String firstName, String lastName);

    MedicalRecord findByFirstNameAndLastName(String firstName, String lastName);

    int countMinors(List<MedicalRecord> medicalRecords);

    int countMajors(List<MedicalRecord> medicalRecords);

    List<MedicalRecord> findMinors (List<MedicalRecord> medicalRecords);

    int calculateAge(MedicalRecord medicalRecord);

}

package com.oc.safetynet.alertsapi.controller;

import com.oc.safetynet.alertsapi.model.MedicalRecord;
import com.oc.safetynet.alertsapi.repository.MedicalRecordRepoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class MedicalRecordController {

    @Autowired
    private MedicalRecordRepoImpl medicalRecordRepoImpl;

    @PostMapping("/medicalRecord")
    public MedicalRecord addMedicalRecord(@RequestBody MedicalRecord medicalRecord) {
        return medicalRecordRepoImpl.addMedicalRecordToList(medicalRecord);
    }

    @DeleteMapping("/medicalRecord")
    public void deleteMedicalRecord(@RequestBody Map<String, String> requestBody) {
        String firstName = requestBody.get("firstName");
        String lastName = requestBody.get("lastName");
        medicalRecordRepoImpl.deleteMedicalRecordFromList(firstName, lastName);
    }

    @PutMapping("/medicalRecord")
    public MedicalRecord updateMedicalRecord(@RequestBody MedicalRecord medicalRecord) {
        return medicalRecordRepoImpl.updateMedicalRecord(medicalRecord);
    }

}

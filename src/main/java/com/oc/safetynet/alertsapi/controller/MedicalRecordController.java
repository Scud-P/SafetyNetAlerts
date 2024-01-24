package com.oc.safetynet.alertsapi.controller;


import com.oc.safetynet.alertsapi.model.MedicalRecord;
import com.oc.safetynet.alertsapi.repository.MedicalRecordRepoImpl;
import com.oc.safetynet.alertsapi.service.MedicalRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class MedicalRecordController {


    @Autowired
    private MedicalRecordRepoImpl medicalRecordRepoImpl;

    @Autowired
    private MedicalRecordService medicalRecordService;

    @GetMapping("/medicalrecords")
    public List<MedicalRecord> getAllMedicalRecords() {
        return medicalRecordService.getAllMedicalRecords();
    }

    @PostMapping("/medicalRecord")
    public void addMedicalRecord(@RequestBody MedicalRecord medicalRecord) {
        medicalRecordRepoImpl.addMedicalRecordToList(medicalRecord);
    }

    @DeleteMapping("/medicalRecord")
    public void deleteMedicalRecord(@RequestBody Map<String, String> requestBody) {
        String firstName = requestBody.get("firstName");
        String lastName = requestBody.get("lastName");
        medicalRecordRepoImpl.deleteMedicalRecordFromList(firstName, lastName);
    }

    @PutMapping("/medicalRecord")
    public void updateMedicalRecord(@RequestBody MedicalRecord medicalRecord) {
        medicalRecordRepoImpl.updateMedicalRecord(medicalRecord);
    }

    @PostMapping("/batchmedicalrecord")
    public List<MedicalRecord> addAllMedicalRecords(@RequestBody List<MedicalRecord> medicalRecords) {
        return medicalRecordService.saveAllMedicalRecords(medicalRecords);
    }
}

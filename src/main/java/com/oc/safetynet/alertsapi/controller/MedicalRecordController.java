package com.oc.safetynet.alertsapi.controller;


import com.oc.safetynet.alertsapi.model.MedicalRecord;
import com.oc.safetynet.alertsapi.service.MedicalRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MedicalRecordController {

    @Autowired
    private MedicalRecordService medicalRecordService;

    @GetMapping("/medicalrecords")
    public List<MedicalRecord> getAllMedicalRecords() {
        return medicalRecordService.getAllMedicalRecords();
    }

    @PostMapping("/medicalRecord")
    public MedicalRecord addMedicalRecord(@RequestBody MedicalRecord medicalRecord) {
        return medicalRecordService.addMedicalRecord(medicalRecord);
    }

    @DeleteMapping("/medicalRecord")
    public void deleteMedicalRecord(@RequestBody MedicalRecord medicalRecord) {
       medicalRecordService.deleteMedicalRecord(medicalRecord);
    }

    @PutMapping("/medicalRecord")
    public MedicalRecord updateMedicalRecord(@RequestBody MedicalRecord medicalRecord) {
        return medicalRecordService.updateMedicalRecord(medicalRecord);
    }

    @PostMapping("/batchmedicalrecord")
    public List<MedicalRecord> addAllMedicalRecords(@RequestBody List<MedicalRecord> medicalRecords) {
        return medicalRecordService.saveAllMedicalRecords(medicalRecords);
    }
}

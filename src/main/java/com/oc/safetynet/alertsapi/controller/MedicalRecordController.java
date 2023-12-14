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

//    @PostMapping("/medicalrecord")
//    public MedicalRecord addMedicalRecord(@RequestBody MedicalRecord medicalRecord) {
//        return medicalRecordService.saveMedicalRecord(medicalRecord);
//    }

    @PostMapping("/batchmedicalrecord")
    public List<MedicalRecord> addAllMedicalRecords(@RequestBody List<MedicalRecord> medicalRecords) {
        return medicalRecordService.saveAllMedicalRecords(medicalRecords);
    }
}

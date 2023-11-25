package com.oc.safetynet.alertsapi.service;

import com.oc.safetynet.alertsapi.model.MedicalRecord;
import com.oc.safetynet.alertsapi.repository.MedicalRecordRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.json.JSONArray;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MedicalRecordService {

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    @Autowired
    private JsonDataService jsonDataService;

    public List<MedicalRecord> getAllMedicalRecords() {
        return medicalRecordRepository.findAll();
    }

    public MedicalRecord saveMedicalRecord(MedicalRecord medicalRecord) {
        return medicalRecordRepository.save(medicalRecord);
    }

    public List<MedicalRecord> saveAllMedicalRecords(List<MedicalRecord> medicalRecords) {
        return medicalRecordRepository.saveAll(medicalRecords);
    }

    public void populateMedicalRecordsTable() {
        JSONObject data = jsonDataService.findDataArrays();
        if (data != null) {
            JSONArray medicalRecordsArray = data.getJSONArray("medicalrecords");
            List<MedicalRecord> medicalRecords = new ArrayList<>();
            for (int i = 0; i < medicalRecordsArray.length(); i++) {
                JSONObject medicalRecordJson = medicalRecordsArray.getJSONObject(i);

                MedicalRecord medicalrecord = new MedicalRecord();
                medicalrecord.setFirstName(medicalRecordJson.getString("firstName"));
                medicalrecord.setLastName(medicalRecordJson.getString("lastName"));

                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                LocalDate birthdate = LocalDate.parse(medicalRecordJson.getString("birthdate"), dateTimeFormatter);
                medicalrecord.setBirthdate(birthdate);

                List<Object> medicationObjects = medicalRecordJson.getJSONArray("medications").toList();
                List<String> medications = medicationObjects.stream().map(Object::toString).toList();
                medicalrecord.setMedications(medications);

                List<Object> allergiesObjects = medicalRecordJson.getJSONArray("allergies").toList();
                List<String> allergies = allergiesObjects.stream().map(Object::toString).toList();
                medicalrecord.setAllergies(allergies);
                medicalRecords.add(medicalrecord);
            }
            medicalRecordRepository.saveAll(medicalRecords);
        }
    }
}

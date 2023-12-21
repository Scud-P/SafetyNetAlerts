package com.oc.safetynet.alertsapi.model.parameter;

import com.oc.safetynet.alertsapi.model.MedicalRecord;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MedicalRecordParameter {

    private String firstName;
    private String lastName;
    private String birthdate;
    private List<String> medications;
    private List<String> allergies;

    public MedicalRecordParameter(String firstName, String lastName, String birthdate, List<String> medications, List<String> allergies) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthdate = birthdate;
        this.medications = medications;
        this.allergies = allergies;
    }

    public MedicalRecordParameter() {
    }

    public MedicalRecord toMedicalRecord() {
        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setFirstName(this.firstName);
        medicalRecord.setLastName(this.lastName);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        medicalRecord.setBirthdate(LocalDate.parse(this.birthdate, formatter));
        medicalRecord.setAllergies(this.allergies);
        medicalRecord.setMedications(this.medications);
        return medicalRecord;
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public List<String> getMedications() {
        return medications;
    }

    public void setMedications(List<String> medications) {
        this.medications = medications;
    }

    public List<String> getAllergies() {
        return allergies;
    }

    public void setAllergies(List<String> allergies) {
        this.allergies = allergies;
    }
}

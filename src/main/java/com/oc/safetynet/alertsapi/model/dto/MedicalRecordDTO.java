package com.oc.safetynet.alertsapi.model.dto;

import java.time.LocalDate;
import java.util.List;

public class MedicalRecordDTO {

    private List<String> medications;
    private List<String> allergies;
    private String firstName;
    private String lastname;
    private int age;


//    private String medicalRecordForPerson;

//    public void setMedicalRecordForPerson() {
//        this.medicalRecordForPerson = String.format("%s %s: Allergies: %s, Medications: %s", getFirstName(), getLastname(), getMedications().toString(), getAllergies().toString());
//    }
//
//    public String getMedicalRecordForPerson() {
//        return medicalRecordForPerson;
//    }


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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

}

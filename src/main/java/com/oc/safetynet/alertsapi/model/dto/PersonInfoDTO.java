package com.oc.safetynet.alertsapi.model.dto;

import com.oc.safetynet.alertsapi.model.MedicalRecord;
import com.oc.safetynet.alertsapi.model.Person;
import lombok.ToString;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;

@ToString
public class PersonInfoDTO {

    private String firstName;
    private String lastName;
    private String address;
    private String email;
    private int age;
    private List<String> allergies;
    private List<String> medications;

    public PersonInfoDTO() {
    }

    public PersonInfoDTO(String firstName, String lastName, String address, String email, int age, List<String> allergies, List<String> medications) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.email = email;
        this.age = age;
        this.allergies = allergies;
        this.medications = medications;
    }

    public PersonInfoDTO(Person person, MedicalRecord medicalRecord) {
        this.firstName = person.getFirstName();
        this.lastName = person.getLastName();
        this.address = person.getAddress();
        this.email = person.getEmail();
        this.allergies = medicalRecord.getAllergies();
        this.medications = medicalRecord.getMedications();
        this.age = calculateAge(medicalRecord.getBirthdate());
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public List<String> getAllergies() {
        return allergies;
    }

    public void setAllergies(List<String> allergies) {
        this.allergies = allergies;
    }

    public List<String> getMedications() {
        return medications;
    }

    public void setMedications(List<String> medications) {
        this.medications = medications;
    }

    public int calculateAge(String birthDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate birthLocalDate = LocalDate.parse(birthDate, formatter);
        LocalDate now = LocalDate.now();
        Period period = Period.between(birthLocalDate, now);
        return period.getYears();
    }
}

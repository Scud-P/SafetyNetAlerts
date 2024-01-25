package com.oc.safetynet.alertsapi.model.dto;

import com.oc.safetynet.alertsapi.model.MedicalRecord;
import com.oc.safetynet.alertsapi.model.Person;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

public class FamilyMemberDTO {

    private String firstName;
    private String lastName;
    private String phone;
    private int age;
    private List<String> allergies;
    private List<String> medications;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FamilyMemberDTO that = (FamilyMemberDTO) o;
        return age == that.age &&
                Objects.equals(firstName, that.firstName) &&
                Objects.equals(lastName, that.lastName) &&
                Objects.equals(phone, that.phone) &&
                Objects.equals(allergies, that.allergies) &&
                Objects.equals(medications, that.medications);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, phone, age, allergies, medications);
    }

    public FamilyMemberDTO(String firstName, String lastName, String phone, int age, List<String> allergies, List<String> medications) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.age = age;
        this.allergies = allergies;
        this.medications = medications;
    }

    public FamilyMemberDTO() {

    }

    public FamilyMemberDTO(Person person, MedicalRecord medicalRecord) {
        this.firstName = person.getFirstName();
        this.lastName = person.getLastName();
        this.phone = person.getPhone();
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

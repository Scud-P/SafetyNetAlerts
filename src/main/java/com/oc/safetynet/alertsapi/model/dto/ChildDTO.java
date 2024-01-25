package com.oc.safetynet.alertsapi.model.dto;

import com.oc.safetynet.alertsapi.model.MedicalRecord;
import com.oc.safetynet.alertsapi.model.Person;
import lombok.ToString;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;

@ToString
public class ChildDTO {

    private String firstName;
    private String lastName;
    private int age;
    private List<Person> familyMembers;

    public ChildDTO(String firstName, String lastName, int age, List<Person> familyMembers) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.familyMembers = familyMembers;
    }

    public ChildDTO() {
    }

    public ChildDTO(Person person, MedicalRecord medicalRecord) {
        this.firstName = person.getFirstName();
        this.lastName = person.getLastName();
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public List<Person> getFamilyMembers() {
        return familyMembers;
    }

    public void setFamilyMembers(List<Person> familyMembers) {
        this.familyMembers = familyMembers;
    }

    public int calculateAge(String birthDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate birthLocalDate = LocalDate.parse(birthDate, formatter);
        LocalDate now = LocalDate.now();
        Period period = Period.between(birthLocalDate, now);
        return period.getYears();
    }
}

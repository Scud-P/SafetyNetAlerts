package com.oc.safetynet.alertsapi.model.dto;

import java.util.List;

public class FamilyMemberDTO {

    private String firstName;
    private String lastName;
    private String phone;
    private int age;
    private List<String> allergies;
    private List<String> medications;

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

}

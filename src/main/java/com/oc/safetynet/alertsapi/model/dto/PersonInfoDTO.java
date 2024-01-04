package com.oc.safetynet.alertsapi.model.dto;

import java.util.List;

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

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("PersonInfoDTO{");
        sb.append("firstName='").append(firstName).append('\'');
        sb.append(", lastName='").append(lastName).append('\'');
        sb.append(", address='").append(address).append('\'');
        sb.append(", email='").append(email).append('\'');
        sb.append(", age=").append(age);
        sb.append(", allergies=").append(allergies);
        sb.append(", medications=").append(medications);
        sb.append('}');
        return sb.toString();
    }
}

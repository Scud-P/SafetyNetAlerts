package com.oc.safetynet.alertsapi.model.dto;

import com.oc.safetynet.alertsapi.model.Person;

import java.util.List;

public class ChildDTO {

    private String firstName;
    private String lastName;
    private int age;
    List<Person> familyMembers;

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

}

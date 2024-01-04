package com.oc.safetynet.alertsapi.model.dto;

import com.oc.safetynet.alertsapi.model.Person;

import java.util.List;

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

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("ChildDTO{");
        sb.append("firstName='").append(firstName).append('\'');
        sb.append(", lastName='").append(lastName).append('\'');
        sb.append(", age=").append(age);
        sb.append(", familyMembers=").append(familyMembers);
        sb.append('}');
        return sb.toString();
    }
}

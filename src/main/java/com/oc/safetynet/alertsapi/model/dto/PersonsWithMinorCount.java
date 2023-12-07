package com.oc.safetynet.alertsapi.model.dto;

import com.oc.safetynet.alertsapi.model.Person;
import jakarta.persistence.Entity;

import java.util.List;


public class PersonsWithMinorCount {


    private List<Person> persons;
    private int minorsCount;
    private int majorsCount;

    public List<Person> getPersons() {
        return persons;
    }

    public void setPersons(List<Person> persons) {
        this.persons = persons;
    }

    public int getMinorsCount() {
        return minorsCount;
    }

    public void setMinorsCount(int minorsCount) {
        this.minorsCount = minorsCount;
    }

    public int getMajorsCount() {
        return majorsCount;
    }

    public void setMajorsCount(int majorsCount) {
        this.majorsCount = majorsCount;
    }
}

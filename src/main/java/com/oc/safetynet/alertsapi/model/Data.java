package com.oc.safetynet.alertsapi.model;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@ToString
public class Data {
    private List<Person> persons;
    private List<FireStation> firestations;
    private List<MedicalRecord> medicalrecords;

    public Data() {
        this.persons = new ArrayList<>();
        this.firestations = new ArrayList<>();
        this.medicalrecords = new ArrayList<>();
    }

    public List<Person> getPersons() {
        return persons;
    }

    public void setPersons(List<Person> persons) {
        this.persons = persons;
    }

    public List<FireStation> getFirestations() {
        return firestations;
    }

    public void setFireStations(List<FireStation> fireStations) {
        this.firestations = fireStations;
    }

    public List<MedicalRecord> getMedicalrecords() {
        return medicalrecords;
    }

    public void setMedicalrecords(List<MedicalRecord> medicalrecords) {
        this.medicalrecords = medicalrecords;
    }
}

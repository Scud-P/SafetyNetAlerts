package com.oc.safetynet.alertsapi.model.dto;

import java.util.List;

public class PersonWithCountDTO {

    private List<PersonDTO> personDTOs;

    private int minorCount;

    private int majorCount;


    public List<PersonDTO> getPersonDTOs() {
        return personDTOs;
    }

    public void setPersonDTOs(List<PersonDTO> personDTOs) {
        this.personDTOs = personDTOs;
    }

    public int getMinorCount() {
        return minorCount;
    }

    public void setMinorCount(int minorCount) {
        this.minorCount = minorCount;
    }

    public int getMajorCount() {
        return majorCount;
    }

    public void setMajorCount(int majorCount) {
        this.majorCount = majorCount;
    }

}

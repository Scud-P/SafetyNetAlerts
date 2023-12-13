package com.oc.safetynet.alertsapi.model.dto;

import com.oc.safetynet.alertsapi.model.Person;

import java.util.List;

public class HomeDTO {

    private String address;

    private List<FamilyMemberDTO> familyMembers;

    public HomeDTO(String address, List<FamilyMemberDTO> familyMembers) {
        this.address = address;
        this.familyMembers = familyMembers;
    }

    public HomeDTO() {

    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<FamilyMemberDTO> getFamilyMembers() {
        return familyMembers;
    }

    public void setFamilyMembers(List<FamilyMemberDTO> familyMembers) {
        this.familyMembers = familyMembers;
    }

}

package com.oc.safetynet.alertsapi.model.dto;

import com.oc.safetynet.alertsapi.model.Person;

import java.util.List;
import java.util.Objects;

public class HomeDTO {

    private String address;

    private List<FamilyMemberDTO> familyMembers;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HomeDTO homeDTO = (HomeDTO) o;
        return Objects.equals(address, homeDTO.address) &&
                Objects.equals(familyMembers, homeDTO.familyMembers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address, familyMembers);
    }

    @Override
    public String toString() {
        return "HomeDTO{" +
                "address='" + address + '\'' +
                ", familyMembers=" + familyMembers +
                '}';
    }

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

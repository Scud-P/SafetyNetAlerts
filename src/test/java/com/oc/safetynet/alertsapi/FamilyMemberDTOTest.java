package com.oc.safetynet.alertsapi;

import com.oc.safetynet.alertsapi.model.dto.FamilyMemberDTO;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class FamilyMemberDTOTest {

    private List<String> allergies;
    private List<String> medications;

    @BeforeEach
    public void setupTest() {
        List<String> allergies = Arrays.asList("Siths", "Peanuts");
        List<String> medications = Arrays.asList("Meditation", "Force");
    }


    @Test
    public void testFamilyMemberDTODefaultConstructorAndSetters () {

        FamilyMemberDTO familyMemberDTO = new FamilyMemberDTO();
        familyMemberDTO.setFirstName("Bob");
        familyMemberDTO.setLastName("Sponge");
        familyMemberDTO.setPhone("test phone");
        familyMemberDTO.setAge(66);
        familyMemberDTO.setAllergies(allergies);
        familyMemberDTO.setMedications(medications);

        assertNotNull(familyMemberDTO);
        assertEquals("Bob", familyMemberDTO.getFirstName());
        assertEquals("Sponge", familyMemberDTO.getLastName());
        assertEquals("test phone", familyMemberDTO.getPhone());
        assertEquals(66, familyMemberDTO.getAge());
        assertEquals(allergies, familyMemberDTO.getAllergies());
        assertEquals(medications, familyMemberDTO.getMedications());

    }

    @Test
    public void testEquals() {

        FamilyMemberDTO familyMemberDTO = new FamilyMemberDTO("Bob", "Sponge", "test phone", 66, allergies, medications);
        assertEquals(familyMemberDTO, familyMemberDTO);

        FamilyMemberDTO familyMemberDTO1 = new FamilyMemberDTO("Bob", "Sponge", "test phone", 66, allergies, medications);
        FamilyMemberDTO familyMemberDTO2 = new FamilyMemberDTO("Bob", "Sponge", "test phone", 66, allergies, medications);

        assertEquals(familyMemberDTO1, familyMemberDTO2);

    }

    @Test
    public void testHashCode() {

        FamilyMemberDTO familyMemberDTO = new FamilyMemberDTO("Bob", "Sponge", "test phone", 66, allergies, medications);
        int hashCode1 = familyMemberDTO.hashCode();
        int hashCode2 = familyMemberDTO.hashCode();
        assertEquals(hashCode1, hashCode2);

        FamilyMemberDTO familyMemberDTO1 = new FamilyMemberDTO("Bob", "Sponge", "test phone", 66, allergies, medications);
        FamilyMemberDTO familyMemberDTO2 = new FamilyMemberDTO("Bob", "Sponge", "test phone", 66, allergies, medications);
        assertEquals(familyMemberDTO1.hashCode(), familyMemberDTO2.hashCode());

        FamilyMemberDTO differentFamilyMemberDTO = new FamilyMemberDTO("Bob", "Ross", "test phone", 66, allergies, medications);
        assertNotEquals(differentFamilyMemberDTO, familyMemberDTO);

    }

}

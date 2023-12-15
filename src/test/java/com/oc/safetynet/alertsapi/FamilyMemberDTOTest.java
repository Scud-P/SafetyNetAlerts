package com.oc.safetynet.alertsapi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.oc.safetynet.alertsapi.model.dto.FamilyMemberDTO;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;


public class FamilyMemberDTOTest {

    @Test
    public void testFamilyMemberDTODefaultConstructorAndSetters () {

        List<String> allergies = Arrays.asList("Siths", "Peanuts");
        List<String> medications = Arrays.asList("Meditation", "Force");

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

}

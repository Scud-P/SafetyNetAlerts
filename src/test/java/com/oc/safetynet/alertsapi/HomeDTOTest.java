package com.oc.safetynet.alertsapi;

import static org.junit.jupiter.api.Assertions.*;

import com.oc.safetynet.alertsapi.model.dto.FamilyMemberDTO;
import com.oc.safetynet.alertsapi.model.dto.HomeDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeDTOTest {

    String address1, address2;
    List<FamilyMemberDTO> familyMemberDTOS1, familyMemberDTOS2;
    List<String> allergies1, allergies2, allergies3, allergies4;
    List<String> medications1, medications2, medications3, medications4;
    FamilyMemberDTO familyMemberDTO1, familyMemberDTO2, familyMemberDTO3, familyMemberDTO4;


    @BeforeEach
    public void setupTest() {

        allergies1 = Arrays.asList("aller1", "aller2");
        medications1 = Arrays.asList("medic1", "medic2");
        allergies2 = Arrays.asList("aller3", "aller3");
        medications2 = Arrays.asList("medic3", "medic4");
        allergies3 = Arrays.asList("aller5", "aller6");
        medications3 = Arrays.asList("medic5", "medic6");
        allergies4 = Arrays.asList("aller7", "aller8");
        medications4 = Arrays.asList("medic7", "medic8");

        address1 = "test address";
        familyMemberDTO1 = new FamilyMemberDTO("Bob", "Ross", "test phone 1", 66, allergies1, medications1);
        familyMemberDTO2 = new FamilyMemberDTO("John", "Ross", "test phone 2", 66, allergies2, medications2);
        familyMemberDTOS1 = new ArrayList<>();
        familyMemberDTOS1.add(familyMemberDTO1);
        familyMemberDTOS1.add(familyMemberDTO2);

        address2 = "test address 2";
        familyMemberDTO3 = new FamilyMemberDTO("Bob", "Smith", "test phone 3", 66, allergies3, medications3);
        familyMemberDTO4 = new FamilyMemberDTO("John", "Smith", "test phone 4", 66, allergies4, medications4);
        familyMemberDTOS2 = new ArrayList<>();
        familyMemberDTOS2.add(familyMemberDTO3);
        familyMemberDTOS2.add(familyMemberDTO4);

    }

    @Test
    public void testEquals() {
        HomeDTO homeDTO = new HomeDTO(address1, familyMemberDTOS1);
        assertEquals(homeDTO, homeDTO);

        HomeDTO homeDTO2 = new HomeDTO(address2, familyMemberDTOS2);

        assertNotEquals(homeDTO, homeDTO2);
    }

    @Test
    public void testHashCode() {
        HomeDTO homeDTO = new HomeDTO(address1, familyMemberDTOS1);
        assertEquals(homeDTO.hashCode(), homeDTO.hashCode());

        HomeDTO homeDTO2 = new HomeDTO(address2, familyMemberDTOS2);

        assertNotEquals(homeDTO.hashCode(), homeDTO2.hashCode());
    }

}

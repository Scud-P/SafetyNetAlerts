package com.oc.safetynet.alertsapi;

import com.oc.safetynet.alertsapi.model.dto.PersonFireDTO;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PersonFireDTOTest {

    @Test
    public void testPersonFireDTODefaultConstructorAndSetters () {

        List<String> allergies = Arrays.asList("Siths", "Peanuts");
        List<String> medications = Arrays.asList("Meditation", "Force");

        PersonFireDTO personFireDTO = new PersonFireDTO();
        personFireDTO.setFirstName("Bob");
        personFireDTO.setLastName("Ross");
        personFireDTO.setPhone("test phone");
        personFireDTO.setAge(66);
        personFireDTO.setAllergies(allergies);
        personFireDTO.setMedications(medications);

        assertNotNull(personFireDTO);
        assertEquals("Bob", personFireDTO.getFirstName());
        assertEquals("Ross", personFireDTO.getLastName());
        assertEquals("test phone", personFireDTO.getPhone());
        assertEquals(66, personFireDTO.getAge());
        assertEquals(allergies, personFireDTO.getAllergies());
        assertEquals(medications, personFireDTO.getMedications());

    }

}

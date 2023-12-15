package com.oc.safetynet.alertsapi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.oc.safetynet.alertsapi.model.dto.PersonInfoDTO;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class PersonInfoDTOTest {

    @Test
    public void testPersonInfoDTOConstructorAndGetters() {

        String firstName = "Obiwan";
        String lastName = "Kenobi";
        String address = "test address";
        String email = "Obiwan.kenobi@google.com";
        int age = 25;
        List<String> allergies = Arrays.asList("Siths", "Peanuts");
        List<String> medications = Arrays.asList("Meditation", "Force");

        PersonInfoDTO personInfoDTO = new PersonInfoDTO(firstName, lastName, address, email, age, allergies, medications);

        assertNotNull(personInfoDTO);
        assertEquals(firstName, personInfoDTO.getFirstName());
        assertEquals(lastName, personInfoDTO.getLastName());
        assertEquals(address, personInfoDTO.getAddress());
        assertEquals(email, personInfoDTO.getEmail());
        assertEquals(age, personInfoDTO.getAge());
        assertEquals(allergies, personInfoDTO.getAllergies());
        assertEquals(medications, personInfoDTO.getMedications());
    }

    @Test
    public void testPersonInfoDTODefaultConstructorAndSetters() {

        List<String> allergies = Arrays.asList("Siths", "Peanuts");
        List<String> medications = Arrays.asList("Meditation", "Force");

        PersonInfoDTO personInfoDTO = new PersonInfoDTO();
        personInfoDTO.setFirstName("Obiwan");
        personInfoDTO.setLastName("Kenobi");
        personInfoDTO.setAddress("test address");
        personInfoDTO.setEmail("Obiwan.kenobi@google.com");
        personInfoDTO.setAge(25);
        personInfoDTO.setAllergies(allergies);
        personInfoDTO.setMedications(medications);

        assertNotNull(personInfoDTO);
        assertEquals("Obiwan", personInfoDTO.getFirstName());
        assertEquals("Kenobi", personInfoDTO.getLastName());
        assertEquals("test address", personInfoDTO.getAddress());
        assertEquals("Obiwan.kenobi@google.com", personInfoDTO.getEmail());
        assertEquals(25, personInfoDTO.getAge());
        assertEquals(allergies, personInfoDTO.getAllergies());
        assertEquals(medications, personInfoDTO.getMedications());

    }

}

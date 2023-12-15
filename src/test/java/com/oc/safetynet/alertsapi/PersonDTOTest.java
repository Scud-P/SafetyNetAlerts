package com.oc.safetynet.alertsapi;

import com.oc.safetynet.alertsapi.model.dto.PersonDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PersonDTOTest {

    @Test
    public void testPersonDTODefaultConstructorAndSetters() {

        PersonDTO personDTO = new PersonDTO();
        personDTO.setFirstName("Bob");
        personDTO.setLastName("Marley");
        personDTO.setAddress("address");
        personDTO.setPhone("test phone");

        assertNotNull(personDTO);
        assertEquals("Bob", personDTO.getFirstName());
        assertEquals("Marley", personDTO.getLastName());
        assertEquals("address", personDTO.getAddress());
        assertEquals("test phone", personDTO.getPhone());
    }
}

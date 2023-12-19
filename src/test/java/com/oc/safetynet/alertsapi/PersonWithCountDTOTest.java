package com.oc.safetynet.alertsapi;

import com.oc.safetynet.alertsapi.model.Person;
import com.oc.safetynet.alertsapi.model.dto.PersonDTO;
import com.oc.safetynet.alertsapi.model.dto.PersonWithCountDTO;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PersonWithCountDTOTest {

    @Test
    public void testPersonWithCountDTODefaultConstructorAndSetters() {

        List<PersonDTO> personDTOs = List.of(
                new PersonDTO("Bob", "Dylan", "test address", "test phone"),
                new PersonDTO("Bob", "Marley", "test address 2", "test phone 2")

        );

        PersonWithCountDTO personWithCountDTO = new PersonWithCountDTO();
        personWithCountDTO.setMinorCount(5);
        personWithCountDTO.setMajorCount(5);
        personWithCountDTO.setPersonDTOs(personDTOs);

        assertEquals(5, personWithCountDTO.getMinorCount());
        assertEquals(5, personWithCountDTO.getMajorCount());
        assertIterableEquals(personDTOs, personWithCountDTO.getPersonDTOs());


    }


}

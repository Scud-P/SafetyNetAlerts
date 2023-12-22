package com.oc.safetynet.alertsapi;
import com.oc.safetynet.alertsapi.model.dto.PersonInfoDTO;
import com.oc.safetynet.alertsapi.model.dto.PersonInfoListDTO;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PersonInfoListTest {

    @Test
    public void testPersonInfoListDTO() {

        PersonInfoListDTO p = new PersonInfoListDTO();

        List<String> allergies1 = Arrays.asList("Siths", "Peanuts");
        List<String> medications1 = Arrays.asList("Meditation", "Force");

        PersonInfoDTO personInfoDTO1 = new PersonInfoDTO();
        personInfoDTO1.setFirstName("Obiwan");
        personInfoDTO1.setLastName("Kenobi");
        personInfoDTO1.setAddress("test address");
        personInfoDTO1.setEmail("Obiwan.kenobi@google.com");
        personInfoDTO1.setAge(25);
        personInfoDTO1.setAllergies(allergies1);
        personInfoDTO1.setMedications(medications1);

        List<String> allergies2 = Arrays.asList("Siths", "Peanuts");
        List<String> medications2 = Arrays.asList("Meditation", "Force");

        PersonInfoDTO personInfoDTO2 = new PersonInfoDTO();
        personInfoDTO2.setFirstName("Anakin");
        personInfoDTO2.setLastName("Skywalker");
        personInfoDTO2.setAddress("test address");
        personInfoDTO2.setEmail("Anakin.skywalker@google.com");
        personInfoDTO2.setAge(25);
        personInfoDTO2.setAllergies(allergies2);
        personInfoDTO2.setMedications(medications2);

        List<PersonInfoDTO> personInfoDTOList = new ArrayList<>();
        personInfoDTOList.add(personInfoDTO1);
        personInfoDTOList.add(personInfoDTO2);

        p.setPersonInfoListDTO(personInfoDTOList);

        assertEquals(p.getPersonInfoListDTO(), personInfoDTOList );

    }

}

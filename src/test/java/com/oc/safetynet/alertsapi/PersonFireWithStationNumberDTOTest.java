package com.oc.safetynet.alertsapi;


import com.oc.safetynet.alertsapi.model.dto.PersonFireDTO;
import com.oc.safetynet.alertsapi.model.dto.PersonFireWithStationNumberDTO;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PersonFireWithStationNumberDTOTest {

    @Test
    public void testDefaultConstructorAndSetters() {

        List<String> allergies1 = Arrays.asList("aller1", "aller2");
        List<String> medications1 = Arrays.asList("medic1", "medic2");
        List<String> allergies2 = Arrays.asList("aller3", "aller3");
        List<String> medications2 = Arrays.asList("medic3", "medic4");

        PersonFireDTO personFireDTO1 = new PersonFireDTO("Bob", "Morane", "test phone", 50, allergies1, medications1);
        PersonFireDTO personFireDTO2 = new PersonFireDTO("John", "Scott", "test phone2", 51, allergies2, medications2);

        List<PersonFireDTO> personFireDTOList = new ArrayList<>();
        personFireDTOList.add(personFireDTO1);
        personFireDTOList.add(personFireDTO2);

        PersonFireWithStationNumberDTO p = new PersonFireWithStationNumberDTO();
        p.setStation(1);
        p.setPersonFireDTOs(personFireDTOList);

        assertNotNull(p);
        assertEquals(p.getPersonFireDTOs(), personFireDTOList);
        assertEquals(p.getStation(), 1);
    }

}

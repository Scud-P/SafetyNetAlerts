package com.oc.safetynet.alertsapi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.oc.safetynet.alertsapi.model.Person;
import com.oc.safetynet.alertsapi.model.dto.ChildDTO;
import org.junit.jupiter.api.Test;

import java.util.List;

public class ChildDTOTest {

    @Test
    public void testChildDTODefaultConstructorAndSetters() {

        List<Person> familyMembers = List.of(
                new Person("Edouard", "Balladur", "test address", "test city", "test zip", "test phone", "test email"),
                new Person("Donald", "Trump", "test address", "test city", "test zip", "test phone", "test email" )
        );

        ChildDTO childDTO = new ChildDTO();
        childDTO.setFirstName("Pif");
        childDTO.setLastName("Paf");
        childDTO.setAge(6);
        childDTO.setFamilyMembers(familyMembers);

        assertNotNull(childDTO);
        assertEquals("Pif", childDTO.getFirstName());
        assertEquals("Paf", childDTO.getLastName());
        assertEquals(6, childDTO.getAge());
        assertEquals(familyMembers, childDTO.getFamilyMembers());

    }

}

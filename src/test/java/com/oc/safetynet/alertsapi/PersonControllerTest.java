package com.oc.safetynet.alertsapi;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import com.oc.safetynet.alertsapi.controller.FireStationController;
import com.oc.safetynet.alertsapi.controller.MedicalRecordController;
import com.oc.safetynet.alertsapi.controller.PersonController;
import com.oc.safetynet.alertsapi.model.dto.FamilyMemberDTO;
import com.oc.safetynet.alertsapi.model.dto.HomeDTO;
import com.oc.safetynet.alertsapi.service.PersonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@WebMvcTest(controllers = PersonController.class)
public class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonService personService;

    @MockBean
    private MedicalRecordController medicalRecordController;

    @MockBean
    private FireStationController fireStationController;

    @Test
    public void testGetPersons() throws Exception {
        mockMvc.perform(get("/persons"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetAddressesByStation() throws Exception {

        int stationNumber = 1;

        List<String> allergiesBob = new ArrayList<>();
        allergiesBob.add("Capitalism");
        allergiesBob.add("Pop Music");
        List<String> medicationsBob = new ArrayList<>();
        medicationsBob.add("Reggae");
        medicationsBob.add("Women");

        List<String> allergiesJim = new ArrayList<>();
        allergiesJim.add("Christianity");
        allergiesJim.add("Ukulele");
        List<String> medicationJim = new ArrayList<>();
        medicationJim.add("Black Magic");
        medicationJim.add("Guitar Solos");

        List<String> allergiesDupont = new ArrayList<>();
        allergiesDupont.add("Capitalism");
        allergiesDupont.add("Pop Music");
        List<String> medicationsDupont = new ArrayList<>();
        medicationsDupont.add("Reggae");
        medicationsDupont.add("Women");

        List<String> allergiesDupond= new ArrayList<>();
        allergiesDupond.add("Christianity");
        allergiesDupond.add("Ukulele");
        List<String> medicationDupond = new ArrayList<>();
        medicationDupond.add("Black Magic");
        medicationDupond.add("Guitar Solos");

        List<FamilyMemberDTO> familyMembers = List.of(
                new FamilyMemberDTO("Bob", "Marley", "420-420-420",78, allergiesBob, medicationsBob),
                new FamilyMemberDTO("Jimmy", "Page", "666-666-666", 79, allergiesJim, medicationJim)
        );

        List<FamilyMemberDTO> familyMembers2 = List.of(
                new FamilyMemberDTO("Alexis", "Dupont", "123-456-789", 45, allergiesDupont, medicationsDupont),
                new FamilyMemberDTO("Léon", "Dupond", "987-654-321", 45, allergiesDupond, medicationDupond)
        );

        List<HomeDTO> homes = List.of(
                new HomeDTO("13, Dead or Al. Drive", familyMembers),
                new HomeDTO("1, je dirais même plus Avenue", familyMembers2)
        );

        when(personService.findHomesByStation(stationNumber)).thenReturn(homes);

        mockMvc.perform(get("/flood/stations").param("stationNumber", String.valueOf(stationNumber)))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$[0].address").value("13, Dead or Al. Drive"))
                .andExpect(jsonPath("$[1].address").value("1, je dirais même plus Avenue"))
                .andExpect(jsonPath("$[0].familyMembers[0].firstName").value("Bob"))
                .andExpect(jsonPath("$[0].familyMembers[1].firstName").value("Jimmy"))
                .andExpect(jsonPath("$[1].familyMembers[0].firstName").value("Alexis"))
                .andExpect(jsonPath("$[1].familyMembers[1].firstName").value("Léon"));
    }

}

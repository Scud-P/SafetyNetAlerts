package com.oc.safetynet.alertsapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oc.safetynet.alertsapi.controller.MedicalRecordController;
import com.oc.safetynet.alertsapi.model.FireStation;
import com.oc.safetynet.alertsapi.repository.DataRepository;
import com.oc.safetynet.alertsapi.repository.FireStationRepoImpl;
import com.oc.safetynet.alertsapi.service.PersonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(FireStationControllerTest.class)
@ComponentScan(basePackages = "com.oc.safetynet.alertsapi")
public class FireStationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonService personService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FireStationRepoImpl fireStationRepoImpl;


    @Autowired
    private DataRepository dataRepository;

    @Test
    public void testGetAllFireStations() throws Exception {

        List<FireStation> fireStations = List.of(
                new FireStation("test address 1", 1),
                new FireStation("test address 2", 2),
                new FireStation("test address 3", 3)
        );

        when(fireStationRepoImpl.getAllFireStations()).thenReturn(fireStations);

        mockMvc.perform(get("/firestations"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].address").value("test address 1"))
                .andExpect(jsonPath("$[1].station").value(2));

    }

    @Test
    public void testAddFireStation() throws Exception {

        FireStation station = new FireStation("test address", 1);

        when(fireStationRepoImpl.addFireStationToList(any())).thenReturn(station);

        mockMvc.perform(post("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(station)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address").value("test address"))
                .andExpect(jsonPath("$.station").value(1));
    }

    @Test
    public void testUpdateFireStation() throws Exception {

        FireStation station = new FireStation("test address", 1);

        when(fireStationRepoImpl.updateFireStationNumber(any())).thenReturn(station);

        mockMvc.perform(put("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(station)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address").value("test address"))
                .andExpect(jsonPath("$.station").value(1));
    }
}

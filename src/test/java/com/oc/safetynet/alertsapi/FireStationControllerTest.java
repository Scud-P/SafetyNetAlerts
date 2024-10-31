package com.oc.safetynet.alertsapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oc.safetynet.alertsapi.model.FireStation;
import com.oc.safetynet.alertsapi.service.JsonReaderService;
import com.oc.safetynet.alertsapi.repository.FireStationRepoImpl;
import com.oc.safetynet.alertsapi.service.PersonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
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
    private JsonReaderService jsonReaderService;

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

    @Test
    public void testDeleteFireStationByNumber() throws Exception {
        int stationNumber = 1;

        doNothing().when(fireStationRepoImpl).deleteFireStationByNumber(stationNumber);

        mockMvc.perform(delete("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new FireStation(null, stationNumber))))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteFireStationByAddress() throws Exception {
        String address = "123 Main St";

        doNothing().when(fireStationRepoImpl).deleteFireStationByAddress(address);

        mockMvc.perform(delete("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new FireStation(address, 0))))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteFireStationWithNoParams() throws Exception {
        mockMvc.perform(delete("/firestation")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}

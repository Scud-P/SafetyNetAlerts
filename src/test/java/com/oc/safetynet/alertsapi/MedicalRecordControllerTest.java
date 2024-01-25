package com.oc.safetynet.alertsapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oc.safetynet.alertsapi.controller.MedicalRecordController;
import com.oc.safetynet.alertsapi.model.MedicalRecord;
import com.oc.safetynet.alertsapi.repository.DataRepository;
import com.oc.safetynet.alertsapi.repository.MedicalRecordRepoImpl;
import com.oc.safetynet.alertsapi.service.PersonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(MedicalRecordController.class)
@ComponentScan(basePackages = "com.oc.safetynet.alertsapi")

public class MedicalRecordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonService personService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MedicalRecordRepoImpl medicalRecordRepoImpl;

    @Autowired
    private DataRepository dataRepository;

    @Test
    public void testAddMedicalRecord() throws Exception {

        List<String> allergiesBob = new ArrayList<>();
        allergiesBob.add("Capitalism");
        allergiesBob.add("Pop Music");
        List<String> medicationsBob = new ArrayList<>();
        medicationsBob.add("Reggae");
        medicationsBob.add("Women");

        MedicalRecord medicalRecord = new MedicalRecord("Bob", "Bober", "1990-01-01", medicationsBob, allergiesBob);

        when(medicalRecordRepoImpl.addMedicalRecordToList(any(MedicalRecord.class))).thenReturn(medicalRecord);

        mockMvc.perform(post("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(medicalRecord)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Bob"))
                .andExpect(jsonPath("$.lastName").value("Bober"))
                .andExpect(jsonPath("$.medications[0]").value("Reggae"))
                .andExpect(jsonPath("$.allergies[1]").value("Pop Music"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void testUpdateMedicalRecord() throws Exception {

        List<String> allergiesBob = new ArrayList<>();
        allergiesBob.add("Capitalism");
        allergiesBob.add("Pop Music");
        List<String> medicationsBob = new ArrayList<>();
        medicationsBob.add("Reggae");
        medicationsBob.add("Women");

        MedicalRecord medicalRecord = new MedicalRecord("Bob", "Bober", "1990-01-01", medicationsBob, allergiesBob);

        when(medicalRecordRepoImpl.updateMedicalRecord(any())).thenReturn(medicalRecord);

        mockMvc.perform(put("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(medicalRecord)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Bob"))
                .andExpect(jsonPath("$.lastName").value("Bober"))
                .andExpect(jsonPath("$.medications[0]").value("Reggae"))
                .andExpect(jsonPath("$.allergies[1]").value("Pop Music"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void testDeleteMedicalRecord() throws Exception {
        String firstName = "John";
        String lastName = "Doe";

        doNothing().when(medicalRecordRepoImpl).deleteMedicalRecordFromList(firstName, lastName);

        mockMvc.perform(delete("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("firstName", firstName, "lastName", lastName))))
                .andExpect(status().isOk());
    }

}

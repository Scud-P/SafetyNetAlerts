package com.oc.safetynet.alertsapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.oc.safetynet.alertsapi.controller.MedicalRecordController;
import com.oc.safetynet.alertsapi.controller.PersonController;
import com.oc.safetynet.alertsapi.model.MedicalRecord;
import com.oc.safetynet.alertsapi.model.Person;
import com.oc.safetynet.alertsapi.repository.FireStationRepository;
import com.oc.safetynet.alertsapi.repository.MedicalRecordRepository;
import com.oc.safetynet.alertsapi.service.MedicalRecordService;
import com.oc.safetynet.alertsapi.service.PersonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(MedicalRecordController.class)
@ComponentScan(basePackages = "com.oc.safetynet.alertsapi")

public class MedicalRecordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MedicalRecordService medicalRecordService;

    @MockBean
    private MedicalRecordRepository medicalRecordRepository;

    @MockBean
    private FireStationRepository fireStationRepository;

    @MockBean
    private PersonService personService;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    public void testGetAllMedicalRecords() throws Exception {

        List<String> allergiesBob = new ArrayList<>();
        allergiesBob.add("Capitalism");
        allergiesBob.add("Pop Music");
        List<String> medicationsBob = new ArrayList<>();
        medicationsBob.add("Reggae");
        medicationsBob.add("Women");

        List<String> allergiesBob2 = new ArrayList<>();
        allergiesBob2.add("Shellfish");
        allergiesBob2.add("Gluten");
        List<String> medicationsBob2 = new ArrayList<>();
        medicationsBob2.add("Prozac 30mg");
        medicationsBob2.add("Viagra 10mg");

        List<String> allergiesBob3 = new ArrayList<>();
        allergiesBob3.add("Penicillin");
        allergiesBob3.add("Lactose");
        List<String> medicationsBob3 = new ArrayList<>();
        medicationsBob3.add("Aspirin");
        medicationsBob3.add("Chocolate");


        LocalDate now = LocalDate.now();
        LocalDate bobBirthDate = now.minusYears(20);
        LocalDate bobBirthDate2 = now.minusYears(25);
        LocalDate bobBirthDate3 = now.minusYears(30);

        List<MedicalRecord> medicalRecords = List.of(
                new MedicalRecord(1L, "Bob", "Bober", bobBirthDate, medicationsBob, allergiesBob),
                new MedicalRecord(2L, "Bob2", "Bober2", bobBirthDate2, medicationsBob2, allergiesBob2),
                new MedicalRecord(3L, "Bob3", "Bober3", bobBirthDate3, medicationsBob3, allergiesBob3)
        );

        when(medicalRecordService.getAllMedicalRecords()).thenReturn(medicalRecords);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


        mockMvc.perform(get("/medicalrecords"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].firstName").value("Bob"))
                .andExpect(jsonPath("$[1].lastName").value("Bober2"))
                .andExpect(jsonPath("$[2].birthdate").value(bobBirthDate3.format(formatter)))
                .andExpect(jsonPath("$[0].medications[0]").value("Reggae"))
                .andExpect(jsonPath("$[1].allergies[1]").value("Gluten"));

    }

//    @Test
//    public void testAddMedicalRecord() throws Exception {
//
//        List<String> allergiesBob = new ArrayList<>();
//        allergiesBob.add("Capitalism");
//        allergiesBob.add("Pop Music");
//        List<String> medicationsBob = new ArrayList<>();
//        medicationsBob.add("Reggae");
//        medicationsBob.add("Women");
//
//        LocalDate now = LocalDate.now();
//        LocalDate bobBirthDate = now.minusYears(20);
//
//        MedicalRecord medicalRecordToAdd = new MedicalRecord(1L, "Bob", "Bober", bobBirthDate, medicationsBob, allergiesBob);
//
//        when(medicalRecordService.saveMedicalRecord(any(MedicalRecord.class))).thenReturn(medicalRecordToAdd);
//
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//
//
//        mockMvc.perform(post("/medicalrecord")
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.firstName").value("Bob"))
//                .andExpect(jsonPath("$.lastName").value("Bober"))
//                .andExpect(jsonPath("$.birthdate").value(bobBirthDate.format(formatter)))
//                .andExpect(jsonPath("$.medications[0]").value("Reggae"))
//                .andExpect(jsonPath("$.allergies[1]").value("Pop Music"))
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andReturn().getResponse().getContentAsString();;
//    }

}

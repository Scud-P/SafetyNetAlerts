package com.oc.safetynet.alertsapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oc.safetynet.alertsapi.controller.PersonController;
import com.oc.safetynet.alertsapi.model.Person;
import com.oc.safetynet.alertsapi.model.dto.*;
import com.oc.safetynet.alertsapi.repository.DataRepository;
import com.oc.safetynet.alertsapi.repository.PersonRepoImpl;
import com.oc.safetynet.alertsapi.service.PersonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(PersonController.class)
@ComponentScan(basePackages = "com.oc.safetynet.alertsapi")
public class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonService personService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PersonRepoImpl personRepoImpl;

    @Autowired
    private DataRepository dataRepository;

    @Test
    public void testAddPerson() throws Exception {

        Person person = new Person("Bob", "Ross", "address1", "test city", "test zip", "test phone", "test email");

        when(personRepoImpl.addPersonToList(any(Person.class))).thenReturn(person);

        mockMvc.perform(post("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(person)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Bob"))
                .andExpect(jsonPath("$.lastName").value("Ross"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void testUpdatePerson() throws Exception {
        Person person = new Person("Bob", "Ross", "address1", "test city", "test zip", "test phone", "test email");
        when(personRepoImpl.updatePerson(any(Person.class))).thenReturn(person);

        mockMvc.perform(put("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(person)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Bob"))
                .andExpect(jsonPath("$.lastName").value("Ross"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void testDeletePerson() throws Exception {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("firstName", "John");
        requestBody.put("lastName", "Doe");

        doNothing().when(personRepoImpl).deletePersonFromList("John", "Doe");

        mockMvc.perform(delete("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isOk());
    }

    @Test
    public void getAddressesByStationTest() throws Exception {
        List<HomeDTO> homeDTOs = List.of(new HomeDTO("address1", null), new HomeDTO("address2", null));
        when(personService.findHomesByAddresses(1)).thenReturn(homeDTOs);

        mockMvc.perform(get("/flood/stations")
                        .param("stationNumber", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(homeDTOs.size()));
    }

    @Test
    public void getPersonsByStationTest() throws Exception {
        PersonWithCountDTO personWithCountDTO = new PersonWithCountDTO(null, 1, 1);
        when(personService.findPersonsByStation(1)).thenReturn(personWithCountDTO);

        mockMvc.perform(get("/firestation")
                        .param("stationNumber", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.minorCount").value(personWithCountDTO.getMinorCount()))
                .andExpect(jsonPath("$.majorCount").value(personWithCountDTO.getMajorCount()));
    }

    @Test
    public void getAllMinorsAtAddressTest() throws Exception {
        String address = "testAddress";
        List<ChildDTO> minors = List.of(new ChildDTO(), new ChildDTO());
        when(personService.findMinorsAtAddress(address)).thenReturn(minors);

        mockMvc.perform(get("/childAlert")
                        .param("address", address))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(minors.size()));
    }

    @Test
    public void getAllPersonsAtAddressTest() throws Exception {
        String address = "testAddress";
        PersonFireWithStationNumberDTO personFireWithStationNumberDTO = new PersonFireWithStationNumberDTO();
        when(personService.findPersonsAtAddress(address)).thenReturn(personFireWithStationNumberDTO);

        mockMvc.perform(get("/fire")
                        .param("address", address))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.station").exists());
    }

    @Test
    public void getPhonesByStationTest() throws Exception {
        int station = 1;
        List<String> phones = List.of("phone1", "phone2");
        when(personService.findPhonesByStation(station)).thenReturn(phones);

        mockMvc.perform(get("/phoneAlert")
                        .param("firestation", String.valueOf(station)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(phones.size()));
    }

    @Test
    public void getEmailsByCityTest() throws Exception {
        String city = "testCity";
        List<String> emails = List.of("email1", "email2");
        when(personRepoImpl.findEmailsByCity(city)).thenReturn(emails);

        mockMvc.perform(get("/communityEmail")
                        .param("city", city))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(emails.size()));
    }

    @Test
    public void getPersonInfoListTest() throws Exception {

        List<PersonInfoDTO> personInfoDTOS = List.of(
                new PersonInfoDTO(),
                new PersonInfoDTO()
        );

        String firstName = "Bob";
        String lastName = "Ross";

        when(personService.findPersonInfoListDTO(any(), any())).thenReturn(personInfoDTOS);

        mockMvc.perform(get("/personInfo")
                        .param("firstName", firstName)
                        .param("lastName", lastName))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(personInfoDTOS.size()));
    }
}


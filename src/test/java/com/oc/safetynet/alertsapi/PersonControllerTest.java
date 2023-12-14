package com.oc.safetynet.alertsapi;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.oc.safetynet.alertsapi.controller.FireStationController;
import com.oc.safetynet.alertsapi.controller.MedicalRecordController;
import com.oc.safetynet.alertsapi.controller.PersonController;
import com.oc.safetynet.alertsapi.model.Person;
import com.oc.safetynet.alertsapi.model.dto.*;
import com.oc.safetynet.alertsapi.repository.PersonRepository;
import com.oc.safetynet.alertsapi.service.PersonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.util.ArrayList;
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

    @MockBean
    private PersonRepository personRepository;

    @Autowired
    private ObjectMapper objectMapper;

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

    @Test
    public void testGetPersonsByStation() throws Exception {

        int stationNumber = 1;

        List<PersonDTO> personDTOs = List.of(
                new PersonDTO("Bob", "Ross", "Address1", "123-456-789"),
                new PersonDTO("Bobette", "Ross", "Address1", "987-654-321"),
                new PersonDTO("Jacques", "Chirac", "Address2", "222-222-222")
        );

        int minorCount = 1;
        int majorCount = 2;

        PersonWithCountDTO personWithCountDTO = new PersonWithCountDTO(personDTOs, minorCount, majorCount);

        when(personService.findPersonsByStation(stationNumber)).thenReturn(personWithCountDTO);

        mockMvc.perform(get("/firestation").param("stationNumber", String.valueOf(stationNumber)))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.personDTOs",hasSize(3)))
                .andExpect(jsonPath("$.minorCount").value(minorCount))
                .andExpect(jsonPath("$.majorCount").value(majorCount))
                .andExpect(jsonPath("$.personDTOs[0].firstName").value("Bob"))
                .andExpect(jsonPath("$.personDTOs[1].lastName").value("Ross"))
                .andExpect(jsonPath("$.personDTOs[2].address").value("Address2"))
                .andExpect(jsonPath("$.personDTOs[0].phone").value("123-456-789"));
    }

    @Test
    public void testGetAllMinorsAtAddress() throws Exception {

        String address = "test address";

        List<Person> familyMembers1 = List.of(
                new Person(1L,"Edouard", "Balladur", "test address", "test city", "test zip", "test phone", "test email"),
                new Person(2L,"Donald", "Trump", "test address", "test city", "test zip", "test phone", "test email" )
        );

        List<Person> familyMembers2 = List.of(
                new Person(1L,"Edouard", "Balladur", "test address", "test city", "test zip", "test phone", "test email"),
                new Person(2L,"Donald", "Trump", "test address", "test city", "test zip", "test phone", "test email" )
        );


        List<ChildDTO> minors = List.of(
                new ChildDTO("Nicolas", "Sarkozy", 15, familyMembers1),
                new ChildDTO("Xi", "Xingping", 12, familyMembers2)
        );

        when(personService.findMinorsAtAddress(address)).thenReturn(minors);

        mockMvc.perform(get("/childAlert").param("address", address))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$",hasSize(2)))
                .andExpect(jsonPath("$[0].firstName").value("Nicolas"))
                .andExpect(jsonPath("$[1].lastName").value("Xingping"))
                .andExpect(jsonPath("$[0].age").value("15"))
                .andExpect(jsonPath("$[0].familyMembers[0].firstName").value("Edouard"))
                .andExpect(jsonPath("$[1].familyMembers[1].lastName").value("Trump"));
    }

    @Test
    public void testGetAllPersonsAtAddress() throws Exception {

        String address = "test address";
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

        List<PersonFireDTO> personFireDTOs= List.of(
                new PersonFireDTO("Bob", "White", "test phone", 50, allergiesBob, medicationsBob),
                new PersonFireDTO("Jim", "Black", " test phone", 25, allergiesJim, medicationJim)
        );

        PersonFireWithStationNumberDTO personFireWithStationNumberDTO = new PersonFireWithStationNumberDTO(stationNumber, personFireDTOs);

        when(personService.findPersonsAtAddress(address)).thenReturn(personFireWithStationNumberDTO);

        mockMvc.perform(get("/fire").param("address", address))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.personFireDTOs",hasSize(2)))
                .andExpect(jsonPath("$.personFireDTOs[0].firstName").value("Bob"))
                .andExpect(jsonPath("$.personFireDTOs[1].lastName").value("Black"))
                .andExpect(jsonPath("$.personFireDTOs[0].phone").value("test phone"))
                .andExpect(jsonPath("$.personFireDTOs[1].age").value(25))
                .andExpect(jsonPath("$.personFireDTOs[0].allergies",hasSize(2)))
                .andExpect(jsonPath("$.personFireDTOs[1].medications",hasSize(2)));
    }

    @Test
    public void testGetPhonesByStation() throws Exception {

        int stationNumber = 1;

        List<String> addresses = List.of(
                "test address 1",
                "test address 2",
                "test address 3"
        );

        when(personService.findPhonesByStation(stationNumber)).thenReturn(addresses);

        mockMvc.perform(get("/phoneAlert").param("firestation", String.valueOf(stationNumber)))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0]").value("test address 1"))
                .andExpect(jsonPath("$[1]").value("test address 2"))
                .andExpect(jsonPath("$[2]").value("test address 3"));
    }

    @Test
    public void testGetEmailsByCity() throws Exception {

        String city = "Gotham";

        List <String> emails = List.of(
                "bob@bob.bob",
                "bib@bib.bib",
                "bab@bab.bab"
        );

        when(personService.findEmailsByCity(city)).thenReturn(emails);

        mockMvc.perform(get("/communityEmail").param("city", city))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0]").value("bob@bob.bob"))
                .andExpect(jsonPath("$[1]").value("bib@bib.bib"))
                .andExpect(jsonPath("$[2]").value("bab@bab.bab"));
    }

    @Test
    public void testAddPerson() throws Exception {

        Person personToAdd = new Person(1L, "Bob", "Dylan", "test address", "test city", "test zip", "test phone", "test email");
        when(personService.savePerson(any(Person.class))).thenReturn(personToAdd);

        mockMvc.perform(post("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(personToAdd)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Bob"))
                .andExpect(jsonPath("$.lastName").value("Dylan"))
                .andExpect(jsonPath("$.address").value("test address"))
                .andExpect(jsonPath("$.city").value("test city"))
                .andExpect(jsonPath("$.zip").value("test zip"))
                .andExpect(jsonPath("$.phone").value("test phone"))
                .andExpect(jsonPath("$.email").value("test email"));
    }

    @Test
    public void testGetPersonByFirstNameAndLastName() throws Exception {

        String firstName = "Bob";
        String lastName = "Dylan";

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

        List<PersonInfoDTO> persons = List.of(
                new PersonInfoDTO("Bob", "Dylan", "test address 1", "bob1@bob.bob", 20, allergiesBob, medicationsBob),
                new PersonInfoDTO("Bob", "Dylan", "test address 2", "bob2@bob.bob", 21, allergiesBob2, medicationsBob2),
                new PersonInfoDTO("Bob", "Dylan", "test address 3", "bob3@bob.bob", 22, allergiesBob3, medicationsBob3)
        );

        when(personService.findPersonInfoListDTO(firstName, lastName)).thenReturn(persons);

        mockMvc.perform(get("/personInfo")
                        .param("firstName", firstName)
                        .param("lastName", lastName))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].firstName").value("Bob"))
                .andExpect(jsonPath("$[1].lastName").value("Dylan"))
                .andExpect(jsonPath("$[2].address").value("test address 3"))
                .andExpect(jsonPath("$[0].email").value("bob1@bob.bob"))
                .andExpect(jsonPath("$[1].age").value(21))
                .andExpect(jsonPath("$[2].allergies[0]").value("Penicillin"))
                .andExpect(jsonPath("$[0].medications[1]").value("Women"));
    }
}

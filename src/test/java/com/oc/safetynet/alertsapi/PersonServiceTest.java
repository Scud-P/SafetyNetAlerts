package com.oc.safetynet.alertsapi;

import com.oc.safetynet.alertsapi.controller.FireStationController;
import com.oc.safetynet.alertsapi.controller.MedicalRecordController;
import com.oc.safetynet.alertsapi.controller.PersonController;
import com.oc.safetynet.alertsapi.model.MedicalRecord;
import com.oc.safetynet.alertsapi.model.Person;
import com.oc.safetynet.alertsapi.model.dto.*;
import com.oc.safetynet.alertsapi.repository.FireStationRepoImpl;
import com.oc.safetynet.alertsapi.repository.MedicalRecordRepoImpl;
import com.oc.safetynet.alertsapi.repository.PersonRepoImpl;
import com.oc.safetynet.alertsapi.service.PersonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

@WebMvcTest(PersonService.class)
@ComponentScan(basePackages = "com.oc.safetynet.alertsapi")

public class PersonServiceTest {

    @Autowired
    private PersonService personService;

    @MockBean
    private PersonController personController;

    @MockBean
    private PersonRepoImpl personRepoImpl;

    @MockBean
    private FireStationRepoImpl fireStationRepoImpl;

    @MockBean
    private MedicalRecordRepoImpl medicalRecordRepoImpl;

    @MockBean
    private MedicalRecordController medicalRecordController;

    @MockBean
    private FireStationController fireStationController;


    @Test
    public void testFindPersonInfoListDTO() {

        String address1 = "test address 1";
        String address2 = "test address 2";

        List<String> allergiesJohnDoe = new ArrayList<>();
        allergiesJohnDoe.add("Capitalism");
        allergiesJohnDoe.add("Pop Music");
        List<String> medicationsJohnDoe = new ArrayList<>();
        medicationsJohnDoe.add("Reggae");
        medicationsJohnDoe.add("Women");


        List<String> allergiesJohnSmith = new ArrayList<>();
        allergiesJohnSmith.add("Shellfish");
        allergiesJohnSmith.add("Gluten");
        List<String> medicationsJohnSmith = new ArrayList<>();
        medicationsJohnSmith.add("Prozac 30mg");
        medicationsJohnSmith.add("Viagra 10mg");

        List<Person> persons = List.of(
                new Person("Bob", "Ross", address1, "test city", "test zip", "test phone", "test email"),
                new Person("Bob", "Ross", address2, "test city", "test zip", "test phone", "test email")
        );

        List<MedicalRecord> medicalRecords = List.of(
                new MedicalRecord("Bob", "Ross", "01/01/2000", medicationsJohnDoe, allergiesJohnDoe),
                new MedicalRecord("Bob", "Ross", "01/01/2020", medicationsJohnSmith, allergiesJohnSmith)
        );

        when(personRepoImpl.findAllByFirstNameAndLastName(any(), any())).thenReturn(persons);
        when(medicalRecordRepoImpl.findAllByFirstNameAndLastName(any(), any())).thenReturn(medicalRecords);

        List<PersonInfoDTO> result = personService.findPersonInfoListDTO("Bob", "Ross");

        verify(personRepoImpl, times(1)).findAllByFirstNameAndLastName("Bob", "Ross");
        verify(medicalRecordRepoImpl, times(1)).findAllByFirstNameAndLastName("Bob", "Ross");
    }
}


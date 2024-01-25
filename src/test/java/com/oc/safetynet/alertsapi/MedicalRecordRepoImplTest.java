package com.oc.safetynet.alertsapi;

import com.oc.safetynet.alertsapi.model.Data;
import com.oc.safetynet.alertsapi.model.MedicalRecord;
import com.oc.safetynet.alertsapi.repository.DataRepository;
import com.oc.safetynet.alertsapi.repository.MedicalRecordRepoImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class MedicalRecordRepoImplTest {

    @Mock
    private DataRepository dataRepository;

    @InjectMocks
    private MedicalRecordRepoImpl medicalRecordRepo;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getAllMedicalRecords_ShouldReturnListOfMedicalRecords() throws IOException {
        List<MedicalRecord> mockMedicalRecords = new ArrayList<>();
        when(dataRepository.readData()).thenReturn(new Data());
        List<MedicalRecord> result = medicalRecordRepo.getAllMedicalRecords();
        assertEquals(mockMedicalRecords, result);
    }

    @Test
    public void addMedicalRecordToList_ShouldAddMedicalRecordToList() throws IOException {

        List<String> allergiesBob = new ArrayList<>();
        allergiesBob.add("Capitalism");
        allergiesBob.add("Pop Music");
        List<String> medicationsBob = new ArrayList<>();
        medicationsBob.add("Reggae");
        medicationsBob.add("Women");

        MedicalRecord medicalRecord = new MedicalRecord("Bob", "Bober", "1990-01-01", medicationsBob, allergiesBob);

        Data mockData = new Data();

        when(dataRepository.readData()).thenReturn(mockData);

        medicalRecordRepo.addMedicalRecordToList(medicalRecord);

        assertEquals(1, mockData.getMedicalrecords().size());
        assertEquals(medicalRecord, mockData.getMedicalrecords().get(0));

        verify(dataRepository, times(1)).writeData(mockData);
    }

    @Test
    public void deletePersonFromList_shouldDeletePersonFromList() throws IOException {

        List<String> allergiesBob = new ArrayList<>();
        allergiesBob.add("Capitalism");
        allergiesBob.add("Pop Music");
        List<String> medicationsBob = new ArrayList<>();
        medicationsBob.add("Reggae");
        medicationsBob.add("Women");

        List<String> allergiesJohn = new ArrayList<>();
        allergiesJohn.add("Peanuts");
        allergiesJohn.add("Children");
        List<String> medicationsJohn = new ArrayList<>();
        medicationsJohn.add("Meditation");
        medicationsJohn.add("Gaming");


        MedicalRecord medicalRecord = new MedicalRecord("Bob", "Bober", "1990-01-01", medicationsBob, allergiesBob);
        MedicalRecord medicalRecord2 = new MedicalRecord("John", "Doe", "1990-01-01", medicationsJohn, allergiesJohn);


        List<MedicalRecord> medicalRecords = new ArrayList<>();

        medicalRecords.add(medicalRecord);
        medicalRecords.add(medicalRecord2);

        Data mockData = new Data();
        mockData.setMedicalrecords(medicalRecords);

        when(dataRepository.readData()).thenReturn(mockData);

        medicalRecordRepo.deleteMedicalRecordFromList("Bob", "Bober");

        assertEquals(1, mockData.getMedicalrecords().size());
        assertEquals(medicalRecord2, mockData.getMedicalrecords().get(0));
    }

    @Test
    public void updatePerson_shouldUpdateAPerson() throws IOException {

        List<String> allergiesBob = new ArrayList<>();
        allergiesBob.add("Capitalism");
        allergiesBob.add("Pop Music");
        List<String> medicationsBob = new ArrayList<>();
        medicationsBob.add("Reggae");
        medicationsBob.add("Women");

        List<String> allergiesJohn = new ArrayList<>();
        allergiesJohn.add("Peanuts");
        allergiesJohn.add("Children");
        List<String> medicationsJohn = new ArrayList<>();
        medicationsJohn.add("Meditation");
        medicationsJohn.add("Gaming");


        MedicalRecord medicalRecord = new MedicalRecord("Bob", "Bober", "1990-01-01", medicationsBob, allergiesBob);
        MedicalRecord medicalRecord2 = new MedicalRecord("John", "Doe", "1990-01-01", medicationsJohn, allergiesJohn);


        List<MedicalRecord> medicalRecords = new ArrayList<>();

        medicalRecords.add(medicalRecord);
        medicalRecords.add(medicalRecord2);

        Data mockData = new Data();
        mockData.setMedicalrecords(medicalRecords);

        MedicalRecord medicalRecord3 = new MedicalRecord("Bob", "Bober", "1990-01-01", medicationsJohn, allergiesJohn);

        when(dataRepository.readData()).thenReturn(mockData);

        MedicalRecord result = medicalRecordRepo.updateMedicalRecord(medicalRecord3);

        assertEquals(2, mockData.getMedicalrecords().size());
        assertEquals(allergiesJohn, result.getAllergies());
    }

    @Test
    public void findAllByFirstNameAndLastNameTest() throws IOException {
        List<String> allergiesBob = new ArrayList<>();
        allergiesBob.add("Capitalism");
        allergiesBob.add("Pop Music");
        List<String> medicationsBob = new ArrayList<>();
        medicationsBob.add("Reggae");
        medicationsBob.add("Women");

        List<String> allergiesJohn = new ArrayList<>();
        allergiesJohn.add("Peanuts");
        allergiesJohn.add("Children");
        List<String> medicationsJohn = new ArrayList<>();
        medicationsJohn.add("Meditation");
        medicationsJohn.add("Gaming");

        MedicalRecord medicalRecord = new MedicalRecord("Bob", "Bober", "1990-01-01", medicationsBob, allergiesBob);
        MedicalRecord medicalRecord2 = new MedicalRecord("Bob", "Bober", "2000-01-01", medicationsJohn, allergiesJohn);

        List<MedicalRecord> medicalRecords = new ArrayList<>();

        medicalRecords.add(medicalRecord);
        medicalRecords.add(medicalRecord2);

        Data mockData = new Data();
        mockData.setMedicalrecords(medicalRecords);

        when(dataRepository.readData()).thenReturn(mockData);

        List<MedicalRecord> result = medicalRecordRepo.findAllByFirstNameAndLastName("Bob", "Bober");

        assertEquals(2, result.size());
        assertEquals(allergiesBob, result.get(0).getAllergies());
    }

    @Test
    public void findByFirstNameAndLastNameTest() throws IOException {
        List<String> allergiesBob = new ArrayList<>();
        allergiesBob.add("Capitalism");
        allergiesBob.add("Pop Music");
        List<String> medicationsBob = new ArrayList<>();
        medicationsBob.add("Reggae");
        medicationsBob.add("Women");

        List<String> allergiesJohn = new ArrayList<>();
        allergiesJohn.add("Peanuts");
        allergiesJohn.add("Children");
        List<String> medicationsJohn = new ArrayList<>();
        medicationsJohn.add("Meditation");
        medicationsJohn.add("Gaming");

        MedicalRecord medicalRecord = new MedicalRecord("Bob", "Bober", "1990-01-01", medicationsBob, allergiesBob);
        MedicalRecord medicalRecord2 = new MedicalRecord("Bob", "Bober", "2000-01-01", medicationsJohn, allergiesJohn);

        List<MedicalRecord> medicalRecords = new ArrayList<>();

        medicalRecords.add(medicalRecord);
        medicalRecords.add(medicalRecord2);

        Data mockData = new Data();
        mockData.setMedicalrecords(medicalRecords);

        when(dataRepository.readData()).thenReturn(mockData);

        MedicalRecord result = medicalRecordRepo.findByFirstNameAndLastName("Bob", "Bober");

        assertEquals(allergiesBob, result.getAllergies());
        assertEquals(medicationsBob, result.getMedications());
    }

}

package com.oc.safetynet.alertsapi;

import com.oc.safetynet.alertsapi.model.Data;
import com.oc.safetynet.alertsapi.model.MedicalRecord;
import com.oc.safetynet.alertsapi.service.JsonReaderService;
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
import static org.mockito.Mockito.*;

public class MedicalRecordRepoImplTest {

    @Mock
    private JsonReaderService jsonReaderService;

    @InjectMocks
    private MedicalRecordRepoImpl medicalRecordRepo;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getAllMedicalRecords_ShouldReturnListOfMedicalRecords() throws IOException {
        List<MedicalRecord> mockMedicalRecords = new ArrayList<>();
        when(jsonReaderService.readData()).thenReturn(new Data());
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

        medicalRecordRepo.addMedicalRecordToList(medicalRecord);

        List<MedicalRecord> updatedMedicalRecords = medicalRecordRepo.getAllMedicalRecords();

        assertEquals(1, updatedMedicalRecords.size());
        assertEquals(medicalRecord, updatedMedicalRecords.get(0));
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

        medicalRecordRepo.addMedicalRecordToList(medicalRecord);
        medicalRecordRepo.addMedicalRecordToList(medicalRecord2);

        medicalRecordRepo.deleteMedicalRecordFromList("Bob", "Bober");

        List<MedicalRecord> updatedMedicalRecords = medicalRecordRepo.getAllMedicalRecords();

        assertEquals(1, updatedMedicalRecords.size());
        assertEquals(medicalRecord2, updatedMedicalRecords.get(0));
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


        medicalRecordRepo.addMedicalRecordToList(medicalRecord);
        medicalRecordRepo.addMedicalRecordToList(medicalRecord2);

        MedicalRecord medicalRecord3 = new MedicalRecord("Bob", "Bober", "1990-01-01", medicationsJohn, allergiesJohn);

        medicalRecordRepo.updateMedicalRecord(medicalRecord3);

        List<MedicalRecord> updatedMedicalRecords = medicalRecordRepo.getAllMedicalRecords();

        assertEquals(2, updatedMedicalRecords.size());
        assertEquals(allergiesJohn, updatedMedicalRecords.get(1).getAllergies());
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
        MedicalRecord medicalRecord2 = new MedicalRecord("John", "Bober", "2000-01-01", medicationsJohn, allergiesJohn);

        medicalRecordRepo.addMedicalRecordToList(medicalRecord);
        medicalRecordRepo.addMedicalRecordToList(medicalRecord2);

        List<MedicalRecord> result = medicalRecordRepo.findAllByFirstNameAndLastName("Bob", "Bober");

        assertEquals(1, result.size());
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
        MedicalRecord medicalRecord2 = new MedicalRecord("John", "Bober", "2000-01-01", medicationsJohn, allergiesJohn);

        medicalRecordRepo.addMedicalRecordToList(medicalRecord);
        medicalRecordRepo.addMedicalRecordToList(medicalRecord2);

        MedicalRecord result = medicalRecordRepo.findByFirstNameAndLastName("Bob", "Bober");

        assertEquals(allergiesBob, result.getAllergies());
    }

    @Test
    public void countMinors_shouldReturnTheAmountOfMinors() throws IOException {
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

        MedicalRecord medicalRecord = new MedicalRecord("Bob", "Bober", "01/01/1990", medicationsBob, allergiesBob);
        MedicalRecord medicalRecord2 = new MedicalRecord("John", "Bober", "01/01/2020", medicationsJohn, allergiesJohn);

        medicalRecordRepo.addMedicalRecordToList(medicalRecord);
        medicalRecordRepo.addMedicalRecordToList(medicalRecord2);

        List<MedicalRecord> medicalRecords = medicalRecordRepo.getAllMedicalRecords();

        int result = medicalRecordRepo.countMinors(medicalRecords);

        assertEquals(1, result);
    }

    @Test
    public void countMajors_shouldReturnTheAmountOfMajors() throws IOException {
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

        MedicalRecord medicalRecord = new MedicalRecord("Bob", "Bober", "01/01/1990", medicationsBob, allergiesBob);
        MedicalRecord medicalRecord2 = new MedicalRecord("John", "Bober", "01/01/2020", medicationsJohn, allergiesJohn);

        medicalRecordRepo.addMedicalRecordToList(medicalRecord);
        medicalRecordRepo.addMedicalRecordToList(medicalRecord2);

        List<MedicalRecord> medicalRecords = medicalRecordRepo.getAllMedicalRecords();

        int result = medicalRecordRepo.countMajors(medicalRecords);

        assertEquals(1, result);
    }

    @Test
    public void findMinors_shouldReturnAListOfMinors() throws IOException {
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

        MedicalRecord medicalRecord = new MedicalRecord("Bob", "Bober", "01/01/1990", medicationsBob, allergiesBob);
        MedicalRecord medicalRecord2 = new MedicalRecord("John", "Bober", "01/01/2020", medicationsJohn, allergiesJohn);

        medicalRecordRepo.addMedicalRecordToList(medicalRecord);
        medicalRecordRepo.addMedicalRecordToList(medicalRecord2);

        List<MedicalRecord> medicalRecords = medicalRecordRepo.getAllMedicalRecords();

        List<MedicalRecord> result = medicalRecordRepo.findMinors(medicalRecords);

        List<MedicalRecord> expected = List.of(
                medicalRecord2
        );

        assertEquals(expected, result);
    }

    @Test
    public void calculateAge_shouldReturnTheAgeOfAMedicalRecord() {
        List<String> allergiesBob = new ArrayList<>();
        allergiesBob.add("Capitalism");
        allergiesBob.add("Pop Music");
        List<String> medicationsBob = new ArrayList<>();
        medicationsBob.add("Reggae");
        medicationsBob.add("Women");

        MedicalRecord medicalRecord = new MedicalRecord("Bob", "Bober", "01/01/1990", medicationsBob, allergiesBob);

        int result = medicalRecordRepo.calculateAge(medicalRecord);

        assertEquals(34, result);
    }

}

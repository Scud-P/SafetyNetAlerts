package com.oc.safetynet.alertsapi;


import com.oc.safetynet.alertsapi.model.MedicalRecord;
import com.oc.safetynet.alertsapi.repository.MedicalRecordRepository;
import com.oc.safetynet.alertsapi.service.MedicalRecordService;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cglib.core.Local;

import static org.assertj.core.api.Assertions.assertThat;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class MedicalRecordServiceTest {

    @MockBean
    private MedicalRecordRepository medicalRecordRepository;

    @Autowired
    private MedicalRecordService medicalRecordService;


    List<MedicalRecord> medicalRecords;
    List<String> allergiesBob, allergiesBob2, allergiesBob3, medicationsBob, medicationsBob2, medicationsBob3;
    LocalDate bobBirthDate, bobBirthDate2, bobBirthDate3;

    @BeforeEach
    public void setupTest() {

        allergiesBob = new ArrayList<>();
        allergiesBob.add("Capitalism");
        allergiesBob.add("Pop Music");
        medicationsBob = new ArrayList<>();
        medicationsBob.add("Reggae");
        medicationsBob.add("Women");

        allergiesBob2 = new ArrayList<>();
        allergiesBob2.add("Shellfish");
        allergiesBob2.add("Gluten");
        medicationsBob2 = new ArrayList<>();
        medicationsBob2.add("Prozac 30mg");
        medicationsBob2.add("Viagra 10mg");

        allergiesBob3 = new ArrayList<>();
        allergiesBob3.add("Penicillin");
        allergiesBob3.add("Lactose");
        List<String> medicationsBob3 = new ArrayList<>();
        medicationsBob3.add("Aspirin");
        medicationsBob3.add("Chocolate");

        LocalDate now = LocalDate.now();
        bobBirthDate = now.minusYears(10);
        bobBirthDate2 = now.minusYears(25);
        bobBirthDate3 = now.minusYears(30);
    }

    @Test
    public void testGetAllMedicalRecords() {

        medicalRecords = List.of(
                new MedicalRecord(1L, "Bob", "Bober", bobBirthDate, medicationsBob, allergiesBob),
                new MedicalRecord(2L, "Bob2", "Bober2", bobBirthDate2, medicationsBob2, allergiesBob2),
                new MedicalRecord(3L, "Bob3", "Bober3", bobBirthDate3, medicationsBob3, allergiesBob3)
        );

        when(medicalRecordRepository.findAll()).thenReturn(medicalRecords);

        List<MedicalRecord> result = medicalRecordService.getAllMedicalRecords();

        assertNotNull(result);
        assertThat(result).hasSize(3);

        verify(medicalRecordRepository, times(1)).findAll();

    }

    @Test
    public void testAddMedicalRecordNoExistingRecord() {

        MedicalRecord medicalRecordToAdd = new MedicalRecord();
        medicalRecordToAdd.setFirstName("Bob");
        medicalRecordToAdd.setLastName("Bober");
        medicalRecordToAdd.setBirthdate(bobBirthDate);
        medicalRecordToAdd.setAllergies(allergiesBob);
        medicalRecordToAdd.setMedications(medicationsBob);

        when(medicalRecordRepository.getByFirstNameAndLastName(anyString(), anyString())).thenReturn(null);

        medicalRecordService.addMedicalRecord(medicalRecordToAdd);

        verify(medicalRecordRepository, times(1)).getByFirstNameAndLastName("Bob", "Bober");
        verify(medicalRecordRepository, times(1)).save(medicalRecordToAdd);

    }

    @Test
    public void testAddMedicalRecordExistingRecord() {

        MedicalRecord medicalRecordToAdd = new MedicalRecord();
        medicalRecordToAdd.setFirstName("Bob");
        medicalRecordToAdd.setLastName("Bober");
        medicalRecordToAdd.setBirthdate(bobBirthDate);
        medicalRecordToAdd.setAllergies(allergiesBob);
        medicalRecordToAdd.setMedications(medicationsBob);

        when(medicalRecordRepository.getByFirstNameAndLastName(anyString(), anyString())).thenReturn(medicalRecordToAdd);

        assertThrows(IllegalArgumentException.class, () -> {
            medicalRecordService.addMedicalRecord(medicalRecordToAdd);
        });

        verify(medicalRecordRepository, times(1)).getByFirstNameAndLastName("Bob", "Bober");
        verify(medicalRecordRepository, times(0)).save(medicalRecordToAdd);

    }

    @Test
    public void testDeleteMedicalRecord() {

        MedicalRecord medicalRecordToDelete = new MedicalRecord();
        medicalRecordToDelete.setFirstName("Bob");
        medicalRecordToDelete.setLastName("Bober");
        medicalRecordToDelete.setBirthdate(bobBirthDate);
        medicalRecordToDelete.setAllergies(allergiesBob);
        medicalRecordToDelete.setMedications(medicationsBob);

        when(medicalRecordRepository.findByFirstNameAndLastName(anyString(), anyString())).thenReturn(medicalRecordToDelete);

        medicalRecordService.deleteMedicalRecord(medicalRecordToDelete);

        verify(medicalRecordRepository, times(1)).deleteByFirstNameAndLastName("Bob", "Bober");

    }

    @Test
    public void testUpdateMedicalRecordFound() {
        MedicalRecord medicalRecordToUpdate = new MedicalRecord();
        medicalRecordToUpdate.setFirstName("Bob");
        medicalRecordToUpdate.setLastName("Bober");
        medicalRecordToUpdate.setBirthdate(bobBirthDate);
        medicalRecordToUpdate.setAllergies(allergiesBob);
        medicalRecordToUpdate.setMedications(medicationsBob);

        when(medicalRecordRepository.findByFirstNameAndLastName(anyString(), anyString())).thenReturn(medicalRecordToUpdate);

        medicalRecordToUpdate.setBirthdate(bobBirthDate2);
        medicalRecordToUpdate.setAllergies(allergiesBob2);
        medicalRecordToUpdate.setMedications(medicationsBob2);

        medicalRecordService.updateMedicalRecord(medicalRecordToUpdate);

        verify(medicalRecordRepository, times(1)).findByFirstNameAndLastName("Bob", "Bober");
        verify(medicalRecordRepository, times(1)).save(medicalRecordToUpdate);
    }

    @Test
    public void testUpdateMedicalRecordNotFound() {
        MedicalRecord medicalRecordToUpdate = new MedicalRecord();
        medicalRecordToUpdate.setFirstName("Bob");
        medicalRecordToUpdate.setLastName("Bober");
        medicalRecordToUpdate.setBirthdate(bobBirthDate);
        medicalRecordToUpdate.setAllergies(allergiesBob);
        medicalRecordToUpdate.setMedications(medicationsBob);

        when(medicalRecordRepository.findByFirstNameAndLastName(anyString(), anyString())).thenReturn(null);

        medicalRecordToUpdate.setBirthdate(bobBirthDate2);
        medicalRecordToUpdate.setAllergies(allergiesBob2);
        medicalRecordToUpdate.setMedications(medicationsBob2);

        assertThrows(NullPointerException.class, () -> {
            medicalRecordService.updateMedicalRecord(medicalRecordToUpdate);
        });

        verify(medicalRecordRepository, times(1)).findByFirstNameAndLastName("Bob", "Bober");
        verify(medicalRecordRepository, times(0)).save(medicalRecordToUpdate);

    }

    @Test
    public void testCountMajorsForMedicalRecords() {

        LocalDate now = LocalDate.now();
        LocalDate majorityDate = now.minusYears(18);

        medicalRecords = List.of(
                new MedicalRecord(1L, "Bob", "Smith", bobBirthDate, medicationsBob, allergiesBob),
                new MedicalRecord(2L, "Bob", "Ross", bobBirthDate2, medicationsBob2, allergiesBob2),
                new MedicalRecord(3L, "Bob", "Dylan", bobBirthDate3, medicationsBob3, allergiesBob3)
                );

        when(medicalRecordRepository.countMajorsInList(majorityDate, medicalRecords)).thenReturn(2);

        medicalRecordService.countMajorsForMedicalRecords(medicalRecords);

        verify(medicalRecordRepository, times(1)).countMajorsInList(majorityDate, medicalRecords);

    }
    @Test
    public void testCountMinorsForMedicalRecords() {

        LocalDate now = LocalDate.now();
        LocalDate majorityDate = now.minusYears(18);

        medicalRecords = List.of(
                new MedicalRecord(1L, "Bob", "Smith", bobBirthDate, medicationsBob, allergiesBob),
                new MedicalRecord(2L, "Bob", "Ross", bobBirthDate2, medicationsBob2, allergiesBob2),
                new MedicalRecord(3L, "Bob", "Dylan", bobBirthDate3, medicationsBob3, allergiesBob3)
        );

        when(medicalRecordRepository.countMinorsInList(majorityDate, medicalRecords)).thenReturn(1);

        medicalRecordService.countMinorsForMedicalRecords(medicalRecords);

        verify(medicalRecordRepository, times(1)).countMinorsInList(majorityDate, medicalRecords);

    }


}

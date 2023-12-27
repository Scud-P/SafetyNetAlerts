package com.oc.safetynet.alertsapi;

import com.oc.safetynet.alertsapi.model.MedicalRecord;
import com.oc.safetynet.alertsapi.model.parameter.MedicalRecordParameter;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MedicalRecordParameterTest {

    @Test
    public void testDefaultConstructorAndSetters() {

        List<String> allergies = List.of("allergy1", "allergy2");
        List<String> medications = List.of("medication1", "medication2");

        MedicalRecordParameter medicalRecordParameter = new MedicalRecordParameter();
        medicalRecordParameter.setFirstName("bob");
        medicalRecordParameter.setLastName("dylan");
        medicalRecordParameter.setBirthdate("01/01/2001");
        medicalRecordParameter.setAllergies(allergies);
        medicalRecordParameter.setMedications(medications);

        assertNotNull(medicalRecordParameter);
        assertEquals("bob", medicalRecordParameter.getFirstName());
        assertEquals("dylan", medicalRecordParameter.getLastName());
        assertEquals("01/01/2001", medicalRecordParameter.getBirthdate());
        assertEquals(allergies, medicalRecordParameter.getAllergies());
        assertEquals(medications, medicalRecordParameter.getMedications());

    }

    @Test
    public void testFullConstructor() {

        List<String> allergies = List.of("allergy1", "allergy2");
        List<String> medications = List.of("medication1", "medication2");

        MedicalRecordParameter medicalRecordParameter = new MedicalRecordParameter("bob", "dylan", "01/01/2001", medications, allergies);

        assertNotNull(medicalRecordParameter);
        assertEquals("bob", medicalRecordParameter.getFirstName());
        assertEquals("dylan", medicalRecordParameter.getLastName());
        assertEquals("01/01/2001", medicalRecordParameter.getBirthdate());
        assertEquals(allergies, medicalRecordParameter.getAllergies());
        assertEquals(medications, medicalRecordParameter.getMedications());
    }

    @Test
    public void testToMedicalRecord() {
        List<String> allergies = List.of("allergy1", "allergy2");
        List<String> medications = List.of("medication1", "medication2");

        MedicalRecordParameter medicalRecordParameter = new MedicalRecordParameter("bob", "dylan", "1990-01-01", medications, allergies);

        MedicalRecord medicalRecord = medicalRecordParameter.toMedicalRecord();

        System.out.println(medicalRecord.getBirthdate());


        assertNotNull(medicalRecord);
        assertEquals("bob", medicalRecord.getFirstName());
        assertEquals("dylan", medicalRecord.getLastName());
        assertEquals("1990-01-01", medicalRecord.getBirthdate().toString());
        assertEquals(allergies, medicalRecord.getAllergies());
        assertEquals(medications, medicalRecord.getMedications());
    }
}

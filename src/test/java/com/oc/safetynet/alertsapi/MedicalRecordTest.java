package com.oc.safetynet.alertsapi;
import static org.junit.jupiter.api.Assertions.*;

import com.oc.safetynet.alertsapi.model.MedicalRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MedicalRecordTest {

    List<String> allergiesBob, medicationsBob, allergiesBob2, medicationsBob2;
    LocalDate bobBirthDate, bobBirthDate2;


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

        LocalDate now = LocalDate.now();
        bobBirthDate = now.minusYears(20);
        bobBirthDate2 = now.minusYears(25);
    }


    @Test
    public void testEquals() {

        MedicalRecord medicalRecord = new  MedicalRecord(1L, "Bob", "Bober", bobBirthDate, medicationsBob, allergiesBob);
        assertEquals(medicalRecord, medicalRecord);

        MedicalRecord sameMedicalRecord = new  MedicalRecord(1L, "Bob", "Bober", bobBirthDate, medicationsBob, allergiesBob);
        assertEquals(medicalRecord, sameMedicalRecord);

        MedicalRecord medicalRecord2 =  new MedicalRecord(2L, "Bob2", "Bober2", bobBirthDate2, medicationsBob2, allergiesBob2);
        assertNotEquals(medicalRecord2, medicalRecord);
    }

    @Test
    public void testHashCodes() {
        MedicalRecord medicalRecord = new  MedicalRecord(1L, "Bob", "Bober", bobBirthDate, medicationsBob, allergiesBob);
        MedicalRecord sameMedicalRecord = new  MedicalRecord(1L, "Bob", "Bober", bobBirthDate, medicationsBob, allergiesBob);
        MedicalRecord medicalRecord2 =  new MedicalRecord(2L, "Bob2", "Bober2", bobBirthDate2, medicationsBob2, allergiesBob2);

        assertEquals(medicalRecord.hashCode(), medicalRecord.hashCode());
        assertEquals(medicalRecord.hashCode(), sameMedicalRecord.hashCode());
        assertNotEquals(medicalRecord.hashCode(), medicalRecord2.hashCode());
    }
}

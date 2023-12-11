package com.oc.safetynet.alertsapi.repository;

import com.oc.safetynet.alertsapi.model.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MedicalRecordRepository extends JpaRepository <MedicalRecord, Long> {
    @Query("SELECT m.birthdate FROM MedicalRecord m WHERE m.firstName = :firstName AND m.lastName = :lastName")
    List<LocalDate> findBirthdateByFirstNameAndLastName(@Param("firstName") String firstName, @Param("lastName") String lastName);

    List<MedicalRecord> findByBirthdateAfter(LocalDate date);

}

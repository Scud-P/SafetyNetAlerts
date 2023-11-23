package com.oc.safetynet.alertsapi.repository;

import com.oc.safetynet.alertsapi.model.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicalRecordRepository extends JpaRepository <MedicalRecord, Long> {

}

package com.oc.safetynet.alertsapi.repository;

import com.oc.safetynet.alertsapi.model.FireStation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FireStationRepository extends JpaRepository<FireStation, Long> {

    List<FireStation> findByStation(int station);

}

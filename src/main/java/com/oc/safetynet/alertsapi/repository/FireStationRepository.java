package com.oc.safetynet.alertsapi.repository;

import com.oc.safetynet.alertsapi.model.FireStation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FireStationRepository extends JpaRepository<FireStation, Long> {

    List<FireStation> findByStation(int station);
    @Query("SELECT f.address FROM FireStation f WHERE f.station = :station")
    List<String> findAddressesByStation(@Param("station") int station);

    @Query("SELECT f.station FROM FireStation f WHERE f.address = :address")
    int findStationNumberByAddress(String address);
}

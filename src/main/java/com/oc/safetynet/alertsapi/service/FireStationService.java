package com.oc.safetynet.alertsapi.service;

import com.oc.safetynet.alertsapi.model.FireStation;
import com.oc.safetynet.alertsapi.repository.FireStationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FireStationService {
    @Autowired
    private FireStationRepository fireStationRepository;

    public List<FireStation> getAllFireStations() {
        return fireStationRepository.findAll();
    }

    public FireStation saveFireStation(FireStation firestation) {
        return fireStationRepository.save(firestation);
    }

    public List<FireStation> saveAllFireStations(List<FireStation> firestations) {
        return fireStationRepository.saveAll(firestations);
    }
}

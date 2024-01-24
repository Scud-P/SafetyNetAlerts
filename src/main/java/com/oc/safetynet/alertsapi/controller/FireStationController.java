package com.oc.safetynet.alertsapi.controller;

import com.oc.safetynet.alertsapi.model.FireStation;
import com.oc.safetynet.alertsapi.repository.FireStationRepoImpl;
import com.oc.safetynet.alertsapi.service.FireStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.annotation.JsonView;


import java.util.List;

@RestController
public class FireStationController {

    @Autowired
    FireStationService fireStationService;

    @Autowired
    FireStationRepoImpl fireStationRepoImpl;

    @GetMapping("/firestations")
    public List<FireStation> getAllFireStations() {
        return fireStationService.getAllFireStations();
    }

    @PostMapping("/firestation")
    public void addFireStation(@RequestBody FireStation fireStation) {
        fireStationRepoImpl.addFireStationToList(fireStation);
    }

    @PutMapping("/firestation")
    public void updateFireStation(@RequestBody FireStation fireStation) {
        fireStationRepoImpl.updateFireStationNumber(fireStation);
    }

    @DeleteMapping("/firestation")
    public void deleteFireStation(@RequestBody FireStation fireStation) {
        if (fireStation.getStation() != 0) {
            fireStationRepoImpl.deleteFireStationByNumber(fireStation.getStation());
        } else if (fireStation.getAddress() != null) {
            fireStationRepoImpl.deleteFireStationByAddress(fireStation.getAddress());
        } else {
            throw new IllegalArgumentException("Either station number or address must be provided to delete a mapping");
        }
    }

    @PostMapping("/batch")
    public List<FireStation> addAllFireStations(@RequestBody List<FireStation> fireStations) {
        return fireStationService.saveAllFireStations(fireStations);
    }

}

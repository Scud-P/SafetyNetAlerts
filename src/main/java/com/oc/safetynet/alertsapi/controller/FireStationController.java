package com.oc.safetynet.alertsapi.controller;

import com.oc.safetynet.alertsapi.model.FireStation;
import com.oc.safetynet.alertsapi.repository.FireStationRepoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class FireStationController {

    @Autowired
    FireStationRepoImpl fireStationRepoImpl;

    @PostMapping("/firestation")
    public FireStation addFireStation(@RequestBody FireStation fireStation) {
        return fireStationRepoImpl.addFireStationToList(fireStation);
    }

    @PutMapping("/firestation")
    public FireStation updateFireStation(@RequestBody FireStation fireStation) {
        return fireStationRepoImpl.updateFireStationNumber(fireStation);
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
}

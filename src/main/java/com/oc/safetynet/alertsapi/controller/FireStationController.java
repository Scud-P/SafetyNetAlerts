package com.oc.safetynet.alertsapi.controller;

import com.oc.safetynet.alertsapi.model.FireStation;
import com.oc.safetynet.alertsapi.service.FireStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stations")
public class FireStationController {

    @Autowired
    FireStationService fireStationService;

    @GetMapping
    public List<FireStation> getAllFireStations() {
        return fireStationService.getAllFireStations();
    }

    @PostMapping
    public FireStation addFireStation(@RequestBody FireStation fireStation) {
        return fireStationService.saveFireStation(fireStation);
    }

    @PostMapping("/batch")
    public List<FireStation> addAllFireStations(@RequestBody List<FireStation> fireStations) {
        return fireStationService.saveAllFireStations(fireStations);
    }
//
//    @PostMapping("/populate")
//    public void populateFireStationTable() {
//        fireStationService.populateFireStationTable();
//    }

}

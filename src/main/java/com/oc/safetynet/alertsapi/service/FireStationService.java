package com.oc.safetynet.alertsapi.service;

import com.oc.safetynet.alertsapi.model.FireStation;
import com.oc.safetynet.alertsapi.repository.FireStationRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FireStationService {
    @Autowired
    private FireStationRepository fireStationRepository;

    @Autowired JsonDataService jsonDataService;

    public List<FireStation> getAllFireStations() {
        return fireStationRepository.findAll();
    }

    public FireStation saveFireStation(FireStation firestation) {
        return fireStationRepository.save(firestation);
    }

    public List<FireStation> saveAllFireStations(List<FireStation> fireStations) {
        return fireStationRepository.saveAll(fireStations);
    }

    public void populateFireStationTable() {
        JSONObject data = jsonDataService.findDataArrays();
        if(data != null) {
            JSONArray fireStationsArray = data.getJSONArray("firestations");
            List<FireStation> fireStations = new ArrayList<>();
            for(int i = 0; i < fireStationsArray.length(); i++) {
                JSONObject fireStationJson = fireStationsArray.getJSONObject(i);
                FireStation fireStation = new FireStation();
                fireStation.setStation(fireStationJson.getInt("station"));
                fireStation.setAddress(fireStationJson.getString("address"));
                fireStations.add(fireStation);
            }
            fireStationRepository.saveAll(fireStations);
        }
    }
}

package com.oc.safetynet.alertsapi.repository;

import com.oc.safetynet.alertsapi.model.FireStation;

import java.util.List;

public interface FireStationRepo {
    List<FireStation> getAllFireStations();

    FireStation addFireStationToList(FireStation fireStation);

    void deleteFireStationByAddress(String address);

    void deleteFireStationByNumber(int station);

    FireStation updateFireStationNumber(FireStation fireStation);

    List<String> findAddressesByStation(int station);

    int findStationByAddress(String address);
}

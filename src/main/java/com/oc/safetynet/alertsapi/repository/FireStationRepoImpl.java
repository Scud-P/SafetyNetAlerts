package com.oc.safetynet.alertsapi.repository;

import com.oc.safetynet.alertsapi.model.Data;
import com.oc.safetynet.alertsapi.model.FireStation;
import com.oc.safetynet.alertsapi.service.JsonReaderService;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class FireStationRepoImpl implements FireStationRepo {

    private static final Logger logger = LoggerFactory.getLogger(FireStationRepoImpl.class);

    private final Data data;

    private List<FireStation> fireStations = new ArrayList<>();


    @Autowired
    public FireStationRepoImpl(JsonReaderService jsonReaderService, Data data) {
        this.data = jsonReaderService.readData();
    }

    @PostConstruct
    private void loadAllFireStations() {
        fireStations = data.getFirestations();
        if (fireStations != null) {
            logger.info("Loaded List of FireStations from data {}.", fireStations);
        }
    }


    @Override
    public List<FireStation> getAllFireStations() {
        return fireStations;
    }

    @Override
    public FireStation addFireStationToList(FireStation fireStation) {
        boolean isDuplicateAddress = fireStations.stream()
                .anyMatch(existingStation -> existingStation.getAddress().equals(fireStation.getAddress()));

        if (isDuplicateAddress) {
            logger.error("FireStation with address {} already in DB", fireStation.getAddress());
        } else {
            fireStations.add(fireStation);
            logger.info("FireStation added: {}", fireStation);
            logger.info("New List of FireStations: {}", fireStations);
            return fireStation;
        }
        return null;
    }

    @Override
    public void deleteFireStationByNumber(int station) {
        Optional<FireStation> removedFireStation = fireStations.stream()
                .filter(fireStation -> fireStation.getStation() == station)
                .findFirst();

        if (removedFireStation.isPresent()) {
            removedFireStation.ifPresent(fireStation -> logger.info("Firestation removed for address: {} and station number: {}",
                    fireStation.getAddress(), fireStation.getStation()));
            fireStations.removeIf(fireStation -> fireStation.getStation() == station);
            logger.info("New List of Firestations: {}", fireStations);
        } else {
            logger.error("No Firestation was found for station {}", station);
        }
    }

    @Override
    public void deleteFireStationByAddress(String address) {
        Optional<FireStation> removedFireStation = fireStations.stream()
                .filter(fireStation -> fireStation.getAddress().equals(address))
                .findFirst();

        if (removedFireStation.isPresent()) {
            removedFireStation.ifPresent(fireStation -> logger.info("Firestation removed for address: {} and station number: {}",
                    fireStation.getAddress(), fireStation.getStation()));
            fireStations.removeIf(fireStation -> fireStation.getAddress().equals(address));
            logger.info("New List of Firestations: {}", fireStations);
        } else {
            logger.error("No Firestation was found for address: {}", address);
        }
    }

    @Override
    public FireStation updateFireStationNumber(FireStation fireStation) {
        Optional<FireStation> foundFireStation = fireStations.stream()
                .filter(currentFireStation -> currentFireStation.getAddress().equals(fireStation.getAddress()))
                .findFirst();

        if (foundFireStation.isPresent()) {
            foundFireStation.get().setStation(fireStation.getStation());
            logger.info("FireStation mapping modified: {}", fireStation);
            logger.info("New List of FireStations: {}", fireStations);
            return fireStation;
        } else {
            logger.error("No Firestation was found for address: {}", fireStation.getAddress());
        }
        return null;
    }

    @Override
    public List<String> findAddressesByStation(int station) {
        return fireStations.stream()
                .filter(fireStation -> fireStation.getStation() == station)
                .map(FireStation::getAddress).toList();
    }

    @Override
    public int findStationByAddress(String address) {
        Optional<FireStation> fire = fireStations.stream()
                .filter(fireStation -> fireStation.getAddress().equalsIgnoreCase(address))
                .findFirst();
        return fire.map(FireStation::getStation).orElse(0);
    }
}

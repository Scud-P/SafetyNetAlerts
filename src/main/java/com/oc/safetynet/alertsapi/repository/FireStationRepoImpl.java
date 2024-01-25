package com.oc.safetynet.alertsapi.repository;

import com.oc.safetynet.alertsapi.model.Data;
import com.oc.safetynet.alertsapi.model.FireStation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class FireStationRepoImpl implements FireStationRepo {

    private static final Logger logger = LoggerFactory.getLogger(FireStationRepoImpl.class);

    private final DataRepository dataRepository;


    @Autowired
    public FireStationRepoImpl(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }


    @Override
    public List<FireStation> getAllFireStations() {
        try {
            Data data = dataRepository.readData();
            return data.getFirestations();
        } catch (IOException e) {
            throw new RuntimeException("Failed to read data from the repository", e);
        }
    }

    @Override
    public FireStation addFireStationToList(FireStation fireStation) {
        try {
            Data data = dataRepository.readData();
            List<FireStation> fireStations = data.getFirestations();

            boolean isDuplicateAddress = fireStations.stream()
                    .anyMatch(existingStation -> existingStation.getAddress().equals(fireStation.getAddress()));

            if (isDuplicateAddress) {
                logger.error("FireStation with address {} already in DB", fireStation.getAddress());
            } else {
                fireStations.add(fireStation);
                data.setFireStations(fireStations);
                logger.info("FireStation added: {}", fireStation);
                logger.info("New List of FireStations: {}", data.getFirestations());
                dataRepository.writeData(data);
                return fireStation;
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read data from the repository", e);
        }
        return null;
    }


    @Override
    public void deleteFireStationByNumber(int station) {
        try {
            Data data = dataRepository.readData();
            List<FireStation> fireStations = data.getFirestations();
            Optional<FireStation> removedFireStation = fireStations.stream()
                    .filter(fireStation -> fireStation.getStation() == station)
                    .findFirst();

            if(removedFireStation.isPresent()) {
                removedFireStation.ifPresent(fireStation -> logger.info("Firestation removed for address: {} and station number: {}",
                        fireStation.getAddress(), fireStation.getStation()));
                fireStations.removeIf(fireStation -> fireStation.getStation() == station);
                data.setFireStations(fireStations);
                logger.info("New List of Firestations: {}", data.getFirestations());
                dataRepository.writeData(data);
            } else {
                logger.error("No Firestation was found for station {}", station);
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to read data from the repository", e);
        }
    }

    @Override
    public void deleteFireStationByAddress(String address) {
        try {
            Data data = dataRepository.readData();
            List<FireStation> fireStations = data.getFirestations();
            Optional<FireStation> removedFireStation = fireStations.stream()
                    .filter(fireStation -> fireStation.getAddress().equals(address))
                    .findFirst();

            if(removedFireStation.isPresent()) {
                removedFireStation.ifPresent(fireStation -> logger.info("Firestation removed for address: {} and station number: {}",
                        fireStation.getAddress(), fireStation.getStation()));
                fireStations.removeIf(fireStation -> fireStation.getAddress().equals(address));
                data.setFireStations(fireStations);
                logger.info("New List of Firestations: {}", data.getFirestations());
                dataRepository.writeData(data);
            } else {
                logger.error("No Firestation was found for address {}", address);
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to read data from the repository", e);
        }
    }

    @Override
    public FireStation updateFireStationNumber(FireStation fireStation) {

        System.out.println(fireStation);
        try {
            Data data = dataRepository.readData();
            List<FireStation> currentFireStations = data.getFirestations();
            currentFireStations.stream()
                    .filter(currentFireStation -> currentFireStation.getAddress().equals(fireStation.getAddress()))
                    .findFirst()
                    .ifPresent(foundFireStation -> {
                        foundFireStation.setStation(fireStation.getStation());
                    });
            data.setFireStations(currentFireStations);
            logger.info("FireStation mapping modified: {}", fireStation);
            logger.info("New List of FireStations: {}", data.getFirestations());

            dataRepository.writeData(data);
            return fireStation;

        } catch (IOException e) {
            throw new RuntimeException("Failed to read data from the repository", e);
        }
    }

    @Override
    public List<String> findAddressesByStation(int station) {
        try {
            Data data = dataRepository.readData();

            return data.getFirestations().stream()
                    .filter(fireStation -> fireStation.getStation() == station)
                    .map(FireStation::getAddress).toList();

        } catch (IOException e) {
            throw new RuntimeException("Failed to read data from the repository", e);
        }
    }
}

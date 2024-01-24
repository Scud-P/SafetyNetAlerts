package com.oc.safetynet.alertsapi.service;

import com.oc.safetynet.alertsapi.exception.FireStationNotFoundException;
import com.oc.safetynet.alertsapi.exception.PersonNotFoundException;
import com.oc.safetynet.alertsapi.model.FireStation;
import com.oc.safetynet.alertsapi.repository.FireStationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FireStationService {

    private static final Logger logger = LoggerFactory.getLogger(FireStationService.class);

    private FireStationRepository fireStationRepository;

    public List<FireStation> getAllFireStations() {
        return fireStationRepository.findAll();
    }

    @Transactional
    public void deleteFireStation(FireStation fireStation) {

        if(fireStation == null) {
            logger.error("Invalid input: FireStation is null");
            throw new IllegalArgumentException("Invalid input: FireStation is null");
        }

        FireStation fireStationToDelete = fireStationRepository.findByAddress(fireStation.getAddress());

        if(fireStationToDelete == null) {
            logger.error("No mapping was found for FireStation number {} and address {}", fireStation.getStation(), fireStation.getAddress());
            throw new FireStationNotFoundException(
                    String.format("No Mapping found for FireStation number %s and address %s",
                            fireStation.getStation(), fireStation.getAddress()));
        }
            int station = fireStation.getStation();
            String address = fireStation.getAddress();
            logger.info("Mapping to delete, Fire Station number: {}, Covering address: {}", station, address);
            fireStationRepository.deleteByAddressAndStation(fireStation.getAddress(), fireStation.getStation());
            logger.info("Mapping deleted");
    }

    public FireStation addFireStation(FireStation fireStation) {
        fireStation.setStation(fireStation.getStation());
        fireStation.setAddress(fireStation.getAddress());
        logger.info("Fire Station added: {}", fireStation);
        return fireStationRepository.save(fireStation);
    }

    public FireStation updateFireStation(FireStation fireStation) {

        if (fireStation == null) {
            logger.error("Invalid input: FireStation is null");
            throw new IllegalArgumentException("Invalid input: FireStation is null");
        }

        FireStation fireStationToUpdate = fireStationRepository.findByAddress(fireStation.getAddress());

        if(fireStationToUpdate == null) {

            logger.error("No FireStation found for address: {}", fireStation.getAddress());
            throw new FireStationNotFoundException(
                    String.format(" No FireStation found for address: %s",
                            fireStation.getAddress()));
        }
            logger.info("Fire Station before update: {}", fireStationToUpdate);
            fireStationToUpdate.setStation(fireStation.getStation());
            logger.info("Fire Station after update: {}", fireStationToUpdate);
            return fireStationRepository.save(fireStationToUpdate);
    }

    public List<FireStation> saveAllFireStations(List<FireStation> firestations) {
        return fireStationRepository.saveAll(firestations);
    }

}

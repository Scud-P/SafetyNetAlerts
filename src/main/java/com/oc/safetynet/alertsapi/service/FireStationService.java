package com.oc.safetynet.alertsapi.service;

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

    private static final Logger logger = LoggerFactory.getLogger(PersonService.class);

    @Autowired
    private FireStationRepository fireStationRepository;

    public List<FireStation> getAllFireStations() {
        return fireStationRepository.findAll();
    }

    @Transactional
    public void deleteFireStation(FireStation fireStation) {
        if (fireStation != null) {
            int station = fireStation.getStation();
            String address = fireStation.getAddress();
            logger.info("Mapping to delete, Fire Station number: {}, Covering address: {}", station, address);
            fireStationRepository.deleteByAddressAndStation(fireStation.getAddress(), fireStation.getStation());
            logger.info("Mapping deleted");
        } else { logger.warn("Mapping wasn't found.");}
    }

    public FireStation addFireStation(FireStation fireStation) {
        if (fireStation.getId() != 0) {
            throw new IllegalArgumentException("ID is automatically incremented");}
        fireStation.setStation(fireStation.getStation());
        fireStation.setAddress(fireStation.getAddress());
        logger.info("Fire Station added: {}", fireStation);
        return fireStationRepository.save(fireStation);
    }

    public FireStation updateFireStation(FireStation fireStation) {
        FireStation fireStationToUpdate = fireStationRepository.findByAddress(fireStation.getAddress());
        if(fireStationToUpdate != null) {
            logger.info("Fire Station before update: {}", fireStationToUpdate);
            fireStationToUpdate.setStation(fireStation.getStation());
            logger.info("Fire Station after update: {}", fireStationToUpdate);
            return fireStationRepository.save(fireStationToUpdate);
        } else {
            logger.warn("No Fire Station mapping was found for this address");
            return null;
        }
    }

    public List<FireStation> saveAllFireStations(List<FireStation> firestations) {
        return fireStationRepository.saveAll(firestations);
    }

}

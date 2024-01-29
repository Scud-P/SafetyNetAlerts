package com.oc.safetynet.alertsapi;

import com.oc.safetynet.alertsapi.model.Data;
import com.oc.safetynet.alertsapi.model.FireStation;
import com.oc.safetynet.alertsapi.repository.DataRepository;
import com.oc.safetynet.alertsapi.repository.FireStationRepoImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class FireStationRepoImplTest {

    @Mock
    private DataRepository dataRepository;

    @InjectMocks
    private FireStationRepoImpl fireStationRepo;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getAllFireStations_ShouldReturnListOfFireStations() throws IOException {
        List<FireStation> mockFireStations = new ArrayList<>();
        when(dataRepository.readData()).thenReturn(new Data());
        List<FireStation> result = fireStationRepo.getAllFireStations();
        assertEquals(mockFireStations, result);
    }

    @Test
    public void addFireStationToList_ShouldAddPersonToList() {
        FireStation fireStation = new FireStation("test address", 8);

        fireStationRepo.addFireStationToList(fireStation);

        List<FireStation> updatedFirestations = fireStationRepo.getAllFireStations();

        assertEquals(1, updatedFirestations.size());
        assertEquals(fireStation, updatedFirestations.get(0));
    }

    @Test
    public void updateFireStation_shouldUpdateAFireStation() {

        FireStation fireStation = new FireStation("test address", 8);
        FireStation fireStation2 = new FireStation("test address2", 9);

        fireStationRepo.addFireStationToList(fireStation);
        fireStationRepo.addFireStationToList(fireStation2);


        FireStation fireStation3 = new FireStation("test address", 10);

        fireStationRepo.updateFireStationNumber(fireStation3);

        List<FireStation> updatedFireStations = fireStationRepo.getAllFireStations();

        assertEquals(2, updatedFireStations.size());
        assertEquals(10, updatedFireStations.get(0).getStation());
    }

    @Test
    public void deleteFireStationByNumberTest() {

        FireStation fireStation = new FireStation("test address", 8);
        FireStation fireStation2 = new FireStation("test address2", 9);

        fireStationRepo.addFireStationToList(fireStation);
        fireStationRepo.addFireStationToList(fireStation2);

        fireStationRepo.deleteFireStationByNumber(8);

        List<FireStation> updatedFireStations = fireStationRepo.getAllFireStations();

        assertEquals(1, updatedFireStations.size());
        assertEquals(9, updatedFireStations.get(0).getStation());
    }

    @Test
    public void deleteFireStationByAddressTest() {

        FireStation fireStation = new FireStation("test address", 8);
        FireStation fireStation2 = new FireStation("test address2", 9);

        fireStationRepo.addFireStationToList(fireStation);
        fireStationRepo.addFireStationToList(fireStation2);

        fireStationRepo.deleteFireStationByAddress("test address2");

        List<FireStation> updatedFireStations = fireStationRepo.getAllFireStations();


        assertEquals(1, updatedFireStations.size());
        assertEquals(8, updatedFireStations.get(0).getStation());
    }

    @Test
    public void findAddressesByStationTest() {
        FireStation fireStation = new FireStation("test address", 8);
        FireStation fireStation2 = new FireStation("test address2", 9);
        FireStation fireStation3 = new FireStation("test address3", 9);
        FireStation fireStation4 = new FireStation("test address4", 9);

        fireStationRepo.addFireStationToList(fireStation);
        fireStationRepo.addFireStationToList(fireStation2);
        fireStationRepo.addFireStationToList(fireStation3);
        fireStationRepo.addFireStationToList(fireStation4);

        List<String> result = fireStationRepo.findAddressesByStation(9);

        assertEquals(3, result.size());
    }

    @Test
    public void findStationByAddress_shouldReturnThisFireStationNumber() {

        FireStation fireStation = new FireStation("test address", 8);
        FireStation fireStation2 = new FireStation("test address2", 9);

        fireStationRepo.addFireStationToList(fireStation);
        fireStationRepo.addFireStationToList(fireStation2);

        int result = fireStationRepo.findStationByAddress("test address");

        assertEquals(8, result);
    }

}

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
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    public void getAllFireStations_IOExceptionThrown_ShouldThrowRuntimeException() throws IOException {
        when(dataRepository.readData()).thenThrow(new IOException("Mocked IOException"));
        assertThrows(RuntimeException.class, () -> fireStationRepo.getAllFireStations());
    }

    @Test
    public void addPersonToList_ShouldAddPersonToList() throws IOException {

        FireStation fireStation = new FireStation("test address", 8);

        Data mockData = new Data();

        when(dataRepository.readData()).thenReturn(mockData);

        fireStationRepo.addFireStationToList(fireStation);

        assertEquals(1, mockData.getFirestations().size());
        assertEquals(fireStation, mockData.getFirestations().get(0));

        verify(dataRepository, times(1)).writeData(mockData);
    }

    @Test
    public void updateFireStation_shouldUpdateAFireStation() throws IOException {

        FireStation fireStation = new FireStation("test address", 8);
        FireStation fireStation2 = new FireStation("test address2", 9);

        List<FireStation> fireStations = new ArrayList<>();
        fireStations.add(fireStation);
        fireStations.add(fireStation2);

        Data mockData = new Data();
        mockData.setFireStations(fireStations);

        when(dataRepository.readData()).thenReturn(mockData);

        FireStation fireStation3 = new FireStation("test address", 10);

        fireStationRepo.updateFireStationNumber(fireStation3);

        assertEquals(2, mockData.getFirestations().size());
        assertEquals(10, mockData.getFirestations().get(0).getStation());
    }

    @Test
    public void deleteFireStationByNumberTest() throws IOException {

        FireStation fireStation = new FireStation("test address", 8);
        FireStation fireStation2 = new FireStation("test address2", 9);

        List<FireStation> fireStations = new ArrayList<>();
        fireStations.add(fireStation);
        fireStations.add(fireStation2);

        Data mockData = new Data();
        mockData.setFireStations(fireStations);

        when(dataRepository.readData()).thenReturn(mockData);

        fireStationRepo.deleteFireStationByNumber(8);

        assertEquals(1, mockData.getFirestations().size());
        assertEquals(9, mockData.getFirestations().get(0).getStation());
    }

    @Test
    public void deleteFireStationByAddressTest() throws IOException {

        FireStation fireStation = new FireStation("test address", 8);
        FireStation fireStation2 = new FireStation("test address2", 9);

        List<FireStation> fireStations = new ArrayList<>();
        fireStations.add(fireStation);
        fireStations.add(fireStation2);

        Data mockData = new Data();
        mockData.setFireStations(fireStations);

        when(dataRepository.readData()).thenReturn(mockData);

        fireStationRepo.deleteFireStationByAddress("test address2");

        assertEquals(1, mockData.getFirestations().size());
        assertEquals(8, mockData.getFirestations().get(0).getStation());
    }

    @Test
    public void findAddressesByStationTest() throws IOException {
        FireStation fireStation = new FireStation("test address", 8);
        FireStation fireStation2 = new FireStation("test address2", 9);
        FireStation fireStation3 = new FireStation("test address3", 9);
        FireStation fireStation4 = new FireStation("test address4", 9);

        List<FireStation> fireStations = new ArrayList<>();
        fireStations.add(fireStation);
        fireStations.add(fireStation2);
        fireStations.add(fireStation3);
        fireStations.add(fireStation4);

        Data mockData = new Data();
        mockData.setFireStations(fireStations);

        when(dataRepository.readData()).thenReturn(mockData);

        List<String> result = fireStationRepo.findAddressesByStation(9);

        assertEquals(3, result.size());
    }

    @Test
    public void findStationByAddress_shouldReturnThisFireStationNumber() throws IOException {

        FireStation fireStation = new FireStation("test address", 8);
        FireStation fireStation2 = new FireStation("test address2", 9);

        List<FireStation> fireStations = new ArrayList<>();
        fireStations.add(fireStation);
        fireStations.add(fireStation2);

        Data mockData = new Data();
        mockData.setFireStations(fireStations);

        when(dataRepository.readData()).thenReturn(mockData);

        int result = fireStationRepo.findStationByAddress("test address");

        assertEquals(8, result);
    }

}

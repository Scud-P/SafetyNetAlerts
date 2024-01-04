package com.oc.safetynet.alertsapi;

import com.oc.safetynet.alertsapi.exception.FireStationNotFoundException;
import com.oc.safetynet.alertsapi.model.FireStation;
import com.oc.safetynet.alertsapi.repository.FireStationRepository;
import com.oc.safetynet.alertsapi.service.FireStationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class FireStationServiceTest {

    @MockBean
    private FireStationRepository fireStationRepository;

    @Autowired
    private FireStationService fireStationService;

    @Test
    public void testAddFireStation() {

        FireStation fireStationToAdd = new FireStation();
        fireStationToAdd.setStation(1);
        fireStationToAdd.setAddress("test address");

        fireStationService.addFireStation(fireStationToAdd);

        verify(fireStationRepository, times(1)).save(fireStationToAdd);

    }

    @Test
    public void testUpdateFireStationFound() {

        FireStation fireStationToUpdate = new FireStation();
        fireStationToUpdate.setStation(1);
        fireStationToUpdate.setAddress("test address");

        when(fireStationRepository.findByAddress(anyString())).thenReturn(fireStationToUpdate);
        fireStationToUpdate.setStation(3);

        fireStationService.updateFireStation(fireStationToUpdate);

        verify(fireStationRepository,times(1)).findByAddress("test address");
        verify(fireStationRepository, times(1)).save(fireStationToUpdate);
    }

    @Test
    public void testUpdateFireStationNotFound() {

        FireStation fireStationToUpdate = new FireStation();
        fireStationToUpdate.setStation(1);
        fireStationToUpdate.setAddress("test address");

        when(fireStationRepository.findByAddress("test address")).thenReturn(null);
        fireStationToUpdate.setStation(3);

        assertThrows(FireStationNotFoundException.class, () ->
                fireStationService.updateFireStation(fireStationToUpdate));

        verify(fireStationRepository, times(1)).findByAddress("test address");
    }

    @Test
    public void testDeleteFireStation() {

        FireStation fireStationToDelete = new FireStation();
        fireStationToDelete.setStation(1);
        fireStationToDelete.setAddress("test address");

        when(fireStationRepository.findByAddress(anyString())).thenReturn(fireStationToDelete);

        fireStationService.deleteFireStation(fireStationToDelete);

        verify(fireStationRepository, times(1)).deleteByAddressAndStation("test address", 1);
    }

    @Test
    public void testDeleteFireStationNotFound() {

        FireStation fireStationToDelete = new FireStation();
        fireStationToDelete.setStation(1);
        fireStationToDelete.setAddress("test address");

        when(fireStationRepository.findByAddress("test address")).thenReturn(null);

        assertThrows(FireStationNotFoundException.class, () ->
                        fireStationService.deleteFireStation(fireStationToDelete));

        verify(fireStationRepository, times(0)).deleteByAddressAndStation("test address", 1);

    }

    @Test
    public void testGetAllFireStations() {

        List<FireStation> stations = List.of(
                new FireStation(1L, "address1", 1),
                new FireStation(2L, "address2", 2),
                new FireStation(3L, "address3", 3)
        );

        when(fireStationRepository.findAll()).thenReturn(stations);

        List<FireStation> results = fireStationService.getAllFireStations();

        assertNotNull(results);
        assertThat(results).hasSize(3);

        verify(fireStationRepository, times(1)).findAll();

    }

}

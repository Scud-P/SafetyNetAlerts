package com.oc.safetynet.alertsapi;

import com.oc.safetynet.alertsapi.model.FireStation;
import com.oc.safetynet.alertsapi.repository.FireStationRepository;
import com.oc.safetynet.alertsapi.service.FireStationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

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

        when(fireStationRepository.findByAddress(anyString())).thenReturn(null);
        fireStationToUpdate.setStation(3);

        fireStationService.updateFireStation(fireStationToUpdate);

        verify(fireStationRepository,times(1)).findByAddress("test address");
        verify(fireStationRepository, times(0)).save(fireStationToUpdate);
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

}

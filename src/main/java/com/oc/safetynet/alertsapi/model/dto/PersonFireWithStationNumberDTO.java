package com.oc.safetynet.alertsapi.model.dto;

import java.util.List;

public class PersonFireWithStationNumberDTO {

    public PersonFireWithStationNumberDTO(int station, List<PersonFireDTO> personFireDTOs) {
        this.station = station;
        this.personFireDTOs = personFireDTOs;
    }

    public PersonFireWithStationNumberDTO() {
    }

    public List<PersonFireDTO> getPersonFireDTOs() {
        return personFireDTOs;
    }

    public void setPersonFireDTOs(List<PersonFireDTO> personFireDTOs) {
        this.personFireDTOs = personFireDTOs;
    }

    public int getStation() {
        return station;
    }

    public void setStation(int station) {
        this.station = station;
    }

    private int station;
    private List<PersonFireDTO> personFireDTOs;

}

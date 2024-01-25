package com.oc.safetynet.alertsapi.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;


@Entity
@ToString
public class FireStation {

    private String address;
    private int station;

    public FireStation( String address, int station) {
        this.address = address;
        this.station = station;
    }
    public FireStation() {
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getStation() {
        return station;
    }

    public void setStation(int station) {
        this.station = station;
    }

}

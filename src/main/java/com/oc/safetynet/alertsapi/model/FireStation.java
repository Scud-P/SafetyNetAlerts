package com.oc.safetynet.alertsapi.model;

import jakarta.persistence.*;
import lombok.Data;


@Entity

public class FireStation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private long id;
    private String address;
    private int station;

    public FireStation(long id, String address, int station) {
        this.id = id;
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

}

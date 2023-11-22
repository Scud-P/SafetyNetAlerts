package com.oc.safetynet.alertsapi.model;

public class FireStation {

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

    private String address;
    private int station;

}

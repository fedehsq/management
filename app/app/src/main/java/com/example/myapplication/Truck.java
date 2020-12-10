package com.example.myapplication;

import androidx.room.Ignore;

public class Truck {
    // "marca" of truck
    private String truckBrand;
    // "targa" of truck
    private String truckLicensePlate;
    // "tara" of truck
    private int truckTare;
    // "lordo" of truck
    private int truckGross;
    // "portata" of truck
    private int truckReach;

    // current liters
    private int currentLiters;

    @Ignore
    public Truck() {}

    public Truck(String truckBrand, String truckLicensePlate, int truckTare, int truckGross,
                 int truckReach, int currentLiters) {
        this.truckBrand = truckBrand;
        this.truckLicensePlate = truckLicensePlate;
        this.truckTare = truckTare;
        this.truckGross = truckGross;
        this.truckReach = truckReach;
        this.currentLiters = currentLiters;
    }

    public String getTruckBrand() {
        return truckBrand;
    }

    public void setTruckBrand(String truckBrand) {
        this.truckBrand = truckBrand;
    }

    public String getTruckLicensePlate() {
        return truckLicensePlate;
    }

    public void setTruckLicensePlate(String truckLicensePlate) {
        this.truckLicensePlate = truckLicensePlate;
    }

    public int getTruckTare() {
        return truckTare;
    }

    public void setTruckTare(int truckTare) {
        this.truckTare = truckTare;
    }

    public int getTruckGross() {
        return truckGross;
    }

    public void setTruckGross(int truckGross) {
        this.truckGross = truckGross;
    }

    public int getTruckReach() {
        return truckReach;
    }

    public void setTruckReach(int truckReach) {
        this.truckReach = truckReach;
    }


    public int getCurrentLiters() {
        return currentLiters;
    }

    public void setCurrentLiters(int currentLiters) {
        this.currentLiters = currentLiters;
    }

}

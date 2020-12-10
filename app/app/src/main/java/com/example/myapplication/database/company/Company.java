package com.example.myapplication.database.company;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.myapplication.Truck;

import java.util.ArrayList;

/**
 * Company with its trucks
 */
@Entity
public class Company {

    // global identifier
    @PrimaryKey(autoGenerate = true)
    public int uid;

    // company name
    @ColumnInfo(name = "company_name")
    public String companyName;

    // company's trucks
    @ColumnInfo(name = "company_trucks")
    @TypeConverters(TrucksTypeConverter.class)
    public ArrayList<Truck> trucks;

    @Ignore
    public Company() {
    }

    @Ignore
    public Company(String companyName) {
        this.companyName = companyName;
        this.trucks = new ArrayList<>();
    }

    public Company(String companyName, ArrayList<Truck> trucks) {
        this.companyName = companyName;
        this.trucks = trucks;
    }



    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public ArrayList<Truck> getTrucks() {
        return trucks;
    }

    /**
     * Get all names of trucks
     * @return names as List<String>
     */
    public ArrayList<String> getTrucksBrands() {
        ArrayList<String> names = new ArrayList<>();
        for (Truck truck : trucks) {
            names.add(truck.getTruckBrand());
        }
        return names;
    }

    public void setTrucks(ArrayList<Truck> trucks) {
        this.trucks = trucks;
    }
}
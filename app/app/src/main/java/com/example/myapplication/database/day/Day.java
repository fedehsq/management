package com.example.myapplication.database.day;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.myapplication.Truck;

/***
 * Class representing filled form in InsertDataActivity: represents the return to the factory
 */

@Entity
public class Day {
    // global identifier
    @PrimaryKey(autoGenerate = true)
    public int uid;

    // date of return to the factory
    @ColumnInfo(name = "date")
    public String date;

    // name of owner of the truck
    @ColumnInfo(name = "company_name")
    public String company;

    // truck chosen
    @Embedded
    public Truck truck;

    // quantity of gas in arrive
    @ColumnInfo(name = "quantity")
    public int quantityArrived;

    // value representing the quantity of gas to recharge
    @ColumnInfo(name = "to_recharge")
    public int toRecharge;

    // remaining of.. ?
    @ColumnInfo(name = "remaining_stock")
    public int remainingStock;

    // quantity of liters in arrive
    @ColumnInfo(name = "liters_arrived")
    public int litersArrived;

    // value representing the liters total
    @ColumnInfo(name = "total_liters")
    public int totalLiters;


    @Ignore
    public Day() {}

    public Day(String date, String company, Truck truck, int quantityArrived,
               int toRecharge, int remainingStock, int litersArrived, int totalLiters) {
        this.date = date;
        this.company = company;
        this.truck = truck;
        this.quantityArrived = quantityArrived;
        this.toRecharge = toRecharge;
        this.remainingStock = remainingStock;
        this.litersArrived = litersArrived;
        this.totalLiters = totalLiters;
    }

    @Override
    public String toString() {
        return "Day{" +
                "uid=" + uid +
                ", date='" + date + '\'' +
                ", company='" + company + '\'' +
                ", truck=" + truck +
                ", quantityArrived=" + quantityArrived +
                ", toRecharge=" + toRecharge +
                ", remainingStock=" + remainingStock +
                ", litersArrived=" + litersArrived +
                ", totalLiters=" + totalLiters +
                '}';
    }
}

package com.example.myapplication.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.myapplication.database.company.Company;
import com.example.myapplication.database.company.CompanyDao;
import com.example.myapplication.database.day.Day;
import com.example.myapplication.database.day.DayDao;
import com.example.myapplication.Truck;

@Database(entities = {Day.class, Company.class}, exportSchema = false, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    public abstract DayDao dayDao();
    public abstract CompanyDao companyDao();
}
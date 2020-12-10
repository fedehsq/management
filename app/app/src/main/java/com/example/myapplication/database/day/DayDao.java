package com.example.myapplication.database.day;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface DayDao {

    @Query("SELECT COUNT(*) FROM DAY")
    int getCount();

    @Query("SELECT * FROM day")
    List<Day> getAll();

    @Query("SELECT * FROM day WHERE uid IN (:dayIds)")
    List<Day> loadAllByIds(int[] dayIds);

    @Query("SELECT * FROM day WHERE date LIKE :date")
    Day findByDate(String date);

    @Query("SELECT * FROM day WHERE date LIKE :uid")
    Day findByUid(int uid);


    @Insert
    void insert(Day... days);

    @Insert
    void insertAll(ArrayList<Day> days);

    @Delete
    void delete(Day day);

    @Delete
    void deleteAll(ArrayList<Day> days);
}

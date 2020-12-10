package com.example.myapplication.database.company;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CompanyDao {
    @Query("SELECT * FROM company")
    List<Company> getAll();

    @Query("SELECT * FROM company WHERE uid IN (:companIDs)")
    List<Company> loadAllByIds(int[] companIDs);

    @Query("SELECT * FROM company WHERE company_name LIKE :name")
    Company findByName(String name);

    @Insert
    void insertAll(Company... companies);

    @Delete
    void delete(Company company);
}
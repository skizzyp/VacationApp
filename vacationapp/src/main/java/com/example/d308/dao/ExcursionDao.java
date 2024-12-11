package com.example.d308.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.d308.entity.Excursion;

import java.util.List;

@Dao
public interface ExcursionDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Excursion excursion);

    @Delete
    void delete(Excursion excursion);

    @Update
    void update(Excursion excursion);

    @Query("Select * FROM excursions ORDER by excursionID ASC")
    List<Excursion> getAllExcursions();



}

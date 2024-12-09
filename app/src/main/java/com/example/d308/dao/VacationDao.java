package com.example.d308.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.d308.entity.Vacation;

import java.util.List;

@Dao
public interface VacationDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Vacation vacation);

    @Delete
    void delete(Vacation vacation);

    @Update
    void update(Vacation vacation);

    @Query("Select * FROM vacations order by vacationID ASC")
    List<Vacation> getAllVacations();


}

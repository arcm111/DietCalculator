package com.arcm.dietcalculator.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.arcm.dietcalculator.DietDate;

import java.util.List;

@Dao
public interface ExerciseEntryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ExerciseEntry entry);

    @Update
    void update(ExerciseEntry entry);

    @Delete
    void delete(ExerciseEntry entry);

    @Query("SELECT * FROM table_exercise WHERE date=:date")
    LiveData<List<ExerciseEntry>> getItemsByDate(DietDate date);
}

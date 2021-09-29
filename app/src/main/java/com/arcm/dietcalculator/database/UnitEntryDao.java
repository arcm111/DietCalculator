package com.arcm.dietcalculator.database;

import androidx.room.Dao;
import androidx.room.Insert;

@Dao
public interface UnitEntryDao {
    @Insert
    long insert(UnitEntry unit);
}

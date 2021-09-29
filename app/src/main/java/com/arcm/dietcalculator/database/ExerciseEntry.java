package com.arcm.dietcalculator.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.arcm.dietcalculator.DietDate;

@Entity(tableName = "table_exercise")
public class ExerciseEntry {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public DietDate date;
    public String label;
    public int caloriesBurnt;

    public ExerciseEntry(DietDate date, String label, int caloriesBurnt) {
        this.date = date;
        this.label = label;
        this.caloriesBurnt = caloriesBurnt;
    }
}

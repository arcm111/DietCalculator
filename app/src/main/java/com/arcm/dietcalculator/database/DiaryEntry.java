package com.arcm.dietcalculator.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.arcm.dietcalculator.DietDate;

@Entity(tableName = "table_diary")
public class DiaryEntry {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public DietDate date;
    public DiaryItem.MealType mealType;
    public long foodId;
    public long unitId;
    public double unitsCount;


    public DiaryEntry(long foodId, DietDate date, DiaryItem.MealType mealType) {
        this.foodId = foodId;
        this.date = date;
        this.mealType = mealType;
    }
}

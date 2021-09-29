package com.arcm.dietcalculator.database;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "table_food_items")
public class FoodEntry {
    @PrimaryKey
    public long id;

    public String foodName;

    @NonNull
    public String brand = "None";

    @NonNull
    public FoodItem.Group group = FoodItem.Group.OTHER;

    @Embedded
    public Nutrients nutrients;
}

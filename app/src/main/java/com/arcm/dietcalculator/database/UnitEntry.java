package com.arcm.dietcalculator.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "table_units")
public class UnitEntry {

    @PrimaryKey(autoGenerate = true)
    public long id;

    public long foodId = -1;
    public long recipeId = -1;

    public String unitTitle;
    public double unitSize; // in grams

    public UnitEntry(long foodId, String unitTitle, double unitSize) {
        this.foodId = foodId;
        this.unitTitle = unitTitle;
        this.unitSize = unitSize;
    }

    @Ignore
    public UnitEntry(String unitTitle, double unitSize) {
        this.unitTitle = unitTitle;
        this.unitSize = unitSize;
    }

    public void setRecipeId(long recipeId) {
        this.recipeId = recipeId;
    }

    @NonNull
    @Override
    public String toString() {
        return unitTitle + "(" + unitSize + " g)";
    }
}

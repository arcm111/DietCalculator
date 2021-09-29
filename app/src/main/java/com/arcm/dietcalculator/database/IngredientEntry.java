package com.arcm.dietcalculator.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "table_recipe_ingredients")
public class IngredientEntry {
    @PrimaryKey(autoGenerate = true)
    public long id;

    public long foodId;
    public long recipeId;
    public long unitId;
    public float weight;
}

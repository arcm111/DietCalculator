package com.arcm.dietcalculator.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RecipeEntryDao {
    @Query("SELECT * FROM table_recipes WHERE id=:id LIMIT 1")
    RecipeEntry getItemById(int id);

    @Query("SELECT * FROM table_recipes ORDER BY name DESC")
    LiveData<List<RecipeEntry>> getRecipes();
}

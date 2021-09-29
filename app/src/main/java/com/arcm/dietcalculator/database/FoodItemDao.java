package com.arcm.dietcalculator.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public abstract class FoodItemDao {
    @Transaction
    @Query("SELECT * FROM table_food_items WHERE id = :id LIMIT 1")
    abstract FoodItem getFoodItemById(long id);

    @Transaction
    @Query("SELECT * FROM table_food_items")
    abstract LiveData<List<FoodItem>> getAllFoodItems();

    @Insert
    abstract void insertUnits(List<UnitEntry> unitEntries);

    @Insert
    abstract void insertFoodEntry(FoodEntry entry);

    public long insert(FoodItem foodItem) {
        if (foodItem.getId() == 0) foodItem.generateUniqueId();
        long id = foodItem.getId();
        for (UnitEntry unit : foodItem.getUnits()) {
            unit.foodId = id;
        }
        insertUnits(foodItem.getUnits());
        insertFoodEntry(foodItem.getFoodEntry());
        return id;
    }

    public void delete(FoodItem foodItem) {

    }
}

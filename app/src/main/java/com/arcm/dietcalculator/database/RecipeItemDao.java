package com.arcm.dietcalculator.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.ArrayList;
import java.util.List;

@Dao
public abstract class RecipeItemDao {
    @Transaction
    @Query("SELECT * FROM table_recipes")
    abstract LiveData<List<RecipeItem>> getRecipes();

    @Transaction
    @Query("SELECT * FROM table_recipes WHERE id = :id")
    abstract RecipeItem getItemById(long id);

    @Insert
    abstract void insertIngredients(List<IngredientEntry> ingredients);

    @Insert
    abstract void insertUnits(List<UnitEntry> units);

    private void insertIngredientsItems(List<IngredientItem> ingredientItems) {
        List<IngredientEntry> entries = new ArrayList<>();
        for (IngredientItem item : ingredientItems) {
            entries.add(item.getIngredientEntry());
        }
        insertIngredients(entries);
    }

    @Insert
    abstract void insertRecipeEntry(RecipeEntry recipeEntry);

    public void insertRecipe(RecipeItem item) {
        long recipeId = item.generateUniqueId();
        for (IngredientItem ingredient : item.getIngredients()) {
            ingredient.setRecipeId(recipeId);
        }
        for (UnitEntry unit : item.getUnits()) {
            unit.setRecipeId(recipeId);
        }
        insertIngredientsItems(item.getIngredients());
        insertUnits(item.getUnits());
        insertRecipeEntry(item.getRecipeEntry());
    }
}

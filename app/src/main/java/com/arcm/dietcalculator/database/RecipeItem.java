package com.arcm.dietcalculator.database;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Relation;

import com.arcm.dietcalculator.Utilities;

import java.util.ArrayList;
import java.util.List;

public class RecipeItem {
    @Embedded
    private RecipeEntry recipeEntry;

    @Relation(parentColumn = "id", entityColumn = "recipeId")
    private List<UnitEntry> units;
    
    @Relation(parentColumn = "id", entityColumn = "recipeId", entity = IngredientEntry.class)
    private List<IngredientItem> ingredients;

    public RecipeItem() {
        this.recipeEntry = new RecipeEntry();
        this.units = new ArrayList<>();
    }

    public long getId() {
        return recipeEntry.id;
    }

    public String getName() {
        return recipeEntry.name;
    }

    public String getAuthor() {
        return recipeEntry.author;
    }
    
    public void updateNutrients() {
        float totalWeight = 0;
        Nutrients recipeNutrients = new Nutrients();
        for (IngredientItem ing : ingredients) {
            totalWeight += ing.getWeight();
            recipeNutrients.cals += ing.getCalories();
            recipeNutrients.carbs += ing.getCarbohydrates();
            recipeNutrients.sugar += ing.getSugar();
            recipeNutrients.fibre += ing.getFibre();
            recipeNutrients.fat += ing.getFat();
            recipeNutrients.saturatedFat += ing.getSaturatedFat();
            recipeNutrients.protein += ing.getProtein();
        }
        float factor = 100.0f / totalWeight;
        recipeNutrients.cals *= factor;
        recipeNutrients.carbs *= factor;
        recipeNutrients.sugar *= factor;
        recipeNutrients.fibre *= factor;
        recipeNutrients.fat *= factor;
        recipeNutrients.saturatedFat *= factor;
        recipeNutrients.protein *= factor;
        recipeEntry.nutrients = recipeNutrients;
    }

    public Nutrients getNutrients() {
        return recipeEntry.nutrients;
    }

    public List<UnitEntry> getUnits() {
        return units;
    }

    public RecipeEntry getRecipeEntry() {
        return recipeEntry;
    }

    public void setName(String name) {
        recipeEntry.name = name;
    }

    public void setAuthor(String author) {
        recipeEntry.author = author;
    }

    public List<IngredientItem> getIngredients() {
        return ingredients;
    }

    public int getCalories() {
        return (int) Math.round(recipeEntry.nutrients.cals);
    }

    public void setIngredients(List<IngredientItem> ingredients) {
        this.ingredients = ingredients;
        updateNutrients();
    }

    public void setUnits(List<UnitEntry> units) {
        this.units = units;
    }

    public void setRecipeEntry(RecipeEntry recipeEntry) {
        this.recipeEntry = recipeEntry;
    }
    
    public long generateUniqueId() {
        StringBuilder builder = new StringBuilder();
        builder.append(getName());
        if (!getAuthor().equals(RecipeEntry.DEFAULT_AUTHOR)) {
            builder.append(getAuthor());
        }
        for (IngredientItem ingredient : getIngredients()) {
            builder.append(ingredient.getFoodId());
        }
        builder.append(recipeEntry.nutrients.cals);
        builder.append(recipeEntry.nutrients.carbs);
        builder.append(recipeEntry.nutrients.sugar);
        builder.append(recipeEntry.nutrients.fibre);
        builder.append(recipeEntry.nutrients.fat);
        builder.append(recipeEntry.nutrients.saturatedFat);
        builder.append(recipeEntry.nutrients.protein);
        builder.append(getName());
        return Utilities.hash(builder.toString());
    }

    @NonNull
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("id: ").append(getId()).append("\n");
        builder.append("name: ").append(getName()).append("\n");
        builder.append("author: ").append(getAuthor()).append("\n");
        builder.append("nutrients: ").append(getNutrients().toString()).append("\n");
        builder.append("units: ").append("\n");
        for (UnitEntry unit: units) {
            builder.append(unit.toString()).append("\n");
        }
        return builder.toString();
    }
}

package com.arcm.dietcalculator.database;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Ignore;
import androidx.room.Relation;

import com.arcm.dietcalculator.Utilities;

public class IngredientItem {
    @Embedded
    private IngredientEntry ingredientEntry;

    @Relation(parentColumn = "foodId", entityColumn = "id", entity = FoodEntry.class)
    private FoodItem foodItem;
    
    @Relation(parentColumn = "unitId", entityColumn = "id")
    private UnitEntry unit;

    public IngredientItem() {
        this.ingredientEntry = new IngredientEntry();
        this.foodItem = new FoodItem();
    }

    @Ignore
    public IngredientItem(FoodItem foodItem) {
        this.ingredientEntry = new IngredientEntry();
        this.foodItem = foodItem;
        this.unit = new UnitEntry(-1, "g", 1);
        ingredientEntry.foodId = foodItem.getId();
    }

    public long getId() {
        return ingredientEntry.id;
    }

    public long getFoodId() {
        return foodItem.getId();
    }

    public String getName() {
        return foodItem.getFoodName();
    }

    public String getBrand() {
        return foodItem.getBrand();
    }
    
    public float getWeight() {
        return ingredientEntry.weight;
    }

    public int getCalories() {
        return (int) (getWeight() / 100.0 * foodItem.getCalories());
    }

    public double getCarbohydrates() {
        return getWeight() / 100.0f * foodItem.getCarbohydrates();
    }

    public double getSugar() {
        return getWeight() / 100.0f * foodItem.getSugar();
    }

    public double getFibre() {
        return getWeight() / 100.0f * foodItem.getFibre();
    }

    public double getFat() {
        return getWeight() / 100.0f * foodItem.getFat();
    }

    public double getSaturatedFat() {
        return getWeight() / 100.0f * foodItem.getSaturatedFat();
    }

    public double getProtein() {
        return getWeight() / 100.0f * foodItem.getProtein();
    }

    public String getCaloriesAsText() {
        return Utilities.formatCalories(getCalories());
    }

    public String getWeightAsText() {
        return Utilities.formatNumber(getWeight()) + unit.unitTitle;
    }

    public IngredientEntry getIngredientEntry() {
        return ingredientEntry;
    }

    public UnitEntry getUnit() {
        return unit;
    }

    public FoodItem.Group getGroup() {
        return foodItem.getGroup();
    }

    public void setUnit(UnitEntry unit) {
        this.unit = unit;
        ingredientEntry.unitId = unit.id;
    }

    public void setWeight(float weight) {
        ingredientEntry.weight = weight;
    }

    public void setRecipeId(long recipeId) {
        ingredientEntry.recipeId = recipeId;
    }

    public void setIngredientEntry(IngredientEntry ingredientEntry) {
        this.ingredientEntry = ingredientEntry;
    }

    public void setFoodItem(FoodItem foodEntry) {
        this.foodItem = foodEntry;
        ingredientEntry.foodId = foodEntry.getId();
    }

    @NonNull
    @Override
    public String toString() {
        return getName() + "[" + getId() + "] => " + getWeight() + " " + unit.toString();
    }
}

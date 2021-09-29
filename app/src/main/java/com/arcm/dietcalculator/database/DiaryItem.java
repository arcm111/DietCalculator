package com.arcm.dietcalculator.database;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Ignore;
import androidx.room.Relation;

import com.arcm.dietcalculator.DietDate;
import com.arcm.dietcalculator.R;

import java.util.Locale;

public class DiaryItem {
    public static final int CARBS_CALS_VALUE = 4;
    public static final int PROTEIN_CALS_VALUE = 4;
    public static final int FAT_CALS_VALUE = 9;
    
    @Embedded
    public DiaryEntry diaryEntry;
    
    @Relation(parentColumn = "unitId", entityColumn = "id")
    public UnitEntry unitEntry;

    @Relation(parentColumn = "foodId", entityColumn = "id", entity = FoodEntry.class)
    public FoodItem foodItem;

    public DiaryItem() {
    }

    @Ignore
    public DiaryItem(FoodItem foodEntry, DietDate date, MealType mealType) {
        this.diaryEntry = new DiaryEntry(foodEntry.getId(), date, mealType);
        this.foodItem = foodEntry;
        this.unitEntry = null;
    }

    public String getName() {
        return foodItem.getFoodName();
    }

    public long getId() {
        return foodItem.getId();
    }

    public double getCals() {
        return getWeight() / 100.0 * foodItem.getCalories();
    }

    public double getCarbs() {
        return getWeight() / 100.0 * foodItem.getCarbohydrates();
    }

    public double getProtein() {
        return getWeight() / 100.0 * foodItem.getProtein();
    }

    public double getFat() {
        return getWeight() / 100.0 * foodItem.getFat();
    }

    public double getSaturatedFat() {
        return getWeight() / 100.0 * foodItem.getSaturatedFat();
    }

    public double getSugar() {
        return getWeight() / 100.0 * foodItem.getSugar();
    }

    public double getFibre() {
        return getWeight() / 100.0 * foodItem.getFibre();
    }

    public double getValueByType(Nutrients.Type type) {
        switch (type) {
            case CARBOHYDRATES:
                return getCarbs();
            case PROTEIN:
                return getProtein();
            case FAT:
                return getFat();
            case SATURATED_FAT:
                return getSaturatedFat();
            case SUGAR:
                return getSugar();
            case FIBRE:
                return getFibre();
        }
        return 0;
    }

    public String getValueByTypeAsText(Nutrients.Type type) {
        return (int) Math.round(getValueByType(type)) + " g";
    }

    public int getWeight() {
        if (unitEntry == null) {
            return (int) diaryEntry.unitsCount;
        }
        return (int) (diaryEntry.unitsCount * unitEntry.unitSize);
    }

    public Nutrients getNutrients() {
        return foodItem.getNutrients();
    }

    public String getBrand() {
        return foodItem.getBrand();
    }

    public FoodItem.Group getGroup() {
        return foodItem.getGroup();
    }

    public MealType getMealType() {
        return diaryEntry.mealType;
    }

    public String getUnit() {
        if (unitEntry == null) return "g";
        return unitEntry.unitTitle;
    }

    public double getServings() {
        return diaryEntry.unitsCount;
    }

    public String getCalsAsText() {
        return String.format(Locale.getDefault(), "%d kcal", (int) getCals());
    }

    public String getCarbsAsText() {
        return String.format(Locale.getDefault(), "%.2f g", getCarbs());
    }

    public String getProteinAsText() {
        return String.format(Locale.getDefault(), "%.2f g", getProtein());
    }

    public String getFatAsText() {
        return String.format(Locale.getDefault(), "%.2f g", getFat());
    }

    public String getSaturatedFatAsText() {
        return String.format(Locale.getDefault(), "%.2f g", getSaturatedFat());
    }

    public String getSugarAsText() {
        return String.format(Locale.getDefault(), "%.2f g", getSugar());
    }

    public String getFibreAsText() {
        return String.format(Locale.getDefault(), "%.2f g", getFibre());
    }

    public String getWeightAsText() {
        return getWeight() + " g";
    }

    public double getCalsPer100g() {
        return foodItem.getCalories();
    }

    public double getCarbsPer100g() {
        return foodItem.getCarbohydrates();
    }

    public double getProteinPer100g() {
        return foodItem.getProtein();
    }

    public double getFatPer100g() {
        return foodItem.getFat();
    }

    public double getSaturatedFatPer100g() {
        return foodItem.getSaturatedFat();
    }

    public double getSugarPer100g() {
        return foodItem.getSugar();
    }

    public double getFibrePer100g() {
        return foodItem.getFibre();
    }

    public String getCalsPer100gAsText() {
        return foodItem.getCalsAsText();
    }

    public String getCarbsPer100gAsText() {
        return foodItem.getCarbsAsText();
    }

    public String getProteinPer100gAsText() {
        return foodItem.getProteinAsText();
    }

    public String getFatPer100gAsText() {
        return foodItem.getFatAsText();
    }

    public String getSaturatedFatPer100gAsText() {
        return foodItem.getSaturatedFatAsText();
    }

    public String getFiberPer100gAsText() {
        return foodItem.getFibreAsText();
    }

    public String getSugarPer100gAsText() {
        return foodItem.getSugarAsText();
    }

    public int getCarbsPercentage() {
        return (int) (((foodItem.getCarbohydrates() * CARBS_CALS_VALUE) / foodItem.getCalories()) * 100);
    }

    public int getProteinPercentage() {
        return (int) (((foodItem.getProtein() * PROTEIN_CALS_VALUE) / foodItem.getCalories()) * 100);
    }

    public int getFatPercentage() {
        return (int) (((foodItem.getFat() * FAT_CALS_VALUE) / foodItem.getCalories()) * 100);
    }

    public int getSaturatedFatPercentage() {
        return (int) (((foodItem.getSaturatedFat() * FAT_CALS_VALUE) / foodItem.getCalories()) * 100);
    }

    public int getSugarPercentage() {
        return (int) (((foodItem.getSugar() * CARBS_CALS_VALUE) / foodItem.getCalories()) * 100);
    }

    public int getFibrePercentage() {
        return (int) ((foodItem.getFibre() / getWeight()) * 100);
    }

    public void setId(int id) {
        foodItem.setId(id);
    }

    public void setName(String name) {
        foodItem.setFoodName(name);
    }

    public void setBrand(String brand) {
        if (brand == null) {
            foodItem.setBrand(FoodItem.DEFAULT_BRAND);
        } else foodItem.setBrand(brand);
    }

    public void setGroup(FoodItem.Group group) {
        foodItem.setGroup(group);
    }

    public void setMealType(MealType mealType) {
        diaryEntry.mealType = mealType;
    }

    public void setCals(double cals) {
        foodItem.setCalories(cals);
    }
    public void setCarbs(double carbs) {
        foodItem.setCarbohydrates(carbs);
    }

    public void setProtein(double protein) {
        foodItem.setProtein(protein);
    }

    public void setFat(double fat) {
        foodItem.setFat(fat);
    }

    public void setSaturatedFat(double saturatedFat) {
        foodItem.setSaturatedFat(saturatedFat);
    }

    public void setSugar(double sugar) {
        foodItem.setSugar(sugar);
    }

    public void setFibre(double fibre) {
        foodItem.setFibre(fibre);
    }

    public void setUnitsCount(double count) {
        diaryEntry.unitsCount = count;
    }

    public void setDate(DietDate date) {
        diaryEntry.date = date;
    }

    public static @DrawableRes
    int getGroupIconResource(FoodItem.Group g) {
        switch (g) {
            case VEGETABLES:
                return R.drawable.ic_food_item_vegetables;
            case FRUITS:
                return R.drawable.ic_food_item_fruits;
            case MEAT:
                return R.drawable.ic_food_item_meat;
            case NUTS:
                return R.drawable.ic_food_item_nuts;
            case LEGUMES:
                return R.drawable.ic_food_item_legumes;
            case PULSES:
                return R.drawable.ic_food_item_pulses;
            case DAIRY:
                return R.drawable.ic_food_item_dairy;
            case BAKERY:
                return R.drawable.ic_food_item_bakery;
            case DRINKS:
                return R.drawable.ic_food_item_drinks;
            case SNACKS:
                return R.drawable.ic_food_item_snacks;
            case SUPPLEMENTS:
                return R.drawable.ic_food_item_supplements;
            case COOKING:
                return R.drawable.ic_food_item_cooking;
            case FASTFOOD:
                return R.drawable.ic_food_item_fast_food;
            case GRAINS:
                return R.drawable.ic_food_item_grains;
            case DESERTS:
                return R.drawable.ic_food_item_deserts;
        }
        return R.drawable.ic_food_item_others;
    }

    public @DrawableRes int getGroupIconResource() {
        return getGroupIconResource(foodItem.getGroup());
    }

    public enum MealType {
        BREAKFAST,
        LUNCH,
        DINNER,
        EXERCISE
    }

    @NonNull
    @Override
    public String toString() {
        StringBuilder builder;
        builder = new StringBuilder(foodItem.getFoodName());
        builder.append("(").append(diaryEntry.id).append(")=>");
        builder.append(foodItem.getCalories()).append("kcal:");
        builder.append(foodItem.getCarbohydrates()).append("g:");
        builder.append(foodItem.getProtein()).append("g:");
        builder.append(foodItem.getFat()).append("g:");
        return builder.toString();
    }
}

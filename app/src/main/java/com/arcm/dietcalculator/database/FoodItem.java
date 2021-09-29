package com.arcm.dietcalculator.database;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Ignore;
import androidx.room.Relation;

import com.arcm.dietcalculator.Utilities;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class FoodItem {
    public static final String DEFAULT_BRAND = "Unknown";

    @Embedded
    private FoodEntry foodEntry;

    @Relation(parentColumn = "id", entityColumn = "foodId")
    public List<UnitEntry> units;

    @Ignore
    public String fdcId;

    public FoodItem() {
        this.foodEntry = new FoodEntry();
        this.units = new ArrayList<>();
        units.add(new UnitEntry("g", 1));
        foodEntry.nutrients = new Nutrients();
    }

    public FoodItem(String foodName, Nutrients nutrients) {
        this.foodEntry = new FoodEntry();
        this.units = new ArrayList<>();
        foodEntry.foodName = foodName;
        foodEntry.nutrients = nutrients;
        units.add(new UnitEntry("g", 1));
    }

    public enum Group {
        VEGETABLES,
        FRUITS,
        MEAT,
        NUTS,
        LEGUMES,
        PULSES,
        DAIRY,
        BAKERY,
        DRINKS,
        SNACKS,
        SUPPLEMENTS,
        COOKING,
        FASTFOOD,
        GRAINS,
        DESERTS,
        OTHER
    }

    public static String getGroupName(Group group) {
        switch (group) {
            case VEGETABLES:
                return "Vegetables";
            case FRUITS:
                return "Fruits";
            case MEAT:
                return "Meat";
            case NUTS:
                return "Nuts";
            case LEGUMES:
                return "Legumes";
            case PULSES:
                return "Pulses";
            case DAIRY:
                return "Dairy";
            case BAKERY:
                return "Bakery";
            case DRINKS:
                return "Drinks";
            case SNACKS:
                return "Snacks";
            case SUPPLEMENTS:
                return "Supplements";
            case COOKING:
                return "Cooking";
            case FASTFOOD:
                return "Fast Food";
            case GRAINS:
                return "Grains";
            case DESERTS:
                return "Deserts";
        }
        return "Other";
    }

    public void generateUniqueId() {
        foodEntry.id = hash();
    }

    public long getId() {
        return foodEntry.id;
    }

    public String getFoodName() {
        return foodEntry.foodName;
    }

    public String getBrand() {
        return foodEntry.brand;
    }

    public Group getGroup() {
        return foodEntry.group;
    }

    public Nutrients getNutrients() {
        return foodEntry.nutrients;
    }
    
    public double getCalories() {
        return foodEntry.nutrients.cals;
    }
    
    public double getCarbohydrates() {
        return foodEntry.nutrients.carbs;
    }
    
    public double getSugar() {
        return foodEntry.nutrients.sugar;
    }
    
    public double getFibre() {
        return foodEntry.nutrients.fibre;
    }
    
    public double getFat() {
        return foodEntry.nutrients.fat;
    }
    
    public double getSaturatedFat() {
        return foodEntry.nutrients.saturatedFat;
    }
    
    public double getProtein() {
        return foodEntry.nutrients.protein;
    }
    
	public String getCalsAsText() {
		return foodEntry.nutrients.getCalsAsText();
	}

	public String getCarbsAsText() {
		return foodEntry.nutrients.getCarbsAsText();
	}

	public String getProteinAsText() {
		return foodEntry.nutrients.getProteinAsText();
	}

	public String getFatAsText() {
		return foodEntry.nutrients.getFatAsText();
	}

	public String getSaturatedFatAsText() {
		return foodEntry.nutrients.getSaturatedFatAsText();
	}

	public String getFibreAsText() {
		return foodEntry.nutrients.getFibreAsText();
	}

	public String getSugarAsText() {
		return foodEntry.nutrients.getSugarAsText();
	}

	public List<UnitEntry> getUnits() {
        return units;
    }

    public void setId(long id) {
        foodEntry.id = id;
    }
    
    public void setFdcId(String fdcId) {
        this.fdcId = fdcId;
    }
    
    public void setFoodName(String foodName) {
        foodEntry.foodName = foodName;
    }
    
    public void setBrand(String brand) {
        foodEntry.brand = brand;
    }
    
    public void setGroup(Group group) {
        foodEntry.group = group;
    }
    
    public void setNutrients(Nutrients nutrients) {
        foodEntry.nutrients = nutrients;
    }
    
    public void setCalories(double value) {
        foodEntry.nutrients.cals = value;
    }
    
    public void setCarbohydrates(double value) {
        foodEntry.nutrients.carbs = value;
    }
    
    public void setSugar(double value) {
        foodEntry.nutrients.sugar = value;
    }
    
    public void setFibre(double value) {
        foodEntry.nutrients.fibre = value;
    }
    
    public void setFat(double value) {
        foodEntry.nutrients.fat = value;
    }
    
    public void setSaturatedFat(double value) {
        foodEntry.nutrients.saturatedFat = value;
    }
    
    public void setProtein(double value) {
        foodEntry.nutrients.protein = value;
    }
    
    public FoodEntry getFoodEntry() {
        return foodEntry;
    }
    
    public void setFoodEntry(FoodEntry entry) {
        this.foodEntry = entry;
    }

    private String sanitize(String s) {
        return s.replaceAll("[^a-zA-Z]+", "").toLowerCase();
    }

    public long hash() {
        StringBuilder builder;
        builder = new StringBuilder();
        builder.append(sanitize(getFoodName()));
        if (!getBrand().equals(DEFAULT_BRAND)) {
            builder.append(sanitize(getBrand()));
        }
        Nutrients nutrients = getNutrients();
        builder.append(nutrients.cals);
        builder.append(nutrients.carbs);
        builder.append(nutrients.protein);
        builder.append(nutrients.fat);
        return Utilities.hash(builder.toString());
    }

    @NonNull
    @NotNull
    @Override
    public String toString() {
        StringBuilder builder;
        builder = new StringBuilder();
        builder.append("id: ").append(getId()).append("\n");
        builder.append("fdcId: ").append(fdcId).append("\n");
        builder.append("Name: ").append(getFoodName()).append("\n");
        builder.append("Brand: ").append(getBrand()).append("\n");
        builder.append("Group: ").append(getGroupName(getGroup())).append("\n");
        builder.append("Nutrients: ").append(getNutrients()).append("\n");
        return builder.toString();
    }
}

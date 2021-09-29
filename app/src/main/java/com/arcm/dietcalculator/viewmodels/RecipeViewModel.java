package com.arcm.dietcalculator.viewmodels;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.arcm.dietcalculator.database.IngredientItem;
import com.arcm.dietcalculator.MutableListLiveData;
import com.arcm.dietcalculator.database.FoodRepository;
import com.arcm.dietcalculator.database.RecipeItem;
import com.arcm.dietcalculator.database.UnitEntry;

import java.util.ArrayList;
import java.util.List;

public class RecipeViewModel extends ViewModel {
    public final MutableListLiveData<IngredientItem> ingredients = new MutableListLiveData<>();
    public final MutableLiveData<String> title = new MutableLiveData<>();
    public final MutableLiveData<String> author = new MutableLiveData<>();
    public final MutableLiveData<List<UnitEntry>> units = new MutableLiveData<>();

    public List<IngredientItem> getIngredients() {
        if (ingredients.getValue() == null) {
            return new ArrayList<>();
        }
        return ingredients.getValue();
    }

    public List<UnitEntry> getUnits() {
        if (units.getValue() == null) {
            return new ArrayList<>();
        }
        return units.getValue();
    }

    public void saveToDatabase(Application application) {
        String recipeTitle = title.getValue();
        String recipeAuthor = author.getValue();
        List<IngredientItem> ingredients = getIngredients();
        if (recipeTitle == null) throw new IllegalArgumentException("Recipe title is null!");
        if (ingredients == null) throw new IllegalArgumentException("Ingredients are null!");
        if (ingredients.size() < 2) {
            throw new IllegalArgumentException("Ingredients < 2: (" + ingredients.size() + ")!");
        }
        RecipeItem recipe = new RecipeItem();
        recipe.setName(recipeTitle);
        if (recipeAuthor != null) recipe.setAuthor(recipeAuthor);
        recipe.setIngredients(ingredients);
        recipe.setUnits(getUnits());

        FoodRepository db = new FoodRepository(application);
        db.insertRecipe(recipe);
    }
}

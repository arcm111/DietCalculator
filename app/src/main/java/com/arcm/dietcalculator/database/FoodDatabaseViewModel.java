package com.arcm.dietcalculator.database;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.arcm.dietcalculator.DietDate;
import com.arcm.dietcalculator.TaskRunner;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FoodDatabaseViewModel extends AndroidViewModel {
    private final FoodRepository repository;

    public final LiveData<List<FoodItem>> foodItems;

    public FoodDatabaseViewModel(@NonNull @NotNull Application application) {
        super(application);
        this.repository = new FoodRepository(application);
        this.foodItems = repository.getFoodItems();
    }

    public LiveData<List<DiaryItem>> getDiaryItemsAsLiveData(DietDate date) {
        return repository.getDiaryItemsByDateAsLiveData(date);
    }

    public void getDiaryItemsByDate(DietDate date, TaskRunner.Callback<List<DiaryItem>> callback) {
        repository.getDiaryItemsByDate(date, callback);
    }

    public void getDiaryItemById(long id, TaskRunner.Callback<DiaryItem> callback) {
        repository.getDiaryItemById(id, callback);
    }

    public void getFoodItemById(long id, TaskRunner.Callback<FoodItem> callback) {
        repository.getFoodItemById(id, callback);
    }

    public LiveData<List<FoodItem>> getAllFoodItems() {
        return repository.getFoodItems();
    }

    public void insertFoodItem(FoodItem item, TaskRunner.Callback<Long> callback) {
        repository.insertFoodItem(item, callback);
    }

    public void insertFoodItem(FoodItem item) {
        insertFoodItem(item, null);
    }

    public void insertDiaryItem(DiaryItem item) {
        repository.insertDiaryItem(item);
    }

    public LiveData<List<ExerciseEntry>> getExercisesByDate(DietDate date) {
        return repository.getExercisesByDateAsLiveDate(date);
    }

    public void insertExercise(ExerciseEntry entry) {
        repository.insertExerciseEntry(entry);
    }

    public void insertRecipe(RecipeItem entry) {
        repository.insertRecipe(entry);
    }

    public void updateRecipe(RecipeItem entry) {
        repository.insertRecipe(entry);
    }

    public void deleteRecipe(RecipeItem entry) {
        repository.insertRecipe(entry);
    }

    public LiveData<List<RecipeItem>> getRecipesAdLiveDate() {
        return repository.getRecipes();
    }

    public void getRecipeItemById(int id, TaskRunner.Callback<RecipeItem> callback) {
        repository.getRecipeItemById(id, callback);
    }
}

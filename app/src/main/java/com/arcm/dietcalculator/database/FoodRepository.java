package com.arcm.dietcalculator.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.arcm.dietcalculator.DietDate;
import com.arcm.dietcalculator.TaskRunner;

import java.util.List;
import java.util.concurrent.Callable;

public class FoodRepository {
    private final FoodItemDao foodItemDao;
    private final DiaryItemDao diaryItemDao;
    private final ExerciseEntryDao exerciseEntryDao;
    private final RecipeItemDao recipeItemDao;

    public FoodRepository(Application application) {
        FoodDatabase db = FoodDatabase.getInstance(application);
        this.foodItemDao = db.foodItemDao();
        this.diaryItemDao = db.diaryFoodItemDao();
        this.exerciseEntryDao = db.exerciseEntryDao();
        this.recipeItemDao = db.recipeItemDao();
    }

    public LiveData<List<FoodItem>> getFoodItems() {
        return foodItemDao.getAllFoodItems();
    }

    public void insertFoodItem(FoodItem foodItem, TaskRunner.Callback<Long> callback) {
        if (foodItem.getId() == 0) foodItem.generateUniqueId();
        new TaskRunner().execute(new InsertFoodItemTask(foodItemDao, foodItem), callback);
    }

    public void deleteFoodItem(FoodItem entry) {
        new TaskRunner().execute(new DeleteFoodItemTask(foodItemDao, entry), null);
    }

    public void getDiaryItemsByDate(DietDate date, TaskRunner.Callback<List<DiaryItem>> callback) {
        new TaskRunner().execute(new GetDiaryItemsByDateTask(diaryItemDao, date), callback);
    }

    public void getDiaryItemById(long id, TaskRunner.Callback<DiaryItem> callback) {
        new TaskRunner().execute(new GetDiaryItemById(diaryItemDao, id), callback);
    }

    public void getFoodItemById(long id, TaskRunner.Callback<FoodItem> callback) {
        new TaskRunner().execute(new GetFoodItemById(foodItemDao, id), callback);
    }

    public LiveData<List<DiaryItem>> getDiaryItemsByDateAsLiveData(DietDate date) {
        return diaryItemDao.getItemsByDateAsLiveData(date);
    }

    public LiveData<List<ExerciseEntry>> getExercisesByDateAsLiveDate(DietDate date) {
        return exerciseEntryDao.getItemsByDate(date);
    }
    
    public void insertDiaryItem(DiaryItem entry) {
        new TaskRunner().execute(new InsertDiaryItemTask(diaryItemDao, entry), null);
    }

    public void insertExerciseEntry(ExerciseEntry entry) {
        new TaskRunner().execute(new InsertExercisesEntryTask(exerciseEntryDao, entry), null);
    }

    public void updateExerciseEntry(ExerciseEntry entry) {
        new TaskRunner().execute(new UpdateExercisesEntryTask(exerciseEntryDao, entry), null);
    }

    public LiveData<List<RecipeItem>> getRecipes() {
        return recipeItemDao.getRecipes();
    }

    public void getRecipeItemById(int id, TaskRunner.Callback<RecipeItem> callable) {
        new TaskRunner().execute(new GetRecipeItemByIdTask(recipeItemDao, id), callable);
    }

    public void insertRecipe(RecipeItem entry) {
        new TaskRunner().execute(new InsertRecipeItemTask(recipeItemDao, entry), null);
    }

    public void updateRecipe(RecipeItem entry) {
        new TaskRunner().execute(new UpdateRecipeItemTask(recipeItemDao, entry), null);
    }

    public void deleteRecipe(RecipeItem entry) {
        new TaskRunner().execute(new DeleteRecipeItemTask(recipeItemDao, entry), null);
    }

    private static class InsertFoodItemTask implements Callable<Long> {
        private final FoodItemDao foodItemDao;
        private final FoodItem foodItem;

        public InsertFoodItemTask(FoodItemDao foodItemDao, FoodItem foodItem) {
            this.foodItemDao = foodItemDao;
            this.foodItem = foodItem;
        }

        @Override
        public Long call() {
            return foodItemDao.insert(foodItem);
        }
    }

    private static class InsertUnitEntryTask implements Callable<Long> {
        private final UnitEntryDao unitEntryDao;
        private final UnitEntry entry;

        public InsertUnitEntryTask(UnitEntryDao unitEntryDao, UnitEntry entry) {
            this.unitEntryDao = unitEntryDao;
            this.entry = entry;
        }

        @Override
        public Long call() {
            return unitEntryDao.insert(entry);
        }
    }

    private static class DeleteFoodItemTask implements Callable<Void> {
        private final FoodItemDao foodItemDao;
        private final FoodItem foodItem;

        public DeleteFoodItemTask(FoodItemDao foodItemDao, FoodItem foodItem) {
            this.foodItemDao = foodItemDao;
            this.foodItem = foodItem;
        }

        @Override
        public Void call() {
            foodItemDao.delete(foodItem);
            return null;
        }
    }

    private static class InsertDiaryItemTask implements Callable<Void> {
        private final DiaryItemDao diaryItemDao;
        private final DiaryItem entry;

        public InsertDiaryItemTask(DiaryItemDao diaryItemDao, DiaryItem entry) {
            this.diaryItemDao = diaryItemDao;
            this.entry = entry;
        }

        @Override
        public Void call() {
            diaryItemDao.insertDiaryItem(entry);
            return null;
        }
    }

    private static class GetDiaryItemsByDateTask implements Callable<List<DiaryItem>> {
        private final DiaryItemDao diaryFoodItemDao;
        private final DietDate date;

        public GetDiaryItemsByDateTask(DiaryItemDao diaryFoodItemDao, DietDate date) {
            this.diaryFoodItemDao = diaryFoodItemDao;
            this.date = date;
        }

        @Override
        public List<DiaryItem> call() {
            return diaryFoodItemDao.getItemsByDate(date);
        }
    }

    private static class GetDiaryItemById implements Callable<DiaryItem> {
        private final DiaryItemDao diaryFoodItemDao;
        private final long id;

        public GetDiaryItemById(DiaryItemDao diaryFoodItemDao, long id) {
            this.diaryFoodItemDao = diaryFoodItemDao;
            this.id = id;
        }

        @Override
        public DiaryItem call() {
            return diaryFoodItemDao.getItemById(id);
        }
    }

    private static class GetFoodItemById implements Callable<FoodItem> {
        private final FoodItemDao foodItemDao;
        private final long id;

        public GetFoodItemById(FoodItemDao foodItemDao, long id) {
            this.foodItemDao = foodItemDao;
            this.id = id;
        }

        @Override
        public FoodItem call() {
            return foodItemDao.getFoodItemById(id);
        }
    }

    private static class InsertExercisesEntryTask implements Callable<Void> {
        private final ExerciseEntryDao exerciseEntryDao;
        private final ExerciseEntry entry;

        public InsertExercisesEntryTask(ExerciseEntryDao exerciseEntryDao, ExerciseEntry entry) {
            this.exerciseEntryDao = exerciseEntryDao;
            this.entry = entry;
        }

        @Override
        public Void call() {
            exerciseEntryDao.insert(entry);
            return null;
        }
    }

    private static class UpdateExercisesEntryTask implements Callable<Void> {
        private final ExerciseEntryDao exerciseEntryDao;
        private final ExerciseEntry entry;

        public UpdateExercisesEntryTask(ExerciseEntryDao exerciseEntryDao, ExerciseEntry entry) {
            this.exerciseEntryDao = exerciseEntryDao;
            this.entry = entry;
        }

        @Override
        public Void call() {
            exerciseEntryDao.update(entry);
            return null;
        }
    }

    private static class GetRecipeItemByIdTask implements Callable<RecipeItem> {
        private final RecipeItemDao recipeItemDao;
        private final int id;

        public GetRecipeItemByIdTask(RecipeItemDao recipeItemDao, int id) {
            this.recipeItemDao = recipeItemDao;
            this.id = id;
        }

        @Override
        public RecipeItem call() {
            return recipeItemDao.getItemById(id);
        }
    }

    private static class InsertRecipeItemTask implements Callable<Void> {
        private final RecipeItemDao recipeItemDao;
        private final RecipeItem entry;

        public InsertRecipeItemTask(RecipeItemDao recipeItemDao, RecipeItem entry) {
            this.recipeItemDao = recipeItemDao;
            this.entry = entry;
        }

        @Override
        public Void call() {
            recipeItemDao.insertRecipe(entry);
            return null;
        }
    }

    private static class UpdateRecipeItemTask implements Callable<Void> {
        private final RecipeItemDao recipeItemDao;
        private final RecipeItem entry;

        public UpdateRecipeItemTask(RecipeItemDao recipeItemDao, RecipeItem entry) {
            this.recipeItemDao = recipeItemDao;
            this.entry = entry;
        }

        @Override
        public Void call() {
//            recipeItemDao.update(entry);
            return null;
        }
    }

    private static class DeleteRecipeItemTask implements Callable<Void> {
        private final RecipeItemDao recipeItemDao;
        private final RecipeItem entry;

        public DeleteRecipeItemTask(RecipeItemDao recipeItemDao, RecipeItem entry) {
            this.recipeItemDao = recipeItemDao;
            this.entry = entry;
        }

        @Override
        public Void call() {
//            recipeItemDao.delete(entry);
            return null;
        }
    }
}

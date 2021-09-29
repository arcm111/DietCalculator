package com.arcm.dietcalculator.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.arcm.dietcalculator.DietDate;

@Database(entities = {FoodEntry.class, DiaryEntry.class, UnitEntry.class, ExerciseEntry.class,
        IngredientEntry.class ,RecipeEntry.class}, version = 11, exportSchema = false)
@TypeConverters({DietDate.class})
public abstract class FoodDatabase extends RoomDatabase {
    private static FoodDatabase instance;

    public abstract FoodItemDao foodItemDao();
    public abstract DiaryItemDao diaryFoodItemDao();
    public abstract ExerciseEntryDao exerciseEntryDao();
    public abstract RecipeItemDao recipeItemDao();

    public static synchronized FoodDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    FoodDatabase.class, "diet_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
package com.arcm.dietcalculator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;

import com.arcm.dietcalculator.database.DiaryItem;
import com.arcm.dietcalculator.database.FoodDatabaseViewModel;
import com.arcm.dietcalculator.database.FoodItem;
import com.arcm.dietcalculator.fragments.AddNewFoodItemActivity.SearchFragment;
import com.arcm.dietcalculator.viewmodels.AddNewFoodItemViewModel;

import java.util.List;

public class AddNewFoodItemActivity extends AppCompatActivity {
    public static final String TAG = "AddNewFoodItemActivity";
    public static final String DATE_EXTRA = "selected-diet-date";
    public static final String MEAL_TYPE_EXTRA = "meal-type";

    private DietDate date;
    private DiaryItem.MealType mealType;

    private AddNewFoodItemViewModel avm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.getSerializable(DATE_EXTRA) != null) {
            this.date = (DietDate) extras.getSerializable(DATE_EXTRA);
            this.mealType = (DiaryItem.MealType) extras.getSerializable(MEAL_TYPE_EXTRA);
        } else this.date = DietDate.today();

        Utilities.changeStatusBarAppearance(this, R.color.background_status_bar, true);

        SearchFragment f = new SearchFragment();
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().add(android.R.id.content, f, SearchFragment.TAG).commit();

        this.avm = new ViewModelProvider(this).get(AddNewFoodItemViewModel.class);
        avm.mealType = mealType;
        avm.date = date;
        FoodDatabaseViewModel db = new ViewModelProvider(this).get(FoodDatabaseViewModel.class);
        db.getAllFoodItems().observe(this, this::onDatabaseQueryComplete);
    }

    private void onDatabaseQueryComplete(List<FoodItem> items) {
        Log.d(TAG, "database food items: ");
        for (FoodItem item: items) Log.d(TAG, item.toString());
        avm.setFoodItems(items);
    }

    public DietDate getDate() {
        return date;
    }

    public DiaryItem.MealType getMealType() {
        return mealType;
    }
}
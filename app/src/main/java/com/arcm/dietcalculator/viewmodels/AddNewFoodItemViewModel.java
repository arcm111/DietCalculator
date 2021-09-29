package com.arcm.dietcalculator.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.arcm.dietcalculator.database.DiaryItem;
import com.arcm.dietcalculator.database.FoodItem;
import com.arcm.dietcalculator.DietDate;

import java.util.ArrayList;
import java.util.List;

public class AddNewFoodItemViewModel extends ViewModel {
    public final MutableLiveData<List<FoodItem>> foodItems = new MutableLiveData<>();
    public DiaryItem.MealType mealType;
    public DietDate date;

    public void setFoodItems(List<FoodItem> items) {
        this.foodItems.setValue(items);
    }

    public List<FoodItem> getItems() {
        if (foodItems.getValue() == null) {
            return new ArrayList<>();
        }
        return foodItems.getValue();
    }

    public List<FoodItem> getItemsByGroup(FoodItem.Group group) {
        List<FoodItem> items = new ArrayList<>();
        for (FoodItem item : getItems()) {
            if (item.getGroup() == group) items.add(item);
        }
        return items;
    }
}

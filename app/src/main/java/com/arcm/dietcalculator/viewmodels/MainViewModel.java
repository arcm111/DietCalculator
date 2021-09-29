package com.arcm.dietcalculator.viewmodels;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.arcm.dietcalculator.DietMath;
import com.arcm.dietcalculator.DietSettings;
import com.arcm.dietcalculator.database.DiaryItem;
import com.arcm.dietcalculator.database.ExerciseEntry;
import com.arcm.dietcalculator.DietDate;
import com.arcm.dietcalculator.database.Nutrients;

import java.util.List;

public class MainViewModel extends ViewModel {
    public final MutableLiveData<DietDate> date = new MutableLiveData<>();
    public final MutableLiveData<List<DiaryItem>> diaryItems = new MutableLiveData<>();
    public final MutableLiveData<List<ExerciseEntry>> exerciseItems = new MutableLiveData<>();
    public final MutableLiveData<Integer> targetCalories = new MutableLiveData<>();
    public final MutableLiveData<Integer[]> targetMacros = new MutableLiveData<>();

    public DietDate getDate() {
        if (date.getValue() == null) return DietDate.today();
        return date.getValue();
    }

    public void setDate(DietDate date) {
        this.date.setValue(date);
    }

    public void setFoodItems(List<DiaryItem> items) {
        diaryItems.setValue(items);
    }

    public void setExerciseItems(List<ExerciseEntry> items) {
        exerciseItems.setValue(items);
    }

    public int getTargetCalories() {
        if (targetCalories.getValue() == null) return 0;
        return targetCalories.getValue();
    }
    
    public void loadSettings(Context context) {
        DietSettings settings = new DietSettings(context);
        int calories = settings.getTargetCalories();
        if (targetCalories.getValue() == null || targetCalories.getValue() != calories) {
            targetCalories.setValue(calories);
        }
        Integer[] macros = new Integer[3];
        macros[0] = settings.getCarbsMacro();
        macros[1] = settings.getProteinMacro();
        macros[2] = settings.getFatMacro();
        if (targetMacros.getValue() != null) {
            if (!areEqual(targetMacros.getValue(), macros)) {
                targetMacros.setValue(macros);
            }
        } else targetMacros.setValue(macros);
    }

    public Nutrients getNutrientsTargets() {
        Nutrients nutrients = new Nutrients();
        int calories = getTargetCalories();
        nutrients.cals = calories;
        Integer[] macros = targetMacros.getValue();
        if (macros != null) {
            nutrients.carbs = DietMath.getCarbsTargetValue(calories, macros[0]);
            nutrients.protein = DietMath.getProteinTargetValue(calories, macros[1]);
            nutrients.fat = DietMath.getFatTargetValue(calories, macros[2]);
        }
        nutrients.saturatedFat = DietMath.getSaturatedFatTarget(calories, (int) nutrients.fat);
        nutrients.sugar = DietMath.getSugarTarget(calories, (int) nutrients.carbs);
        nutrients.fibre = DietMath.getFiberTarget(calories);
        return nutrients;
    }

    private boolean areEqual(Integer[] a, Integer[] b) {
        for (int i = 0; i < Math.min(a.length, b.length); i++) {
            if (!a[i].equals(b[i])) return false;
        }
        return true;
    }
}
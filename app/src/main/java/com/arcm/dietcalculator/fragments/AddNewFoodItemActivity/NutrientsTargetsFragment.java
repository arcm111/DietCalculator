package com.arcm.dietcalculator.fragments.AddNewFoodItemActivity;

import android.os.Bundle;
import android.util.Log;

import androidx.lifecycle.ViewModelProvider;

import com.arcm.dietcalculator.database.DiaryItem;
import com.arcm.dietcalculator.database.FoodDatabaseViewModel;
import com.arcm.dietcalculator.database.Nutrients;
import com.arcm.dietcalculator.DietSettings;

public class NutrientsTargetsFragment extends FoodNutrientsFragment {
    public static final String TAG = "NutrientsTargetsFrag";
    public static final String TITLE = "Remaining";

    private final Nutrients tmp = new Nutrients();
    private Nutrients consumed;
    private Nutrients targets;
    private FoodDatabaseViewModel db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.db = new ViewModelProvider(requireActivity()).get(FoodDatabaseViewModel.class);
        this.consumed = new Nutrients();
    }

    @Override
    protected void initValues(Nutrients item) {
        this.targets = DietSettings.getNutrientsTargets(getContext());
        Log.d(TAG, "Targets: " + targets.toString());
        setMaxValues(targets);
        getConsumedNutrients(item);
    }

    @Override
    public void updateValues(Nutrients item) {
        super.updateValues(item);
        if (isValuesInitiated()) {
            setProgressValues(item);
            setRemainingValues(item);
        } else initValues(item);
    }
    
    private void setMaxValues(Nutrients targets) {
        setCalsMaxValue((int) targets.cals);
        setCarbsMaxValue((int) targets.carbs);
        setProteinMaxValue((int) targets.protein);
        setFatMaxValue((int) targets.fat);
        setSfatMaxValue((int) targets.saturatedFat);
        setSugarMaxValue((int) targets.sugar);
        setFibreMaxValue((int) targets.fibre);
    }
    
    private void setProgressValues(Nutrients item) {
        setCalsProgressValue((int) (consumed.cals + item.cals));
        setCarbsProgressValue((int) (consumed.carbs + item.carbs));
        setProteinProgressValue((int) (consumed.protein + item.protein));
        setFatProgressValue((int) (consumed.fat + item.fat));
        setSfatProgressValue((int) (consumed.saturatedFat + item.saturatedFat));
        setSugarProgressValue((int) (consumed.sugar + item.sugar));
        setFibreProgressValue((int) (consumed.fibre + item.fibre));
    }
    
    private void setRemainingValues(Nutrients item) {
        Nutrients remaining = getRemaining(item);

        setCalValue(remaining.getCalsAsText());
        setCarbValue(remaining.getCarbsAsText());
        setSugarValue(remaining.getSugarAsText());
        setProteinValue(remaining.getProteinAsText());
        setFatValue(remaining.getFatAsText());
        setSatfatValue(remaining.getSaturatedFatAsText());
        setFibreValue(remaining.getFibreAsText());
    }
    
    private void getConsumedNutrients(Nutrients item) {
        db.getDiaryItemsByDate(getDate(), result -> {
            Nutrients consumed = new Nutrients();
            for (DiaryItem item1 : result) {
                consumed.cals += item1.getCals();
                consumed.carbs += item1.getCarbs();
                consumed.protein += item1.getProtein();
                consumed.fat += item1.getFat();
                consumed.saturatedFat += item1.getSaturatedFat();
                consumed.sugar += item1.getSugar();
                consumed.fibre += item1.getFibre();
            }
            onTaskComplete(consumed, item);
        });
    }

    private void onTaskComplete(Nutrients consumed, Nutrients item) {
        this.consumed = consumed;
        setProgressValues(item);
        setRemainingValues(item);
        setValuesInitiated();
    }
    
    private Nutrients getRemaining(Nutrients item) {
        tmp.cals = targets.cals - (consumed.cals + item.cals);
        tmp.carbs = targets.carbs - (consumed.carbs + item.carbs);
        tmp.protein = targets.protein - (consumed.protein + item.protein);
        tmp.fat = targets.fat - (consumed.fat + item.fat);
        tmp.saturatedFat = targets.saturatedFat - (consumed.saturatedFat + item.saturatedFat);
        tmp.sugar = targets.sugar - (consumed.sugar + item.sugar);
        tmp.fibre = targets.fibre - (consumed.fibre + item.fibre);
        return tmp;
    }
}

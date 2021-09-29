package com.arcm.dietcalculator.viewmodels;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.arcm.dietcalculator.DietMath;
import com.arcm.dietcalculator.DietSettings;

public class SetupViewModel extends ViewModel {
    private final MutableLiveData<Integer> age = new MutableLiveData<>();
    private final MutableLiveData<Double> weight = new MutableLiveData<>();
    private final MutableLiveData<Integer> height = new MutableLiveData<>();
    private final MutableLiveData<Sex> gender = new MutableLiveData<>();
    private final MutableLiveData<ActivityFactor> factor = new MutableLiveData<>();
    private final MutableLiveData<Integer> carbsMacro = new MutableLiveData<>();
    private final MutableLiveData<Integer> proteinMacro = new MutableLiveData<>();
    private final MutableLiveData<Integer> fatMacro = new MutableLiveData<>();
    private final MutableLiveData<Integer> maintenanceCalories = new MutableLiveData<>();
    private final MutableLiveData<Integer> targetCalories = new MutableLiveData<>();
    private final MutableLiveData<Float> targetWeightChange = new MutableLiveData<>();

    public enum Sex {
        MALE,
        FEMALE,
        UNDEFINED
    }

    public enum ActivityFactor {
        SEDENTARY(1.2),
        LIGHT(1.375),
        MODERATE(1.55),
        HIGH(1.725),
        EXTREME(1.9),
        UNDEFINED(0);

        private final double value;

        ActivityFactor(double value) {
            this.value = value;
        }

        public double getValue() {
            return value;
        }

        public static ActivityFactor fromValue(double value) {
            if (value == 1.2) return SEDENTARY;
            if (value == 1.375) return LIGHT;
            if (value == 1.55) return MODERATE;
            if (value == 1.725) return HIGH;
            if (value == 1.9) return EXTREME;
            return UNDEFINED;
        }

        @NonNull
        @Override
        public String toString() {
            switch (this) {
                case SEDENTARY:
                    return "Sedentary";
                case LIGHT:
                    return "Light";
                case MODERATE:
                    return "Moderate";
                case HIGH:
                    return "High";
                case EXTREME:
                    return "Extreme";
                case UNDEFINED:
                    return "Undefined";
            }
            return "Unknown";
        }
    }

    private int getMaintenanceValue() {
        return DietMath.calculateMaintenanceValue(
                getWeight(), getHeight(), getAge(), isFemale(), getFactor().getValue());
    }

    public int getTargetCarbsValue() {
        return DietMath.getCarbsTargetValue(getTargetCalories(), getCarbsMacro());
    }

    public int getTargetProteinValue() {
        return DietMath.getProteinTargetValue(getTargetCalories(), getProteinMacro());
    }

    public int getTargetFatValue() {
        return DietMath.getFatTargetValue(getTargetCalories(), getFatMacro());
    }

    public int getTargetCalories() {
        if (targetCalories.getValue() == null) {
            return getMaintenanceCalories();
        }
        return targetCalories.getValue();
    }

    public float getTargetWeightChange() {
        if (targetWeightChange.getValue() == null) {
            return 0;
        }
        return targetWeightChange.getValue();
    }

    public int getMaintenanceCalories() {
        if (maintenanceCalories.getValue() == null) {
            return getMaintenanceValue();
        }
        return maintenanceCalories.getValue();
    }

    public int getAge() {
        if (age.getValue() == null) return 0;
        return age.getValue();
    }

    public double getWeight() {
        if (weight.getValue() == null) return 0;
		return weight.getValue();
    }

    public int getHeight() {
        if (height.getValue() == null) return 0;
		return height.getValue();
    }

    public Sex getGender() {
        if (gender.getValue() == null) return Sex.UNDEFINED;
		return gender.getValue();
    }
    
    public boolean isFemale() {
        return getGender() == Sex.FEMALE;
    }

    public ActivityFactor getFactor() {
        if (factor.getValue() == null) return ActivityFactor.UNDEFINED;
		return factor.getValue();
    }

    public int getCarbsMacro() {
        if (carbsMacro.getValue() == null) return 0;
		return carbsMacro.getValue();
    }

    public int getProteinMacro() {
        if (proteinMacro.getValue() == null) return 0;
		return proteinMacro.getValue();
    }

    public int getFatMacro() {
        if (fatMacro.getValue() == null) return 0;
		return fatMacro.getValue();
    }

    public LiveData<Integer> getAgeLiveData() {
        return age;
    }

    public LiveData<Double> getWeightLiveData() {
        return weight;
    }

    public LiveData<Integer> getHeightLiveData() {
        return height;
    }

    public LiveData<Sex> getGenderLiveData() {
        return gender;
    }

    public LiveData<ActivityFactor> getFactorLiveData() {
        return factor;
    }

    public LiveData<Integer> getCarbsMacroLiveData() {
        return carbsMacro;
    }

    public LiveData<Integer> getProteinMacroLiveData() {
        return proteinMacro;
    }

    public LiveData<Integer> getFatMacroLiveData() {
        return fatMacro;
    }

    public LiveData<Integer> getMaintenanceCaloriesLiveData() {
        return maintenanceCalories;
    }
    
    public LiveData<Integer> getTargetCaloriesLiveDate() {
        return targetCalories;
    }

    public LiveData<Float> getTargetWeightChangeLiveData() {
        return targetWeightChange;
    }

    public void setAge(Integer value) {
        this.age.setValue(value);
        updateMaintenanceValue();
    }

    public void setWeight(Double value) {
        weight.setValue(value);
        updateMaintenanceValue();
    }

    public void setHeight(Integer value) {
        height.setValue(value);
        updateMaintenanceValue();
    }

    public void setGender(Sex value) {
        gender.setValue(value);
        updateMaintenanceValue();
    }

    public void setFactor(ActivityFactor value) {
        factor.setValue(value);
        updateMaintenanceValue();
    }

    public void setCarbsMacro(Integer value) {
        carbsMacro.setValue(value);
    }

    public void setProteinMacro(Integer value) {
        proteinMacro.setValue(value);
    }

    public void setFatMacro(Integer value) {
        fatMacro.setValue(value);
    }

    public void setTargetWeightChange(Float value) {
        targetWeightChange.setValue(value);
        targetCalories.setValue(getMaintenanceCalories() + DietMath.weightToCalories(value));
    }

    public void setTargetCalories(int calories) {
        targetCalories.setValue(calories);
        targetWeightChange.setValue(DietMath.caloriesToWeight(calories - getMaintenanceCalories()));
    }

    public void updateMaintenanceValue() {
        maintenanceCalories.setValue(getMaintenanceValue());
        targetCalories.setValue(getMaintenanceCalories());
        targetWeightChange.setValue(0.0f);
    }
    
    public void loadSettings(Context context) {
        DietSettings settings = new DietSettings(context);
        age.setValue(settings.getAge());
        weight.setValue(settings.getWeight());
        height.setValue(settings.getHeight());
        gender.setValue(settings.getGender());
        factor.setValue(settings.getFactor());
        carbsMacro.setValue(settings.getCarbsMacro());
        proteinMacro.setValue(settings.getProteinMacro());
        fatMacro.setValue(settings.getFatMacro());
        maintenanceCalories.setValue(getMaintenanceValue());
        targetWeightChange.setValue(settings.getTargetWeightChange());
        targetCalories.setValue(settings.getTargetCalories());
    }
    
    public void saveSettings(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(DietSettings.AGE, getAge());
        editor.putLong(DietSettings.WEIGHT, Double.doubleToRawLongBits(getWeight()));
        editor.putInt(DietSettings.HEIGHT, getHeight());
        editor.putBoolean(DietSettings.IS_FEMALE, isFemale());
        editor.putLong(DietSettings.FACTOR, Double.doubleToRawLongBits(getFactor().getValue()));
        editor.putInt(DietSettings.CARBS_MACRO, getCarbsMacro());
        editor.putInt(DietSettings.PROTEIN_MACRO, getProteinMacro());
        editor.putInt(DietSettings.FAT_MACRO, getFatMacro());
        editor.putLong(DietSettings.TARGET_WEIGHT_CHANGE, Double.doubleToRawLongBits(getTargetWeightChange()));
        editor.putInt(DietSettings.TARGET_CALORIES, getTargetCalories());
        editor.apply();
    }
}

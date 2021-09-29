package com.arcm.dietcalculator;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.arcm.dietcalculator.database.Nutrients;
import com.arcm.dietcalculator.viewmodels.SetupViewModel;

public class DietSettings {
    public static final String FIRST_RUN = "first-run";                    // boolean
    public static final String AGE = "age";                                // int
    public static final String HEIGHT = "height";                          // int
    public static final String WEIGHT = "weight";                          // long
    public static final String IS_FEMALE = "female";                       // boolean
    public static final String FACTOR = "factor";                          // long
    public static final String TARGET_CALORIES = "calories-target";        // int
    public static final String TARGET_WEIGHT_CHANGE = "weight-target";     // long
    public static final String CARBS_MACRO = "macro-carbs";                // int
    public static final String PROTEIN_MACRO = "macro-protein";            // int
    public static final String FAT_MACRO = "macro-fat";                    // int

    private static final int DEFAULT_AGE = 18;
    private static final int DEFAULT_HEIGHT = 150;
    private static final long DEFAULT_WEIGHT = Double.doubleToRawLongBits(60.0f);
    private static final long DEFAULT_FACTOR = 0;
    private static final int DEFAULT_CARBS_MACRO = 50;
    private static final int DEFAULT_PROTEIN_MACRO = 20;
    private static final int DEFAULT_FAT_MACRO = 30;
    private static final boolean DEFAULT_IS_FEMALE = false;

    private final SharedPreferences prefs;
    
    public DietSettings(Context context) {
        this.prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }
    
    public static Nutrients getNutrientsTargets(Context context) {
        Nutrients nutrients = new Nutrients();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        
        int targetCalories = prefs.getInt(TARGET_CALORIES, 0);
        int targetCarbs = DietMath.getCarbsTargetValue(targetCalories, prefs.getInt(CARBS_MACRO, 0));
        int targetProtein = DietMath.getProteinTargetValue(targetCalories, prefs.getInt(PROTEIN_MACRO, 0));
        int targetFat = DietMath.getFatTargetValue(targetCalories, prefs.getInt(FAT_MACRO, 0));
        nutrients.cals = targetCalories;
        nutrients.carbs = targetCarbs;
        nutrients.protein = targetProtein;
        nutrients.fat = targetFat;
        nutrients.saturatedFat = DietMath.getSaturatedFatTarget(targetCalories, targetFat);
        nutrients.sugar = DietMath.getSugarTarget(targetCalories, targetCarbs);
        nutrients.fibre = DietMath.getFiberTarget(targetCalories);
        
        return nutrients;
    }
    
    public static int getAge(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getInt(AGE, DEFAULT_AGE);
    }
    
    public int getAge() {
        return prefs.getInt(AGE, DEFAULT_AGE);
    }

    public static boolean getGender(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(IS_FEMALE, DEFAULT_IS_FEMALE);
    }
    
    public static int getHeight(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getInt(HEIGHT, DEFAULT_HEIGHT);
    }
    
    public int getHeight() {
        return prefs.getInt(HEIGHT, DEFAULT_HEIGHT);
    }
    
    public static double getWeight(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return Double.longBitsToDouble(prefs.getLong(WEIGHT, DEFAULT_WEIGHT));
    }

    public SetupViewModel.ActivityFactor getFactor() {
        double factor = Double.longBitsToDouble(prefs.getLong(FACTOR, DEFAULT_FACTOR));
        return SetupViewModel.ActivityFactor.fromValue(factor);
    }
    
    public double getWeight() {
        return Double.longBitsToDouble(prefs.getLong(WEIGHT, DEFAULT_WEIGHT));
    }

    public boolean isFemale() {
        return prefs.getBoolean(IS_FEMALE, DEFAULT_IS_FEMALE);
    }

    public SetupViewModel.Sex getGender() {
        return isFemale() ? SetupViewModel.Sex.FEMALE : SetupViewModel.Sex.MALE;
    }

    public float getTargetWeightChange() {
        return (float) Double.longBitsToDouble(prefs.getLong(TARGET_WEIGHT_CHANGE, 0));
    }

    public int getTargetCalories() {
        return prefs.getInt(TARGET_CALORIES, 0);
    }
    
    public static SetupViewModel.ActivityFactor getActivityLevel(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        double factor = Double.longBitsToDouble(prefs.getLong(FACTOR, DEFAULT_FACTOR));
        return SetupViewModel.ActivityFactor.fromValue(factor);
    }
    
    public SetupViewModel.ActivityFactor getActivityLevel() {
        double factor = Double.longBitsToDouble(prefs.getLong(FACTOR, DEFAULT_FACTOR));
        return SetupViewModel.ActivityFactor.fromValue(factor);
    }

    public int getCarbsMacro() {
        return prefs.getInt(CARBS_MACRO, DEFAULT_CARBS_MACRO);
    }

    public static int getCarbsMacro(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getInt(CARBS_MACRO, DEFAULT_CARBS_MACRO);
    }

    public int getProteinMacro() {
        return prefs.getInt(PROTEIN_MACRO, DEFAULT_PROTEIN_MACRO);
    }

    public int getProteinMacro(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getInt(PROTEIN_MACRO, DEFAULT_PROTEIN_MACRO);
    }

    public int getFatMacro() {
        return prefs.getInt(FAT_MACRO, DEFAULT_FAT_MACRO);
    }

    public int getFatMacro(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getInt(FAT_MACRO, DEFAULT_FAT_MACRO);
    }

    public int[] getMacros() {
        return new int[] {getCarbsMacro(), getProteinMacro(), getFatMacro()};
    }

    public static int[] getMacros(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        int carbs = prefs.getInt(CARBS_MACRO, DEFAULT_CARBS_MACRO);
        int protein = prefs.getInt(PROTEIN_MACRO, DEFAULT_PROTEIN_MACRO);
        int fat = prefs.getInt(FAT_MACRO, DEFAULT_FAT_MACRO);
        return new int[] {carbs, protein, fat};
    }



    public void setFirstRun(boolean firstRun) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(DietSettings.FIRST_RUN, false);
        editor.apply();
    }
    
    public void setAge(int age) {
		SharedPreferences.Editor editor = prefs.edit();
		editor.putInt(DietSettings.AGE, age);
		editor.apply();
	}

    public void setHeight(int height) {
		SharedPreferences.Editor editor = prefs.edit();
		editor.putInt(DietSettings.HEIGHT, height);
		editor.apply();
	}

    public void setWeight(double weight) {
		SharedPreferences.Editor editor = prefs.edit();
		editor.putLong(DietSettings.WEIGHT, Double.doubleToRawLongBits(weight));
		editor.apply();
	}

    public void setGender(boolean isFemale) {
		SharedPreferences.Editor editor = prefs.edit();
		editor.putBoolean(DietSettings.IS_FEMALE, isFemale);
		editor.apply();
	}

    public void setFactor(double factor) {
		SharedPreferences.Editor editor = prefs.edit();
		editor.putLong(DietSettings.FACTOR, Double.doubleToRawLongBits(factor));
		editor.apply();
	}

    public void setTargetCalories(int targetCalories) {
		SharedPreferences.Editor editor = prefs.edit();
		editor.putInt(DietSettings.TARGET_CALORIES, targetCalories);
		editor.apply();
	}

    public void setCarbsMacro(int carbsMacro) {
		SharedPreferences.Editor editor = prefs.edit();
		editor.putInt(DietSettings.CARBS_MACRO, carbsMacro);
		editor.apply();
	}

    public void setProteinMacro(int proteinMacro) {
		SharedPreferences.Editor editor = prefs.edit();
		editor.putInt(DietSettings.PROTEIN_MACRO, proteinMacro);
		editor.apply();
	}

    public void setFatMacro(int fatMacro) {
		SharedPreferences.Editor editor = prefs.edit();
		editor.putInt(DietSettings.FAT_MACRO, fatMacro);
		editor.apply();
	}
}

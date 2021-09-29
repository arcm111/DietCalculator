package com.arcm.dietcalculator;

public class DietMath {

    /**
     * Basic Metabolic Rate.
     */
    public static int BMR(double weight, double height, int age, boolean female) {
        if (female) {
            return (int) (655.1 + (9.563 * weight) + (1.85 * height) - (4.676 * age));
        }
        return (int) (66.47 + (13.75 * weight) + (5.003 * height) - (6.755 * age));
    }

    /**
     * Body Mass Index.
     */
    public static int BMI(double weight, double height) {
        return (int) (weight / (height * height));
    }

    public static int calculateMaintenanceValue(double weight, double height, int age,
                                                boolean female, double activityFactor) {
        return (int) (BMR(weight, height, age, female) * activityFactor);
    }

    public static double kgToPound(double weight) {
        return weight * 2.20462;
    }

    public static double poundToKg(double weight) {
        return weight / 2.20462;
    }

    public static double footToCm(double height) {
        return height * 30.48;
    }

    public static double cmToFoot(double height) {
        return height / 30.48;
    }

    public static double inchToCm(double height) {
        return height * 2.54;
    }

    public static double cmToInch(double height) {
        return height / 2.54;
    }

    public static int parseIntOrDefault(String s, int defaultValue) {
        try {
            return Integer.parseInt(s);
        } catch (Exception ex) {
            return defaultValue;
        }
    }

    public static int getCarbsTargetValue(int targetCalories, int macro) {
        return (int) (targetCalories * (macro / 100.0) / 4.0);
    }

    public static int getProteinTargetValue(int targetCalories, int macro) {
        return (int) (targetCalories * (macro / 100.0) / 4.0);
    }

    public static int getFatTargetValue(int targetCalories, int macro) {
        return (int) (targetCalories * (macro / 100.0) / 9.0);
    }

    /**
     * Total daily sugar should be 25% (15% - 30%) of daily carbs and less than 10% of daily calories.
     * https://health.gov/sites/default/files/2019-09/2015-2020_Dietary_Guidelines.pdf
     */
    public static int getSugarTarget(int targetCalories, int targetCarbs) {
        int n1 = (int) (targetCalories * 10.0 / 100.0);
        int n2 = (int) (targetCarbs * 25 / 100.0 / 4.0);
        return Math.min(n1, n2);
    }

    /**
     * Total daily saturated fat should be half total daily fat and less than 10% of daily calories.
     * https://health.gov/sites/default/files/2019-09/2015-2020_Dietary_Guidelines.pdf
     */
    public static int getSaturatedFatTarget(int targetCalories, int targetFat) {
        int n1 = targetFat / 2;
        int n2 = targetCalories * 10 / 100;
        return Math.min(n1, n2);
    }

    /**
     * 14g per 1000 calories a day.
     * https://health.gov/sites/default/files/2019-09/2015-2020_Dietary_Guidelines.pdf
     */
    public static int getFiberTarget(int targetCalories) {
        return (int) (targetCalories / 1000.0 * 14);
    }

    public static int weightToCalories(double weight) {
        return (int) (weight * 1000);
    }

    public static float caloriesToWeight(int calories) {
        return calories / 1000.0f;
    }
}

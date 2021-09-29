package com.arcm.dietcalculator.database;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Nutrients in 100g of a food item.
 */
public class Nutrients {
    public static final int CARBS_CALS_VALUE = 4;
    public static final int PROTEIN_CALS_VALUE = 4;
    public static final int FAT_CALS_VALUE = 9;
    
    public double cals = 0;
    public double carbs = 0;
    public double protein = 0;
    public double fat = 0;
    public double saturatedFat = 0;
    public double sugar = 0;
    public double fibre = 0;

    public int getCarbsPercentage() {
        return (int) (((carbs * CARBS_CALS_VALUE) / cals) * 100);
    }

    public int getProteinPercentage() {
        return (int) (((protein * PROTEIN_CALS_VALUE) / cals) * 100);
    }

    public int getFatPercentage() {
        return (int) (((fat * FAT_CALS_VALUE) / cals) * 100);
    }

    public int getSaturatedFatPercentage() {
        return (int) (((saturatedFat * FAT_CALS_VALUE) / cals) * 100);
    }

    public int getSugarPercentage() {
        return (int) (((sugar * CARBS_CALS_VALUE) / cals) * 100);
    }

    public int getFibrePercentage() {
        return (int) fibre;
    }
    
    public String getCalsAsText() {
		return formatCalories((int) Math.round(cals));
	}

    public String getCarbsAsText() {
		return formatWeight(carbs);
	}

    public String getProteinAsText() {
		return formatWeight(protein);
	}

    public String getFatAsText() {
		return formatWeight(fat);
	}

    public String getSaturatedFatAsText() {
		return formatWeight(saturatedFat);
	}

    public String getSugarAsText() {
		return formatWeight(sugar);
	}

    public String getFibreAsText() {
		return formatWeight(fibre);
	}

    private String formatCalories(int value) {
        return NumberFormat.getNumberInstance().format(value) + " kcal";
    }

    private String formatWeight(double value) {
        return String.format(Locale.getDefault(), "%.1f g", value);
    }
    
    public int[] toIntArray() {
        return new int[] {(int) carbs, (int) sugar, (int) fibre, (int) protein, (int) fat, (int) saturatedFat};
    }

    public double getValueByType(Type type) {
        switch (type) {
            case CARBOHYDRATES:
                return carbs;
            case PROTEIN:
                return protein;
            case FAT:
                return fat;
            case SATURATED_FAT:
                return saturatedFat;
            case SUGAR:
                return sugar;
            case FIBRE:
                return fibre;
        }
        return 0;
    }

    public String getValueByTypeAsText(Type type) {
        return String.valueOf((int) Math.round(getValueByType(type)));
    }

    public enum Type {
        CARBOHYDRATES,
        PROTEIN,
        FAT,
        SATURATED_FAT,
        SUGAR,
        FIBRE
    }

    @NonNull
    @NotNull
    @Override
    public String toString() {
        StringBuilder builder;
        builder = new StringBuilder("\n");
        builder.append("Calories      => ").append(cals).append("\n");
        builder.append("Carbohydrates => ").append(carbs).append("\n");
        builder.append("Protein       => ").append(protein).append("\n");
        builder.append("Fat           => ").append(fat).append("\n");
        builder.append("Saturated Fat => ").append(saturatedFat).append("\n");
        builder.append("Sugar         => ").append(sugar).append("\n");
        builder.append("Fibre         => ").append(fibre).append("\n");
        return builder.toString();
    }

    public boolean isValid() {
        int sum = 0;
        sum += CARBS_CALS_VALUE * carbs;
        sum += PROTEIN_CALS_VALUE * protein;
        sum += FAT_CALS_VALUE * fat;
        return sum <= cals;
    }
}

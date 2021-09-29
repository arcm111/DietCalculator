package com.arcm.dietcalculator;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.ColorRes;
import androidx.core.view.WindowInsetsControllerCompat;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class Utilities {
    private static final DecimalFormat decimalFormat = new DecimalFormat("0.00");

    private static boolean isInteger(double n) {
        return n % 1 == 0;
    }

    /**
     * Return a string representation of a number with comma as thousands separator.
     * @param n the signed number to be formatted
     * @return the formatted number as a string
     */
    public static String formatNumber(int n) {
        return NumberFormat.getNumberInstance().format(n);
    }

    public static String formatNumber(double n) {
        if (isInteger(n)) {
            return formatNumber((int) n);
        }
        return formatDecimal(n);
    }

    public static String formatDecimal(double n) {
        return decimalFormat.format(n);
    }
    
    public static String formatCalories(double n) {
        return formatNumber(n) + " kcal";
    }
    
    public static String formatCalories(int n) {
        return formatNumber(n) + " kcal";
    }

    public static String formatKilograms(double n) {
        return formatNumber(n) + " kg";
    }

    public static String formatKilograms(int n) {
        return formatNumber(n) + " kg";
    }
    
    public static String formatGrams(double n) {
        return formatNumber(n) + " g";
    }
    
    public static String formatGrams(int n) {
        return formatNumber(n) + " g";
    }

    public static String getPercent(int n) {
        return n + "%";
    }

    public static String sentenceCapitalise(String s) {
        if (s.isEmpty()) {
            return "";
        }
        String[] words = s.split(" ");
        StringBuilder builder = new StringBuilder();
        for (String w : words) {
            if (w.length() == 1) {
                builder.append(w).append(" ");
            } else {
                builder.append(Character.toUpperCase(w.charAt(0)));
                builder.append(w.substring(1).toLowerCase());
                builder.append(" ");
            }
        }
        builder.setLength(builder.length() - 1);
        return builder.toString();
    }

    public static void changeStatusBarAppearance(Activity context, @ColorRes int colour, boolean light) {
        Window window = context.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        View decor = window.getDecorView();
        WindowInsetsControllerCompat controller = new WindowInsetsControllerCompat(window, decor);
        controller.setAppearanceLightStatusBars(light);
        window.setStatusBarColor(context.getResources().getColor(colour));
    }

    public static int toInteger(String s) {
        if (s == null || s.isEmpty()) return 0;
        try {
            return Integer.parseInt(s);
        } catch (Exception ignored) {

        }
        return 0;
    }

    public static void showErrorDialog(Context context, String errorMessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(errorMessage);
        builder.setCancelable(false);
        builder.setPositiveButton("Ok", null);
        builder.create().show();
    }

    private static long mod(long a, long b) {
        return (a % b + b) % b;
    }

    /**
     * Hash a string into a unique key.
     * Uses Rabin-Karp hashing algorithm.
     * @param txt the string to be hashed
     * @return the hash key
     */
    public static long hash(String txt) {
        long p = 72057594037927931L; // largest 56-bit prime
        int d = 256; // size of characters set (8 bits)
        long q = 0;
        for (int i = 0; i < txt.length(); i++) {
            q = mod(q * d + txt.charAt(i), p);
        }
        return q;
    }
}

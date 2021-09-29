package com.arcm.dietcalculator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.TypeConverter;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DietDate implements Serializable {
    public static final String TAG = "DietDate";
    public final int day;
    public final int month;
    public final int year;

    public DietDate(String date) {
        if (!date.matches("\\d{2}-\\d{2}-\\d{4}")) {
            throw new IllegalArgumentException("Invalid date: " + date);
        }
        String[] parts = date.split("-");
        this.day = Integer.parseInt(parts[0]);
        this.month = Integer.parseInt(parts[1]);
        this.year = Integer.parseInt(parts[2]);
    }

    public static DietDate today() {
        Date time = Calendar.getInstance().getTime();
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String date = df.format(time);
        return new DietDate(date);
    }

    public static DietDate yesterday() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        Date time = cal.getTime();
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String date = df.format(time);
        return new DietDate(date);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null) return false;
        if (obj.getClass() != getClass()) return false;
        DietDate that = (DietDate) obj;
        return (this.day == that.day && this.month == that.month && this.year == that.year);
    }

    @NonNull
    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "%02d-%02d-%d", day, month, year);
    }

    public String toShortDateString() {
        return String.format(Locale.getDefault(), "%02d/%02d", day, month);
    }

    @TypeConverter
    public static DietDate fromString(String dateString) {
        return new DietDate(dateString);
    }

    @TypeConverter
    public static String dateToString(DietDate date) {
        return date.toString();
    }
}

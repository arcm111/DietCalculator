package com.arcm.nutrientsgraph;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.annotation.ColorInt;

public class Attributes {
    public final int remValMargin;
    public final int barHeight;
    public final int barWidth;
    public final int spacing;
    public final int cornerRadius;
    public final int iconSize;
    public final int iconMargin;
    public final int labelMargin;
    public final int targetValMargin;
    public final int textSize;
    public final @ColorInt int activeTextColour;
    public final @ColorInt int inactiveTextColour;
    public final @ColorInt int valueTextColour;
    public final @ColorInt int barContColour;
    public final @ColorInt int progressColour;
    public final @ColorInt int progressOverflowColour;

    public final int[] values;
    public final int[] targetValues;

    public final String unit;

    public Attributes(Context context, AttributeSet set, int defStyle) {
        int[] attrs = R.styleable.NutrientsGraph;
        int defStyleRes = R.style.NutrientsGraph;
        final TypedArray a = context.obtainStyledAttributes(set, attrs, defStyle, defStyleRes);
        
         remValMargin = a.getDimensionPixelSize(R.styleable.NutrientsGraph_ng_rem_val_margin, 0);
         barHeight = a.getDimensionPixelSize(R.styleable.NutrientsGraph_ng_bar_height, 0);
         barWidth = a.getDimensionPixelSize(R.styleable.NutrientsGraph_ng_bar_width, 0);
         spacing = a.getDimensionPixelSize(R.styleable.NutrientsGraph_ng_spacing, 0);
         cornerRadius = a.getDimensionPixelSize(R.styleable.NutrientsGraph_ng_corner_radius, 0);
         iconSize = a.getDimensionPixelSize(R.styleable.NutrientsGraph_ng_icon_size, 0);
         iconMargin = a.getDimensionPixelSize(R.styleable.NutrientsGraph_ng_icon_margin, 0);
         labelMargin = a.getDimensionPixelSize(R.styleable.NutrientsGraph_ng_label_margin, 0);
         targetValMargin = a.getDimensionPixelSize(R.styleable.NutrientsGraph_ng_target_val_margin, 0);
         textSize = a.getDimensionPixelSize(R.styleable.NutrientsGraph_ng_text_size, 0);
         activeTextColour = a.getColor(R.styleable.NutrientsGraph_ng_active_text_colour, 0);
         inactiveTextColour = a.getColor(R.styleable.NutrientsGraph_ng_inactive_text_colour, 0);
         valueTextColour = a.getColor(R.styleable.NutrientsGraph_ng_value_text_colour, 0);
         barContColour = a.getColor(R.styleable.NutrientsGraph_ng_bar_cont_colour, 0);
         progressColour = a.getColor(R.styleable.NutrientsGraph_ng_progress_colour, 0);
         progressOverflowColour = a.getColor(R.styleable.NutrientsGraph_ng_progress_overflow_colour, 0);
         values = getIntArray(a, R.styleable.NutrientsGraph_ng_values);
         targetValues = getIntArray(a, R.styleable.NutrientsGraph_ng_target_values);
         unit = context.getString(R.string.gram_unit);
        
        a.recycle();
    }

    private int[] getIntArray(TypedArray a, int attribute) {
        if (a.hasValue(attribute)) {
            int resId = a.getResourceId(attribute, 0);
            if (resId != 0) return a.getResources().getIntArray(resId);
        }
        return new int[NutrientsGraph.BARS_COUNT];
    }
}

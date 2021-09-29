package com.arcm.nutrientsgraph;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import androidx.core.content.res.ResourcesCompat;

public class NutrientsGraph extends View {
    public static final String TAG = "NutrientsGraph";
    public static final int BARS_COUNT = 6;

    private final String[] labels = new String[BARS_COUNT];
    private final Drawable[] icons = new Drawable[BARS_COUNT];
    private final NutrientBar[] bars = new NutrientBar[BARS_COUNT];
    private Attributes attrs;
    private int spacing;
    private int offsetX, offsetY;

    public NutrientsGraph(Context context) {
        super(context);
        init(null, 0);
    }

    public NutrientsGraph(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public NutrientsGraph(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attributeSet, int defStyle) {
        this.attrs = new Attributes(getContext(), attributeSet, defStyle);
        this.spacing = attrs.spacing;

        Context context = getContext();
        labels[0] = context.getString(R.string.carbohydrates_label);
        labels[1] = context.getString(R.string.sugar_label);
        labels[2] = context.getString(R.string.fibre_label);
        labels[3] = context.getString(R.string.protein_label);
        labels[4] = context.getString(R.string.fat_label);
        labels[5] = context.getString(R.string.saturated_fat_label);

        Resources resources = context.getResources();
        icons[0] = ResourcesCompat.getDrawable(resources, R.drawable.ic_carbohydrates, null);
        icons[1] = ResourcesCompat.getDrawable(resources, R.drawable.ic_sugar, null);
        icons[2] = ResourcesCompat.getDrawable(resources, R.drawable.ic_fibre, null);
        icons[3] = ResourcesCompat.getDrawable(resources, R.drawable.ic_protein, null);
        icons[4] = ResourcesCompat.getDrawable(resources, R.drawable.ic_fat, null);
        icons[5] = ResourcesCompat.getDrawable(resources, R.drawable.ic_saturated_fat, null);

        for (int i = 0; i < BARS_COUNT; i++) {
            bars[i] = new NutrientBar(attrs, labels[i], icons[i], attrs.values[i], attrs.targetValues[i]);
        }
    }

    private void invalidateWidth(int newWidth) {
        this.spacing = (newWidth - (BARS_COUNT * attrs.barWidth)) / (BARS_COUNT - 1);
    }

    private void invalidateHeight(int newHeight) {
        for (NutrientBar b : bars) b.setHeight(newHeight);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int contentWidth = BARS_COUNT * attrs.barWidth + (BARS_COUNT - 1) * spacing;
        int contentHeight = bars[0].height();
        int hPad = getPaddingLeft() + getPaddingRight();
        int vPad = getPaddingTop() + getPaddingBottom();
        int width = getSize(widthMeasureSpec, contentWidth + hPad);
        int height = getSize(heightMeasureSpec, contentHeight + vPad);
        if (height - vPad < contentHeight) {
            invalidateHeight(height - vPad);
            this.offsetY = getPaddingTop();
        } else this.offsetY = getPaddingTop() + (height - vPad - contentHeight) / 2;
        if (width - hPad < contentWidth) {
            invalidateWidth(width - hPad);
            this.offsetX = getPaddingLeft();
        } else this.offsetX = getPaddingLeft() + (width - hPad - contentWidth) / 2;
        setMeasuredDimension(width, height);
    }

    private int getSize(int measureSpec, int fallback) {
        int size = MeasureSpec.getSize(measureSpec);
        int mode = MeasureSpec.getMode(measureSpec);
        if (mode == MeasureSpec.EXACTLY) return size;
        if (mode == MeasureSpec.AT_MOST) return Math.min(size, fallback);
        return fallback;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.translate(offsetX, offsetY);
        for (NutrientBar b : bars) {
            b.draw(canvas);
            canvas.translate(attrs.barWidth + spacing, 0);
        }
        canvas.restore();
    }

    public void setValues(int[] values) {
        for (int i = 0; i < Math.min(values.length, BARS_COUNT); i++) {
            bars[i].setValue(values[i]);
            bars[i].updateProgress();
        }
        invalidate();
    }

    public void setTargetValues(int[] targets) {
        for (int i = 0; i < Math.min(targets.length, BARS_COUNT); i++) {
            bars[i].setTargetValue(targets[i]);
            bars[i].updateProgress();
        }
        invalidate();
    }
}
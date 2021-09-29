package com.arcm.dietpiechart;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;

import java.util.Arrays;

public class DietPieChart extends View {
    public static final String TAG = "DietPieChart";
    public static final String EXTRA_SUPER = "super_extra_state";
    private static final int DEF_STYLE_RES = R.style.DietPieChartStyle;

    private final int touchRadius = getPixelDimension(R.dimen.default_touch_radius);
    private int strokeWidth = getPixelDimension(R.dimen.default_stroke_width);
    private int orbitsGap = getPixelDimension(R.dimen.default_orbit_gap);
    private int knobRadius = getPixelDimension(R.dimen.default_knob_radius);
    private int valueTextSize = getPixelDimension(R.dimen.default_value_text_size);
    private int legendsTextSize = getPixelDimension(R.dimen.default_legends_text_size);
    private int outerRadius = 6 * orbitsGap;

    private @ColorInt int strokeColour = getColour(R.color.stroke_purple_dark);
    private @ColorInt int valueTextColour = getColour(R.color.sector_text_colour);
    private @ColorInt int legendsTextColour = getColour(R.color.legends_text_colour);
    private @ColorInt int activeKnobColour = getColour(R.color.knob_active_colour);
    private @ColorInt int inactiveKnobColour = getColour(R.color.knob_inactive_colour);
    private @ColorInt int pressedKnobColour = getColour(R.color.knob_pressed_colour);

    private final PointF touchPoint = new PointF();

    private int sectorsCount = 3;
    private String[] labels = {"Item 1", "Item 2", "Item 3"};
    private int[] values = {45, 25, 30};
    private @ColorInt int[] colours = new int[3];

    private boolean adjustable;

    private Chart chart;
    private OnValuesChangedListener listener;

    public DietPieChart(Context context) {
        super(context);
        init(null, 0);
    }

    public DietPieChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public DietPieChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle state = new Bundle();
        state.putParcelable(EXTRA_SUPER, super.onSaveInstanceState());
//        state.putInt(EXTRA_CHECKED, currentChecked.ordinal());
        return state;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable parcel) {
        Bundle state = (Bundle) parcel;
        super.onRestoreInstanceState(state.getParcelable(EXTRA_SUPER));
//        currentChecked = Checked.values()[state.getInt(EXTRA_CHECKED, 0)];
    }

    private void init(AttributeSet attrs, int defStyle) {
        colours[0] = getColour(R.color.pie_section_purple);
        colours[1] = getColour(R.color.pie_section_red);
        colours[2] = getColour(R.color.pie_section_blue);

        // Load attributes
        loadAttributes(attrs, R.styleable.DietPieChart, defStyle);

        chart = new Chart(sectorsCount);
        chart.setOrbitsGap(orbitsGap);
        chart.setStrokeWidth(strokeWidth);
        chart.setKnobRadius(knobRadius);
        chart.setTouchRadius(touchRadius);
        chart.setStrokeColour(strokeColour);
        chart.setValueTextSize(valueTextSize);
        chart.setValueTextColour(valueTextColour);
        chart.setLegendsTextSize(legendsTextSize);
        chart.setLegendsTextColour(legendsTextColour);
        chart.setActiveKnobColour(activeKnobColour);
        chart.setInactiveKnobColour(inactiveKnobColour);
        chart.setPressedKnobColour(pressedKnobColour);
        chart.setValues(values);
        chart.setLabels(labels);
        chart.setFillColours(colours);
        chart.setAdjustable(adjustable);
    }

    private void loadAttributes(AttributeSet attrs, int[] styleable, int defStyle) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, styleable, defStyle, DEF_STYLE_RES);
        this.sectorsCount = a.getInt(R.styleable.DietPieChart_dpc_sectors_count, sectorsCount);
        this.knobRadius = a.getDimensionPixelSize(R.styleable.DietPieChart_dpc_knob_radius, knobRadius);
        this.orbitsGap = a.getDimensionPixelSize(R.styleable.DietPieChart_dpc_orbits_gap, orbitsGap);
        this.orbitsGap = Math.max(orbitsGap, touchRadius / 2);
        this.outerRadius = 6 * orbitsGap;
        this.strokeWidth = a.getDimensionPixelSize(R.styleable.DietPieChart_dpc_stroke_width, strokeWidth);
//        this.touchRadius = a.getDimensionPixelSize(R.styleable., touchRadius);
        this.strokeColour = a.getColor(R.styleable.DietPieChart_dpc_stroke_colour, strokeColour);
        this.valueTextSize = a.getDimensionPixelSize(R.styleable.DietPieChart_dpc_value_text_size, valueTextSize);
        this.valueTextColour = a.getColor(R.styleable.DietPieChart_dpc_value_text_colour, valueTextColour);
        this.legendsTextSize = a.getDimensionPixelSize(R.styleable.DietPieChart_dpc_legends_text_size, legendsTextSize);
        this.legendsTextColour = a.getColor(R.styleable.DietPieChart_dpc_legends_text_colour, legendsTextColour);
        this.activeKnobColour = a.getColor(R.styleable.DietPieChart_dpc_knob_active_colour, activeKnobColour);
        this.inactiveKnobColour = a.getColor(R.styleable.DietPieChart_dpc_Knob_inactive_colour, inactiveKnobColour);
        this.pressedKnobColour = a.getColor(R.styleable.DietPieChart_dpc_knob_pressed_colour, pressedKnobColour);
        this.values = a.getResources().getIntArray(a.getResourceId(R.styleable.DietPieChart_dpc_values, 0));
        CharSequence[] tmp = a.getTextArray(R.styleable.DietPieChart_dpc_labels);
        if (tmp != null) {
            this.labels = new String[tmp.length];
            for (int i = 0; i < tmp.length; i++) labels[i] = tmp[i].toString();
        }
        this.colours = a.getResources().getIntArray(a.getResourceId(R.styleable.DietPieChart_dpc_fill_colours, 0));
        this.adjustable = a.getBoolean(R.styleable.DietPieChart_dpc_adjustable, true);
        a.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        chart.draw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int desiredWidth = outerRadius * 2 + getPaddingRight() + getPaddingLeft();
        int desiredHeight = outerRadius * 2 + chart.getLegendsHeight() + getPaddingTop() + getPaddingBottom();

        int width = getSize(widthMeasureSpec, desiredWidth);
        int height = getSize(heightMeasureSpec, desiredHeight);

        int innerWidth = width - getPaddingLeft() - getPaddingRight();
        int innerHeight = height - getPaddingTop() - getPaddingBottom();

        int contentWidth = Math.min(innerWidth, (int) (innerHeight * 4.0f / 5.0f)); // diameter
        int contentHeight = Math.min(contentWidth + chart.getLegendsHeight(), (int) (contentWidth * 5.0f / 4.0f));

        if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.AT_MOST) {
            width = contentWidth + getPaddingLeft() + getPaddingRight();
        }
        if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST) {
            height = contentHeight + getPaddingTop() + getPaddingBottom();
        }

        int radius = contentWidth / 2;
        int cx = (width - contentWidth) / 2 + radius;
        int cy = (height - contentHeight) / 2 + contentHeight - radius;
        chart.setDimensions(contentWidth, contentHeight, cx, cy);

        setMeasuredDimension(width, height);
    }

    private int getSize(int measureSpec, int fallbackSize) {
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        switch (mode) {
            case MeasureSpec.AT_MOST:
            case MeasureSpec.EXACTLY:
                return size;
            case MeasureSpec.UNSPECIFIED:
                return fallbackSize;
        }
        return size;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (!adjustable) return false;

        MotionEvent event = MotionEvent.obtain(e);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                onDown(event);
                break;
            case MotionEvent.ACTION_MOVE:
                onMove(event);
                break;
            case MotionEvent.ACTION_UP:
                onUp();
                performClick();
                break;
            case MotionEvent.ACTION_CANCEL:
                onUp();
                break;
        }
        event.recycle();
        return true;
    }

    private void onDown(MotionEvent e) {
        touchPoint.set(e.getX(), e.getY());
        chart.getValues(values);
        chart.onDown(touchPoint);
    }

    private void onMove(MotionEvent e) {
        touchPoint.set(e.getX(), e.getY());
        chart.onMove(touchPoint);
        if (chart.isDragging()) invalidate();
    }

    private void onUp() {
        boolean dragging = chart.isDragging();
        if (dragging && chart.hasValuesChanged(values)) {
            chart.getValues(values);
            notifyValuesChanged(values);
        }
        chart.onUp();
        if (dragging) invalidate();
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    private @ColorInt int getColour(@ColorRes int id) {
        return getResources().getColor(id);
    }

    private int getPixelDimension(@DimenRes int id) {
        return getResources().getDimensionPixelSize(id);
    }

    private void notifyValuesChanged(int[] values) {
        Log.i(TAG, "values changed: " + Arrays.toString(values));
        if (listener != null) listener.onValueChanged(values);
    }

    public void setOnValuesChangedListener(OnValuesChangedListener listener) {
        this.listener = listener;
    }

    public interface OnValuesChangedListener{
        void onValueChanged(int[] values);
    }

    public void setLabels(String... labels) {
        chart.setLabels(labels);
    }

    public void setValues(int... values) {
        chart.setValues(values);
    }
}
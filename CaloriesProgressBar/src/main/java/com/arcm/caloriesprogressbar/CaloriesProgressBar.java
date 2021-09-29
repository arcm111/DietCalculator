package com.arcm.caloriesprogressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.core.content.res.ResourcesCompat;

import java.text.NumberFormat;

public class CaloriesProgressBar extends View {
    public static final String TAG = "CaloriesProgressBar";
    private static final int MAX_CALS_VALUE = 10000;

    private int caloriesTextSize;
    private int unitTextSize;
    private int labelTextSize;
    private int progressBarWidth;
    private int innerPadding;
    private int unitTextMargin;
    private int valueTextMargin;

    private @ColorInt int primaryTextColour;
    private @ColorInt int secondaryTextColour;
    private @ColorInt int progressBackgroundColour;
    private @ColorInt int progressColour;
    
    private Drawable icon;
    private int iconSize;

    private final Paint progressBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint progressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private final MultiLineTextDrawable labelTextDrawable = new MultiLineTextDrawable();
    private final TextDrawable unitTextDrawable = new TextDrawable();
    private final TextDrawable valueTextDrawable = new TextDrawable();

    private final RectF bounds = new RectF();

    private int valueTextMaxWidth;

    private int radius, cx, cy;
    private int sweepAngle;
    private int value;
    private int maxValue;

    public CaloriesProgressBar(Context context) {
        super(context);
        init(null, 0);
    }

    public CaloriesProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public CaloriesProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        loadAttributes(attrs, R.styleable.CaloriesProgressBar, defStyle, R.style.CaloriesProgressBar);

        // initiate paints parameters
        invalidateProgressBarPaints();

        // Update TextPaint and text measurements from attributes
        invalidateTextDrawables();
        invalidateTextMeasurements();
    }
    
    private void loadAttributes(AttributeSet set, int[] attrs, int defStyle, int defStyleRes) {
        final TypedArray a = getContext().obtainStyledAttributes(set, attrs, defStyle, defStyleRes);
        primaryTextColour = a.getColor(R.styleable.CaloriesProgressBar_cpb_primary_text_colour, 0);
        secondaryTextColour = a.getColor(R.styleable.CaloriesProgressBar_cpb_secondary_text_colour, 0);
        caloriesTextSize = a.getDimensionPixelSize(R.styleable.CaloriesProgressBar_cpb_calories_text_size, 0);
        unitTextSize = a.getDimensionPixelSize(R.styleable.CaloriesProgressBar_cpb_unit_text_size, 0);
        labelTextSize = a.getDimensionPixelSize(R.styleable.CaloriesProgressBar_cpb_label_text_size, 0);
        if (a.hasValue(R.styleable.CaloriesProgressBar_cpb_calories_icon)) {
            int iconResId = a.getResourceId(R.styleable.CaloriesProgressBar_cpb_calories_icon, 0);
            if (iconResId != 0) {
                icon = ResourcesCompat.getDrawable(getResources(), iconResId, null);
                if (icon != null) {
                    icon.setColorFilter(secondaryTextColour, PorterDuff.Mode.SRC_IN);
                }
            }
        }
        iconSize = a.getDimensionPixelSize(R.styleable.CaloriesProgressBar_cpb_icon_size, 0);
        progressBackgroundColour = a.getColor(R.styleable.CaloriesProgressBar_cpb_progress_background_colour, 0);
        progressColour = a.getColor(R.styleable.CaloriesProgressBar_cpb_progress_colour, 0);
        progressBarWidth = a.getDimensionPixelSize(R.styleable.CaloriesProgressBar_cpb_progress_bar_width, 0);
        innerPadding = a.getDimensionPixelSize(R.styleable.CaloriesProgressBar_cpb_inner_padding, 0); 
        unitTextMargin = a.getDimensionPixelSize(R.styleable.CaloriesProgressBar_cpb_unit_margin, 0);
        valueTextMargin = a.getDimensionPixelSize(R.styleable.CaloriesProgressBar_cpb_value_margin, 0);
        maxValue = a.getInteger(R.styleable.CaloriesProgressBar_cpb_max_value, 0);
        int consumed = a.getInteger(R.styleable.CaloriesProgressBar_cpb_value, 0);
        value = Math.max(0, Math.min(consumed, maxValue));
        updateSweepAngle();
        a.recycle();
    }

    private void invalidateProgressBarPaints() {
        initStrokePaint(progressBgPaint, progressBarWidth, progressBackgroundColour);
        initStrokePaint(progressPaint, progressBarWidth, progressColour);
    }

    private void invalidateTextDrawables() {
        valueTextDrawable.setTextPaintParameters(Paint.Align.CENTER, caloriesTextSize, primaryTextColour);
        unitTextDrawable.setTextPaintParameters(Paint.Align.LEFT, unitTextSize, primaryTextColour);
        labelTextDrawable.setTextPaintParameters(Paint.Align.CENTER, labelTextSize, secondaryTextColour);
        labelTextDrawable.setText(getResources().getString(R.string.label));
        unitTextDrawable.setText(getResources().getString(R.string.unit));
        valueTextDrawable.setText(formatString(value));
        this.valueTextMaxWidth = valueTextDrawable.measureTextWidth(formatString(MAX_CALS_VALUE));
    }

    private void invalidateTextMeasurements() {
        int vh = valueTextDrawable.height() / 2; // half of value text height
        int vw = valueTextDrawable.width() / 2; // half of value text width
        valueTextDrawable.setPosition(cx, cy + vh);
        unitTextDrawable.setX(cx + vw + unitTextMargin);
        unitTextDrawable.setY(cy + vh - valueTextDrawable.textOffset() + unitTextDrawable.textOffset());
        labelTextDrawable.setPosition(cx, cy - vh - valueTextMargin);
        int y = cy + vh + valueTextMargin + iconSize / 2;
        int e = iconSize / 2;
        icon.setBounds(cx - e, y - e, cx + e, y + e);
    }

    private void initStrokePaint(Paint paint, int width, @ColorInt int colour) {
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(width);
        paint.setColor(colour);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int horizontalPadding = getPaddingLeft() + getPaddingRight();
        int verticalPadding = getPaddingTop() + getPaddingBottom();
        int diameter = measureRadius() * 2;

        int width = getSize(widthMeasureSpec, diameter + horizontalPadding);
        int height = getSize(heightMeasureSpec, diameter + verticalPadding);

        int contentWidth = width - horizontalPadding;
        int contentHeight = height - verticalPadding;
        int d = Math.min(contentWidth, contentHeight);

        this.radius = (d - progressBarWidth) / 2;
        this.cx = getPaddingLeft() + contentWidth / 2;
        this.cy = getPaddingTop() + contentHeight / 2;
        bounds.set(cx - radius, cy - radius, cx + radius, cy + radius);
        invalidateTextMeasurements();

        setMeasuredDimension(width, height);
    }

    private int getSize(int measureSpec, int fallback) {
        int size = MeasureSpec.getSize(measureSpec);
        int mode = MeasureSpec.getMode(measureSpec);
        if (mode == MeasureSpec.EXACTLY) return size;
        if (mode == MeasureSpec.AT_MOST) return Math.min(size, fallback);
        return fallback;
    }

    private int measureRadius() {
        int pbw = progressBarWidth / 2 + innerPadding;
        int w = pbw + unitTextDrawable.width() + unitTextMargin + valueTextMaxWidth / 2;
        int h = pbw + labelTextDrawable.height() + valueTextMargin + valueTextDrawable.height() / 2;
        return Math.max(w, h);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(cx, cy, radius, progressBgPaint);
        canvas.drawArc(bounds, 270, sweepAngle, false, progressPaint);
        valueTextDrawable.draw(canvas);
        unitTextDrawable.draw(canvas);
        labelTextDrawable.draw(canvas);
        icon.draw(canvas);
    }

    private String formatString(int value) {
        return NumberFormat.getNumberInstance().format(value);
    }

    private void updateSweepAngle() {
        float progress = (maxValue - value) * 360.0f / maxValue;
        this.sweepAngle = (int) Math.max(0, Math.min(progress, 360));
    }

    public void setValue(int value) {
        this.value = Math.max(0, Math.min(value, maxValue));
        valueTextDrawable.setText(formatString(value));
        unitTextDrawable.setX(cx + valueTextDrawable.width() / 2 + unitTextMargin);
        updateSweepAngle();
        invalidate();
    }

    public void setMaxValue(int newMaxValue) {
        int remaining = value + newMaxValue - maxValue;
        this.maxValue = newMaxValue;
        setValue(remaining);
    }
}
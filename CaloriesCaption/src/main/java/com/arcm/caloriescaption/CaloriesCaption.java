package com.arcm.caloriescaption;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import java.text.NumberFormat;

public class CaloriesCaption extends View {
    public static final String TAG = "CaloriesCaption";
    
    private int valuesTextSize;
    private int unitTextSize;
    private int labelsTextSize;

    private @ColorInt int primaryColour;
    private @ColorInt int secondaryColour;

    private Drawable targetIcon;

    private final TextDrawable consumedTextDrawable = new TextDrawable();
    private final TextDrawable targetTextDrawable = new TextDrawable();
    private final TextDrawable consumedLabelTextDrawable = new TextDrawable();
    private final TextDrawable targetLabelTextDrawable = new TextDrawable();
    private final TextDrawable consumedUnitTextDrawable = new TextDrawable();
    private final TextDrawable targetUnitTextDrawable = new TextDrawable();

    private final Paint dividerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private int unitMargin;
    private int dividerWidth;
    private int dividerMargin;
    private int iconPadding;
    private int iconSize;

    private final Point dividerStart = new Point();
    private final Point dividerEnd = new Point();

    private int consumedValue;
    private int targetValue;

    private int offsetX, offsetY, cx;

    public CaloriesCaption(Context context) {
        super(context);
        init(null, 0);
    }

    public CaloriesCaption(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public CaloriesCaption(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyleAttr) {
        loadAttributes(attrs, R.styleable.CaloriesCaption, defStyleAttr, R.style.CaloriesCaption);
        
        consumedTextDrawable.setTextPaintParameters(Paint.Align.RIGHT, valuesTextSize, primaryColour);
        targetTextDrawable.setTextPaintParameters(Paint.Align.LEFT, valuesTextSize, primaryColour);
        consumedLabelTextDrawable.setTextPaintParameters(Paint.Align.RIGHT, labelsTextSize, secondaryColour);
        targetLabelTextDrawable.setTextPaintParameters(Paint.Align.LEFT, labelsTextSize, secondaryColour);
        consumedUnitTextDrawable.setTextPaintParameters(Paint.Align.RIGHT, unitTextSize, primaryColour);
        targetUnitTextDrawable.setTextPaintParameters(Paint.Align.LEFT, unitTextSize, primaryColour);

        consumedTextDrawable.setText(formatString(consumedValue));
        targetTextDrawable.setText(formatString(targetValue));
        consumedLabelTextDrawable.setText(getResources().getString(R.string.consumed_calories_label));
        targetLabelTextDrawable.setText(getResources().getString(R.string.target_calories_label));
        consumedUnitTextDrawable.setText(getResources().getString(R.string.unit_kcal));
        targetUnitTextDrawable.setText(getResources().getString(R.string.unit_kcal));

        dividerPaint.setStyle(Paint.Style.STROKE);
        dividerPaint.setColor(secondaryColour);
        dividerPaint.setStrokeWidth(dividerWidth);
    }
    
    private void loadAttributes(AttributeSet set, int[] attrs, int defStyle, int defStyleRes) {
        final TypedArray a = getContext().obtainStyledAttributes(set, attrs, defStyle, defStyleRes);
        
        valuesTextSize = a.getDimensionPixelSize(R.styleable.CaloriesCaption_cc_values_textSize, 0);
        unitTextSize = a.getDimensionPixelSize(R.styleable.CaloriesCaption_cc_unit_textSize, 0);
        labelsTextSize = a.getDimensionPixelSize(R.styleable.CaloriesCaption_cc_labels_textSize, 0);
        primaryColour = a.getColor(R.styleable.CaloriesCaption_cc_primary_colour, 0);
        secondaryColour = a.getColor(R.styleable.CaloriesCaption_cc_secondary_colour, 0);
        int resId = a.getResourceId(R.styleable.CaloriesCaption_cc_target_icon, 0);
        if (resId != 0) {
            targetIcon = ResourcesCompat.getDrawable(getResources(), resId, null);
        }
        unitMargin = a.getDimensionPixelSize(R.styleable.CaloriesCaption_cc_unit_margin, 0);
        dividerWidth = a.getDimensionPixelSize(R.styleable.CaloriesCaption_cc_divider_width, 0);
        dividerMargin = a.getDimensionPixelSize(R.styleable.CaloriesCaption_cc_divider_margin, 0);
        iconPadding = a.getDimensionPixelSize(R.styleable.CaloriesCaption_cc_icon_padding, 0);
        iconSize = a.getDimensionPixelSize(R.styleable.CaloriesCaption_cc_icon_size, 0);
        consumedValue = a.getInteger(R.styleable.CaloriesCaption_cc_consumed_value, 0);
        targetValue = a.getInteger(R.styleable.CaloriesCaption_cc_target_value, 0);

        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int hPad = getPaddingLeft() + getPaddingRight();
        int vPad = getPaddingTop() + getPaddingBottom();
        int contentWidth = measureContentWidth();
        int contentHeight = measureContentHeight();
        int width = getSize(widthMeasureSpec, contentWidth + hPad);
        int height = getSize(heightMeasureSpec, contentHeight + vPad);

        this.offsetX = getPaddingLeft() + (width - hPad - contentWidth) / 2;
        this.offsetY = getPaddingTop() + (height - vPad - contentHeight) / 2;
        this.cx = contentWidth / 2;

        invalidateMeasurements();

        setMeasuredDimension(width, height);
    }

    private int measureContentWidth() {
        int width1 = targetTextDrawable.width() + unitMargin + targetUnitTextDrawable.width();
        width1 += iconSize + 2 * iconPadding + dividerMargin;
        int width2 = Math.max(targetLabelTextDrawable.width(), consumedLabelTextDrawable.width());
        width2 += iconSize + 2 * iconPadding + dividerMargin;
        return Math.max(width1, width2) * 2;
    }

    private int measureContentHeight() {
        return targetTextDrawable.height() + targetLabelTextDrawable.height() +
                targetTextDrawable.textOffset();
    }
    
    private int getSize(int measureSpec, int fallback) {
        int size = MeasureSpec.getSize(measureSpec);
        int mode = MeasureSpec.getMode(measureSpec);
        if (mode == MeasureSpec.EXACTLY) return size;
        if (mode == MeasureSpec.AT_MOST) return Math.min(size, fallback);
        return fallback;
    }

    private void invalidateMeasurements() {
        int unitOffset = dividerMargin + 2 * iconPadding + iconSize;
        int valueHeight = targetTextDrawable.height();
        int valueWidth = targetTextDrawable.width();
        int baseLine = valueHeight - consumedTextDrawable.textOffset() + consumedUnitTextDrawable.textOffset();
        int unitWidth = consumedUnitTextDrawable.width();

        consumedTextDrawable.setPosition(cx - unitOffset - unitWidth - unitMargin, valueHeight);
        targetTextDrawable.setPosition(cx + unitOffset, valueHeight);

        consumedUnitTextDrawable.setPosition(cx - unitOffset, baseLine);
        targetUnitTextDrawable.setPosition(cx + unitOffset + valueWidth + unitMargin, baseLine);

        int labelHeight = consumedLabelTextDrawable.height();
        int labelOffset = valueHeight + labelHeight + consumedLabelTextDrawable.textOffset();
        consumedLabelTextDrawable.setPosition(cx - unitOffset, labelOffset);
        targetLabelTextDrawable.setPosition(cx + unitOffset, labelOffset);

        if (targetIcon != null) {
            int iconX = cx + dividerMargin + iconPadding;
            int iconY = (valueHeight - iconSize) / 2;
            targetIcon.setBounds(iconX, iconY, iconX + iconSize, iconY + iconSize);
        }
        int dividerHeight = (int) (valueHeight * 0.7f);
        dividerStart.set(cx, (valueHeight - dividerHeight) / 2);
        dividerEnd.set(cx, dividerStart.y + dividerHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.translate(offsetX, offsetY);

        consumedTextDrawable.draw(canvas);
        consumedUnitTextDrawable.draw(canvas);
        consumedLabelTextDrawable.draw(canvas);

        canvas.drawLine(dividerStart.x, dividerStart.y, dividerEnd.x, dividerEnd.y, dividerPaint);
        if (targetIcon != null) targetIcon.draw(canvas);

        targetTextDrawable.draw(canvas);
        targetUnitTextDrawable.draw(canvas);
        targetLabelTextDrawable.draw(canvas);

        canvas.restore();
    }
    
    private String formatString(int value) {
        return NumberFormat.getNumberInstance().format(value);
    }

    public void setTargetCalories(int targetValue) {
        this.targetValue = targetValue;
        targetTextDrawable.setText(formatString(targetValue));
        invalidate();
    }

    public void setConsumedCalories(int consumedValue) {
        this.consumedValue = consumedValue;
        consumedTextDrawable.setText(formatString(consumedValue));
        invalidate();
    }
}

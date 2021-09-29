package com.arcm.nutrientsgraph;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

public class NutrientBar {
    private final TextDrawable valTextDrawable = new TextDrawable();
    private final TextDrawable remValTextDrawable = new TextDrawable();
    private final TextDrawable targetValTextDrawable = new TextDrawable();
    private final TextDrawable labelTextDrawable = new TextDrawable();
    private final Paint barContPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint barProgressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Drawable icon;
    private final Attributes attrs;
    private final RectF barContainer = new RectF();
    private final RectF barProgress = new RectF();

    private int barHeight;
    private int value, targetValue;

    public NutrientBar(Attributes attrs, String label, Drawable icon, int value, int targetValue) {
        this.icon = icon;
        this.attrs = attrs;
        this.value = value;
        this.targetValue = targetValue;
        this.barHeight = attrs.barHeight;

        Paint.Align centre = Paint.Align.CENTER;
        valTextDrawable.setTextPaintParameters(centre, attrs.textSize, attrs.valueTextColour);
        targetValTextDrawable.setTextPaintParameters(centre, attrs.textSize, attrs.inactiveTextColour);
        remValTextDrawable.setTextPaintParameters(centre, attrs.textSize, attrs.activeTextColour);
        labelTextDrawable.setTextPaintParameters(centre, attrs.textSize, attrs.inactiveTextColour);

        valTextDrawable.setText(String.valueOf(value));
        targetValTextDrawable.setText(targetValue + attrs.unit);
        remValTextDrawable.setText((targetValue - value) + attrs.unit);
        labelTextDrawable.setText(label);

        barContPaint.setStyle(Paint.Style.FILL);
        barContPaint.setColor(attrs.barContColour);
        barProgressPaint.setStyle(Paint.Style.FILL);
        if (value > targetValue) {
            barProgressPaint.setColor(attrs.progressOverflowColour);
        } else barProgressPaint.setColor(attrs.progressColour);

        invalidateMeasurements();
    }

    private void invalidateMeasurements() {
        int width = attrs.barWidth;
        int cx = width / 2;
        int top = remValTextDrawable.height();
        remValTextDrawable.setPosition(cx, top);
        top += attrs.remValMargin;
        barContainer.set(0, top, width, top + barHeight);
        int contBottom = top + barHeight;
        int progressHeight = (int) (getProgressPercentage() * barHeight);
        int progressTop = contBottom - progressHeight;
        barProgress.set(0, progressTop, width, barContainer.bottom);
        if (progressHeight >= valTextDrawable.height()) {
            int valueTop = contBottom - progressHeight / 2 + valTextDrawable.height() / 2;
            valTextDrawable.setPosition(cx, Math.min(valueTop, contBottom));
            valTextDrawable.setTextColour(attrs.valueTextColour);
        } else {
            valTextDrawable.setPosition(cx, progressTop);
            valTextDrawable.setTextColour(attrs.activeTextColour);
        }
        top += barHeight + attrs.iconMargin;
        int left = cx - attrs.iconSize / 2;
        icon.setBounds(left, top, left + attrs.iconSize, top + attrs.iconSize);
        top += attrs.iconSize + attrs.labelMargin + labelTextDrawable.height();
        labelTextDrawable.setPosition(cx, top);
        top += attrs.targetValMargin + targetValTextDrawable.height();
        targetValTextDrawable.setPosition(cx, top);
    }

    private float getProgressPercentage() {
        return Math.min((float) value / (float) targetValue, 1.0f);
    }

    public void draw(Canvas canvas) {
        remValTextDrawable.draw(canvas);
        canvas.drawRoundRect(barContainer, attrs.cornerRadius, attrs.cornerRadius, barContPaint);
        canvas.drawRoundRect(barProgress, attrs.cornerRadius, attrs.cornerRadius, barProgressPaint);
        if (value > 0) valTextDrawable.draw(canvas);
        icon.draw(canvas);
        labelTextDrawable.draw(canvas);
        targetValTextDrawable.draw(canvas);
    }

    public void updateProgress() {
        float progressHeight = barContainer.height() * getProgressPercentage();
        barProgress.top = barProgress.bottom - progressHeight;
        valTextDrawable.setY((int) (barProgress.bottom - progressHeight / 2.0f));
        invalidateMeasurements();
    }

    public void setValue(int value) {
        if (this.value == value) return;

        this.value = value;
        if (value > targetValue) {
            barProgressPaint.setColor(attrs.progressOverflowColour);
        } else barProgressPaint.setColor(attrs.progressColour);
        valTextDrawable.setText(String.valueOf(value));
        remValTextDrawable.setText((targetValue - value) + attrs.unit);
        invalidateMeasurements();
    }

    public void setTargetValue(int targetValue) {
        if (this.targetValue == targetValue) return;

        this.targetValue = targetValue;
        targetValTextDrawable.setText(targetValue + attrs.unit);
    }

    public int width() {
        return attrs.barWidth;
    }

    public int height() {
        int h = remValTextDrawable.height() + attrs.remValMargin + barHeight;
        h += attrs.iconMargin + attrs.iconSize + attrs.labelMargin + labelTextDrawable.height();
        h += attrs.targetValMargin + targetValTextDrawable.height();
        return h;
    }

    public void setHeight(int newHeight) {
        int dh = newHeight - height();
        this.barHeight += dh;
        invalidateMeasurements();
    }
}

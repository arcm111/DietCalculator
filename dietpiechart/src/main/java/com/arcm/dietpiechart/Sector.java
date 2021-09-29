package com.arcm.dietpiechart;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextPaint;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

public class Sector {
    private String label = "Undefined";
    private double value;
    private int radius;
    private double sweepAngle;

    private final Paint fillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint strokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final TextPaint valueTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    private final TextPaint legendsTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    private final RectF bounds = new RectF();
    private final Point center = new Point();
    private final Rect textBound = new Rect();

    public final Knob knob = new Knob();

    public Sector prev = null;
    public Sector next = null;

    public Sector() {
        init();
    }

    public Sector(String label, int value) {
        this();
        setLabel(label);
        setValue(value);
    }

    public Sector(String label, int value, @ColorInt int fillColour) {
        this(label, value);
        fillPaint.setColor(fillColour);
    }

    private void init() {
        fillPaint.setStyle(Paint.Style.FILL);
        strokePaint.setStyle(Paint.Style.STROKE);
        valueTextPaint.setTextAlign(Paint.Align.CENTER);
        legendsTextPaint.setTextAlign(Paint.Align.LEFT);
    }

    public void draw(Canvas canvas, float startAngle, boolean adjustable) {
        canvas.drawArc(bounds, startAngle, (float) sweepAngle, true, fillPaint);
        canvas.drawArc(bounds, startAngle, (float) sweepAngle, true, strokePaint);
        if (adjustable) {
            knob.draw(canvas, center, radius, startAngle, strokePaint, next != null);
        }
        drawPercentValue(canvas, startAngle, radius / 2.0f);
    }

    public void drawPercentValue(Canvas canvas, float startAngle, float radius) {
        double a = Math.toRadians(startAngle + sweepAngle / 2.0f);
        float x = (float) (radius * Math.cos(a) + center.x);
        float y = (float) (radius * Math.sin(a) + center.y);
        String text = ((int) Math.round(value)) + "%";
        valueTextPaint.getTextBounds(text, 0, text.length(), textBound);
        canvas.drawText(text, x, y + textBound.height() / 2.0f, valueTextPaint);
    }
    
    private void updateBounds() {
        bounds.set(center.x - radius, center.y - radius, center.x + radius, center.y + radius);
    }

    public double getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }

    public int getRadius() {
        return radius;
    }

    /**
     * Get this sector angle in degrees.
     * @return sweep angle in degrees
     */
    public double getSweepAngle() {
        return sweepAngle;
    }

    public Paint getFillPaint() {
        return fillPaint;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setValue(double value) {
        this.value = value;
        // calculate sweep angle in degrees
        this.sweepAngle = (value / 100.0f) *  360;
    }

    public void setFillColour(@ColorInt int colour) {
        fillPaint.setColor(colour);
    }

    public void setStrokeColour(@ColorInt int colour) {
        strokePaint.setColor(colour);
    }

    public void setKnobRadius(float radius) {
        knob.setRadius(radius);
    }
    
    public void setRadius(int radius) {
        this.radius = radius;
        updateBounds();
    }
    
    public void setCenter(Point c) {
        center.set(c.x, c.y);
        updateBounds();
    }

    public void setStrokeWidth(int strokeWidth) {
        strokePaint.setStrokeWidth(strokeWidth);
    }

    public void setValueTextSize(int size) {
        valueTextPaint.setTextSize(size);
    }

    public void setValueTextColour(@ColorInt int colour) {
        valueTextPaint.setColor(colour);
    }

    @NonNull
    @Override
    public String toString() {
        StringBuilder builder;
        builder = new StringBuilder();
        builder.append("label: ").append(label).append("\n");
        builder.append("value: ").append(value).append("\n");
        builder.append("center: (").append(center.x).append(", ").append(center.y).append(")\n");
        builder.append("radius: ").append(radius).append("\n");
        builder.append("sweep angle: ").append(sweepAngle).append("\n");
        builder.append("knob location: (").append(knob.center.x).append(", ").append(knob.center.y).append(")\n");
        return builder.toString();
    }

    /**
     * Knob is the handle that the user can drag to adjust the sector value.
     */
    public static class Knob {
        private final PointF center = new PointF();
        private final Paint activeFillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        private final Paint inactiveFillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        private final Paint pressedFillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        private float radius;
        private boolean pressed = false;

        public Knob() {
            activeFillPaint.setStyle(Paint.Style.FILL);
            activeFillPaint.setColor(Color.WHITE);
            inactiveFillPaint.setStyle(Paint.Style.FILL);
            inactiveFillPaint.setColor(Color.GRAY);
            pressedFillPaint.setStyle(Paint.Style.FILL);
            pressedFillPaint.setColor(Color.BLUE);
        }

        public void draw(Canvas canvas, Point p0, float r0, float startAngle, Paint strokePaint, boolean active) {
            updateCenter(p0, r0, startAngle);
            float r = pressed ? radius * 1.2f : radius;
            Paint paint = pressed ? pressedFillPaint : active ? activeFillPaint : inactiveFillPaint;
            canvas.drawCircle(center.x, center.y, r, paint);
            canvas.drawCircle(center.x, center.y, r, strokePaint);
        }

        public void updateCenter(Point p0, float r0, float startAngle) {
            final double rad = Math.toRadians(startAngle);
            center.set((float) (p0.x + r0 * Math.cos(rad)), (float) (p0.y + r0 * Math.sin(rad)));
        }

        public PointF getCenter() {
            return center;
        }

        public void setRadius(float radius) {
            this.radius = radius;
        }

        public void setActiveColour(@ColorInt int colour) {
            activeFillPaint.setColor(colour);
        }

        public void setInactiveColour(@ColorInt int colour) {
            inactiveFillPaint.setColor(colour);
        }

        public void setPressedColour(@ColorInt int colour) {
            pressedFillPaint.setColor(colour);
        }

        public void setPressed(boolean pressed) {
            this.pressed = pressed;
        }
    }
}

package com.arcm.dietpiechart;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.text.TextPaint;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

public class Chart {
    public final static String TAG = "Chart";

    public final int maxSize;
    private int size;
    private Sector first;
    private Sector last;

    private final TextPaint legendsTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    private final Point centre = new Point();
    private final Paint orbitPaint;

    private @ColorInt int strokeColour = 0;
    private @ColorInt int valueTextColour = 0;
    private @ColorInt int activeKnobColour = 0;
    private @ColorInt int inactiveKnobColour = 0;
    private @ColorInt int pressedKnobColour = 0;

    private int radius = 0;
    private int orbitsGap = 0;
    private int innerPadding = 0;

    private int strokeWidth = 0;
    private int knobRadius = 0;
    private int valueTextSize = 0;

    private boolean adjustable;


    // Drag event handling variables
    private final DragEventHandler dragHandler = new DragEventHandler();

    {
        this.orbitPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        orbitPaint.setStyle(Paint.Style.STROKE);
    }

    public Chart(int maxSize) {
        this.maxSize = maxSize;
        for (int i = 0; i < maxSize; i++) add(new Sector());
    }

    public void add(Sector sector) {
        if (size > maxSize) {
            throw new IllegalStateException("Sectors full!");
        }

        if (first == null) {
            this.first = sector;
        } else {
            last.next = sector;
            sector.prev = last;
        }
        this.last = sector;

        setCommonSectorAttributes(sector);
        size++;
    }

    private void setCommonSectorAttributes(Sector sector) {
        if (radius > 0) sector.setRadius(radius - size * orbitsGap);
        if (valueTextSize > 0) sector.setValueTextSize(valueTextSize);
        if (strokeWidth > 0) sector.setStrokeWidth(strokeWidth);
        if (strokeColour != 0) sector.setStrokeColour(strokeColour);
        if (valueTextColour != 0) sector.setValueTextColour(valueTextColour);

        if (knobRadius > 0) sector.knob.setRadius(knobRadius);
        if (activeKnobColour != 0) sector.knob.setActiveColour(activeKnobColour);
        if (inactiveKnobColour != 0) sector.knob.setInactiveColour(inactiveKnobColour);
        if (pressedKnobColour != 0) sector.knob.setPressedColour(pressedKnobColour);
    }

    public void draw(Canvas canvas) {
        drawOrbits(canvas);
        float startAngle = 270;
        for (Sector cur = first; cur != null; cur = cur.next) {
            startAngle -= cur.getSweepAngle();
            cur.draw(canvas, startAngle, adjustable);
        }
        drawLegends(canvas);
    }

    private void drawOrbits(Canvas canvas) {
        int r = radius - knobRadius - strokeWidth - innerPadding;
        for (int i = 0; i < size; i++) {
            canvas.drawCircle(centre.x, centre.y, r - i * orbitsGap, orbitPaint);
        }
        canvas.drawLine(centre.x, centre.y, centre.x, centre.y - r, orbitPaint);
    }

    private void drawLegends(Canvas canvas) {
        int textHeight = getLabelHeight();
        int margin = textHeight / 2;
        int x = centre.x - radius;
        int y = centre.y - radius - getLegendsHeight();
        for (Sector cur = first; cur != null; cur = cur.next) {
            canvas.drawRect(x, y, x + textHeight, y + textHeight, cur.getFillPaint());
            canvas.drawText(cur.getLabel(), x + textHeight + margin * 2, y + textHeight, legendsTextPaint);
            y += textHeight + margin;
        }
    }

    public void onDown(PointF p) {
        dragHandler.onDragStarted(p, first);
    }

    public void onMove(PointF p) {
        dragHandler.onDrag(p);
    }

    public void onUp() {
        dragHandler.onDragEnded();
    }
    
    public boolean isDragging() {
        return dragHandler.isDragging();
    }

    public void setLabels(String... labels) {
        Sector cur = first;
        for (int i = 0; i < Math.min(labels.length, size); i++) {
            cur.setLabel(labels[i]);
            cur = cur.next;
        }
    }

    public void setValues(int[] values) {
        Sector cur = first;
        for (int i = 0; i < Math.min(values.length, size); i++) {
            cur.setValue(values[i]);
            cur = cur.next;
        }
    }

    public void setFillColours(@ColorInt int... colours) {
        Sector cur = first;
        for (int i = 0; i < Math.min(colours.length, size); i++) {
            cur.setFillColour(colours[i]);
            cur = cur.next;
        }
    }

    public void setStrokeColours(@ColorInt int... colours) {
        Sector cur = first;
        for (int i = 0; i < Math.min(colours.length, size); i++) {
            cur.setStrokeColour(colours[i]);
            cur = cur.next;
        }
    }

    public void setStrokeColour(@ColorInt int colour) {
        for (Sector cur = first; cur != null; cur = cur.next) {
            cur.setStrokeColour(colour);
        }
        this.strokeColour = colour;
        orbitPaint.setColor(colour);
    }

    public int getLabelHeight() {
        Rect maxBounds = new Rect();
        Rect tmpBounds = new Rect();
        for (Sector cur = first; cur != null; cur = cur.next) {
            legendsTextPaint.getTextBounds(cur.getLabel(), 0, cur.getLabel().length(), tmpBounds);
            if (tmpBounds.height() > maxBounds.height()) {
                maxBounds = tmpBounds;
            }
        }
        return maxBounds.height();
    }

    public int getLegendsHeight() {
        return getLabelHeight() * 4;
    }

    public void setDimensions(int width, int height, int cx, int cy) {
        double h = height / 5.0; // proposed legends box height
        this.radius = width / 2;
        // if proposed height is lower than expected height decrease font sizes
        if (h < getLegendsHeight()) {
            double f = h / getLegendsHeight(); // legends box shrink factor
            setLegendsTextSize((int) (legendsTextPaint.getTextSize() * f));
            setValueTextSize((int) (valueTextSize * f));
        }
        setCentre(cx, cy);
        // if orbits gap is too larger decrease it
        if (radius / 4.0 < orbitsGap) {
            this.orbitsGap = radius / 4;
            setKnobRadius(Math.min(knobRadius, orbitsGap / 2));
        }
        updateSectorsRadii();
    }

    private void updateSectorsRadii() {
        int r = radius - knobRadius - strokeWidth - innerPadding;
        for (Sector cur = first; cur != null; cur = cur.next) {
            cur.setRadius(r);
            if (adjustable) r -= orbitsGap;
        }
    }

    public void setOrbitsGap(int orbitsGap) {
        this.orbitsGap = orbitsGap;
        updateSectorsRadii();
    }

    public void setInnerPadding(int innerPadding) {
        this.innerPadding = innerPadding;
    }

    public void setKnobRadius(int knobRadius) {
        for (Sector cur = first; cur != null; cur = cur.next) {
            cur.setKnobRadius(knobRadius);
        }
        this.knobRadius = knobRadius;
    }

    public void setCentre(int x, int y) {
        centre.set(x, y);
        for (Sector cur = first; cur != null; cur = cur.next) {
            cur.setCenter(centre);
        }
        dragHandler.setCentre(centre);
    }

    public void setStrokeWidth(int strokeWidth) {
        for (Sector cur = first; cur != null; cur = cur.next) {
            cur.setStrokeWidth(strokeWidth);
        }
        this.strokeWidth = strokeWidth;
        orbitPaint.setStrokeWidth(strokeWidth);
    }

    public void setTouchRadius(int radius) {
        dragHandler.setTouchRadius(radius);
    }

    public void setValueTextSize(int size) {
        for (Sector cur = first; cur != null; cur = cur.next) {
            cur.setValueTextSize(size);
        }
        this.valueTextSize = size;
    }

    public void setValueTextColour(@ColorInt int colour) {
        for (Sector cur = first; cur != null; cur = cur.next) {
            cur.setValueTextColour(colour);
        }
        this.valueTextColour = colour;
    }

    public void setLegendsTextSize(int size) {
        legendsTextPaint.setTextSize(size);
    }

    public void setLegendsTextColour(@ColorInt int colour) {
        legendsTextPaint.setColor(colour);
    }

    public void setActiveKnobColour(@ColorInt int colour) {
        for (Sector cur = first; cur != null; cur = cur.next) {
            cur.knob.setActiveColour(colour);
        }
        this.activeKnobColour = colour;
    }

    public void setInactiveKnobColour(@ColorInt int colour) {
        for (Sector cur = first; cur != null; cur = cur.next) {
            cur.knob.setInactiveColour(colour);
        }
        this.inactiveKnobColour = colour;
    }

    public void setPressedKnobColour(@ColorInt int colour) {
        for (Sector cur = first; cur != null; cur = cur.next) {
            cur.knob.setPressedColour(colour);
        }
        this.pressedKnobColour = colour;
    }

    public void getValues(int[] values) {
        int i = 0;
        for (Sector cur = first; cur != null; cur = cur.next) {
            values[i++] = (int) Math.round(cur.getValue());
        }
    }

    public boolean hasValuesChanged(int[] values) {
        int i = 0;
        for (Sector cur = first; cur != null; cur = cur.next) {
            if (values[i++] != (int) cur.getValue()) return true;
        }
        return false;
    }

    public void setAdjustable(boolean adjustable) {
        this.adjustable = adjustable;
    }

    @NonNull
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("center: (").append(centre.x).append(", ").append(centre.y).append(")\n");
        builder.append("radius: ").append(radius).append("\n");
        builder.append("orbits gap: ").append(orbitsGap).append("\n");
        builder.append("\n");
        for (Sector cur = first; cur != null; cur = cur.next) {
            builder.append(cur.toString()).append("\n");
        }
        return builder.toString();
    }
}

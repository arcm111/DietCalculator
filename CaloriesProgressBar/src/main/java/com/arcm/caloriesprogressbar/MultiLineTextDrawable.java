package com.arcm.caloriesprogressbar;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextPaint;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

public class MultiLineTextDrawable {
    private final TextPaint textPaint;
    private TextDrawable[] textDrawables;
    private String text;
    private int width, height, x, y;

    public MultiLineTextDrawable() {
        this.textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    }

    public void setTextPaintParameters(Paint.Align align, int textSize, @ColorInt int colour) {
        setTextAlign(align);
        setTextColour(colour);
        setTextSize(textSize);
    }

    public void setTextAlign(Paint.Align align) {
        textPaint.setTextAlign(align);
    }

    public void setTextColour(@ColorInt int colour) {
        textPaint.setColor(colour);
    }

    public void setTextSize(int textSize) {
        textPaint.setTextSize(textSize);
        if (text != null) invalidateTextMeasurements();
    }
    public int height() {
        return height;
    }

    public int width() {
        return width;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    private int measureLineHeight() {
        Paint.FontMetrics metrics = textPaint.getFontMetrics();
        return (int) (metrics.descent - metrics.ascent);
    }

    private int measureLineSpacing() {
        Paint.FontMetrics metrics = textPaint.getFontMetrics();
        return (int) (-(metrics.top - metrics.ascent) + (metrics.bottom - metrics.descent));
    }

    public void setText(String text) {
        this.text = text;
        invalidateTextMeasurements();
    }

    private void invalidateTextMeasurements() {
        String[] lines = text.split("\n");
        int n = lines.length;
        int lineHeight = measureLineHeight();
        int spacing = measureLineSpacing();
        this.textDrawables = new TextDrawable[lines.length];
        this.height = n * lineHeight + (n - 1) * spacing;
        int q = 0;
        for (int i = n - 1; i >= 0; i--) {
            TextDrawable t = new TextDrawable(textPaint, lines[i]);
            width = Math.max(width, t.width());
            if (i < n - 1) {
                q -= spacing;
            }
            t.setY(q);
            q -= lineHeight;
            textDrawables[i] = t;
        }
    }

    public void draw(Canvas canvas) {
        for (TextDrawable t : textDrawables) {
            canvas.drawText(t.getText(), x, y + t.getY() - t.textOffset(), textPaint);
        }
    }

    @NonNull
    @Override
    public String toString() {
        StringBuilder builder;
        builder = new StringBuilder();
        builder.append("Text: ").append("\n");
        for (TextDrawable t : textDrawables) builder.append(t.getText()).append("\n");
        builder.append("Height: ").append(height).append("\n");
        builder.append("Width: ").append(width).append("\n");
        builder.append("Position: (").append(x).append(", ").append(y).append(")\n");
        builder.append("Lines: \n");
        for (TextDrawable t : textDrawables) builder.append(t);
        return builder.toString();
    }
}

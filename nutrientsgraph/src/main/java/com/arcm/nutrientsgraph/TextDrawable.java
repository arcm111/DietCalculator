package com.arcm.nutrientsgraph;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextPaint;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

public class TextDrawable {
    private final TextPaint textPaint;
    private String text;
    private int height;
    private int width;
    private int offset;
    private int x;
    private int y;

    public TextDrawable() {
        this.textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    }

    public TextDrawable(TextPaint textPaint, String text) {
        this.textPaint = textPaint;
        setText(text);
    }

    public void setText(String text) {
        this.text = text;
        this.width = measureTextWidth(text);
        this.height = measureTextHeight();
        this.offset = measureTextOffset();
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
    }

    public String getText() {
        return text;
    }

    public int height() {
        return height;
    }

    public int width() {
        return width;
    }

    public int textOffset() {
        return offset;
    }

    public int baseline() {
        return height - offset;
    }

    public void setPosition(int x, int y) {
        setX(x);
        setY(y);
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int measureTextWidth(String text) {
        return (int) textPaint.measureText(text);
    }

    private int measureTextHeight() {
        Paint.FontMetrics metrics = textPaint.getFontMetrics();
        return (int) (metrics.bottom - metrics.top);
    }

    public int measureTextOffset() {
        Paint.FontMetrics metrics = textPaint.getFontMetrics();
        return (int) metrics.bottom;
    }

    public void draw(Canvas canvas) {
        canvas.drawText(text, x, y - offset, textPaint);
    }

    @NonNull
    @Override
    public String toString() {
        StringBuilder builder;
        builder = new StringBuilder();
        builder.append("Text: ").append(text).append("\n");
        builder.append("Height: ").append(height).append("\n");
        builder.append("Width: ").append(width).append("\n");
        builder.append("Base Line Offset: ").append(offset).append("\n");
        builder.append("Position: (").append(x).append(", ").append(y).append(")\n");
        return builder.toString();
    }
}


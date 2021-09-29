package com.arcm.dietpiechart;

import android.graphics.Point;
import android.graphics.PointF;
import android.util.Log;

/**
 * Handles drag events.
 * Valid drag event is when one knob is found within the touch radius of
 * the touch down location <b>and</b> the knob is not the last one int the chain.
 */
public class DragEventHandler {
    public final static String TAG = "DragEventHandler";
    public final double CROSS_ANGLE_THRESHOLD = 1.75 * Math.PI;

    private final PointF centre = new PointF();
    private final PointF touchDown = new PointF();

    private int touchRadius;
    private boolean dragging = false;
    private Sector draggedSector = null;
    private double touchDownAngle = 0;
    private double sectorInitialValue;
    private double neighbourInitialValue;

    private double lastTouchAngle;

    public void onDragStarted(PointF p, Sector node) {
        Sector s = findNearestKnob(p, node);
        if (s != null && s.next != null) {
            this.dragging = true;
            this.draggedSector = s;
            this.sectorInitialValue = draggedSector.getValue();
            this.neighbourInitialValue = draggedSector.next.getValue();
            this.touchDownAngle = measureAngleFromCentre(p);
            this.lastTouchAngle = touchDownAngle;
            draggedSector.knob.setPressed(true);
            touchDown.set(p.x, p.y);
            Log.i(TAG, "touch down: " + p.toString());
            Log.i(TAG, "touch radius: " + touchRadius);
            Log.i(TAG, "touch down angle: " + touchDownAngle);
        } else this.dragging = false;
    }

    public void onDrag(PointF p) {
        if (dragging) {
            double curTouchAngle = measureAngleFromCentre(p);
            Log.i(TAG, "touch angle: " + curTouchAngle);
            if (lastTouchAngle - curTouchAngle > CROSS_ANGLE_THRESHOLD) {
                // if touch angle crosses from 2pi to 0 we increment by 2pi to allow
                // uninterrupted angular movement
                curTouchAngle += Math.PI * 2;
                Log.i(TAG, "new touch angle: " + curTouchAngle);
            } else if (lastTouchAngle - curTouchAngle < -CROSS_ANGLE_THRESHOLD) {
                // or if touch angle crosses from 0 to 2pi we decrement by 2pi to allow
                // uninterrupted angular movement as well
                curTouchAngle -= Math.PI * 2;
                Log.i(TAG, "new touch angle: " + curTouchAngle);
            }
            this.lastTouchAngle = curTouchAngle;
            double dv = getSwipeValue(touchDownAngle - curTouchAngle);
            double sectorNewValue = Math.round(sectorInitialValue + dv);
            double neighbourNewValue = Math.round(neighbourInitialValue - dv);
            double maxValue = sectorInitialValue + neighbourInitialValue;
            if (inRange(sectorNewValue, maxValue) && inRange(neighbourNewValue, maxValue)) {
                draggedSector.setValue(sectorInitialValue + dv);
                draggedSector.next.setValue(neighbourInitialValue - dv);
            }
        }
    }

    public void onDragEnded() {
        if (dragging) {
            draggedSector.setValue(Math.round(draggedSector.getValue()));
            draggedSector.next.setValue(Math.round(draggedSector.next.getValue()));
            draggedSector.knob.setPressed(false);
            this.dragging = false;

            Log.i(TAG, draggedSector.getValue() + "->" + draggedSector.next.getValue());
        }
    }

    private Sector findNearestKnob(PointF p, Sector node) {
        Sector nearest = null;
        double dist = Double.POSITIVE_INFINITY;
        for (Sector cur = node; cur != null; cur = cur.next) {
            double d = distance(cur.knob.getCenter(), p);
            Log.i(TAG, "distance: " + d);
            if (d < dist) {
                dist = d;
                nearest = cur;
            }
        }
        if (dist <= touchRadius) return nearest;
        return null;
    }

    private double distance(PointF p1, PointF p2) {
        return Math.sqrt(Math.pow(p2.x - p1.x, 2) + Math.pow(p2.y - p1.y, 2));
    }

    private boolean inRange(double x, double range) {
        return (x >= 0 && x <= range);
    }

    /**
     * Calculate the radian angle of a point p on the chart's circle.
     *
     * {@code Math.atan2} return results between -pi and +pi with values arranged along the
     * unit circles from 9 O'clock and clockwise as follow:
     * 0 -pi/2 [-pi,+pi] +pi/2 0 => very weird and annoying and gives inconsistent results.
     * to fix it we subtract pi/2 and add 2pi to each negative value and we end up with a
     * unit circle starting at 12 O'clock with values from 0 to 2pi clockwise.
     * @param p the points to measure its angle with the circle's center
     * @return the angle between [12 O'clock <-- center --> p].
     */
    private double measureAngleFromCentre(PointF p) {
        double a = Math.atan2(centre.y - p.y, centre.x - p.x) - (Math.PI / 2);
        if (a < 0) a += Math.PI * 2;
        return a;
    }

    private double getSwipeValue(double angle) {
        return (angle / (2 * Math.PI)) * 100.0f;
    }

    public void setCentre(Point p) {
        centre.set(p.x, p.y);
    }

    public void setTouchRadius(int radius) {
        this.touchRadius = radius;
    }

    public boolean isDragging() {
        return dragging;
    }
}

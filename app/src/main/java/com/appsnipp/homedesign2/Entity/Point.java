package com.appsnipp.homedesign2.Entity;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

public class Point {
    public float x = 0;
    public float y = 0;

    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }
    private void drawPoly(Canvas canvas, int color, Point[] points) {
        // line at minimum...
        if (points.length < 2) {
            return;
        }

        // paint
        Paint polyPaint = new Paint();
        polyPaint.setColor(color);
        polyPaint.setStyle(Paint.Style.FILL);

        // path
        Path polyPath = new Path();
        polyPath.moveTo(points[0].x, points[0].y);
        int i, len;
        len = points.length;
        for (i = 0; i < len; i++) {
            polyPath.lineTo(points[i].x, points[i].y);
        }
        polyPath.lineTo(points[0].x, points[0].y);

        // draw
        canvas.drawPath(polyPath, polyPaint);
    }
}

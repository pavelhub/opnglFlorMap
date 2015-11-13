package com.example.pavel.openglmap.gl.servermodel;

/**
 * Created by Pavel on 11.11.2015.
 */
public class Point {
    public float x;
    public float y;


    public Point getPointParalelWeight(float k, float b) {
        Point point = new Point();
        point.y = k * x + b;
        point.x = (point.y - b) / k;


        return point;
    }

    public float[] getPointMas(float z) {
        return new float[]{x, y, z};
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Point point = (Point) o;

        if (Float.compare(point.x, x) != 0) return false;
        return Float.compare(point.y, y) == 0;

    }

    @Override
    public int hashCode() {
        int result = (x != +0.0f ? Float.floatToIntBits(x) : 0);
        result = 31 * result + (y != +0.0f ? Float.floatToIntBits(y) : 0);
        return result;
    }
}

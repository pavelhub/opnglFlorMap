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

}

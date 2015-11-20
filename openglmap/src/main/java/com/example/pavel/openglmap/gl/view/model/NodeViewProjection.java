package com.example.pavel.openglmap.gl.view.model;

/**
 * Created by pavel on 11/20/15.
 */
public class NodeViewProjection {
    float x;
    float y;

    public float[] getPointMas(float z) {
        return new float[]{x, y, z};
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }



    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

}

package com.example.pavel.openglmap.gl.view.model;

/**
 * Created by pavel on 11/20/15.
 */
public class NodeViewProjection {
    float x;
    float y;
    float z;

    public float[] getPointMas() {
        return new float[]{x, y, z};
    }

}

package com.example.pavel.openglmap.gl.model;

/**
 * Created by Pavel on 20.11.2015.
 */
public abstract class GeneralOpenGLDrawing {
    public abstract void draw(float[] mvpMatrix);
    public void draw(float[] vMatrix, float[] mvMatrix, float[] mvpMatrix)
    {}
}

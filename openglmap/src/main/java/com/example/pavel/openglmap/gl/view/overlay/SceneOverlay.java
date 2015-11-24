package com.example.pavel.openglmap.gl.view.overlay;

import com.example.pavel.openglmap.gl.model.MyGeneralOpenGLES2DrawingClass;
import com.example.pavel.openglmap.net.model.FloorModel;

/**
 * Created by Pavel on 24.11.2015.
 */
public class SceneOverlay extends BaseOverlay {
    FloorModel floorModel;
    MyGeneralOpenGLES2DrawingClass myGeneralOpenGLES2DrawingClass;

    public SceneOverlay(FloorModel floorModel) {
        this.floorModel = floorModel;

        myGeneralOpenGLES2DrawingClass = new MyGeneralOpenGLES2DrawingClass(3,
                new float[]{-2.0f, 2.0f, -0.2f,
                        -2.0f, -2.0f, -0.2f,
                        2.f, -2.00f, -0.2f,
                        2.f, 2.0f, -0.2f},
                new float[]{0.5f, 0.5f, 0.5f, 1.0f},
                new short[]{0, 1, 2, 0, 2, 3});
    }

    @Override
    public void draw(float[] mvpMatrix) {
        myGeneralOpenGLES2DrawingClass.draw(mvpMatrix);
    }
}

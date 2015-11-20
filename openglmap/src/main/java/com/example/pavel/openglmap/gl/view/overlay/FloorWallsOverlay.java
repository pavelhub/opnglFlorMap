package com.example.pavel.openglmap.gl.view.overlay;

import com.example.pavel.openglmap.gl.model.MyGeneralOpenGLES2DrawingClass;
import com.example.pavel.openglmap.gl.view.model.NodeViewModel;
import com.example.pavel.openglmap.net.model.FloorModel;
import com.example.pavel.openglmap.net.model.NodeModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pavel on 10.11.2015.
 */
public class FloorWallsOverlay extends BaseOverlay {

    private short drawOrder[] = {
            0, 1, 2,
            0, 2, 3,

            4, 5, 6,
            4, 6, 7,

            4, 0, 3,
            4, 3, 7,

            5, 1, 2,
            5, 2, 6,

            4, 0, 1,
            4, 1, 5,

            6, 2, 3,
            6, 3, 7

    };
    private short drawOrderTriangle[] = {
            0, 1, 2,
            3, 4, 5,

            0, 2, 4,
            0, 4, 1,

            0, 2, 5,
            0, 5, 3,

            1, 4, 5,
            1, 5, 2,

    };

    public List<MyGeneralOpenGLES2DrawingClass> wallsModel = new ArrayList<>();
    public List<MyGeneralOpenGLES2DrawingClass> triangleDraw = new ArrayList<>();

    public FloorWallsOverlay(FloorModel floorModel) {
        List<NodeViewModel> nodeViewModel;
        for (NodeModel nodeModel : floorModel.getNodeModels()) {

        }
    }


    public void draw(float[] mvpMatrix) {
        for (MyGeneralOpenGLES2DrawingClass model : wallsModel) {
            model.draw(mvpMatrix);
        }
        for (MyGeneralOpenGLES2DrawingClass model : triangleDraw) {
            model.draw(mvpMatrix);
        }
    }

}

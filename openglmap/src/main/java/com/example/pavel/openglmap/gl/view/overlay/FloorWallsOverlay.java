package com.example.pavel.openglmap.gl.view.overlay;

import com.example.pavel.openglmap.gl.model.MyGeneralOpenGLES2DrawingClass;
import com.example.pavel.openglmap.gl.view.model.WallViewModel;
import com.example.pavel.openglmap.gl.view.model.function.WallLinearFunction;
import com.example.pavel.openglmap.net.model.FloorModel;
import com.example.pavel.openglmap.net.model.WallModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pavel on 10.11.2015.
 */
public class FloorWallsOverlay extends BaseOverlay {

    public List<MyGeneralOpenGLES2DrawingClass> wallsModelGl = new ArrayList<>();
    public List<MyGeneralOpenGLES2DrawingClass> triangleDraw = new ArrayList<>();

    public FloorWallsOverlay(FloorModel floorModel, boolean is3d) {
        List<WallViewModel> wallViewModels = new ArrayList<>();
        for (WallModel wallModel : floorModel.getWalls()) {

            WallLinearFunction wallLinearFunction = null;
            if (wallModel.getType().equals("linear")) {
                wallLinearFunction = WallLinearFunction.create(wallModel.getNodeModels().get(0), wallModel.getNodeModels().get(1), wallModel.getDepth());
            }

            if (wallLinearFunction != null) {
                WallViewModel.Builder builder = new WallViewModel.Builder();
                builder.setWallFunction(wallLinearFunction);
                wallViewModels.add(builder.build());
            }
//            break;
        }
        for (WallViewModel wallViewModel : wallViewModels) {
            float[] nodes;
            short[] wallDrawOrder;
            if (is3d) {
                nodes = wallViewModel.getWallNodes3D();
                wallDrawOrder = wallViewModel.getWallDrawOrder3D();
            } else {
                nodes = wallViewModel.getWallNodes2D();
                wallDrawOrder = wallViewModel.getWallDrawOrder2D();
            }
            MyGeneralOpenGLES2DrawingClass object = new MyGeneralOpenGLES2DrawingClass(3,
                    nodes,
                    //                    new float[]{-1.250f, 1.0f, 0.0f,
                    //                            -1.250f, -0.5f, 0.0f,
                    //                            -0.75f, -0.5f, 0.0f,
                    //                            -0.75f, 1.f, 0.0f},
                    new float[]{0.7f, 0.7f, 0.7f, 1.0f},
                    //                    new short[]{0, 1, 2, 0, 2, 3}
                    wallDrawOrder
            );
            wallsModelGl.add(object);
        }

    }


    public void draw(float[] mvpMatrix) {

        for (MyGeneralOpenGLES2DrawingClass model : wallsModelGl) {
            model.draw(mvpMatrix);
        }
        for (MyGeneralOpenGLES2DrawingClass model : triangleDraw) {
            model.draw(mvpMatrix);
        }
    }

}

package com.example.pavel.openglmap.gl.view.overlay;

import com.example.pavel.openglmap.gl.model.GeneralOpenGLDrawing;
import com.example.pavel.openglmap.gl.model.MyGeneralOpenGLES2DrawingClass;
import com.example.pavel.openglmap.gl.model.MyGeneralOpenGLES2DrawingClass3d;
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

    public List<GeneralOpenGLDrawing> wallsModelGl = new ArrayList<>();
    public List<GeneralOpenGLDrawing> triangleDraw = new ArrayList<>();

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
        WallViewModel wallViewModelPrevius = wallViewModels.get(wallViewModels.size() - 1);

        for (WallViewModel wallViewModel : wallViewModels) {

            if (wallViewModelPrevius != null && wallViewModel.getStartPoint().equals(wallViewModelPrevius.getEndPoint())) {
                if (wallViewModel.getWallFunction() instanceof WallLinearFunction && wallViewModelPrevius.getWallFunction() instanceof WallLinearFunction) {
                    wallViewModel.getWallFunction().findPointIntersection(wallViewModel.getWallFunction(), wallViewModelPrevius.getWallFunction());
                }
            }
            wallViewModelPrevius = wallViewModel;
        }

        for (WallViewModel wallViewModel : wallViewModels) {

            float[] nodes;
            short[] wallDrawOrder;
            float[] color;
            GeneralOpenGLDrawing generalOpenGLDrawing;
            if (is3d) {
                nodes = wallViewModel.getWallNodes3D();
                wallDrawOrder = wallViewModel.getWallDrawOrder3D();
                color = wallViewModel.getWallColor3D();
                generalOpenGLDrawing = new MyGeneralOpenGLES2DrawingClass3d(3,
                        nodes,
                        //                    new float[]{-1.250f, 1.0f, 0.0f,
                        //                            -1.250f, -0.5f, 0.0f,
                        //                            -0.75f, -0.5f, 0.0f,
                        //                            -0.75f, 1.f, 0.0f},
                        color,//new float[]{0.7f, 0.7f, 0.7f, 1.0f},
                        //                    new short[]{0, 1, 2, 0, 2, 3}
                        wallDrawOrder);

            } else {
                nodes = wallViewModel.getWallNodes2D();
                wallDrawOrder = wallViewModel.getWallDrawOrder2D();
                color = wallViewModel.getWallColor2D();
                generalOpenGLDrawing = new MyGeneralOpenGLES2DrawingClass(3,
                        nodes,
                        //                    new float[]{-1.250f, 1.0f, 0.0f,
                        //                            -1.250f, -0.5f, 0.0f,
                        //                            -0.75f, -0.5f, 0.0f,
                        //                            -0.75f, 1.f, 0.0f},
                        color,//new float[]{0.7f, 0.7f, 0.7f, 1.0f},
                        //                    new short[]{0, 1, 2, 0, 2, 3}
                        wallDrawOrder);

            }
            wallsModelGl.add(generalOpenGLDrawing);

        }

    }


    public void draw(float[] vMatrix, float[] mMatrix, float[] pMatrix, float[] mvpMatrix) {

        for (GeneralOpenGLDrawing model : wallsModelGl) {
            if (model instanceof MyGeneralOpenGLES2DrawingClass3d) {
                model.draw(vMatrix, mMatrix, pMatrix);
            } else {

                model.draw(mvpMatrix);
            }

        }
        for (GeneralOpenGLDrawing model : triangleDraw) {
            model.draw(mvpMatrix);
        }
    }

    @Override
    public void draw(float[] mvpMatrix) {

    }
}

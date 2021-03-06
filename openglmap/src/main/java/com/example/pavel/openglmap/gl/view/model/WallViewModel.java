package com.example.pavel.openglmap.gl.view.model;

import com.example.pavel.openglmap.gl.view.model.function.WallLinearFunction;

/**
 * Created by Pavel on 20.11.2015.
 */
public class WallViewModel {
    //    NodeViewModel nodeViewModelStart;
//    NodeViewModel nodeViewModelEnd;
    WallLinearFunction wallFunction;
    String type;

    public WallLinearFunction getWallFunction() {
        return wallFunction;
    }

    public void setWallFunction(WallLinearFunction wallFunction) {
        this.wallFunction = wallFunction;
    }

    public NodeViewModel getStartPoint() {
        return wallFunction.getStart();

    }

    public NodeViewModel getEndPoint() {
        return wallFunction.getEnd();

    }

    private WallViewModel() {

    }

    public float[] getWallNodes2D() {

        return wallFunction.getCoordinatesModel(false);
    }

    public short[] getWallDrawOrder2D() {

        return wallFunction.getDrawOrder(false);

    }

    public float[] getWallNodes3D() {

        return wallFunction.getCoordinatesModel(true);
    }

    public short[] getWallDrawOrder3D() {

        return wallFunction.getDrawOrder(true);
    }

    public float[] getWallColor2D() {

        return wallFunction.getColor(false);
    }

    public float[] getWallColor3D() {

        return wallFunction.getColor(true);
    }
//
//    private WallViewModel(WallModel wallModel) {
//        nodeViewModelStart = new NodeViewModel(wallModel.getNodeModels().get(0));
//        nodeViewModelEnd = new NodeViewModel(wallModel.getNodeModels().get(1));
//
//        wallFunction = WallLinearFunction.create(nodeViewModelStart, nodeViewModelEnd);
//
//    }

    public static class Builder {
        WallViewModel wallViewModel;

        public Builder() {
            wallViewModel = new WallViewModel();
        }

        public void setWallFunction(WallLinearFunction wallFunction) {
            wallViewModel.wallFunction = wallFunction;
        }

        public WallViewModel build() {
            return wallViewModel;
        }
    }

}

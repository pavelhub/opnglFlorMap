package com.example.pavel.openglmap.gl.view.model.function;

import com.example.pavel.openglmap.gl.view.model.NodeViewModel;
import com.example.pavel.openglmap.gl.view.model.NodeViewProjection;
import com.example.pavel.openglmap.net.model.NodeModel;

/**
 * Created by Pavel on 20.11.2015.
 */
public class WallLinearFunction {
    float k;
    float b;
    float kPerp;
    float bPerpStart;
    float bPerpEnd;
    float kParale;
    float bParalelLeft;
    float bParalelRight;
    float totalDistance;
    NodeViewModel start;
    NodeViewModel end;

    public static WallLinearFunction create(NodeModel start, NodeModel end, float depth) {
        return new WallLinearFunction(start, end, depth);
    }

    private WallLinearFunction(NodeModel start, NodeModel end, float depth) {
        getK(start, end);
        getB(start, end);

        if (Math.abs(k) == 0.0)
            kPerp = k = 0.0f;
        else
            kPerp = -1 / k;
        kParale = k;

        if (Math.abs(k) == 0.0)
            kPerp = k = 0.0f;
        else
            kPerp = -1 / k;

        bPerpStart = getB(start, kPerp);
        bPerpEnd = getB(end, kPerp);
        float halfDepth = depth / 2.f;

        bParalelRight = (float) (halfDepth * Math.sqrt(k * k + 1) + b);
        bParalelLeft = (float) (-halfDepth * Math.sqrt(k * k + 1) + b);
        if (k == 0) {
            bParalelLeft = b - halfDepth;
            bParalelRight = b + halfDepth;
        }
        totalDistance = (float) getDistanceToSegment(start, end);

        this.start = new NodeViewModel(start);
        this.end = new NodeViewModel(end);

        buildProjections();
    }

    private void buildProjections() {

        start.setLeft(getPointParalePerp(start, kParale, bParalelLeft, kPerp, bPerpStart));
        start.setRight(getPointParalePerp(start, kParale, bParalelRight, kPerp, bPerpStart));
        end.setLeft(getPointParalePerp(end, kParale, bParalelLeft, kPerp, bPerpEnd));
        end.setRight(getPointParalePerp(end, kParale, bParalelRight, kPerp, bPerpEnd));
    }

    public float[] getCoordinatesModel(boolean is3d) {
        float[] coordinates = new float[4 * 3 * (is3d ? 2 : 1)];
        int arrayPosition = 0;
        //0
        float[] pointMas = start.getLeft().getPointMas(0f);
        System.arraycopy(pointMas, 0, coordinates, arrayPosition, pointMas.length);
        arrayPosition += pointMas.length;
//1


        pointMas = end.getLeft().getPointMas(0f);
        System.arraycopy(pointMas, 0, coordinates, arrayPosition, pointMas.length);
        arrayPosition += pointMas.length;
//2
        pointMas = end.getRight().getPointMas(0f);
        System.arraycopy(pointMas, 0, coordinates, arrayPosition, pointMas.length);
        arrayPosition += pointMas.length;
//3
        pointMas = start.getRight().getPointMas(0f);
        System.arraycopy(pointMas, 0, coordinates, arrayPosition, pointMas.length);
        arrayPosition += pointMas.length;


        if (is3d) {
            //4
            pointMas = start.getLeft().getPointMas(-1f);
            System.arraycopy(pointMas, 0, coordinates, arrayPosition, pointMas.length);
            arrayPosition += pointMas.length;
//1
            pointMas = end.getLeft().getPointMas(-1f);
            System.arraycopy(pointMas, 0, coordinates, arrayPosition, pointMas.length);
            arrayPosition += pointMas.length;
//2
            pointMas = end.getRight().getPointMas(-1f);
            System.arraycopy(pointMas, 0, coordinates, arrayPosition, pointMas.length);
            arrayPosition += pointMas.length;
//3
            pointMas = start.getRight().getPointMas(-1f);
            System.arraycopy(pointMas, 0, coordinates, arrayPosition, pointMas.length);
            arrayPosition += pointMas.length;



        }

        return coordinates;
    }

    public short[] getDrawOrder(boolean is3D) {
        if (!is3D)
            return new short[]{
                    0, 1, 2,
                    0, 2, 3,
            };
        else
            return new short[]{
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
    }


    public float[] getColor(boolean is3D) {
        if (!is3D)
            return new float[]{
                    1.0f, 1.0f, 1.0f, 1.f
            };
        else {
            return new float[]
                    {
//                            // Front face (red)
                            1.0f, 0.0f, 0.0f, 1.0f,
                            1.0f, 0.0f, 0.0f, 1.0f,
                            1.0f, 0.0f, 0.0f, 1.0f,
                            1.0f, 0.0f, 0.0f, 1.0f,
                            1.0f, 0.0f, 0.0f, 1.0f,
                            1.0f, 0.0f, 0.0f, 1.0f,

                            // Right face (green)
                            0.0f, 1.0f, 0.0f, 1.0f,
                            0.0f, 1.0f, 0.0f, 1.0f,
                            0.0f, 1.0f, 0.0f, 1.0f,
                            0.0f, 1.0f, 0.0f, 1.0f,
                            0.0f, 1.0f, 0.0f, 1.0f,
                            0.0f, 1.0f, 0.0f, 1.0f,

                            // Back face (blue)
                            0.0f, 0.0f, 1.0f, 1.0f,
                            0.0f, 0.0f, 1.0f, 1.0f,
                            0.0f, 0.0f, 1.0f, 1.0f,
                            0.0f, 0.0f, 1.0f, 1.0f,
                            0.0f, 0.0f, 1.0f, 1.0f,
                            0.0f, 0.0f, 1.0f, 1.0f,

                            // Left face (yellow)
                            1.0f, 1.0f, 0.0f, 1.0f,
                            1.0f, 1.0f, 0.0f, 1.0f,
                            1.0f, 1.0f, 0.0f, 1.0f,
                            1.0f, 1.0f, 0.0f, 1.0f,
                            1.0f, 1.0f, 0.0f, 1.0f,
                            1.0f, 1.0f, 0.0f, 1.0f,

                            // Top face (cyan)
                            0.0f, 1.0f, 1.0f, 1.0f,
                            0.0f, 1.0f, 1.0f, 1.0f,
                            0.0f, 1.0f, 1.0f, 1.0f,
                            0.0f, 1.0f, 1.0f, 1.0f,
                            0.0f, 1.0f, 1.0f, 1.0f,
                            0.0f, 1.0f, 1.0f, 1.0f,

                            // Bottom face (magenta)
                            1.0f, 0.0f, 1.0f, 1.0f,
                            1.0f, 0.0f, 1.0f, 1.0f,
                            1.0f, 0.0f, 1.0f, 1.0f,
                            1.0f, 0.0f, 1.0f, 1.0f,
                            1.0f, 0.0f, 1.0f, 1.0f,
                            1.0f, 0.0f, 1.0f, 1.0f
                    };
//        }
//            return new float[]{
//                    1.0f, 1.0f, 1.0f, 1.f,
//                    1.0f, 1.0f, 1.0f, 1.f,
//                    1.0f, 1.0f, 1.0f, 1.f,
//                    1.0f, 1.0f, 1.0f, 1.f,
//
//                    1.0f, 1.0f, 1.0f, 1.f,
//                    1.0f, 1.0f, 1.0f, 1.f,
//                    1.0f, 1.0f, 1.0f, 1.f,
//                    1.0f, 1.0f, 1.0f, 1.f,
//
//                    1.0f, 1.0f, 1.0f, 1.f,
//                    1.0f, 1.0f, 1.0f, 1.f,
//                    1.0f, 1.0f, 1.0f, 1.f,
//                    1.0f, 1.0f, 1.0f, 1.f,
//
//                    0.5f, 0.5f, 0.0f, 1.f,
//                    0.5f, 0.5f, 0.0f, 1.f,
//                    0.5f, 0.5f, 0.0f, 1.f,
//                    0.5f, 0.5f, 0.0f, 1.f,
//
//                    0.7f, 0.7f, 0.7f, 1.f,
//                    0.7f, 0.7f, 0.7f, 1.f,
//                    0.7f, 0.7f, 0.7f, 1.f,
//                    0.7f, 0.7f, 0.7f, 1.f,
//
//                    0.7f, 0.7f, 0.7f, 1.f,
//                    0.7f, 0.7f, 0.7f, 1.f,
//                    0.7f, 0.7f, 0.7f, 1.f,
//                    0.7f, 0.7f, 0.7f, 1.f,
//            };
        }
    }

    private NodeViewProjection getPointParalePerp(NodeViewModel point, float kParale, float bParalel, float kPerp, float bPerp) {
        NodeViewProjection nodeProjection = new NodeViewProjection();
        if (start.getX() == end.getX()) {
            nodeProjection.setY(point.getY());
            nodeProjection.setX(bParalel);
        } else if (start.getY() == end.getY()) {
            nodeProjection.setX(point.getX());
            nodeProjection.setY(bParalel);
        } else {
            nodeProjection.setX(getPerpX(kParale, kPerp, bParalel, bPerp));
            nodeProjection.setY(getPerpY(nodeProjection.getX(), kPerp, bPerp));
        }

        return nodeProjection;
    }


    private void getK(NodeModel start, NodeModel end) {
        float deltaX = start.getX() - end.getX();
        float deltaY = start.getY() - end.getY();
        if (deltaX == 0) {
            k = 0;
        } else {
            k = deltaY
                    /
                    (deltaX * 1.0f);
        }


    }

    private void getB(NodeModel start, NodeModel end) {
        float deltaX = start.getX() - end.getX();
        float deltaY = start.getY() - end.getY();
        if (k == 0) {
            if (deltaX == 0)
                b = start.getX();
            if (deltaY == 0)
                b = start.getY();
        } else
            b = getB(start, k);
    }

    private float getB(NodeModel nodeModel, float k) {
        return nodeModel.getY() - k * nodeModel.getX();
    }

    private float getPerpX(float kParalel, float kPerp, float bParalel, float bPerp) {
        float deltaK = kParalel - kPerp;
        if (deltaK == 0.0f)
            deltaK = 1.0f;
        return (bPerp - bParalel) / deltaK;
    }

    private float getPerpY(float x, float kPerp, float bPerp) {
        return kPerp * x + bPerp;
    }

    public static double getDistanceToSegment(NodeModel start, NodeModel end) {

        return Math.sqrt(
                Math.pow(start.getX() - end.getX(), 2) +
                        Math.pow(start.getY() - end.getY(), 2));
    }
}

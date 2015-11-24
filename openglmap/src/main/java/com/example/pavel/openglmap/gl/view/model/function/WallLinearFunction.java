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
        if (k < 0) {
            bParalelRight = (float) (halfDepth * Math.sqrt(k * k + 1) + b);
            bParalelLeft = (float) (-halfDepth * Math.sqrt(k * k + 1) + b);
        } else if (k > 0) {
            bParalelRight = (float) (-halfDepth * Math.sqrt(k * k + 1) + b);
            bParalelLeft = (float) (halfDepth * Math.sqrt(k * k + 1) + b);
        } else {
            if (start.getX() == end.getX()) {
                if (start.getY() > end.getY()) {
                    bParalelLeft = b + halfDepth;
                    bParalelRight = b - halfDepth;
                } else {
                    bParalelLeft = b - halfDepth;
                    bParalelRight = b + halfDepth;

                }
            } else {
                if (b < 0) {
                    bParalelLeft = b + halfDepth;
                    bParalelRight = b - halfDepth;
                } else {
                    bParalelLeft = b - halfDepth;
                    bParalelRight = b + halfDepth;

                }
            }
        }
//        if(k<0) {
//            bParalelRight = (float) (-halfDepth * Math.sqrt(k * k + 1) + b);
//            bParalelLeft = (float) (halfDepth * Math.sqrt(k * k + 1) + b);
//        }
//        if (k == 0) {
//            if (b < 0) {
//                bParalelLeft = b + halfDepth;
//                bParalelRight = b - halfDepth;
//            } else {
//                bParalelLeft = b - halfDepth;
//                bParalelRight = b + halfDepth;
//
//            }
//        }
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

    public void findPointIntersection(WallLinearFunction wallLinearFunctionStart, WallLinearFunction wallLinearFunctionEnd) {
        NodeViewProjection rightStart = wallLinearFunctionStart.getStart().getRight();
        NodeViewProjection leftStart = wallLinearFunctionStart.getStart().getLeft();
        NodeViewProjection rightEnd = wallLinearFunctionEnd.getEnd().getRight();
        NodeViewProjection leftEnd = wallLinearFunctionEnd.getEnd().getLeft();
        if (rightStart.equals(rightEnd))
            return;
//        if (start.getX() == end.getX()) {
//            nodeProjection.setY(point.getY());
//            nodeProjection.setX(bParalel);
//        } else if (start.getY() == end.getY()) {
//            nodeProjection.setX(point.getX());
//            nodeProjection.setY(bParalel);
//        }
        float a1 = 0, b1 = 0, c1 = 0;

        if (wallLinearFunctionStart.getStart().getX() == wallLinearFunctionStart.getEnd().getX()) {
            b1 = 0;
            a1 = 1;
            c1 = -wallLinearFunctionStart.getStart().getRight().getX();
        } else if (wallLinearFunctionStart.getStart().getY() == wallLinearFunctionStart.getEnd().getY()) {
            b1 = 1;
            a1 = 0;
            c1 = -wallLinearFunctionStart.getStart().getRight().getY();
        } else {
            b1 = -1;
            a1 = wallLinearFunctionStart.kParale;
            c1 = -(a1 * wallLinearFunctionStart.getStart().getRight().getX() + b1 * wallLinearFunctionStart.getStart().getRight().getY());
        }

        float a2 = 0, b2 = 0, c2 = 0;
        if (wallLinearFunctionEnd.getStart().getX() == wallLinearFunctionEnd.getEnd().getX()) {
            b2 = 0;
            a2 = 1;
            c2 = -wallLinearFunctionEnd.getEnd().getRight().getX();
        } else if (wallLinearFunctionEnd.getStart().getY() == wallLinearFunctionEnd.getEnd().getY()) {
            b2 = 1;
            a2 = 0;
            c2 = -wallLinearFunctionEnd.getEnd().getRight().getY();
        } else {
            b2 = -1;
            a2 = wallLinearFunctionEnd.kParale;
            c2 = -(a2 * wallLinearFunctionEnd.getEnd().getRight().getX() + b2 * wallLinearFunctionEnd.getEnd().getRight().getY());
        }

        NodeViewProjection nodeViewIntersectionRight = new NodeViewProjection();
        float rightXIntersection = -(c1 * b2 - c2 * b1) / (a1 * b2 - a2 * b1);
        float rightYIntersection = -(a1 * c2 - a2 * c1) / (a1 * b2 - a2 * b1);
        nodeViewIntersectionRight.setX(rightXIntersection);
        nodeViewIntersectionRight.setY(rightYIntersection);
        wallLinearFunctionStart.getStart().setRight(nodeViewIntersectionRight);
        wallLinearFunctionEnd.getEnd().setRight(nodeViewIntersectionRight);


        if (wallLinearFunctionStart.getStart().getX() == wallLinearFunctionStart.getEnd().getX()) {
            b1 = 0;
            a1 = 1;
            c1 = -wallLinearFunctionStart.getStart().getLeft().getX();
        } else if (wallLinearFunctionStart.getStart().getY() == wallLinearFunctionStart.getEnd().getY()) {
            b1 = 1;
            a1 = 0;
            c1 = -wallLinearFunctionStart.getStart().getLeft().getY();
        } else {
            b1 = -1;
            a1 = wallLinearFunctionStart.kParale;
            c1 = -(a1 * wallLinearFunctionStart.getStart().getLeft().getX() + b1 * wallLinearFunctionStart.getStart().getLeft().getY());
        }


        if (wallLinearFunctionEnd.getStart().getX() == wallLinearFunctionEnd.getEnd().getX()) {
            b2 = 0;
            a2 = 1;
            c2 = -wallLinearFunctionEnd.getEnd().getLeft().getX();
        } else if (wallLinearFunctionEnd.getStart().getY() == wallLinearFunctionEnd.getEnd().getY()) {
            b2 = 1;
            a2 = 0;
            c2 = -wallLinearFunctionEnd.getEnd().getLeft().getY();
        } else {
            b2 = -1;
            a2 = wallLinearFunctionEnd.kParale;
            c2 = -(a2 * wallLinearFunctionEnd.getEnd().getLeft().getX() + b2 * wallLinearFunctionEnd.getEnd().getLeft().getY());
        }

        NodeViewProjection nodeViewIntersectionLeft = new NodeViewProjection();
        float leftXIntersection = -(c1 * b2 - c2 * b1) / (a1 * b2 - a2 * b1);
        float leftYIntersection = -(a1 * c2 - a2 * c1) / (a1 * b2 - a2 * b1);
        nodeViewIntersectionLeft.setX(leftXIntersection);
        nodeViewIntersectionLeft.setY(leftYIntersection);
        wallLinearFunctionStart.getStart().setLeft(nodeViewIntersectionLeft);
        wallLinearFunctionEnd.getEnd().setLeft(nodeViewIntersectionLeft);

///kx+b=y
//        if (wallLinearFunctionStart.kParale == wallLinearFunctionEnd.kParale && wallLinearFunctionEnd.kParale != 0) {
//            nodeViewIntersectionRight = new NodeViewProjection();
//            rightXIntersection = getXIntersections(wallLinearFunctionStart.kParale, wallLinearFunctionStart.bParalelRight, wallLinearFunctionEnd.kParale, wallLinearFunctionEnd.bParalelRight);
//            rightYIntersection = getY(rightXIntersection, wallLinearFunctionStart.kParale, wallLinearFunctionStart.bParalelRight);
//            nodeViewIntersectionRight.setX(rightXIntersection);
//            nodeViewIntersectionRight.setY(rightYIntersection);
//            wallLinearFunctionStart.getStart().setRight(nodeViewIntersectionRight);
//            wallLinearFunctionEnd.getEnd().setRight(nodeViewIntersectionRight);
//        }
//        NodeViewProjection nodeViewIntersectionLeft = new NodeViewProjection();
//        float leftXIntersection = getXIntersections(wallLinearFunctionStart.kParale, wallLinearFunctionStart.bParalelLeft, wallLinearFunctionEnd.kParale, wallLinearFunctionEnd.bParalelLeft);
//        float leftYIntersection = getY(leftXIntersection, wallLinearFunctionStart.kParale, wallLinearFunctionStart.bParalelLeft);
//        nodeViewIntersectionLeft.setX(leftXIntersection);
//        nodeViewIntersectionLeft.setY(leftYIntersection);
//        wallLinearFunctionStart.getStart().setLeft(nodeViewIntersectionLeft);
//        wallLinearFunctionEnd.getEnd().setRight(nodeViewIntersectionLeft);

    }

    private float getXIntersections(float k1, float b1, float k2, float b2) {
        float deltaK = k1 - k2;
        if (deltaK == 0.0f)
            deltaK = 1.0f;
        return (b2 - b1) / deltaK;
    }

    public float[] getCoordinatesModel(boolean is3d) {
        float[] coordinates = new float[4 * 3 * (is3d ? 9 : 1)];
        int arrayPosition = 0;
        //0
        float z = 0;
        NodeViewProjection rightStart = start.getRight();
        NodeViewProjection rightEnd = end.getRight();
        NodeViewProjection leftEnd = end.getLeft();
        NodeViewProjection leftStart = start.getLeft();
//        if (start.getY() > end.getY()) {
//            rightStart = end.getRight();
//            rightEnd = start.getRight();
//            leftEnd = start.getLeft();
//            leftStart = end.getLeft();
//        }

        float[] pointMas = rightStart.getPointMas(z);
        System.arraycopy(pointMas, 0, coordinates, arrayPosition, pointMas.length);
        arrayPosition += pointMas.length;
//1

        pointMas = rightEnd.getPointMas(z);
        System.arraycopy(pointMas, 0, coordinates, arrayPosition, pointMas.length);
        arrayPosition += pointMas.length;
//2

        pointMas = leftEnd.getPointMas(z);
        System.arraycopy(pointMas, 0, coordinates, arrayPosition, pointMas.length);
        arrayPosition += pointMas.length;
//3

        pointMas = leftStart.getPointMas(z);
        System.arraycopy(pointMas, 0, coordinates, arrayPosition, pointMas.length);
        arrayPosition += pointMas.length;

        if (is3d) {
            pointMas = rightStart.getPointMas(z);
            System.arraycopy(pointMas, 0, coordinates, arrayPosition, pointMas.length);
            arrayPosition += pointMas.length;

            pointMas = leftEnd.getPointMas(z);
            System.arraycopy(pointMas, 0, coordinates, arrayPosition, pointMas.length);
            arrayPosition += pointMas.length;

            //down
            float heightOfFloor = -0.2f;
            z = heightOfFloor;
            pointMas = rightStart.getPointMas(z);
            System.arraycopy(pointMas, 0, coordinates, arrayPosition, pointMas.length);
            arrayPosition += pointMas.length;
//1
            pointMas = rightEnd.getPointMas(z);
            System.arraycopy(pointMas, 0, coordinates, arrayPosition, pointMas.length);
            arrayPosition += pointMas.length;
//2
            pointMas = leftEnd.getPointMas(z);
            System.arraycopy(pointMas, 0, coordinates, arrayPosition, pointMas.length);
            arrayPosition += pointMas.length;
//3
            pointMas = leftStart.getPointMas(z);
            System.arraycopy(pointMas, 0, coordinates, arrayPosition, pointMas.length);
            arrayPosition += pointMas.length;

            pointMas = rightStart.getPointMas(z);
            System.arraycopy(pointMas, 0, coordinates, arrayPosition, pointMas.length);
            arrayPosition += pointMas.length;

            pointMas = leftEnd.getPointMas(z);
            System.arraycopy(pointMas, 0, coordinates, arrayPosition, pointMas.length);
            arrayPosition += pointMas.length;

            //front
            z = heightOfFloor;
            pointMas = leftEnd.getPointMas(z);
            System.arraycopy(pointMas, 0, coordinates, arrayPosition, pointMas.length);
            arrayPosition += pointMas.length;

            pointMas = leftStart.getPointMas(z);
            System.arraycopy(pointMas, 0, coordinates, arrayPosition, pointMas.length);
            arrayPosition += pointMas.length;
//1

            z = 0;
            pointMas = leftStart.getPointMas(z);
            System.arraycopy(pointMas, 0, coordinates, arrayPosition, pointMas.length);
            arrayPosition += pointMas.length;

            pointMas = leftStart.getPointMas(z);
            System.arraycopy(pointMas, 0, coordinates, arrayPosition, pointMas.length);
            arrayPosition += pointMas.length;

            pointMas = leftEnd.getPointMas(z);
            System.arraycopy(pointMas, 0, coordinates, arrayPosition, pointMas.length);
            arrayPosition += pointMas.length;

            z = heightOfFloor;
            pointMas = leftEnd.getPointMas(z);
            System.arraycopy(pointMas, 0, coordinates, arrayPosition, pointMas.length);
            arrayPosition += pointMas.length;

            //back
            z = heightOfFloor;
            pointMas = rightStart.getPointMas(z);
            System.arraycopy(pointMas, 0, coordinates, arrayPosition, pointMas.length);
            arrayPosition += pointMas.length;
//1
            pointMas = rightEnd.getPointMas(z);
            System.arraycopy(pointMas, 0, coordinates, arrayPosition, pointMas.length);
            arrayPosition += pointMas.length;

            z = 0;
            pointMas = rightEnd.getPointMas(z);
            System.arraycopy(pointMas, 0, coordinates, arrayPosition, pointMas.length);
            arrayPosition += pointMas.length;

            pointMas = rightEnd.getPointMas(z);
            System.arraycopy(pointMas, 0, coordinates, arrayPosition, pointMas.length);
            arrayPosition += pointMas.length;

            pointMas = rightStart.getPointMas(z);
            System.arraycopy(pointMas, 0, coordinates, arrayPosition, pointMas.length);
            arrayPosition += pointMas.length;

            z = heightOfFloor;
            pointMas = rightStart.getPointMas(z);
            System.arraycopy(pointMas, 0, coordinates, arrayPosition, pointMas.length);
            arrayPosition += pointMas.length;

            //left_side
            z = heightOfFloor;
            pointMas = leftStart.getPointMas(z);
            System.arraycopy(pointMas, 0, coordinates, arrayPosition, pointMas.length);
            arrayPosition += pointMas.length;
//1
            pointMas = rightStart.getPointMas(z);
            System.arraycopy(pointMas, 0, coordinates, arrayPosition, pointMas.length);
            arrayPosition += pointMas.length;

            z = 0;
            pointMas = rightStart.getPointMas(z);
            System.arraycopy(pointMas, 0, coordinates, arrayPosition, pointMas.length);
            arrayPosition += pointMas.length;

            pointMas = rightStart.getPointMas(z);
            System.arraycopy(pointMas, 0, coordinates, arrayPosition, pointMas.length);
            arrayPosition += pointMas.length;

            pointMas = leftStart.getPointMas(z);
            System.arraycopy(pointMas, 0, coordinates, arrayPosition, pointMas.length);
            arrayPosition += pointMas.length;

            z = heightOfFloor;
            pointMas = leftStart.getPointMas(z);
            System.arraycopy(pointMas, 0, coordinates, arrayPosition, pointMas.length);
            arrayPosition += pointMas.length;

            //right_side
            z = heightOfFloor;
            pointMas = rightEnd.getPointMas(z);
            System.arraycopy(pointMas, 0, coordinates, arrayPosition, pointMas.length);
            arrayPosition += pointMas.length;

            pointMas = leftEnd.getPointMas(z);
            System.arraycopy(pointMas, 0, coordinates, arrayPosition, pointMas.length);
            arrayPosition += pointMas.length;
//1

            z = 0;
            pointMas = leftEnd.getPointMas(z);
            System.arraycopy(pointMas, 0, coordinates, arrayPosition, pointMas.length);
            arrayPosition += pointMas.length;

            pointMas = leftEnd.getPointMas(z);
            System.arraycopy(pointMas, 0, coordinates, arrayPosition, pointMas.length);
            arrayPosition += pointMas.length;

            pointMas = rightEnd.getPointMas(z);
            System.arraycopy(pointMas, 0, coordinates, arrayPosition, pointMas.length);
            arrayPosition += pointMas.length;

            z = heightOfFloor;
            pointMas = rightEnd.getPointMas(z);
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
                    7.0f, 7.0f, 7.0f, 1.f
            };
        else {
            return new float[]
                    {
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
                            1.0f, 0.0f, 1.0f, 1.0f,

//                            // Front face (red)
                            1.0f, 0.0f, 0.0f, 1.0f,
                            1.0f, 0.0f, 0.0f, 1.0f,
                            1.0f, 0.0f, 0.0f, 1.0f,
                            1.0f, 0.0f, 0.0f, 1.0f,
                            1.0f, 0.0f, 0.0f, 1.0f,
                            1.0f, 0.0f, 0.0f, 1.0f,

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

                            // Right face (green)
                            0.0f, 1.0f, 0.0f, 1.0f,
                            0.0f, 1.0f, 0.0f, 1.0f,
                            0.0f, 1.0f, 0.0f, 1.0f,
                            0.0f, 1.0f, 0.0f, 1.0f,
                            0.0f, 1.0f, 0.0f, 1.0f,
                            0.0f, 1.0f, 0.0f, 1.0f,


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
            nodeProjection.setY(getY(nodeProjection.getX(), kPerp, bPerp));
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

    public NodeViewModel getStart() {
        return start;
    }

    public NodeViewModel getEnd() {
        return end;
    }

    private float getY(float x, float kPerp, float bPerp) {
        return kPerp * x + bPerp;
    }

    public static double getDistanceToSegment(NodeModel start, NodeModel end) {

        return Math.sqrt(
                Math.pow(start.getX() - end.getX(), 2) +
                        Math.pow(start.getY() - end.getY(), 2));
    }
}

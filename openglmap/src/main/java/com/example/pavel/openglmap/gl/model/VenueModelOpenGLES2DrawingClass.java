package com.example.pavel.openglmap.gl.model;

import com.example.pavel.openglmap.gl.servermodel.Point;
import com.example.pavel.openglmap.gl.servermodel.Venue;
import com.example.pavel.openglmap.gl.servermodel.Wall;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pavel on 10.11.2015.
 */
public class VenueModelOpenGLES2DrawingClass {

    private final String vertexShaderCode =
            // This matrix member variable provides a hook to manipulate
            // the coordinates of the objects that use this vertex shader
            "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 vPosition;" +
                    "void main() {" +
                    // The matrix must be included as a modifier of gl_Position.
                    // Note that the uMVPMatrix factor *must be first* in order
                    // for the matrix multiplication product to be correct.
                    "  gl_Position = uMVPMatrix * vPosition;" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";

    private short drawOrder[] = {
            0, 1, 2,
            0, 2, 3,

            4, 5, 6,
            4, 6, 7,

//            4, 0, 3,
//            4, 3, 7,
//
//            5, 1, 2,
//            5, 2, 6,
//
//            4, 0, 1,
//            4, 1, 5,
//
//            6, 2, 3,
//            6, 3, 7

    };
    List<MyGeneralOpenGLES2DrawingClass> coordsPerVertex = new ArrayList<>();


    public VenueModelOpenGLES2DrawingClass(Venue venue) {



        for (int i = 0; i < venue.getWallList().size(); i++) {
            float coords[] = new float[ 4 * 2 * 3];
            int arrayPosition = 0;
            Wall wall = venue.getWallList().get(i);

            List<Point> listProjections = wall.getListPerpendecularparalellPoints();
            Point parale0 = listProjections.get(0);//point.getPointParalelWeight(wall.k, wall.b - waight_2);
            Point parale1 = listProjections.get(1);//point.getPointParalelWeight(wall.k, wall.b + waight_2);


            Point parale2 = listProjections.get(2);//point.getPointParalelWeight(wall.k, wall.b + waight_2);
            Point parale3 = listProjections.get(3);//point.getPointParalelWeight(wall.k, wall.b - waight_2);

            float[] pointMas = parale0.getPointMas(0f);
            System.arraycopy(pointMas, 0, coords, arrayPosition, pointMas.length);
            arrayPosition += pointMas.length;

            pointMas = parale1.getPointMas(0f);
            System.arraycopy(pointMas, 0, coords, arrayPosition, pointMas.length);
            arrayPosition += pointMas.length;

            pointMas = parale2.getPointMas(0f);
            System.arraycopy(pointMas, 0, coords, arrayPosition, pointMas.length);
            arrayPosition += pointMas.length;

            pointMas = parale3.getPointMas(0f);
            System.arraycopy(pointMas, 0, coords, arrayPosition, pointMas.length);
            arrayPosition += pointMas.length;

            //z-1
//            pointMas = parale0.getPointMas(-1.f);
//            System.arraycopy(pointMas, 0, coords, arrayPosition, pointMas.length);
//            arrayPosition += pointMas.length;
//
//            pointMas = parale1.getPointMas(-1.f);
//            System.arraycopy(pointMas, 0, coords, arrayPosition, pointMas.length);
//            arrayPosition += pointMas.length;
//
//            pointMas = parale2.getPointMas(-1.f);
//            System.arraycopy(pointMas, 0, coords, arrayPosition, pointMas.length);
//            arrayPosition += pointMas.length;
//
//            pointMas = parale3.getPointMas(-1.f);
//            System.arraycopy(pointMas, 0, coords, arrayPosition, pointMas.length);
//            arrayPosition += pointMas.length;

            coordsPerVertex.add(new MyGeneralOpenGLES2DrawingClass(3, coords, wall.color, drawOrder));
        }


    }


    public void draw(float[] mvpMatrix) {
        for (MyGeneralOpenGLES2DrawingClass item : coordsPerVertex)
            item.draw(mvpMatrix);
    }


}

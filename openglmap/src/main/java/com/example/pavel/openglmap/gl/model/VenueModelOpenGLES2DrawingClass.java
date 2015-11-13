package com.example.pavel.openglmap.gl.model;

import android.util.Log;

import com.example.pavel.openglmap.gl.servermodel.Point;
import com.example.pavel.openglmap.gl.servermodel.Venue;
import com.example.pavel.openglmap.gl.servermodel.Wall;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pavel on 10.11.2015.
 */
public class VenueModelOpenGLES2DrawingClass {


    public final String vertexShader =
            "uniform mat4 u_MVPMatrix;      \n"     // A constant representing the combined model/view/projection matrix.
                    + "uniform mat4 u_MVMatrix;       \n"     // A constant representing the combined model/view matrix.
                    + "uniform vec3 u_LightPos;       \n"     // The position of the light in eye space.

                    + "attribute vec4 a_Position;     \n"     // Per-vertex position information we will pass in.
                    + "attribute vec4 a_Color;        \n"     // Per-vertex color information we will pass in.
                    + "attribute vec3 a_Normal;       \n"     // Per-vertex normal information we will pass in.

                    + "varying vec4 v_Color;          \n"     // This will be passed into the fragment shader.

                    + "void main()                    \n"     // The entry point for our vertex shader.
                    + "{                              \n"
// Transform the vertex into eye space.
                    + "   vec3 modelViewVertex = vec3(u_MVMatrix * a_Position);              \n"
// Transform the normal's orientation into eye space.
                    + "   vec3 modelViewNormal = vec3(u_MVMatrix * vec4(a_Normal, 0.0));     \n"
// Will be used for attenuation.
                    + "   float distance = length(u_LightPos - modelViewVertex);             \n"
// Get a lighting direction vector from the light to the vertex.
                    + "   vec3 lightVector = normalize(u_LightPos - modelViewVertex);        \n"
// Calculate the dot product of the light vector and vertex normal. If the normal and light vector are
// pointing in the same direction then it will get max illumination.
                    + "   float diffuse = max(dot(modelViewNormal, lightVector), 0.1);       \n"
// Attenuate the light based on distance.
                    + "   diffuse = diffuse * (1.0 / (1.0 + (0.25 * distance * distance)));  \n"
// Multiply the color by the illumination level. It will be interpolated across the triangle.
                    + "   v_Color = a_Color * diffuse;                                       \n"
// gl_Position is a special variable used to store the final position.
// Multiply the vertex by the matrix to get the final point in normalized screen coordinates.
                    + "   gl_Position = u_MVPMatrix * a_Position;                            \n"
                    + "}             ";

    final String fragmentShader =
            "precision mediump float;       \n"     // Set the default precision to medium. We don't need as high of a
                    // precision in the fragment shader.
                    + "varying vec4 v_Color;          \n"     // This is the color from the vertex shader interpolated across the
                    // triangle per fragment.
                    + "void main()                    \n"     // The entry point for our fragment shader.
                    + "{                              \n"
                    + "   gl_FragColor = v_Color;     \n"     // Pass the color directly through the pipeline.
                    + "}                              \n";
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

    List<MyGeneralOpenGLES2DrawingClass> coordsPerVertex = new ArrayList<>();
    List<MyGeneralOpenGLES2DrawingClass> triangleDraw = new ArrayList<>();


    public VenueModelOpenGLES2DrawingClass(Venue venue) {


        for (int i = 0; i < venue.getWallList().size(); i++) {
            float coords[] = new float[4 * 2 * 3];
            int arrayPosition = 0;
            Wall wall = venue.getWallList().get(i);

            List<Point> listProjections = wall.getListPerpendecularparalellPoints();
            Point parale0 = listProjections.get(0);//point.getPointParalelWeight(wall.k, wall.b - waight_2);
            Point parale1 = listProjections.get(1);//listProjections.get(1);//point.getPointParalelWeight(wall.k, wall.b + waight_2);


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

//            //z-1
            pointMas = parale0.getPointMas(-1.f);
            System.arraycopy(pointMas, 0, coords, arrayPosition, pointMas.length);
            arrayPosition += pointMas.length;

            pointMas = parale1.getPointMas(-1.f);
            System.arraycopy(pointMas, 0, coords, arrayPosition, pointMas.length);
            arrayPosition += pointMas.length;

            pointMas = parale2.getPointMas(-1.f);
            System.arraycopy(pointMas, 0, coords, arrayPosition, pointMas.length);
            arrayPosition += pointMas.length;

            pointMas = parale3.getPointMas(-1.f);
            System.arraycopy(pointMas, 0, coords, arrayPosition, pointMas.length);
            arrayPosition += pointMas.length;

            coordsPerVertex.add(new MyGeneralOpenGLES2DrawingClass(3, coords, wall.color, drawOrder));
        }

        for (Wall wall : venue.getWallList()) {
            Log.e("GETMODEL TIRANGLE", "WAL:" + wall.point1.x + "," + wall.point1.y + ";" + wall.point2.x + "," + wall.point2.y);
            for (int i = 0; i < venue.getWallList().size(); i++) {
                Wall wallSecond = venue.getWallList().get(i);
                Log.e("GETMODEL TIRANGLE", "WAL2:" + wallSecond.point1.x + "," + wallSecond.point1.y + ";" + wallSecond.point2.x + "," + wallSecond.point2.y);
//                Log.e("GETMODEL TIRANGLE", "WAL:" + wall.point1.x + "," + wall.point1.y + ";" + wall.point2.x + "," + wall.point2.y
//                        + " WAL2:" + wallSecond.point1.x + "," + wallSecond.point1.y + ";" + wallSecond.point2.x + "," + wallSecond.point2.y);
                if (!wall.equals(wallSecond)) {
                    if (wall.point1.equals(wallSecond.point1) || wall.point1.equals(wallSecond.point2)
                            ||wall.point2.equals(wallSecond.point1)||wall.point2.equals(wallSecond.point2)
                            ) {
                        Log.e("GETMODEL TIRANGLE SAME", "WAL:" + wall.point1.x + "," + wall.point1.y + ";" + wall.point2.x + "," + wall.point2.y
                                + " WAL2:" + wallSecond.point1.x + "," + wallSecond.point1.y + ";" + wallSecond.point2.x + "," + wallSecond.point2.y);
                        Point pointOutWall = wall.getPointOutWall(wallSecond);
                        Point pointOutWall2 = wallSecond.getPointOutWall(wall);
                        Point centeer = wall.point1;
                        if(wall.point2.equals(wallSecond.point1) || wall.point2.equals(wallSecond.point2))
                            centeer = wall.point2;
                        createNewTriangleFor2Wals(wallSecond, pointOutWall, pointOutWall2, centeer);
                    }
                }
//                    else if (wall.point1.equals(wallSecond.point2)) {
//                        Point pointOutWall = wall.getPointOutWall(wallSecond);
//                        Point pointOutWall2 = wallSecond.getPointOutWall(wall);
//                        Point centeer = wall.point1;
//                        createNewTriangleFor2Wals(wall, pointOutWall, pointOutWall2, centeer);
//                    }
            }
        }
    }

    private void createNewTriangleFor2Wals(Wall wall, Point pointOutWall, Point pointOutWall2, Point centeer) {
        if (pointOutWall != null && pointOutWall2 != null) {
            float coords[] = new float[3 * 2 * 3];
            int arrayPosition = 0;
            float[] pointMas = pointOutWall.getPointMas(0f);
            System.arraycopy(pointMas, 0, coords, arrayPosition, pointMas.length);
            arrayPosition += pointMas.length;

            pointMas = pointOutWall2.getPointMas(0f);
            System.arraycopy(pointMas, 0, coords, arrayPosition, pointMas.length);
            arrayPosition += pointMas.length;

            pointMas = centeer.getPointMas(0f);
            System.arraycopy(pointMas, 0, coords, arrayPosition, pointMas.length);
            arrayPosition += pointMas.length;

            //Z -1
            pointMas = pointOutWall.getPointMas(-1f);
            System.arraycopy(pointMas, 0, coords, arrayPosition, pointMas.length);
            arrayPosition += pointMas.length;

            pointMas = pointOutWall2.getPointMas(-1f);
            System.arraycopy(pointMas, 0, coords, arrayPosition, pointMas.length);
            arrayPosition += pointMas.length;

            pointMas = centeer.getPointMas(-1f);
            System.arraycopy(pointMas, 0, coords, arrayPosition, pointMas.length);
            arrayPosition += pointMas.length;
//            new float[]{0.5f, .50f, .50f, 1.0f}
            triangleDraw.add(new MyGeneralOpenGLES2DrawingClass(3, coords, new float[]{0.5f, .50f, .50f, 1.0f}, drawOrderTriangle));
        }
    }


    public void draw(float[] mvpMatrix) {
        for (MyGeneralOpenGLES2DrawingClass item : triangleDraw)
            item.draw(mvpMatrix);
        for (MyGeneralOpenGLES2DrawingClass item : coordsPerVertex)
            item.draw(mvpMatrix);

    }


}

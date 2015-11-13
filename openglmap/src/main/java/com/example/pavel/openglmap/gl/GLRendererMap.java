package com.example.pavel.openglmap.gl;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import com.example.pavel.openglmap.gl.model.MyGeneralOpenGLES2DrawingClass;
import com.example.pavel.openglmap.gl.model.VenueModelOpenGLES2DrawingClass;
import com.example.pavel.openglmap.gl.servermodel.Point;
import com.example.pavel.openglmap.gl.servermodel.Venue;
import com.example.pavel.openglmap.gl.servermodel.Wall;

import java.util.HashMap;
import java.util.Map;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Pavel on 06.11.2015.
 */
public class GLRendererMap implements GLSurfaceView.Renderer {

    //    myShapes triangle, line, circle, rect, custRect;
    Map<String, MyGeneralOpenGLES2DrawingClass> myShapes;
    VenueModelOpenGLES2DrawingClass venueModelOpenGLES2DrawingClass;

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
//        / Use culling to remove back faces.
//        GLES20.glEnable(GLES20.GL_CULL_FACE);

// Enable depth testing
//        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
//        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
// Accept fragment if it closer to the camera than the former one
        GLES20.glDepthFunc(GLES20.GL_LESS);
        Venue venue = new Venue();
        venue.widht = 100;
        venue.height = 100;

        myShapes = new HashMap<>();
//        List<Wall> walls = new ArrayList<>();
        Point point1 = new Point();
        point1.x = -1f;
        point1.y = -1f;

        Point point2 = new Point();
        point2.x = -1f;
        point2.y = 1f;

        Point point3 = new Point();
        point3.x = 0f;
        point3.y = 1f;

        Point point4 = new Point();
        point4.x = 0.0f;
        point4.y = 0f;

        Point point5 = new Point();
        point5.x = 0f;
        point5.y = -1f;

        Point point6 = new Point();
        point6.x = 0.7f;
        point6.y = 1f;

        Point point7 = new Point();
        point7.x = 0.3f;
        point7.y = 0f;

        Point point8 = new Point();
        point8.x = 1.f;
        point8.y = -0.5f;
        float[] color =new float[]{0.5f, .50f, .50f, 1.0f};
        Wall object = new Wall(point1, point2, 0.1f);//red
        object.color =color ;// new float[]{1.0f, 0.0f, 0.0f, 1.0f};
        venue.getWallList().add(object);

        Wall object2 = new Wall(point1, point5, 0.1f);//green
        object2.color =color ;// new float[]{0.0f, 1.0f, 0.0f, 1.0f};
        venue.getWallList().add(object2);

        Wall object3 = new Wall(point2, point3, 0.1f);//blue
        object3.color =color ;// new float[]{0.0f, 0.0f, 1.0f, 1.0f};
        venue.getWallList().add(object3);
////
////
        Wall object4 = new Wall(point5, point4, 0.1f);
        object4.color =color ;// new float[]{1.0f, 1.0f, 0.0f, 1.0f};
        venue.getWallList().add(object4);
////
        Wall object5 = new Wall(point3, point6, 0.1f);
        object5.color =color ;// new float[]{1.0f, 0.0f, 1.0f, 1.0f};
        venue.getWallList().add(object5);

        Wall object6 = new Wall(point4, point7, 0.1f);
        object6.color =color ;// new float[]{1.0f, .50f, 1.0f, 1.0f};
        venue.getWallList().add(object6);

        Wall object7 = new Wall(point6, point7, 0.1f);
        object7.color =color ;// new float[]{1.0f, .50f, .50f, 1.0f};
        venue.getWallList().add(object7);
        venueModelOpenGLES2DrawingClass = new VenueModelOpenGLES2DrawingClass(venue);
//        myShapes.put("Line", new MyGeneralOpenGLES2DrawingClass(3,
//                new float[]{-0.5f, 0.5f, 0.0f, -0.5f, -0.5f, 0.0f, -0.49f, -0.5f, 0.0f, -0.49f, 0.5f, 0.0f},
//                new float[]{1.0f, 0.0f, 0.0f, 1.0f},
//                new short[]{0, 1, 2, 0, 2, 3}));
//
//        // Rectangle Demo..............................
//        myShapes.put("Rect", new MyGeneralOpenGLES2DrawingClass(3,
//                new float[]{-1.0f, 0.5f, 0.0f,
//                        -1.0f, 0.2f, 0.0f,
//                        0, .20f, 0.0f,
//                        0, .5f, 0.0f},
//                new float[]{0.0f, 1.5f, 0.0f, 1.0f},
//                new short[]{0, 1, 2, 0, 2, 3}))
//        ;
//        //Triangle..............................
//        myShapes.put("Triangle", new MyGeneralOpenGLES2DrawingClass(3,
//                new float[]{0.9f, 0.7f, 0.0f,
//                        .9f, .2f, 0.0f,
//                        .4f, .2f, 0.0f,},
//                new float[]{0.0f, 0.0f, 1.0f, 1.0f},
//                new short[]{0, 2, 1}));
//        //////////////////////// Circle/////////////////////////////////////
//        float vertices[] = new float[364 * 3];
//        vertices[0] = 0.2f;
//        vertices[1] = 0.0f;
//        vertices[2] = 0f;
//
//        float radious = .3f;
//
//        for (int i = 1; i < 364; i++) {
//            vertices[(i * 3) + 0] = (float) (radious * Math.cos((3.14 / 180) * (float) i) + vertices[0]);
//            vertices[(i * 3) + 1] = (float) (radious * Math.sin((3.14 / 180) * (float) i) + vertices[1]);
//            vertices[(i * 3) + 2] = 0;
//        }
//        myShapes.put("Circle", new MyGeneralOpenGLES2DrawingClass(3, vertices, new float[]{1.5f, 0.0f, 0.0f, 1.0f}, new short[]{0, 1, 2}));
//
//        myShapes.put("Shape", new MyGeneralOpenGLES2DrawingClass(3,
//                new float[]{
//                        -0.5f, 0.0f, 0.0f,
//                        -0.5f, -0.4f, 0.0f,
//                        -0.2f, -0.4f, 0.0f,
//                        -0.2f, -0.2f, 0.0f,
//                        0.0f, -0.2f, 0.0f,
//                        0.0f, .0f, 0.0f,
//
//                        -0.5f, 0.0f, 1.0f,
//                        -0.5f, -0.4f, 1.0f,
//                        -0.2f, -0.4f, 1.0f,
//                        -0.2f, -0.2f, 1.0f,
//                        0.0f, -0.2f, 1.0f,
//                        0.0f, .0f, 1.0f
//                },
//                new float[]{0.0f, 1.0f, 0.0f, 0.5f},
//                new short[]{
//                        0, 1, 2,
//                        0, 2, 3,
//                        3, 4, 5,
//                        0, 3, 5,
//
//                        6, 7, 8,
//                        0, 2, 3,
//                        3, 4, 5,
//                        0, 3, 5
//                }));
//
//        ///CUBE
//        float verticesCube[] = {
//                -1.0f, -1.0f, -1.0f,
//                1.0f, -1.0f, -1.0f,
//                1.0f, 1.0f, -1.0f,
//                -1.0f, 1.0f, -1.0f,
//                -1.0f, -1.0f, 1.0f,
//                1.0f, -1.0f, 1.0f,
//                1.0f, 1.0f, 1.0f,
//                -1.0f, 1.0f, 1.0f
//        };
//        for (int i = 0; i < verticesCube.length; i++) {
//            verticesCube[i] = verticesCube[i] / 3;
//        }
//        short indicesCube[] = {
//                0, 4, 5, 0, 5, 1,
//                1, 5, 6, 1, 6, 2,
//                2, 6, 7, 2, 7, 3,
//                3, 7, 4, 3, 4, 0,
//                4, 7, 6, 4, 6, 5,
//                3, 0, 1, 3, 1, 2
//        };
//        float colorsCube[] = {
//                0.3f, 0.2f, 1.0f, 1.0f,
//        };
//        myShapes.put("CUBE", new MyGeneralOpenGLES2DrawingClass(3,
//                verticesCube,
//                colorsCube,
//                indicesCube));

    }

    // mMVPMatrix is an abbreviation for "Model View Projection Matrix"
    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
    private float[] mRotationMatrix = new float[16];

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);

        float ratio = (float) width / height;

        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);

    }
    private float mAngle;
    @Override
    public void onDrawFrame(GL10 gl) {
        float[] scratch = new float[16];

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        Matrix.setLookAtM(mViewMatrix, 0, 0, -2, 4, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

//        for (String shape : myShapes.keySet()) {
//            if (shape.equals(touchedObject)) {
//                Matrix.setRotateM(mRotationMatrix, 0, 5, 0, 0, -1.0f);
////                Matrix.scaleM(mRotationMatrix, 1, 0, 0, -1.0f);
//                Matrix.multiplyMM(scratch, 0, mMVPMatrix, 0, mRotationMatrix, 0);
//                myShapes.get(shape).draw(scratch);
//            } else if (shape.equals("CUBE")) {
//                Matrix.setRotateEulerM(mRotationMatrix, 0, 2, 2, 3);
////                Matrix.scaleM(mRotationMatrix, 1, 0, 0, -1.0f);
//                Matrix.multiplyMM(scratch, 0, mMVPMatrix, 0, mRotationMatrix, 0);
//                myShapes.get(shape).draw(scratch);
//            } else {
//                myShapes.get(shape).draw(mMVPMatrix);
//            }
//        }

        Matrix.setRotateM(mRotationMatrix, 0, mAngle, 0f, 0f, 1.0f);

        // Combine the rotation matrix with the projection and camera view
        // Note that the mMVPMatrix factor *must be first* in order
        // for the matrix multiplication product to be correct.
        Matrix.multiplyMM(scratch, 0, mMVPMatrix, 0, mRotationMatrix, 0);


        venueModelOpenGLES2DrawingClass.draw(scratch);

//
//        // Draw shape
////        triangle.draw(mMVPMatrix);
//        // Create a rotation transformation for the triangle
//        long time = SystemClock.uptimeMillis() % 6000L;
//        float angle = 0.090f * ((int) time);
//        Matrix.setRotateM(mRotationMatrix, 0, angle, 0, 0, -1.0f);
////        x = x + 0.090f*time;
////        Matrix.translateM(mRotationMatrix, 0, x, 1, 0);
//        // Combine the rotation matrix with the projection and camera view
//        // Note that the mMVPMatrix factor *must be first* in order
//        // for the matrix multiplication product to be correct.
//        Matrix.multiplyMM(scratch, 0, mMVPMatrix, 0, mRotationMatrix, 0);
//
//        // Draw triangle
//        triangle.draw(scratch);
    }

    String touchedObject;

    public String getTouchedObject(float[] glCoord) {
        String touched = " ";
        for (String shape : myShapes.keySet()) {

            if (myShapes.get(shape).isTouched(glCoord)) {
                touched += shape + " TOUCHED, ";
                touchedObject = shape;

            }

        }
        return touched;
    }

    public float getmAngle() {
        return mAngle;
    }

    public void setmAngle(float mAngle) {
        this.mAngle = mAngle;
    }

    public static int loadShader(int type, String shaderCode) {

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }

    public float[] glCoordinate(float normalizedX, float normalizedY) {
        float[] invertedMatrix, transformMatrix,
                normalizedInPoint, outPoint;
        invertedMatrix = new float[16];
        transformMatrix = new float[16];
        normalizedInPoint = new float[4];
        normalizedInPoint[0] =
                normalizedX;
        normalizedInPoint[1] =
                normalizedY;
        normalizedInPoint[2] = -1.0f;
        normalizedInPoint[3] = 1.0f;

        outPoint = new float[4];
//        Matrix.multiplyMM(
//                transformMatrix, 0,
//                mProjectionMatrix, 0,
//                mMVPMatrix, 0);
        Matrix.invertM(invertedMatrix, 0,
                mMVPMatrix, 0);
        Matrix.multiplyMV(
                outPoint, 0,
                invertedMatrix, 0,
                normalizedInPoint, 0);

        if (outPoint[3] == 0.0) {
            // Avoid /0 error.
            Log.e("World coords", "ERROR!");
            return new float[]{9999, 9999, 9999};
        }
        float[] c = new float[]{outPoint[0] / outPoint[3], outPoint[1] / outPoint[3]};
        return c;


    }

}

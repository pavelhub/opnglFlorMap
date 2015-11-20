package com.example.pavel.openglmap.gl;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import com.example.pavel.openglmap.gl.model.MyGeneralOpenGLES2DrawingClass;
import com.example.pavel.openglmap.gl.view.overlay.FloorWallsOverlay;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Pavel on 06.11.2015.
 */
public class GLRendererMap implements GLSurfaceView.Renderer {
    FloorWallsOverlay floorWallsOverlay;
    RenderConfig renderConfig;
    // mMVPMatrix is an abbreviation for "Model View Projection Matrix"
    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
    private float[] mRotationMatrix = new float[16];

    public GLRendererMap(RenderConfig renderConfig) {
        this.renderConfig = renderConfig;
//        floorWallsOverlay = new FloorWallsOverlay(floorModel);
    }

    MyGeneralOpenGLES2DrawingClass textShape;

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
//        / Use culling to remove back faces.
//        GLES20.glEnable(GLES20.GL_CULL_FACE);

// Enable depth testing
//        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
// Accept fragment if it closer to the camera than the former one
        GLES20.glDepthFunc(GLES20.GL_LESS);

        floorWallsOverlay = new FloorWallsOverlay(renderConfig.floorModel, renderConfig.is3DModel);
//        textShape = new MyGeneralOpenGLES2DrawingClass(3,
//                new float[]{1.250f, 1.0f, 0.0f,
//                        1.250f, -0.5f, 0.0f,
//                        0.75f, -0.5f, 0.0f,
//                        0.75f, 1.f, 0.0f},
//                new float[]{1.5f, 0.5f, 0.5f, 1.0f},
//                new short[]{0, 1, 2, 0, 2, 3});

    }


    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);

        float ratio = (float) width / height;

        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 10);

    }

    private float mAngle;

    @Override
    public void onDrawFrame(GL10 gl) {
        float[] scratch = new float[16];

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        if (renderConfig.is3DModel) {
            Matrix.setLookAtM(mViewMatrix, 0, 0, -2, 6, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        } else {

            Matrix.setLookAtM(mViewMatrix, 0, 0, 0, 6, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        }
        // Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);


        Matrix.setRotateM(mRotationMatrix, 0, mAngle, 0f, 0f, 1.0f);

        // Combine the rotation matrix with the projection and camera view
        // Note that the mMVPMatrix factor *must be first* in order
        // for the matrix multiplication product to be correct.
        Matrix.multiplyMM(scratch, 0, mMVPMatrix, 0, mRotationMatrix, 0);

        floorWallsOverlay.draw(scratch);


    }

    private void drawFacilityWalls(float[] scratch) {

    }

    public float getAngle() {
        return mAngle;
    }

    public void setAngle(float mAngle) {
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

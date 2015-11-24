package com.example.pavel.openglmap.gl;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.Log;

import com.example.pavel.openglmap.gl.model.MyGeneralOpenGLES2DrawingClass;
import com.example.pavel.openglmap.gl.view.overlay.FloorWallsOverlay;
import com.example.pavel.openglmap.gl.view.overlay.SceneOverlay;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Pavel on 06.11.2015.
 */
public class GLRendererMap implements GLSurfaceView.Renderer {
    FloorWallsOverlay floorWallsOverlay;
    SceneOverlay sceneOverlay;
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
        // Set the background clear color to black.
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        // Use culling to remove back faces.
        if (renderConfig.is3DModel) {
            GLES20.glEnable(GLES20.GL_CULL_FACE);
        }
        // Enable depth testing
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        // Position the eye in front of the origin.
        final float eyeX = 0.0f;
        final float eyeY = -0.2f;
        final float eyeZ = 1.5f;

        // We are looking toward the distance
        final float lookX = 0.0f;
        final float lookY = 0.0f;
        final float lookZ = 0.0f;

        // Set our up vector. This is where our head would be pointing were we holding the camera.
        final float upX = 0.0f;
        final float upY = 1.0f;
        final float upZ = 0.0f;

        // Set the view matrix. This matrix can be said to represent the camera position.
        // NOTE: In OpenGL 1, a ModelView matrix is used, which is a combination of a model and
        // view matrix. In OpenGL 2, we can keep track of these matrices separately if we choose.
        Matrix.setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ);

        floorWallsOverlay = new FloorWallsOverlay(renderConfig.floorModel, renderConfig.is3DModel);
        sceneOverlay = new SceneOverlay(renderConfig.floorModel);
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
        // Set the OpenGL viewport to the same size as the surface.
        GLES20.glViewport(0, 0, width, height);

        // Create a new perspective projection matrix. The height will stay the same
        // while the width will vary as per aspect ratio.
        final float ratio = (float) width / height;
        final float left = -ratio;
        final float right = ratio;
        final float bottom = -1.0f;
        final float top = 1.0f;
        final float near = 1.0f;
        final float far = 10.0f;
//        if (renderConfig.is3DModel)
            Matrix.frustumM(mProjectionMatrix, 0, left, right, bottom, top, near, far);
//        else
//            Matrix.orthoM(mProjectionMatrix, 0, left, right, bottom, top, near, far);

    }

    private float mAngle;
    private float[] mModelMatrix = new float[16];

    @Override
    public void onDrawFrame(GL10 gl) {
        float[] scratch = new float[16];

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        if (renderConfig.is3DModel) {
//            Matrix.setLookAtM(mViewMatrix, 0, 0, 0, -0.5f, 0f, 0f, -2.0f, 0f, 1.0f, 0.0f);
        } else {

            Matrix.setLookAtM(mViewMatrix, 0, 0, 0, 2, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        }
        // Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

        Matrix.setIdentityM(mRotationMatrix, 0);
        Matrix.translateM(mRotationMatrix, 0, 0.0f, 0.0f, -5.0f);
        Matrix.setRotateM(mRotationMatrix, 0, mAngle, .0f, .0f, -1f);

        // Combine the rotation matrix with the projection and camera view
        // Note that the mMVPMatrix factor *must be first* in order
        // for the matrix multiplication product to be correct.
        Matrix.multiplyMM(scratch, 0, mMVPMatrix, 0, mRotationMatrix, 0);

        // Do a complete rotation every 10 seconds.
        long time = SystemClock.uptimeMillis() % 10000L;
        float angleInDegrees = (360.0f / 10000.0f) * ((int) time);

        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, 0.0f, 0.0f, 0.0f);
        Matrix.rotateM(mModelMatrix, 0, mAngle, .0f, .0f, 1.0f);

        floorWallsOverlay.draw(mViewMatrix, mModelMatrix, mProjectionMatrix, scratch);
        sceneOverlay.draw(mMVPMatrix);

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

package com.example.pavel.openglmap.gl.model;

import android.opengl.GLES20;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created by Pavel on 10.11.2015.
 */
public class MyGeneralOpenGLES2DrawingClass {

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

    public final FloatBuffer vertexBuffer;
    private final int mProgram;
    private int mPositionHandle;
    private int mColorHandle;
    private int mMVPMatrixHandle;
    public ShortBuffer drawListBuffer;
    private short drawOrder[] = {0, 1, 2};

    int COORDS_PER_VERTEX = 0;// Argument1
    float coords[] = {};// Argument 2
    private final int vertexCount;
    private final int vertexStride; // 4 bytes per vertex
    public float[] color = new float[4];
    int drawMode = GLES20.GL_TRIANGLES;
    /**
     * Stores a copy of the model matrix specifically for the light position.
     */
    private float[] mLightModelMatrix = new float[16];

    public MyGeneralOpenGLES2DrawingClass(int coordsPerVertex, float[] coordinates, float[] color, short[] drawOrder) {
        this.drawOrder = drawOrder;
        COORDS_PER_VERTEX = coordsPerVertex;
        coords = coordinates;

        vertexCount = coords.length / COORDS_PER_VERTEX;
        vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex
        this.color = color;


        ////////////////////////////////////////
        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (number of coordinate values * 4 bytes per float)
                coords.length * 4);
        // use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder());

        // create a floating point buffer from the ByteBuffer
        vertexBuffer = bb.asFloatBuffer();
        // add the coordinates to the FloatBuffer
        vertexBuffer.put(coords);
        // set the buffer to read the first coordinate
        vertexBuffer.position(0);
        ///////////////////////////////////
        ByteBuffer dlb = ByteBuffer.allocateDirect(
                // (# of coordinate values * 2 bytes per short)
                drawOrder.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(drawOrder);
        drawListBuffer.position(0);
        //////////////////////////////////////////
        //   prepare shaders and OpenGL program
        int vertexShader = loadShader(
                GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = loadShader(
                GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

        mProgram = GLES20.glCreateProgram();             // create empty OpenGL Program
        GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
        GLES20.glLinkProgram(mProgram);                  // create OpenGL program executables

        /////////////////////////////////////////////////
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

    public void draw(float mvpMatix) {
//    // Set program handles for cube drawing.
//    mMVPMatrixHandle = GLES20.glGetUniformLocation(mPerVertexProgramHandle, "u_MVPMatrix");
//    mMVMatrixHandle = GLES20.glGetUniformLocation(mPerVertexProgramHandle, "u_MVMatrix");
//    mLightPosHandle = GLES20.glGetUniformLocation(mPerVertexProgramHandle, "u_LightPos");
//    mPositionHandle = GLES20.glGetAttribLocation(mPerVertexProgramHandle, "a_Position");
//    mColorHandle = GLES20.glGetAttribLocation(mPerVertexProgramHandle, "a_Color");
//    mNormalHandle = GLES20.glGetAttribLocation(mPerVertexProgramHandle, "a_Normal");
//
//
//    // Calculate position of the light. Rotate and then push into the distance.
//    Matrix.setIdentityM(mLightModelMatrix, 0);
//    Matrix.translateM(mLightModelMatrix, 0, 0.0f, 0.0f, -5.0f);
//    Matrix.rotateM(mLightModelMatrix, 0, angleInDegrees, 0.0f, 1.0f, 0.0f);
//    Matrix.translateM(mLightModelMatrix, 0, 0.0f, 0.0f, 2.0f);
//
//    Matrix.multiplyMV(mLightPosInWorldSpace, 0, mLightModelMatrix, 0, mLightPosInModelSpace, 0);
//    Matrix.multiplyMV(mLightPosInEyeSpace, 0, mViewMatrix, 0, mLightPosInWorldSpace, 0);
    }

    public void draw(float[] mvpMatrix) {
        // Add program to OpenGL environment
        GLES20.glUseProgram(mProgram);

        // get handle to vertex shader's vPosition member
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(
                mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);

        // get handle to fragment shader's vColor member
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");

        // Set color for drawing the triangle
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);

        // get handle to shape's transformation matrix
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");


        // Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);


        // Draw the triangle
        //GLES20.glDrawArrays(drawMode, 0, vertexCount);
        if (drawOrder.length == 36)//cube
        {
            GLES20.glDrawElements(drawMode, 36, GLES20.GL_UNSIGNED_SHORT, drawListBuffer);
        } else if (coords.length > 100)
            GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 364);
        else
            GLES20.glDrawElements(drawMode, drawOrder.length, GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }

//    public void draw(float[] mvpMatrix) {
//        vertexBuffer.position(0);
//        GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false,
//                0, vertexBuffer);
//
//        GLES20.glEnableVertexAttribArray(mPositionHandle);
//
//        // Pass in the color information
//        drawListBuffer.position(0);
//        GLES20.glVertexAttribPointer(mColorHandle, 4, GLES20.GL_FLOAT, false,
//                0, drawListBuffer);
//
//        GLES20.glEnableVertexAttribArray(mColorHandle);
//
//        // Pass in the normal information
//        mCubeNormals.position(0);
//        GLES20.glVertexAttribPointer(mNormalHandle, mNormalDataSize, GLES20.GL_FLOAT, false,
//                0, mCubeNormals);
//
//        GLES20.glEnableVertexAttribArray(mNormalHandle);
//
//        // This multiplies the view matrix by the model matrix, and stores the result in the MVP matrix
//        // (which currently contains model * view).
//        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);
//
//        // Pass in the modelview matrix.
//        GLES20.glUniformMatrix4fv(mMVMatrixHandle, 1, false, mMVPMatrix, 0);
//
//        // This multiplies the modelview matrix by the projection matrix, and stores the result in the MVP matrix
//        // (which now contains model * view * projection).
//        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);
//
//        // Pass in the combined matrix.
//        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);
//
//        // Pass in the light position in eye space.
//        GLES20.glUniform3f(mLightPosHandle, mLightPosInEyeSpace[0], mLightPosInEyeSpace[1], mLightPosInEyeSpace[2]);
//
//        // Draw the cube.
//        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 36);
//    }

    public boolean isTouched(float[] touchPoint)//x y z
    {
        float x2 = touchPoint[0];
        float y2 = touchPoint[1];
        if (coords.length == 9) {

            // Triangle
            float midPointX = (coords[0] + coords[3] + coords[6]) / 3;
            float midPointY = (coords[1] + coords[4] + coords[7]) / 3;
            // Distance from 2nd vertex will work as threshold
            float thrDist = eucledian(midPointX, midPointY, coords[3], coords[4]);
            float dstFromTouch = eucledian(midPointX, midPointY, x2, y2);
            if (dstFromTouch <= thrDist) {
                Log.i("Matched", "Triangle");
                return true;
            }


        }
        if (coords.length == 12) {
            //Line square or Rectangle
            // Just checking if touch point is between 1st and last vertex is enough
            if (x2 >= coords[0] && x2 <= coords[6] && y2 >= coords[7] && y2 <= coords[1]) {
                Log.i("Matched", "Rect/Line");
                return true;
            }

        }
        if (coords.length > 100) {
            //Circle
            // So calculate the distance between first and second vertex. That's the radious
            // Check for proximity of touch distance with radious.
            float radi = eucledian(coords[0], coords[1], coords[3], coords[4]);
            float dstFromTouch = eucledian(coords[0], coords[1], x2, y2);
            if (dstFromTouch <= radi) {
                Log.i("Matched", "Circle");
                return true;
            }
        }

        return false;
    }

    float eucledian(float x1, float y1, float x2, float y2) {
        float d = (float) Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
        return d;

    }

    private int createAndLinkProgram(final int vertexShaderHandle, final int fragmentShaderHandle, final String[] attributes) {
        int programHandle = GLES20.glCreateProgram();

        if (programHandle != 0) {
            // Bind the vertex shader to the program.
            GLES20.glAttachShader(programHandle, vertexShaderHandle);

            // Bind the fragment shader to the program.
            GLES20.glAttachShader(programHandle, fragmentShaderHandle);

            // Bind attributes
            if (attributes != null) {
                final int size = attributes.length;
                for (int i = 0; i < size; i++) {
                    GLES20.glBindAttribLocation(programHandle, i, attributes[i]);
                }
            }

            // Link the two shaders together into a program.
            GLES20.glLinkProgram(programHandle);

            // Get the link status.
            final int[] linkStatus = new int[1];
            GLES20.glGetProgramiv(programHandle, GLES20.GL_LINK_STATUS, linkStatus, 0);

            // If the link failed, delete the program.
            if (linkStatus[0] == 0) {
                Log.e("TAG", "Error compiling program: " + GLES20.glGetProgramInfoLog(programHandle));
                GLES20.glDeleteProgram(programHandle);
                programHandle = 0;
            }
        }

        if (programHandle == 0) {
            throw new RuntimeException("Error creating program.");
        }

        return programHandle;
    }
}

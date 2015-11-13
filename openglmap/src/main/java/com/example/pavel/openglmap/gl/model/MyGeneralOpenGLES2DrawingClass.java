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

    private final FloatBuffer vertexBuffer;
    private final int mProgram;
    private int mPositionHandle;
    private int mColorHandle;
    private int mMVPMatrixHandle;
    private ShortBuffer drawListBuffer;
    private short drawOrder[] = {0, 1, 2};

    int COORDS_PER_VERTEX = 0;// Argument1
    float coords[] = {};// Argument 2
    private final int vertexCount;
    private final int vertexStride; // 4 bytes per vertex
    public float[] color = new float[4];
    int drawMode = GLES20.GL_TRIANGLES;

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
}

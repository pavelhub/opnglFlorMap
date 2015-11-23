package com.example.pavel.openglmap.gl.model;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created by Pavel on 10.11.2015.
 */
public class MyGeneralOpenGLES2DrawingClass3d extends GeneralOpenGLDrawing {

    final String vertexShaderString =
            "uniform mat4 u_MVPMatrix;      \n"        // A constant representing the combined model/view/projection matrix.
                    + "uniform mat4 u_MVMatrix;       \n"        // A constant representing the combined model/view matrix.
                    + "uniform vec3 u_LightPos;       \n"        // The position of the light in eye space.

                    + "attribute vec4 a_Position;     \n"        // Per-vertex position information we will pass in.
                    + "attribute vec4 a_Color;        \n"        // Per-vertex color information we will pass in.
                    + "attribute vec3 a_Normal;       \n"        // Per-vertex normal information we will pass in.

                    + "varying vec4 v_Color;          \n"        // This will be passed into the fragment shader.

                    + "void main()                    \n"    // The entry point for our vertex shader.
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
                    + "}                                                                     \n";


    final String fragmentShaderString =
            "precision mediump float;       \n"        // Set the default precision to medium. We don't need as high of a
                    // precision in the fragment shader.
                    + "varying vec4 v_Color;          \n"        // This is the color from the vertex shader interpolated across the
                    // triangle per fragment.
                    + "void main()                    \n"        // The entry point for our fragment shader.
                    + "{                              \n"
                    + "   gl_FragColor = v_Color;     \n"        // Pass the color directly through the pipeline.
                    + "}";

    public final FloatBuffer vertexBuffer;
    public final FloatBuffer colorBuffer;
    private final int mProgram;

    /**
     * This will be used to pass in the transformation matrix.
     */
    private int mMVPMatrixHandle;

    /**
     * This will be used to pass in the modelview matrix.
     */
    private int mMVMatrixHandle;

    /**
     * This will be used to pass in the light position.
     */
    private int mLightPosHandle;

    /**
     * This will be used to pass in model position information.
     */
    private int mPositionHandle;

    /**
     * This will be used to pass in model color information.
     */
    private int mColorHandle;

    /**
     * This will be used to pass in model normal information.
     */
    private int mNormalHandle;
    final float[] cubeNormalData =
            {
                    // Front face
                    0.0f, 0.0f, 1.0f,
                    0.0f, 0.0f, 1.0f,
                    0.0f, 0.0f, 1.0f,
                    0.0f, 0.0f, 1.0f,
                    0.0f, 0.0f, 1.0f,
                    0.0f, 0.0f, 1.0f,

                    // Right face
                    1.0f, 0.0f, 0.0f,
                    1.0f, 0.0f, 0.0f,
                    1.0f, 0.0f, 0.0f,
                    1.0f, 0.0f, 0.0f,
                    1.0f, 0.0f, 0.0f,
                    1.0f, 0.0f, 0.0f,

                    // Back face
                    0.0f, 0.0f, -1.0f,
                    0.0f, 0.0f, -1.0f,
                    0.0f, 0.0f, -1.0f,
                    0.0f, 0.0f, -1.0f,
                    0.0f, 0.0f, -1.0f,
                    0.0f, 0.0f, -1.0f,

                    // Left face
                    -1.0f, 0.0f, 0.0f,
                    -1.0f, 0.0f, 0.0f,
                    -1.0f, 0.0f, 0.0f,
                    -1.0f, 0.0f, 0.0f,
                    -1.0f, 0.0f, 0.0f,
                    -1.0f, 0.0f, 0.0f,

                    // Top face
                    0.0f, 1.0f, 0.0f,
                    0.0f, 1.0f, 0.0f,
                    0.0f, 1.0f, 0.0f,
                    0.0f, 1.0f, 0.0f,
                    0.0f, 1.0f, 0.0f,
                    0.0f, 1.0f, 0.0f,

                    // Bottom face
                    0.0f, -1.0f, 0.0f,
                    0.0f, -1.0f, 0.0f,
                    0.0f, -1.0f, 0.0f,
                    0.0f, -1.0f, 0.0f,
                    0.0f, -1.0f, 0.0f,
                    0.0f, -1.0f, 0.0f
            };
    private FloatBuffer mNormals;
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
    /**
     * Used to hold a light centered on the origin in model space. We need a 4th coordinate so we can get translations to work when
     * we multiply this by our transformation matrices.
     */
    private final float[] mLightPosInModelSpace = new float[]{0.0f, 0.0f, 0.0f, 1.0f};

    /**
     * Used to hold the current position of the light in world space (after transformation via model matrix).
     */
    private final float[] mLightPosInWorldSpace = new float[4];

    /**
     * Used to hold the transformed position of the light in eye space (after transformation via modelview matrix)
     */
    private final float[] mLightPosInEyeSpace = new float[4];
    /**
     * Size of the position data in elements.
     */
    private final int mPositionDataSize = 3;

    /**
     * Size of the color data in elements.
     */
    private final int mColorDataSize = 4;

    /**
     * Size of the normal data in elements.
     */
    private final int mNormalDataSize = 3;

    public MyGeneralOpenGLES2DrawingClass3d(int coordsPerVertex, float[] coordinates, float[] color, short[] drawOrder) {

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
        //Normals
        mNormals = ByteBuffer.allocateDirect(cubeNormalData.length * 4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mNormals.put(cubeNormalData).position(0);
        ///////////////////////////////////////////
        //COLOR
        // float has 4 bytes, colors (RGBA) * 4 bytes
        ByteBuffer cbb = ByteBuffer.allocateDirect(color.length * 4);
        cbb.order(ByteOrder.nativeOrder());
        colorBuffer = cbb.asFloatBuffer();
        colorBuffer.put(color);
        colorBuffer.position(0);

        //   prepare shaders and OpenGL program
        int vertexShader = compileShader(
                GLES20.GL_VERTEX_SHADER, vertexShaderString);
        int fragmentShader = compileShader(
                GLES20.GL_FRAGMENT_SHADER, fragmentShaderString);

        mProgram = createAndLinkProgram(vertexShader, fragmentShader, new String[]{"a_Position", "a_Color", "a_Normal"});//GLES20.glCreateProgram();             // create empty OpenGL Program

    }

    private int compileShader(final int shaderType, final String shaderSource) {
        int shaderHandle = GLES20.glCreateShader(shaderType);

        if (shaderHandle != 0) {
            // Pass in the shader source.
            GLES20.glShaderSource(shaderHandle, shaderSource);

            // Compile the shader.
            GLES20.glCompileShader(shaderHandle);

            // Get the compilation status.
            final int[] compileStatus = new int[1];
            GLES20.glGetShaderiv(shaderHandle, GLES20.GL_COMPILE_STATUS, compileStatus, 0);

            // If the compilation failed, delete the shader.
            if (compileStatus[0] == 0) {
                Log.e("TAG", "Error compiling shader: " + GLES20.glGetShaderInfoLog(shaderHandle));
                GLES20.glDeleteShader(shaderHandle);
                shaderHandle = 0;
            }
        }

        if (shaderHandle == 0) {
            throw new RuntimeException("Error creating shader.");
        }

        return shaderHandle;
    }

//    public void draw(float mvpMatix) {
////    // Set program handles for cube drawing.
////    mMVPMatrixHandle = GLES20.glGetUniformLocation(mPerVertexProgramHandle, "u_MVPMatrix");
////    mMVMatrixHandle = GLES20.glGetUniformLocation(mPerVertexProgramHandle, "u_MVMatrix");
////    mLightPosHandle = GLES20.glGetUniformLocation(mPerVertexProgramHandle, "u_LightPos");
////    mPositionHandle = GLES20.glGetAttribLocation(mPerVertexProgramHandle, "a_Position");
////    mColorHandle = GLES20.glGetAttribLocation(mPerVertexProgramHandle, "a_Color");
////    mNormalHandle = GLES20.glGetAttribLocation(mPerVertexProgramHandle, "a_Normal");
////
////
////    // Calculate position of the light. Rotate and then push into the distance.
////    Matrix.setIdentityM(mLightModelMatrix, 0);
////    Matrix.translateM(mLightModelMatrix, 0, 0.0f, 0.0f, -5.0f);
////    Matrix.rotateM(mLightModelMatrix, 0, angleInDegrees, 0.0f, 1.0f, 0.0f);
////    Matrix.translateM(mLightModelMatrix, 0, 0.0f, 0.0f, 2.0f);
////
////    Matrix.multiplyMV(mLightPosInWorldSpace, 0, mLightModelMatrix, 0, mLightPosInModelSpace, 0);
////    Matrix.multiplyMV(mLightPosInEyeSpace, 0, mViewMatrix, 0, mLightPosInWorldSpace, 0);
//    }

    public void draw(float[] vMatrix, float[] mMatrix, float[] pMatrix) {
        // Add program to OpenGL environment
        GLES20.glUseProgram(mProgram);
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "u_MVPMatrix");
        mMVMatrixHandle = GLES20.glGetUniformLocation(mProgram, "u_MVMatrix");
        mLightPosHandle = GLES20.glGetUniformLocation(mProgram, "u_LightPos");
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "a_Position");
        mColorHandle = GLES20.glGetAttribLocation(mProgram, "a_Color");
        mNormalHandle = GLES20.glGetAttribLocation(mProgram, "a_Normal");

        // get handle to vertex shader's vPosition member

        // Calculate position of the light. Rotate and then push into the distance.
        // Do a complete rotation every 10 seconds.
        long time = SystemClock.uptimeMillis() % 10000L;
        float angleInDegrees = (360.0f / 10000.0f) * ((int) time);
        Matrix.setIdentityM(mLightModelMatrix, 0);
        Matrix.translateM(mLightModelMatrix, 0, 0.0f, 0.0f, 1.0f);
        Matrix.rotateM(mLightModelMatrix, 0, angleInDegrees, 0.0f, 1.0f, 0.0f);
//        Matrix.translateM(mLightModelMatrix, 0, 0.0f, 0.0f, .50f);

        Matrix.multiplyMV(mLightPosInWorldSpace, 0, mLightModelMatrix, 0, mLightPosInModelSpace, 0);
        Matrix.multiplyMV(mLightPosInEyeSpace, 0, vMatrix, 0, mLightPosInWorldSpace, 0);

        vertexBuffer.position(0);
        GLES20.glVertexAttribPointer(mPositionHandle, mPositionDataSize, GLES20.GL_FLOAT, false,
                0, vertexBuffer);

        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Pass in the color information
        colorBuffer.position(0);
        GLES20.glVertexAttribPointer(mColorHandle, mColorDataSize, GLES20.GL_FLOAT, false,
                0, colorBuffer);

        GLES20.glEnableVertexAttribArray(mColorHandle);

//        // Pass in the normal information
//        mNormals.position(0);
//        GLES20.glVertexAttribPointer(mNormalHandle, mNormalDataSize, GLES20.GL_FLOAT, false,
//                0, mNormals);
//
//        GLES20.glEnableVertexAttribArray(mNormalHandle);

        // This multiplies the view matrix by the model matrix, and stores the result in the MVP matrix
        // (which currently contains model * view).
        float[] mMVPMatrix = new float[16];
        Matrix.multiplyMM(mMVPMatrix, 0, vMatrix, 0, mMatrix, 0);

        // Pass in the modelview matrix.
        GLES20.glUniformMatrix4fv(mMVMatrixHandle, 1, false, mMVPMatrix, 0);

        // This multiplies the modelview matrix by the projection matrix, and stores the result in the MVP matrix
        // (which now contains model * view * projection).
        Matrix.multiplyMM(mMVPMatrix, 0, pMatrix, 0, mMVPMatrix, 0);

        // Pass in the combined matrix.
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);

        // Pass in the light position in eye space.
        GLES20.glUniform3f(mLightPosHandle, mLightPosInEyeSpace[0], mLightPosInEyeSpace[1], mLightPosInEyeSpace[2]);

        // Draw the cube.
//			GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 36);
        GLES20.glDrawElements(GLES20.GL_TRIANGLE_STRIP, 36, GLES20.GL_UNSIGNED_SHORT, drawListBuffer);
//        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 36);
        // Disable vertex array
//        GLES20.glDisableVertexAttribArray(mPositionHandle);
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

    @Override
    public void draw(float[] mvpMatrix) {

    }
}

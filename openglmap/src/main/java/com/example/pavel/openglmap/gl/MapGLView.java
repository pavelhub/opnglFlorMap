package com.example.pavel.openglmap.gl;

import android.content.Context;
import android.graphics.Point;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Pavel on 06.11.2015.
 */
public class MapGLView extends GLSurfaceView {
    LessonTwoRenderer glRendererMap;

    public MapGLView(Context context) {
        super(context);
        initViewContext();
    }

    public MapGLView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViewContext();
    }

    private void initViewContext() {
        setEGLContextClientVersion(2);
        glRendererMap = new LessonTwoRenderer();
        setRenderer(glRendererMap);

//        setRenderMode(RENDERMODE_WHEN_DIRTY);
    }
    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
    private float mPreviousX;
    private float mPreviousY;
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        // MotionEvent reports input details from the touch screen
        // and other input controls. In this case, you are only
        // interested in events where the touch position changed.

        float x = e.getX();
        float y = e.getY();

        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:

                float dx = x - mPreviousX;
                float dy = y - mPreviousY;

                // reverse direction of rotation above the mid-line
                if (y > getHeight() / 2) {
                    dx = dx * -1 ;
                }

                // reverse direction of rotation to left of the mid-line
                if (x < getWidth() / 2) {
                    dy = dy * -1 ;
                }

//                glRendererMap.setmAngle(
//                        glRendererMap.getmAngle() -
//                                ((dx + dy) * TOUCH_SCALE_FACTOR));  // = 180.0f / 320
                requestRender();
        }

        mPreviousX = x;
        mPreviousY = y;
        return true;
    }
//    @Override
//    public boolean onTouchEvent(MotionEvent e) {
//        final float x = e.getX();
//        final float y = e.getY();
//
//        final float[] normCoord = simpleTouch2GLCoord(new Point((int) x, (int) y));
//        Log.i("GlX=" + normCoord[0] + " glY=" + normCoord[1], "X=" + x + " Y=" + y);
//        final float[] glCoord = glRendererMap.glCoordinate(normCoord[0], normCoord[1]);
//        Log.i("GlX=" + glCoord[0] + " glY=" + glCoord[1], "X=" + x + " Y=" + y);
//
//        ((Activity) getContext()).runOnUiThread(new Runnable() {
//
//            @Override
//            public void run() {
//                String s = "GlX=" + glCoord[0] + " glY=" + glCoord[1] + " X=" + x + " Y=" + y;
//
//                s += glRendererMap.getTouchedObject(glCoord);
//                requestRender();
//                ((Activity) getContext()).setTitle(s);
//            }
//        });
//        return false;
//
//    }

    public float[] simpleTouch2GLCoord(Point touch) {
//        Display display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
//        Point size = new Point();
//        display.getSize(size);

        float screenW = getWidth();
        float screenH = getHeight();

        float normalizedX = 2f * touch.x / screenW - 1f;
        float normalizedY = 1f - 2f * touch.y / screenH;
        float normalizedZ = 0.0f;
        return (new float[]{normalizedX, normalizedY, normalizedZ});

    }

}

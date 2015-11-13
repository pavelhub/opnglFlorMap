package com.example.pavel.openglmap.gl;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by Pavel on 06.11.2015.
 */
public class MapGLView extends GLSurfaceView {
    GLRendererMap glRendererMap;

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
        glRendererMap = new GLRendererMap();
        setRenderer(glRendererMap);

        setRenderMode(RENDERMODE_WHEN_DIRTY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        final float x = e.getX();
        final float y = e.getY();

        final float[] normCoord = simpleTouch2GLCoord(new Point((int) x, (int) y));
        Log.i("GlX=" + normCoord[0] + " glY=" + normCoord[1], "X=" + x + " Y=" + y);
        final float[] glCoord = glRendererMap.glCoordinate(normCoord[0], normCoord[1]);
        Log.i("GlX=" + glCoord[0] + " glY=" + glCoord[1], "X=" + x + " Y=" + y);

        ((Activity) getContext()).runOnUiThread(new Runnable() {

            @Override
            public void run() {
                String s = "GlX=" + glCoord[0] + " glY=" + glCoord[1] + " X=" + x + " Y=" + y;

                s += glRendererMap.getTouchedObject(glCoord);
                requestRender();
                ((Activity) getContext()).setTitle(s);
            }
        });
        return false;

    }

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

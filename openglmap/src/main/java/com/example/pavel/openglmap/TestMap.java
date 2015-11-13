package com.example.pavel.openglmap;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.pavel.openglmap.gl.MapGLView;

public class TestMap extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new MapGLView(this));
//        GLSurfaceView glSurfaceView = (GLSurfaceView) findViewById(R.id.GLSurfaceView);

    }
}

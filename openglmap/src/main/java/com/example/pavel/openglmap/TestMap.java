package com.example.pavel.openglmap;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.example.pavel.openglmap.gl.MapGLView;
import com.example.pavel.openglmap.gl.RenderConfig;
import com.example.pavel.openglmap.gl.utils.ObjectModelTransform;
import com.example.pavel.openglmap.net.NetworkHelper;
import com.example.pavel.openglmap.net.model.VenueModel;

public class TestMap extends AppCompatActivity {
    MapGLView glSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_map);


        findViewById(R.id.ButtonLoadMap).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncTask<Void, Void, VenueModel>() {
                    @Override
                    protected VenueModel doInBackground(Void... params) {
                        VenueModel venueServerModel = new NetworkHelper().getVenueServerModel(TestMap.this);
                        ObjectModelTransform objectModelTransform = new ObjectModelTransform();
                        objectModelTransform.normalizeVenueModel(venueServerModel);
                        return venueServerModel;
                    }

                    @Override
                    protected void onPostExecute(VenueModel venueModel) {
                        super.onPostExecute(venueModel);
                        RenderConfig renderConfig = new RenderConfig();
                        renderConfig.floorModel = venueModel.getFloors().get(0);
                        renderConfig.is3DModel = false;
                        showGLBaseOnConfig(renderConfig);
                    }
                }.execute();

            }
        });
        findViewById(R.id.ButtonLoadMap3d).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncTask<Void, Void, VenueModel>() {
                    @Override
                    protected VenueModel doInBackground(Void... params) {
                        VenueModel venueServerModel = new NetworkHelper().getVenueServerModel(TestMap.this);
                        ObjectModelTransform objectModelTransform = new ObjectModelTransform();
                        objectModelTransform.normalizeVenueModel(venueServerModel);
                        return venueServerModel;
                    }

                    @Override
                    protected void onPostExecute(VenueModel venueModel) {
                        super.onPostExecute(venueModel);
                        RenderConfig renderConfig = new RenderConfig();
                        renderConfig.floorModel = venueModel.getFloors().get(0);
                        renderConfig.is3DModel = true;
                        showGLBaseOnConfig(renderConfig);
                    }
                }.execute();

            }
        });

    }

    private void showGLBaseOnConfig(RenderConfig renderConfig) {
        LinearLayout container = (LinearLayout) findViewById(R.id.containerGL);

        glSurfaceView = new MapGLView(TestMap.this);
        glSurfaceView.initViewRender(renderConfig);
        container.removeAllViews();
        container.addView(glSurfaceView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (glSurfaceView != null)
            glSurfaceView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (glSurfaceView != null)
            glSurfaceView.onPause();
    }
}

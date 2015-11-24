package com.example.pavel.openglmap;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.pavel.openglmap.gl.MapGLView;
import com.example.pavel.openglmap.gl.RenderConfig;
import com.example.pavel.openglmap.gl.utils.ObjectModelTransform;
import com.example.pavel.openglmap.net.NetworkHelper;
import com.example.pavel.openglmap.net.model.VenueModel;

public class TestMap extends AppCompatActivity {
    MapGLView glSurfaceView;
    VenueModel venueServerModel;
    int selectedFloor = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_map);

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
                TestMap.this.venueServerModel = venueModel;
            }
        }.execute();

        findViewById(R.id.buttonChooseFloor).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] items = new String[venueServerModel.getFloors().size()];
                for (int i = 0; i < venueServerModel.getFloors().size(); i++) {
                    items[i] = "Floor " + i;

                }
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setAdapter(new ArrayAdapter<String>(v.getContext(), android.R.layout.simple_list_item_1, items), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        findViewById(R.id.buttonChooseFloor).setTag(which);
                        selectedFloor = which;
                        ((Button) findViewById(R.id.buttonChooseFloor)).setText("Selected " + which + " floor");

                        LinearLayout container = (LinearLayout) findViewById(R.id.containerGL);
                        container.removeAllViews();
                    }
                });
                builder.create().show();
            }
        });
        findViewById(R.id.ButtonLoadMap).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedFloor == -1) {
                    Toast.makeText(v.getContext(), "Please select floor", Toast.LENGTH_LONG).show();
                    return;
                }
                RenderConfig renderConfig = new RenderConfig();
                renderConfig.floorModel = venueServerModel.getFloors().get(selectedFloor);
                renderConfig.is3DModel = false;
                showGLBaseOnConfig(renderConfig);


            }
        });
        findViewById(R.id.ButtonLoadMap3d).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedFloor == -1) {
                    Toast.makeText(v.getContext(), "Please select floor", Toast.LENGTH_LONG).show();
                    return;
                }
                RenderConfig renderConfig = new RenderConfig();
                renderConfig.floorModel = venueServerModel.getFloors().get(selectedFloor);
                renderConfig.is3DModel = true;
                showGLBaseOnConfig(renderConfig);

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

package com.example.pavel.openglmap.net;

import android.content.Context;

import com.example.pavel.openglmap.net.model.VenueModel;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by pavel on 11/19/15.
 */
public class NetworkHelper {
    public VenueModel getVenueServerModel(Context context) {
        InputStream inputStream = null;
        try {
            inputStream = context.getAssets().open("model/venue_model");
        } catch (IOException e) {
            e.printStackTrace();
        }
        String modelString = convertStreamToString(inputStream);
        Gson gson = new Gson();

        return gson.fromJson(modelString, VenueModel.class);
    }

    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}

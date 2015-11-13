package com.example.pavel.openglmap.gl.servermodel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pavel on 12.11.2015.
 */
public class Venue {
    public float widht;
    public float height;

    public List<Wall> getWallList() {
        if (wallList == null)
            wallList = new ArrayList<>();
        return wallList;
    }

    public void setWallList(List<Wall> wallList) {
        this.wallList = wallList;
    }

    List<Wall> wallList;

    public Point normalize(Point point) {
//        Display display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
//        Point size = new Point();
//        display.getSize(size);

        Point point1 = new Point();
        point1.x = 2f * point.x / widht - 1f;
        point1.x = 1f - 2f * point.y / height;

        return point1;//(new float[]{normalizedX, normalizedY, normalizedZ});

    }
}

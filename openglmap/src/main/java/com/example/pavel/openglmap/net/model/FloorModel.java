package com.example.pavel.openglmap.net.model;

import java.util.List;

/**
 * Created by pavel on 11/19/15.
 */
public class FloorModel {
    int floor_id;
    int venue_id;
    String walls_color;
    String floor_color;
    float width;
    float height;
    List<WallModel> walls;


    public int getFloor_id() {
        return floor_id;
    }

    public void setFloor_id(int floor_id) {
        this.floor_id = floor_id;
    }

    public int getVenue_id() {
        return venue_id;
    }

    public void setVenue_id(int venue_id) {
        this.venue_id = venue_id;
    }

    public String getWalls_color() {
        return walls_color;
    }

    public void setWalls_color(String walls_color) {
        this.walls_color = walls_color;
    }

    public String getFloor_color() {
        return floor_color;
    }

    public void setFloor_color(String floor_color) {
        this.floor_color = floor_color;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public List<WallModel> getWalls() {
        return walls;
    }

    public void setWalls(List<WallModel> walls) {
        this.walls = walls;
    }
}

package com.example.pavel.openglmap.net.model;

import java.util.List;

/**
 * Created by pavel on 11/19/15.
 */
public class WallModel {
    String type;
    List<NodeModel> nodeModels;
    float depth;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public float getDepth() {
        return depth;
    }

    public void setDepth(float depth) {
        this.depth = depth;
    }

    public List<NodeModel> getNodeModels() {
        return nodeModels;
    }

    public void setNodeModels(List<NodeModel> nodeModels) {
        this.nodeModels = nodeModels;
    }
}

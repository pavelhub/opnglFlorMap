package com.example.pavel.openglmap.gl.utils;

import com.example.pavel.openglmap.net.model.FloorModel;
import com.example.pavel.openglmap.net.model.NodeModel;
import com.example.pavel.openglmap.net.model.VenueModel;
import com.example.pavel.openglmap.net.model.WallModel;

/**
 * Created by pavel on 11/19/15.
 */
public class ObjectModelTransform {
    public VenueModel normalizeVenueModel(VenueModel venueModel) {
        VenueModel normalizeModel = venueModel;

        for (FloorModel floorModel : venueModel.getFloors()) {
            for (NodeModel nodeModel : floorModel.getNodeModels()) {
                normalizeNode(floorModel, nodeModel);
            }

            for (WallModel wall : floorModel.getWalls()) {
                if (wall.getType().equals("linear")) {
                    wall.setDepth(normalizeDepth(wall.getDepth(), Math.max(floorModel.getWidth(), floorModel.getHeight())));
                }
            }
        }
        return normalizeModel;
    }

    private void normalizeNode(FloorModel floorModel, NodeModel nodeModel) {
        float[] nodeNormalize = normalizePoint(nodeModel.getX(), nodeModel.getY(), Math.max(floorModel.getWidth(), floorModel.getHeight()));
        nodeModel.setX(nodeNormalize[0]);
        nodeModel.setY(nodeNormalize[1]);
    }

    public float[] normalizePoint(float x, float y, float maxSide) {

        float normalizedX = 2f * x / maxSide - 1f;
        float normalizedY = 1f - 2f * y / maxSide;
        float normalizedZ = 0.0f;
        return (new float[]{normalizedX, normalizedY, normalizedZ});

    }

    public float normalizeDepth(float depth, float maxSide) {
        return depth / maxSide;

    }
}

package com.example.pavel.openglmap.gl.view.model;

import com.example.pavel.openglmap.net.model.NodeModel;

/**
 * Created by pavel on 11/20/15.
 */
public class NodeViewModel {
    float x;
    float y;

    NodeViewProjection left;
    NodeViewProjection right;
    public NodeViewModel(NodeModel nodeModel) {
        this.x = nodeModel.getX();
        this.y = nodeModel.getY();
    }


    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public NodeViewProjection getLeft() {
        return left;
    }

    public void setLeft(NodeViewProjection left) {
        this.left = left;
    }

    public NodeViewProjection getRight() {
        return right;
    }

    public void setRight(NodeViewProjection right) {
        this.right = right;
    }
}

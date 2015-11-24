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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NodeViewModel that = (NodeViewModel) o;

        if (Float.compare(that.x, x) != 0) return false;
        return Float.compare(that.y, y) == 0;

    }

    @Override
    public int hashCode() {
        int result = (x != +0.0f ? Float.floatToIntBits(x) : 0);
        result = 31 * result + (y != +0.0f ? Float.floatToIntBits(y) : 0);
        return result;
    }
}

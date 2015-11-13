package com.example.pavel.openglmap.gl.servermodel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pavel on 11.11.2015.
 */
public class Wall {
    public Point point1;
    public Point point2;
    public float[] color;
    public float weight;
    public float k;
    public float b;
    public float kParale;
    public float bParalel1;
    public float bParalel2;
    public float bPerp2;
    public float bPerp1;
    public float kPerp;

    public Wall(Point poit1, Point poit2, float weight) {
        this.point1 = poit1;
        this.point2 = poit2;
        this.weight = weight;
        getK();
        getB();
        kParale = k;
        bPerp1 = getBPerp(poit1);
        bPerp2 = getBPerp(poit2);

        if (Math.abs(k) == 0.0)
            kPerp = k = 0.0f;
        else
            kPerp = -1 / k;

//        if(b>0) {
        float bX = (float) (weight / (2.f * Math.sqrt(poit1.x * poit1.x + poit1.y * poit1.y)) - k * poit1.x + poit1.y);
        if (k == 0) {
            bX = weight / 2.f;
        }
        bParalel1 = b + bX;//weight / () k* point1.x+poit1.y+bb - ;
        bParalel2 = b - bX;// weight / 2.f;
//        }else {
//            bParalel1 = b + weight / 2.f;
//            bParalel2 = b - weight / 2.f;
//        }


    }

    private Point getPointParalePerp(Point point, float kParale, float bParalel, float kPerp, float bPerp) {
        Point pointPerp = new Point();
        if (point1.x == point2.x) {
            pointPerp.y = point.y;
            pointPerp.x = bParalel;
        } else if (point1.y == point2.y) {
            pointPerp.x = point.x;
            pointPerp.y = bParalel;
        } else {
            pointPerp.x = getPerpX(kParale, kPerp, bParalel, bPerp);
            pointPerp.y = getPerpY(pointPerp.x, kPerp, bPerp);
        }

        return pointPerp;
    }

    public List<Point> getListPerpendecularparalellPoints() {
        List<Point> list = new ArrayList<>();
        list.add(getPointParalePerp(point1, kParale, bParalel1, kPerp, bPerp1));
        list.add(getPointParalePerp(point1, kParale, bParalel2, kPerp, bPerp1));

        list.add(getPointParalePerp(point2, kParale, bParalel2, kPerp, bPerp2));
        list.add(getPointParalePerp(point2, kParale, bParalel1, kPerp, bPerp2));

        return list;
    }


    private void getK() {
        float deltaX = point1.x - point2.x;
        float deltaY = point1.y - point2.y;
        if (deltaX == 0) {
            k = 0;
        } else {

            k = deltaY
                    /
                    (deltaX * 1.0f);
        }


    }

    private void getB() {
        float deltaX = point1.x - point2.x;
        float deltaY = point1.y - point2.y;
        if (k == 0) {
            if (deltaX == 0)
                b = point1.x;
            if (deltaY == 0)
                b = point1.y;
        } else
            b = point1.y - k * point1.x;
    }

    public float getBPerp(Point point) {
        return point.y - kPerp * point.x;
    }

    public float getPerpX(float kParalel, float kPerp, float bParalel, float bPerp) {
        float deltaK = kParalel - kPerp;
        if (deltaK == 0.0f)
            deltaK = 1.0f;
        return (bPerp - bParalel) / deltaK;
    }

    public float getPerpY(float x, float kPerp, float bPerp) {
        return kPerp * x + bPerp;
    }

//    private Point getPointOnSegment() {
//        float x1 = point1.x,
//                x2 = point2.x,
//                y1 = point1.y,
//                y2 = point2.x;
//        MathUtil.Point onsegmentPoint = new MathUtil.Point(-1, -1);
//        if (x1 == x2) {
//            onsegmentPoint.x = x1;
//            onsegmentPoint.y = userPoint.y;
//        } else if (y1 == y2) {
//            onsegmentPoint.y = y1;
//            onsegmentPoint.x = userPoint.x;
//        } else {
//            segment.getB2(userPoint);
//            onsegmentPoint.x = segment.getPerpX();
//            onsegmentPoint.y = segment.getPerpY(onsegmentPoint.x);
//        }
//
//        return onsegmentPoint;
//    }
}

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
    public float totalDistance = 0;

    public Wall(Point poit1, Point poit2, float weight) {
        this.point1 = poit1;
        this.point2 = poit2;
        totalDistance = (float) getDistanceToSegment(poit1, poit2);
        this.weight = weight;
        getK();
        getB();
        kParale = k;

        if (Math.abs(k) == 0.0)
            kPerp = k = 0.0f;
        else
            kPerp = -1 / k;
        bPerp1 = getBPerp(poit1);
        bPerp2 = getBPerp(poit2);

//        if(b>0) {
        float halfWeight = weight / 2.f;

        bParalel1 = (float) (halfWeight * Math.sqrt(k * k + 1) + b);
        bParalel2 = (float) (-halfWeight * Math.sqrt(k * k + 1) + b);
        if (k == 0) {
//            bX = halfWeight;
            bParalel1 = b - halfWeight;//weight / () k* point1.x+poit1.y+bb - ;
            bParalel2 = b + halfWeight;// weight / 2.f;
        }
//        bParalel1 = b - bX;//weight / () k* point1.x+poit1.y+bb - ;
//        bParalel2 = b + bX;// weight / 2.f;


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

    List<Point> listParalel;

    public List<Point> getListPerpendecularparalellPoints() {
        if (listParalel == null) {
            listParalel = new ArrayList<>();

            listParalel.add(getPointParalePerp(point1, kParale, bParalel1, kPerp, bPerp1));
            listParalel.add(getPointParalePerp(point1, kParale, bParalel2, kPerp, bPerp1));
            listParalel.add(getPointParalePerp(point2, kParale, bParalel2, kPerp, bPerp2));
            listParalel.add(getPointParalePerp(point2, kParale, bParalel1, kPerp, bPerp2));

        }
        return listParalel;
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

    private boolean isPointInWall(Point point) {
        if (totalDistance == getDistanceToSegment(point, point1) + getDistanceToSegment(point, point2))
            return true;
        else return false;
    }


    public Point getPointOutWall(Wall nextWall) {
        if (point1.equals(nextWall.point1)) {
            return getPointProjectionNotInWall(this, point1, nextWall.getListPerpendecularparalellPoints());
        } else if (point1.equals(nextWall.point2)) {
            return getPointProjectionNotInWall(this, point1, nextWall.getListPerpendecularparalellPoints());
        } else if (point2.equals(nextWall.point1)) {
            return getPointProjectionNotInWall(this, point2, nextWall.getListPerpendecularparalellPoints());
        } else if (point2.equals(nextWall.point2)) {
            return getPointProjectionNotInWall(this, point2, nextWall.getListPerpendecularparalellPoints());
        }

//
//            if (!isPointInPeremetr(nextWall.getListPerpendecularparalellPoints().get(0)))
//                return nextWall.getListPerpendecularparalellPoints().get(0);
//            else
////            if (!isPointInPeremetr(nextWall.getListPerpendecularparalellPoints().get(1)))
//                return nextWall.getListPerpendecularparalellPoints().get(1);
//
//        } else if (point1.equals(nextWall.point2)) {
//            if (!isPointInPeremetr(nextWall.getListPerpendecularparalellPoints().get(2)))
//                return nextWall.getListPerpendecularparalellPoints().get(2);
//            else
////            if (!isPointInPeremetr(getListPerpendecularparalellPoints().get(3)))
//                return nextWall.getListPerpendecularparalellPoints().get(3);
//
//        } else if (point2.equals(nextWall.point1)) {
//            if (!isPointInPeremetr(nextWall.getListPerpendecularparalellPoints().get(0)))
//                return nextWall.getListPerpendecularparalellPoints().get(0);
//            else
////            if (!isPointInPeremetr(nextWall.getListPerpendecularparalellPoints().get(1)))
//                return nextWall.getListPerpendecularparalellPoints().get(1);
//
//        } else if (point1.equals(nextWall.point2)) {
//            if (!isPointInPeremetr(nextWall.getListPerpendecularparalellPoints().get(2)))
//                return nextWall.getListPerpendecularparalellPoints().get(2);
//            else
////            if (!isPointInPeremetr(getListPerpendecularparalellPoints().get(3)))
//                return nextWall.getListPerpendecularparalellPoints().get(3);
//
//        }
        return null;
    }

    public static Point getPointProjectionInWall(Wall wall, Point point1, Point point2) {
        if (!wall.isPointInPeremetr(point1))
            return point1;
        else if (!wall.isPointInPeremetr(point2))
            return point2;
        return null;
    }

    public static Point getPointProjectionNotInWall(Wall wall, Point toPoint, List<Point> points) {
        List<Point> pointsNotIn = new ArrayList<>();
        for (Point point : points) {
            if (!wall.isPointInPeremetr(point))
                pointsNotIn.add(point);
        }
        Point current = pointsNotIn.get(0);
        float minDist = 99999;
        for (Point point : pointsNotIn) {
            double distanceToSegment = getDistanceToSegment(point, toPoint);
            if (distanceToSegment <= minDist) {
                current = point;
                minDist = (float) distanceToSegment;
            }
        }

        return current;
    }

    private boolean isPointInPeremetr(Point point) {

        if (getListPerpendecularparalellPoints().get(0).x < getListPerpendecularparalellPoints().get(2).x && getListPerpendecularparalellPoints().get(0).y < getListPerpendecularparalellPoints().get(2).y) {
            if (point.x >= getListPerpendecularparalellPoints().get(0).x && point.x <= getListPerpendecularparalellPoints().get(2).x && point.y >= getListPerpendecularparalellPoints().get(0).y && point.y <= getListPerpendecularparalellPoints().get(2).y) {
                return true;
            }
        } else if (getListPerpendecularparalellPoints().get(0).x > getListPerpendecularparalellPoints().get(2).x && getListPerpendecularparalellPoints().get(0).y > getListPerpendecularparalellPoints().get(2).y) {
            if (point.x >= getListPerpendecularparalellPoints().get(2).x && point.x <= getListPerpendecularparalellPoints().get(0).x && point.y >= getListPerpendecularparalellPoints().get(2).y && point.y <= getListPerpendecularparalellPoints().get(0).y) {
                return true;
            }
        } else if (getListPerpendecularparalellPoints().get(0).x > getListPerpendecularparalellPoints().get(2).x && getListPerpendecularparalellPoints().get(0).y < getListPerpendecularparalellPoints().get(2).y) {
            if (point.x >= getListPerpendecularparalellPoints().get(2).x && point.x <= getListPerpendecularparalellPoints().get(0).x && point.y >= getListPerpendecularparalellPoints().get(0).y && point.y <= getListPerpendecularparalellPoints().get(2).y) {
                return true;
            }
        } else if (getListPerpendecularparalellPoints().get(0).x < getListPerpendecularparalellPoints().get(2).x && getListPerpendecularparalellPoints().get(0).y > getListPerpendecularparalellPoints().get(2).y) {
            if (point.x >= getListPerpendecularparalellPoints().get(0).x && point.x <= getListPerpendecularparalellPoints().get(2).x && point.y >= getListPerpendecularparalellPoints().get(2).y && point.y <= getListPerpendecularparalellPoints().get(0).y) {
                return true;
            }
        }

        return false;
    }

    public static double getDistanceToSegment(Point point, Point point2) {

        return Math.sqrt(
                Math.pow(point.x - point2.x, 2) +
                        Math.pow(point.y - point2.y, 2));
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

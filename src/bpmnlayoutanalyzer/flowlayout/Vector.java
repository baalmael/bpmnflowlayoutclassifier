package bpmnlayoutanalyzer.flowlayout;


import bpmnlayoutanalyzer.layoutanalyzer.bpmnmodel.SequenceFlow;
import bpmnlayoutanalyzer.layoutanalyzer.bpmnmodel.WayPoint;

import java.util.Objects;

/**
 * Representing a two dimensional Vector.
 */
public class Vector {
    private double x;
    private double y;
    private double centerX; // Position of the center of the vector in the diagram
    private double centerY;
    private double combinationErrorSum = 0; // sum of pairwise angles between the connected Vectors
    int numberOfConnectedVectors = 1;
    private boolean mark; // true if Node is on this Vector

    public Vector(double x, double y) {
        this(x, y, false);
    }

    public Vector(double x, double y, boolean mark) {
        this.x = x;
        this.y = y;
        this.mark = mark;
    }

    public Vector(WayPoint wp1, WayPoint wp2) {
        this(wp2.getX() - wp1.getX(), wp2.getY() - wp1.getY(), false);
    }

    public Vector(SequenceFlow sf) {
        this(sf.getWayPoints().getWaypoints().get(0),
                sf.getWayPoints().getWaypoints().get(sf.getWayPoints().getWaypoints().size() - 1));
    }

    public Vector(Vector v1, Vector v2, boolean mark) {
        this(v2.getX() - v1.getX(), v2.getY() - v1.getY(), mark);
    }

    public Vector(Vector v1, Vector v2) {
        this(v1, v2, false);
    }

    public Vector(WayPoint wayPoint) {
        this(wayPoint.getX(), wayPoint.getY());
    }

    private Vector(double x, double y, boolean mark, int numberOfConnectedVectors, double combinationError) {
        this(x, y, mark);
        this.numberOfConnectedVectors = numberOfConnectedVectors;
        this.combinationErrorSum = combinationError;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getDirection() {
        return Math.atan2(y, x);
    }

    public double getLength() {
        return Math.sqrt(x * x + y * y);
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double angle(Vector vector) {
        return vector.getDirection() - getDirection();
    }

    /**
     * @return angle between 0 and pi
     */
    public double smallestAbsoluteAngleTo(Vector vector) {
        double absAngle = Math.abs(angle(vector));
        return Math.min(absAngle, 2 * Math.PI - absAngle);
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + "," + mark + ")";
    }

    public Vector plus(Vector vector) {
        Vector v = new Vector(x + vector.getX(), y + vector.getY(), true, this.numberOfConnectedVectors + vector.numberOfConnectedVectors, this.combinationErrorSum + vector.combinationErrorSum + smallestAbsoluteAngleTo(vector));
        Vector center1 = getCenterPosition();
        Vector center2 = vector.getCenterPosition();
        Vector toAdd = new Vector(center1, center2).divide(2.0);
        v.setCenterPosition(center1.getX() + toAdd.getX(), center1.getY() + toAdd.getY());
        return v;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector vector = (Vector) o;
        return Double.compare(vector.x, x) == 0 && Double.compare(vector.y, y) == 0 && mark == vector.mark;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, mark);
    }

    public boolean isMarked() {
        return mark;
    }



    static double smallestPositiveAngle(Vector v1, Vector v2) {
        double absAngle = Math.abs(v1.getDirection() - v2.getDirection());
        return Math.min(absAngle, 2 * Math.PI - absAngle);
    }

    static Vector add(Vector v1, Vector v2) {
        return v1.plus(v2);
    }

    public boolean isHorizontal() {
        return Math.abs(x) > Math.abs(y);
    }

    public void setMark(boolean mark) {
        this.mark = mark;
    }

    public double getCombinationErrorSum() {
        return combinationErrorSum;
    }

    public double getAverageCombinationError() {
        if (numberOfConnectedVectors == 1) return 0;
        return combinationErrorSum / (numberOfConnectedVectors - 1);
    }


    public void setCenterPosition(double x, double y) {
        centerX = x;
        centerY = y;
    }

    public Vector divide(double divisor) {
        return new Vector(x / divisor, y / divisor);
    }

    public void setCenterPosition(Vector center) {
        setCenterPosition(center.getX(), center.getY());
    }

    public Vector getCenterPosition() {
        return new Vector(centerX, centerY);
    }
}

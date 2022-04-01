package bpmnlayoutanalyzer.layoutanalyzer.bpmnmodel;


import bpmnlayoutanalyzer.flowlayout.Vector;

/**
 * From BPMN-Layout-Analyzer (edited by Elias Baalmann)
 */
public class Bounds {
    private double x;
    private double y;
    private double width;
    private double height;

    public Bounds() {
    }

    public Bounds(double x, double y, double width, double height) {
        super();
        set(x, y, width, height);
    }

    public Bounds(Bounds bounds) {
        set(bounds.x, bounds.y, bounds.width, bounds.height);
    }

    public void set(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setMinX(double x) {
        if (this.x >= x) {
            this.x = x;
        }
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setMinY(double y) {
        if (this.y >= y) {
            this.y = y;
        }
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public void extendTo(Bounds b) {
        if (b == null)
            return;

        double myMaxX = getX() + getWidth();
        double myMaxY = getY() + getHeight();
        double theirMaxX = b.getX() + b.getWidth();
        double theirMaxY = b.getY() + b.getHeight();

        double maxX = Math.max(myMaxX, theirMaxX);
        double maxY = Math.max(myMaxY, theirMaxY);

        setMinX(b.getX());
        setMinY(b.getY());


        setWidth(maxX - getX());
        setHeight(maxY - getY());
    }

    public double getArea() {
        return getWidth() * getHeight();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (this.getClass() != o.getClass()) {
            return false;
        }

        Bounds b = (Bounds) o;

        return
                this.x == b.x &&
                        this.y == b.y &&
                        this.width == b.width &&
                        this.height == b.height
                ;
    }

    @Override
    public int hashCode() {
        return Double.hashCode(x + y + width + height);
    }

    @Override
    public String toString() {
        return "x=" + x + ",y=" + y + ",w=" + width + ",h=" + height;
    }

    public double getCenterX() {
        return x + width / 2;
    }

    public double getCenterY() {
        return y + height / 2;
    }

    public Vector getCenter() {
        return new Vector(this.getCenterX(), this.getCenterY());
    }
}

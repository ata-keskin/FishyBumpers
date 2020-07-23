package de.tum.in.ase.eist;

public class Point2D {

    private int x;
    private int y;

    public Point2D(int x, int y){
        this.x = x;
        this.y = y;
    }

    public Point2D(double x, double y){
        this.x = (int) x;
        this.y = (int) y;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    @Override
    public String toString() {
        return "Point2D{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}

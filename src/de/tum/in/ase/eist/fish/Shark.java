package de.tum.in.ase.eist.fish;

import de.tum.in.ase.eist.Dimension2D;
import de.tum.in.ase.eist.Point2D;
import de.tum.in.ase.eist.car.Car;

public class Shark extends Car {

    private static final String sharkLFile = "fish/shark/shark_l";
    private static final String sharkRFile = "fish/shark/shark_r";
    private static final String extension = ".png";
    private static final Dimension2D collisionBox = new Dimension2D(610, 85);
    private boolean facingLeft;

    public Shark(int maxX, int maxY) {
        super(maxX, maxY, new Dimension2D(642, 168), 2, 5);
        this.setIcon(sharkLFile, sharkRFile);
        setLevel(4);

        reset(maxX, maxY);

        setExtension(extension);
    }

    public Point2D getCollisionPos() {
        return new Point2D(position.getX() + 16, position.getY() - 60); //magic numbers for the image file
    }

    public Dimension2D getCollisionBox() {
        return collisionBox;
    }


    @Override
    public void updatePosition(int maxX, int maxY) {
        this.position = new Point2D(this.position.getX() + speed, this.position.getY());
    }

    public void reset(int maxX, int maxY) {
        double rand = Math.random();
        facingLeft = rand > 0.5;

        int yPos;
        if(rand > 0.6) {
            yPos = (maxY/ 2) + 168/2 - maxY/3;
        } else if (rand > 0.3) {
            yPos = (maxY/ 2) + 168/2;
        } else {
            yPos = (maxY/ 2) + 168/2 + maxY/3;
        }

        if(facingLeft) {
            speed = -8;
            position = new Point2D( maxX + 1000, yPos);
            setDirection(270);
        } else {
            speed = 8;
            position = new Point2D(-1000 - 642, yPos);
            setDirection(90);
        }

    }


}

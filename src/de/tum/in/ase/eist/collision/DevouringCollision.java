package de.tum.in.ase.eist.collision;

import de.tum.in.ase.eist.Dimension2D;
import de.tum.in.ase.eist.Point2D;
import de.tum.in.ase.eist.car.Car;
import de.tum.in.ase.eist.fish.Shark;

public class DevouringCollision extends Collision {
    private int heightOfCanvas;

    /**
     * The fish with the higher level wins the collision, the player can level up by "eating" other fish!
     * @param car1 Fish1
     * @param car2 Fish2
     */
    public DevouringCollision(Car car1, Car car2, int heightOfCanvas) {
        super(car1, car2);
        this.heightOfCanvas = heightOfCanvas; //this matters when the window size is big, required for correct collision handling
    }



    @Override
    public boolean detectCollision() {
        Point2D p1 = car1.getPosition();
        Dimension2D d1 = car1.getSize();

        Point2D p2 = car2.getPosition();
        Dimension2D d2 = car2.getSize();

        if(car2 instanceof Shark) {
            p2 = ((Shark) car2).getCollisionPos();
            d2 = ((Shark) car2).getCollisionBox();
        }

        //p1s sides have no gap between them and p2s sides (better collision detection)
        return p1.getX() < p2.getX() + d2.getWidth() &&
                p1.getX() + d1.getWidth() > p2.getX() &&
                heightOfCanvas - p1.getY() < heightOfCanvas - p2.getY() + d2.getHeight() &&
                heightOfCanvas - p1.getY() + d1.getHeight() > heightOfCanvas - p2.getY();
    }

    @Override
    public Car evaluate() {
        return car1.getLevel() > car2.getLevel() ? car1 : car2;
    }

    @Override
    public Car evaluateLoser() {
        return car1.getLevel() > car2.getLevel() ? car2 : car1;
    }
}

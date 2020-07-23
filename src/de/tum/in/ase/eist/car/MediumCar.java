package de.tum.in.ase.eist.car;

import de.tum.in.ase.eist.Dimension2D;

public class MediumCar extends Car {

    private static final String largeLFile = "fish/large/large_l.png";
    private static final String largeRFile = "fish/large/large_r.png";

    /**
     * Constructor for a MediumCar.
     *
     * @param maxX Maximum x coordinate (width) of the game board
     * @param maxY Maximum y coordinate (height) of the game board
     */
    public MediumCar(int maxX, int maxY) {
        super(maxX, maxY, new Dimension2D(40, 32));
        this.MIN_SPEED = 3;
        this.MAX_SPEED = 4;
        this.setRandomSpeed();
        this.setIcon(largeLFile, largeRFile);
        setLevel(1);
    }

}

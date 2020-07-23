package de.tum.in.ase.eist.fish;

import de.tum.in.ase.eist.Dimension2D;
import de.tum.in.ase.eist.car.Car;

public class PlayerFish extends Car {

    private static final String playerLFile = "fish/player/player_l.png";
    private static final String playerRFile = "fish/player/player_r.png";

    /**
     * Constructor for the PlayerFish.
     *
     * @param maxX Maximum x coordinate (width) of the game board
     * @param maxY Maximum y coordinate (height) of the game board
     */
    public PlayerFish(int maxX, int maxY) {
        super(maxX, maxY, new Dimension2D(40, 32));
        this.MIN_SPEED = 3;
        this.MAX_SPEED = 3;
        this.setRandomSpeed();
        this.setIcon(playerLFile, playerRFile);
        setLevel(1);
    }

    public void setLevel(int level) {
        super.setLevel(level);
    }

    public void updateSize(Dimension2D newSize) {
        super.setSize(newSize);
    }


}

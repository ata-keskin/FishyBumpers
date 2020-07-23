package de.tum.in.ase.eist.car;

import de.tum.in.ase.eist.Dimension2D;

public class FastCar extends Car {

	private static final String littleYellowLFile = "fish/little/little_yellow_l.png";
	private static final String littleYellowRFile = "fish/little/little_yellow_r.png";
	private static final String littleRedLFile = "fish/little/little_red_l.png";
	private static final String littleRedRFile = "fish/little/little_red_r.png";

	/**
	 * Constructor for a FastCar
	 * 
	 * @param maxX Maximum x coordinate (width) of the game board
	 * @param maxY Maximum y coordinate (height) of the game board
	 */
	public FastCar(int maxX, int maxY) {
		super(maxX, maxY, new Dimension2D(32, 12));
		this.MIN_SPEED = 3;
		this.MAX_SPEED = 5;
		this.setRandomSpeed();
		if(Math.random() < 0.5) {
			this.setIcon(littleRedLFile, littleRedRFile);
		} else {
			this.setIcon(littleYellowLFile, littleYellowRFile);
		}
		setLevel(0);
	}
}

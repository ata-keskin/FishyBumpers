package de.tum.in.ase.eist.car;

import de.tum.in.ase.eist.Dimension2D;

public class SlowCar extends Car {

	private static final String mediumGreenLFile = "fish/angler/angler_pink_l";
	private static final String mediumGreenRFile = "fish/angler/angler_pink_r";
	private static final String mediumOrangeLFile = "fish/angler/angler_orange_l";
	private static final String mediumOrangeRFile = "fish/angler/angler_orange_r";
	private static final String extension = ".png";

	/**
	 * Constructor for a SlowCar.
	 * 
	 * @param maxX Maximum x coordinate (width) of the game board
	 * @param maxY Maximum y coordinate (height) of the game board
	 */
	public SlowCar(int maxX, int maxY) {
		super(maxX, maxY, new Dimension2D(59, 53), 4, 15);
		this.MIN_SPEED = 1;
		this.MAX_SPEED = 3;
		this.setRandomSpeed();
		if(Math.random() < 0.5) {
			this.setIcon(mediumGreenLFile, mediumGreenRFile);
		} else {
			this.setIcon(mediumOrangeLFile, mediumOrangeRFile);
		}
		setExtension(extension);
		setLevel(2);
	}
}

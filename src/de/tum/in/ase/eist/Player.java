package de.tum.in.ase.eist;


import de.tum.in.ase.eist.car.Car;
import de.tum.in.ase.eist.fish.PlayerFish;

/**
 * This class defines the player. Each player has its own fish.
 *
 */
public class Player {

	private Car playerFish;
	private int fishEaten;

	/**
	 * Constructor that allocates a fish to the player
	 * 
	 * @param fish the fish that should be the player's fish
	 */
	public Player(Car fish) {
		this.playerFish = fish;
	}

	/**
	 * @param fish the player's new fish
	 */
	public void setFish(Car fish) {
		this.playerFish = fish;
	}

	/**
	 * @return The player's current fish
	 */
	public Car getFish() {
		return this.playerFish;
	}

	public int getFishEaten() {
		return fishEaten;
	}

	public void incrementFishEaten() {
		fishEaten++;
	}
}

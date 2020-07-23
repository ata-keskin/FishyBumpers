package de.tum.in.ase.eist.car;

import de.tum.in.ase.eist.Dimension2D;
import de.tum.in.ase.eist.Point2D;

/**
 * Abstract class for cars. Objects for this class cannot be instantiated.
 */
public abstract class Car {

	public int MAX_SPEED = 10;
	public int MIN_SPEED = 2;
	protected int speed = this.MIN_SPEED;

	private String leftIconLocation;
	private String rightIconLocation;
	private String upIconLocation;
	private String downIconLocation;

	private boolean animated;
	private int frameCount;
	private int frameDelay;
	private int currentFrame;
	private String extension;

	private int level = 0;

	protected Point2D position;
	private Dimension2D size;
	
	// the direction is seen as degree within a circle
	private int direction = 90;
	private boolean isCrunched = false;

	/**
	 * DEFAULT CONSTRUCTOR THAT SHOULD NOT BE USED!!! ONLY LEFT HERE TO ALLOW FOR THE TESTS TO PASS IN CASE THEY NEED THIS GUY!
	 *
	 * @param maxX Maximum x coordinate (width) of the game board
	 * @param maxY Maximum y coordinate (height) of the game board
	 */
	public Car(int maxX, int maxY) {
		this.size = new Dimension2D(1, 1);
		int fishX = (int) (Math.random() * (maxX - getSize().getWidth()));
		int fishY = (int) (Math.random() * (maxY - getSize().getHeight()));
		this.position = new Point2D(fishX, fishY);
		if (fishY < getSize().getHeight()) {
			this.position = new Point2D(fishX, getSize().getHeight());
		}
		setDirection((int) (Math.random() * 360));
		setRandomSpeed();
		this.isCrunched = false;
	}

	/**
	 * Constructor, taking the maximum coordinates of the game board. Each car
	 * gets a random X and Y coordinate, a random direction and a random speed
	 * 
	 * The position of the car cannot be larger then the parameters, i.e. the
	 * dimensions of the game board
	 * 
	 * @param maxX Maximum x coordinate (width) of the game board
	 * @param maxY Maximum y coordinate (height) of the game board
	 * @param size Dimensions of the fish
	 */
	public Car(int maxX, int maxY, Dimension2D size) {
		this.size = size;
		int fishX = (int) (Math.random() * (maxX - getSize().getWidth()));
		int fishY = (int) (Math.random() * (maxY - getSize().getHeight()));
		this.position = new Point2D(fishX, fishY);
		if (fishY < getSize().getHeight()) {
			this.position = new Point2D(fishX, getSize().getHeight());
		}
		setDirection((int) (Math.random() * 360));
		setRandomSpeed();
		this.isCrunched = false;
	}

	/**
	 *
	 * @param maxX Maximum x coordinate (width) of the game board
	 * @param maxY Maximum y coordinate (height) of the game board
	 * @param size image dimensions
	 * @param frameCount animation frame count
	 * @param frameDelay animation frame delay, (time between each frame)
	 */
	protected Car(int maxX, int maxY, Dimension2D size, int frameCount, int frameDelay) {
		this(maxX, maxY, size);
		this.animated = true;
		this.frameCount = frameCount;
		this.frameDelay = frameDelay;
	}

    /**
	 * The car's position is reset to the top left corner of the game. The speed
	 * is set to 5 and the directions points to 90 degrees.
	 * 
	 * @param maxY Top left corner of the game board
	 */
	public void reset(int maxY) {
		this.position = new Point2D(0, maxY);
		setDirection(90);
		this.speed = 5;
		this.isCrunched = false;
	}

	/**
	 * Sets the speed of the fish to a random value based on its initial speed
	 */
	protected void setRandomSpeed() {
		int initialSpeed = (int) (Math.random() * this.MAX_SPEED);
		if (initialSpeed < this.MIN_SPEED) {
			initialSpeed = this.MIN_SPEED;
		}
		this.speed = initialSpeed;
	}

	/**
	 * Sets the car's direction
	 * 
	 * @param direction
	 * @throws IllegalArgumentException
	 */
	public void setDirection(int direction) throws IllegalArgumentException {
		if (direction < 0 || direction > 360) {
			throw new IllegalArgumentException("Direction must be between 0 and 360");
		}
		this.direction = direction;
	}

	public int getDirection() {
		return this.direction;
	}

	public int getSpeed() {
		return this.speed;
	}

	public boolean isAnimated() {
		return animated;
	}

	/**
	 * Increments the car's speed, won't exceed the maximum speed
	 */
	public void incrementSpeed() {
		if (this.speed < this.MAX_SPEED) {
			this.speed++;
		}
	}

	/**
	 * Used to reset normal fish
	 * @param maxX maxX value
	 * @param maxY maxY value
	 */
	public void reset(int maxX, int maxY) {
		int fishX = (int) (Math.random() * (maxX - getSize().getWidth()));
		int fishY = (int) (Math.random() * (maxY - getSize().getHeight()));
		this.position = new Point2D(fishX, fishY);
		if (fishY < getSize().getHeight()) {
			this.position = new Point2D(fishX, getSize().getHeight());
		}
		setDirection((int) (Math.random() * 360));
		setRandomSpeed();
	}

	/**
	 * Decrements the car's speed, won't fall below the minimum speed
	 */
	public void decrementSpeed() {
		if (this.speed > this.MIN_SPEED) {
			this.speed--;
		}
	}

	public int getLevel() {
		return level;
	}

	protected void setLevel(int level) {
		this.level = level;
	}

    public String[] getIconLocation() {
		if(this.upIconLocation == null)
        	return new String[]{this.leftIconLocation, this.rightIconLocation};
		return new String[]{this.leftIconLocation, this.rightIconLocation, this.upIconLocation, this.downIconLocation};
    }

	/**
	 * Sets the image of the fish
	 *
	 * @param iconLocation path of the image file
	 * @throws IllegalArgumentException
	 */
	protected void setIcon(String iconLocation) throws IllegalArgumentException {
		if (iconLocation == null) {
			throw new IllegalArgumentException("The image of a fish cannot be null.");
		}
		this.leftIconLocation = iconLocation;
		this.rightIconLocation = iconLocation;
	}

    /**
     * Sets the image of the fish
     *
     * @param leftIconLocation,rightIconLocation path of the image file
     * @throws IllegalArgumentException
     */
    protected void setIcon(String leftIconLocation, String rightIconLocation) throws IllegalArgumentException {
        if (rightIconLocation == null || leftIconLocation == null) {
            throw new IllegalArgumentException("The image of a fish cannot be null.");
        }
		this.leftIconLocation = leftIconLocation;
        this.rightIconLocation = rightIconLocation;
    }

	/**
	 * Sets the image of the fish
	 *
	 * @param leftIconLocation,rightIconLocation path of the image file
	 * @throws IllegalArgumentException
	 */
	protected void setIcon(String leftIconLocation, String rightIconLocation, String upIconLocation, String downIconLocation) throws IllegalArgumentException {
		if (rightIconLocation == null || leftIconLocation == null || upIconLocation == null || downIconLocation == null) {
			throw new IllegalArgumentException("The image of a fish cannot be null.");
		}
		this.leftIconLocation = leftIconLocation;
		this.rightIconLocation = rightIconLocation;
		this.upIconLocation = upIconLocation;
		this.downIconLocation = downIconLocation;
	}

    protected void setExtension(String extension) {
    	if(!isAnimated()) throw new UnsupportedOperationException("Illegal use of setExtension! Car must be animated!");
    	this.extension = extension;
	}

	public String getExtension() {
		if(!isAnimated()) throw new UnsupportedOperationException("Illegal use of getExtension! Car must be animated!");
		return extension;
	}

	public int getFrameCount() {
		if(!isAnimated()) throw new UnsupportedOperationException("Illegal use of getFrameCount! Car must be animated!");
		return frameCount;
	}

	public int getFrameDelay() {
		if(!isAnimated()) throw new UnsupportedOperationException("Illegal use of getFrameDelay! Car must be animated!");
		return frameDelay;
	}

	public void incrementCurrentFrame() {
		if(!isAnimated()) throw new UnsupportedOperationException("Illegal use of incrementCurrentFrame! Car must be animated!");
		currentFrame = (currentFrame + 1) % frameCount;
	}

	public int getCurrentFrame() {
		if(!isAnimated()) throw new UnsupportedOperationException("Illegal use of getCurrentFrame! Car must be animated!");
		return currentFrame;
	}

	public Point2D getPosition() {
		return this.position;
	}

	/**
	 * Sets the car's position
	 * 
	 * @param x the position along the x-axes
	 * @param y the position along the y-axes
	 */
	public void setPosition(int x, int y) {
		this.position = new Point2D(x, y);
	}

	public Dimension2D getSize() {
		return this.size;
	}

	protected void setSize(Dimension2D size) {
		this.size = size;
	}

	public void setCrunched() {
		this.isCrunched = true;
		this.speed = 0;
	}

	public void setCrunched(boolean value) {
		if(value) setCrunched();
	}

	public boolean isCrunched() {
		return this.isCrunched;
	}

	/**
	 * Calculates the new X and Y coordinations based on the current position,
	 * direction and speed
	 * 
	 * @param maxX the current position along the x-axes
	 * @param maxY the current position along the y-axes
	 */
	public void updatePosition(int maxX, int maxY) {
		if (this.isCrunched)
			return;
		// calculate delta between old coordinates and new ones based on speed and direction
		float delta_x = (int) Math.round(this.speed * Math.sin(Math.toRadians(this.direction)));
		float delta_y = (int) Math.round(this.speed * Math.cos(Math.toRadians(this.direction)));

		// set coordinates
		this.position = new Point2D(this.position.getX() + delta_x, this.position.getY() + delta_y);


		// calculate position in case the border of the game board has been reached
		if (this.position.getX() < 1) {
			this.position = new Point2D(2, this.position.getY());
			this.direction = 360 - this.direction;
		}

		if (this.position.getX() + this.size.getWidth() > maxX - 1) {
			this.position = new Point2D(maxX - this.size.getWidth() - 2, this.position.getY());
			this.direction = 360 - this.direction;
		}
		if (this.position.getY() - this.size.getHeight() < 1) {
			this.position = new Point2D(this.position.getX(), this.size.getHeight() + 2);
			this.direction = 180 - this.direction;
			if (this.direction < 0) {
				this.direction = 360 + this.direction;
			}
		}
		if (this.position.getY() > maxY - 1) {
			this.position = new Point2D(this.position.getX(), maxY - 2);
			this.direction = 180 - this.direction;
			if (this.direction < 0) {
				this.direction = 360 + this.direction;
			}
		}
	}

}

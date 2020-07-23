package de.tum.in.ase.eist;

import java.util.ArrayList;
import java.util.List;

import de.tum.in.ase.eist.audio.AudioPlayerInterface;
import de.tum.in.ase.eist.car.*;
import de.tum.in.ase.eist.collision.Collision;
import de.tum.in.ase.eist.collision.DevouringCollision;
import de.tum.in.ase.eist.fish.PlayerFish;
import de.tum.in.ase.eist.fish.Shark;

/**
 * Creates all car objects, detects collisions, updates car positions, notifies
 * player about victory or defeat
 */
public class GameBoard {

    private static int sharkCooldown = 25 * 15;
    private static int gameDifficulty = 1;
    private static boolean gameWonOnce;
    private boolean nightmareSlain;

    private boolean sharkAttacking;

    // list of all active fish, does not contain player fish
    private List<Car> cars;
    private List<Particle> particles;
    private Shark shark;

    // the player object with player car object
    private Player player;

    private AudioPlayerInterface audioPlayer;

    private Dimension2D size;

    // list of all loser fish (needed for testing, DO NOT DELETE THIS)
    private List<Car> loserCars = new ArrayList<>();

    // true if game is running, false if game is stopped
    private boolean isRunning;

    private Boolean gameWon;

    //constants
    public static int NUMBER_OF_LITTLE_FISH;
    public static int NUMBER_OF_MEDIUM_FISH;
    public static int NUMBER_OF_LARGE_FISH;

    public static int MAX_NUMBER_OF_LITTLE_FISH = 20;
    public static int MAX_NUMBER_OF_MEDIUM_FISH = 2000;
    public static int MAX_NUMBER_OF_LARGE_FISH = 20;

    public static int LEVEL_ONE = 12;
    public static int LEVEL_TWO = 24;

    /**
     * Constructor, creates the gameboard based on size
     *
     * @param size of the gameboard
     */
    public GameBoard(Dimension2D size) {
        PlayerFish playerFish = new PlayerFish(1000 - 40, 600 - 32); //250 ,30
        particles = new ArrayList<>();
        cars = new ArrayList<>();
        loserCars = new ArrayList<>();
        shark = new Shark(size.getWidth(), size.getHeight());
        this.player = new Player(playerFish);
        this.size = size;
        this.addCar();
        this.updateFishStates();
        sharkAttacking = false;
    }

    /**
     * Adds specified number of fish to the fish list, creates new object for each car
     */
    public void addCar() {
        for (int i = 0; i < MAX_NUMBER_OF_LITTLE_FISH; i++) {
            this.cars.add(new FastCar(this.size.getWidth(), this.size.getHeight()));
        }
        for (int i = 0; i < MAX_NUMBER_OF_MEDIUM_FISH; i++) {
            this.cars.add(new MediumCar(this.size.getWidth(), this.size.getHeight()));
        }
        for (int i = 0; i < MAX_NUMBER_OF_LARGE_FISH; i++) {
            this.cars.add(new SlowCar(this.size.getWidth(), this.size.getHeight()));
        }
    }

    public static void setGameDifficulty(int gameDifficulty) {
        GameBoard.gameDifficulty = gameDifficulty;
    }

    public void updateFishStates() {
        //total number of alive fish = 36 (this seems to give the best visual experience)
        //reuses fishes instead of creating new ones for better performance

        GameBoard.LEVEL_TWO = 24;
        switch(gameDifficulty) {

            case 0: //20, 8, 4
                NUMBER_OF_LITTLE_FISH = 20;
                NUMBER_OF_MEDIUM_FISH = 8;
                NUMBER_OF_LARGE_FISH = 4;
                break;
            case 1: //16, 12, 8
                NUMBER_OF_LITTLE_FISH = 16;
                NUMBER_OF_MEDIUM_FISH = 1200;
                NUMBER_OF_LARGE_FISH = 8;
                break;
            case 2: //12, 14, 10
                NUMBER_OF_LITTLE_FISH = 12;
                NUMBER_OF_MEDIUM_FISH = 14;
                NUMBER_OF_LARGE_FISH = 10;
                break;
            case 3:
                GameBoard.LEVEL_TWO = 32;
                NUMBER_OF_LITTLE_FISH = 12;
                NUMBER_OF_MEDIUM_FISH = 20;
                NUMBER_OF_LARGE_FISH = 18;
                break;
        }
        for(Car fish : cars) {
            if(fish instanceof FastCar) {
                if(NUMBER_OF_LITTLE_FISH > 0) {
                    fish.setCrunched(false);
                    NUMBER_OF_LITTLE_FISH--;
                }
                else fish.setCrunched(true);
            } else if (fish instanceof MediumCar) {
                if(NUMBER_OF_MEDIUM_FISH > 0) {
                    fish.setCrunched(false);
                    NUMBER_OF_MEDIUM_FISH--;
                }
                else fish.setCrunched(true);
            } else {
                if(NUMBER_OF_LARGE_FISH > 0) {
                    fish.setCrunched(false);
                    NUMBER_OF_LARGE_FISH--;
                }
                else fish.setCrunched(true);
            }
            fish.reset(size.getWidth(), size.getHeight());
        }
    }

    private void createParticle(Point2D position, int speed) {
        Particle particle = new Particle(new Point2D(position.getX(), size.getHeight() - position.getY()), speed, 25);
        particles.add(particle);
    }

    private void updateParticles() {
        for (Particle particle : particles) {
            if (particle.getY() > size.getHeight()) particle.setAlive(false);
        }
        particles.removeIf(particle -> !particle.isAlive());
    }

    public List<Particle> getParticles() {
        return particles;
    }

    public void initiateSharkAttack() {
        System.out.println("Shark Attack Initiated");
        if (shark == null)
            shark = new Shark(size.getWidth(), size.getHeight());
        else
            shark.reset(size.getWidth(), size.getHeight());
        getAudioPlayer().fadeOutToSharkMusic();
        sharkAttacking = true;
    }

    public void endSharkAttack() {
        System.out.println("Shark Attack Finished");
        stopMusic();
        playMusic();
        sharkAttacking = false;
    }

    public boolean isSharkAttacking() {
        return sharkAttacking;
    }

    public Shark getShark() {
        if (shark == null)
            shark = new Shark(size.getWidth(), size.getHeight());
        return shark;
    }

    public void resetSharkTimer() {
        sharkCooldown = 25 * 15;
        sharkAttacking = false;
    }

    public void updateSharkTimer() {
        if (!sharkAttacking) {
            if (sharkCooldown == 0) {
                initiateSharkAttack();
                sharkCooldown = 25 * 15; //end after 15 seconds
            } else if (sharkCooldown > 0) sharkCooldown--;
        } else {
            if (sharkCooldown == 0) {
                endSharkAttack();
                sharkCooldown = 25 * 20; //25sec cooldown
            } else if (sharkCooldown > 0) sharkCooldown--;
        }
    }

    /**
     * Removes all existing fish from the car list, resets the position of the
     * player car Invokes the creation of new car objects by calling addCars()
     */
    public void resetCars() {
        this.player.getFish().reset(this.size.getHeight());
        //this.cars.clear();
        //addCar();
        updateFishStates();
    }

    /**
     * Checks if game is currently running by checking if the thread is running
     *
     * @return boolean isRunning
     */
    public boolean isRunning() {
        return this.isRunning;
    }

    /**
     * Used for testing only
     */
    public void setRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }

    /**
     * @return null if the game is running; true if the player has won; false if the player has lost
     */
    public Boolean hasWon() {
        return this.gameWon;
    }

    /**
     * @return list of fish
     */
    public List<Car> getCars() {
        return this.cars;
    }

    /**
     * @return the player's car
     */
    public Car getPlayerCar() {
        return this.player.getFish();
    }

    /**
     * @return the gameboard's instance of AudioPlayer
     */
    public AudioPlayerInterface getAudioPlayer() {
        return this.audioPlayer;
    }

    public void setAudioPlayer(AudioPlayerInterface audioPlayer) {
        this.audioPlayer = audioPlayer;
    }

    /**
     * Updates the position of each car
     */
    public void update() {
        moveCars();
        updateSharkTimer();
        updateParticles();
    }

    /**
     * Starts the game. Cars start to move and background music starts to play.
     */
    public void startGame() {
        playMusic();
        this.isRunning = true;
    }

    /**
     * Stops the game. Cars stop moving and background music stops playing.
     */
    public void stopGame() {
        stopMusic();
        resetSharkTimer();
        this.isRunning = false;
    }

    /**
     * Starts the background music
     */
    public void playMusic() {
        if(audioPlayer!=null)
        this.audioPlayer.playBackgroundMusic();
    }

    /**
     * Stops the background music
     */
    public void stopMusic() {
        if(audioPlayer!=null)
        this.audioPlayer.stopBackgroundMusic();
    }

    /**
     * @return list of loser fish
     */
    public List<Car> getLoserCars() {
        return this.loserCars;
    }

    /**
     * Iterate through list of fish (without the player fish) and update each fish's position
     * Update player car afterwards separately
     */
    public void moveCars() {

        List<Car> cars = getCars();

        // maximum x and y values a fish can have depending on the size of the game board
        int maxX = (int) size.getWidth();
        int maxY = (int) size.getHeight();

        // update the positions of the player fish and the autonomous fish
        for (Car car : cars) {
            car.updatePosition(maxX, maxY);
        }

        player.getFish().updatePosition(maxX, maxY);

        if(shark!=null)
        if (sharkAttacking) {
            shark.updatePosition(maxX, maxY);
            Collision devouringCollision = new DevouringCollision(player.getFish(), shark, size.getHeight());
            if (devouringCollision.isCollision) {
                audioPlayer.playCrashSound();
                getPlayerCar().setCrunched();
                gameWon = false;
                return;
            }
        }

        // iterate through all fish (except player car) and check if it is crunched
        for (Car car : cars) {
            if (car.isCrunched()) {
                continue; // because there is no need to check for a collision
            }


            Collision devouringCollision = new DevouringCollision(player.getFish(), car, size.getHeight());

            if (devouringCollision.isCollision) {
                Car loser = devouringCollision.evaluateLoser();

                if(audioPlayer!=null)
                audioPlayer.playCrashSound();

                loserCars.add(loser);
                loser.setCrunched();

                if (isWinner()) {
                    gameWon = true;
                    if(gameDifficulty == 2)
                        gameWonOnce = true;
                    if(gameDifficulty == 3)
                        nightmareSlain = true;
                }
                else if (loser == getPlayerCar()) {
                    gameWon = false;
                    return;
                }

                player.incrementFishEaten();
                createParticle(getPlayerCar().getPosition(), -2);

                //set player level and size
                if (player.getFishEaten() == LEVEL_TWO) {
                    getAudioPlayer().playLevelUpSound();
                    ((PlayerFish) getPlayerCar()).setLevel(3);
                    ((PlayerFish) getPlayerCar()).updateSize(new Dimension2D(70, 56));
                } else if (player.getFishEaten() == LEVEL_ONE) {
                    getAudioPlayer().playLevelUpSound();
                    ((PlayerFish) getPlayerCar()).setLevel(2);
                    ((PlayerFish) getPlayerCar()).updateSize(new Dimension2D(50, 40));
                }

            }
        }
    }

    /**
     * If all fish are crunched, the player wins.
     *
     * @return false if game is not (yet) won
     */
    private boolean isWinner() {
        for (Car car : getCars()) {
            if (!car.isCrunched()) {
                return false;
            }
        }
        return true;
    }

    public static boolean isGameWonOnce() {
        return gameWonOnce;
    }

    public boolean isNightmareSlain() {
        return nightmareSlain;
    }
}

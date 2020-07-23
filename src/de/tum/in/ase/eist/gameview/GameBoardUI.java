package de.tum.in.ase.eist.gameview;


import de.tum.in.ase.eist.*;
import de.tum.in.ase.eist.audio.AudioPlayer;
import de.tum.in.ase.eist.car.Car;
import de.tum.in.ase.eist.usercontrol.MouseSteering;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;

/**
 * This class implements the user interface for steering the player fish. The
 * user interface is implemented as a Thread that is started by clicking the
 * start button on the tool bar and stops by the stop button.
 */
public class GameBoardUI extends Canvas implements Runnable {

    //private static final Color backgroundColor = Color.WHITE;
    private final Image backgroundImage;
    private final Image deathIcon;
    private final Image victoryIcon;

    private AnimatedDecor corals;
    private static int clock = 0;

    private static final int SLEEP_TIME = 1000 / 25; // this gives us 25fps
    private static final Dimension2D DEFAULT_SIZE = new Dimension2D(1000, 600);
    // attribute inherited by the JavaFX Canvas class
    private GraphicsContext graphicsContext = this.getGraphicsContext2D();

    // thread responsible for starting game
    private Thread theThread;

    // user interface objects
    private GameBoard gameBoard;
    private Dimension2D size;
    private Toolbar toolBar;

    // user control objects
    private MouseSteering mouseSteering;

    private HashMap<Car, Image[]> carImages;
    private HashMap<Car, Image[][]> fishAnimations;

    /**
     * Sets up all attributes, starts the mouse steering and sets up all graphics
     *
     * @param toolBar used to start and stop the game
     */
    public GameBoardUI(Toolbar toolBar) {
        backgroundImage = getImage("other/ocean_bg.png");
        deathIcon = getImage("other/death_icon.png");
        victoryIcon = getImage("other/victory_icon.png");

        this.toolBar = toolBar;
        this.size = getPreferredSize();
        gameSetup();
    }

    /**
     * Called after starting the game thread
     * Constantly updates the game board and renders graphics
     *
     * @see Runnable#run()
     */
    @Override
    public void run() {
        while (this.gameBoard.isRunning()) {
            // updates fish positions and re-renders graphics
            this.gameBoard.update();
            // when this.gameBoard.hasWon() is null, do nothing
            if (this.gameBoard.hasWon() == Boolean.FALSE) {
                showAsyncAlert("You got chomped!", deathIcon);
                this.stopGame();
            } else if (this.gameBoard.hasWon() == Boolean.TRUE) {
                showAsyncAlert(gameBoard.isNightmareSlain() ? "Congratulations! You have defeated nightmare mode!" : "Congratulations! You won!", victoryIcon);
                this.stopGame();
            }
            paint(this.graphicsContext);
            try {
                Thread.sleep(SLEEP_TIME); // milliseconds to sleep
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            clock++;
        }
    }

    /**
     * @return current gameBoard
     */
    public GameBoard getGameBoard() {
        return this.gameBoard;
    }

    /**
     * @return mouse steering control object
     */
    public MouseSteering getMouseSteering() {
        return this.mouseSteering;
    }

    /**
     * @return preferred gameBoard size
     */
    public static Dimension2D getPreferredSize() {
        return DEFAULT_SIZE;
    }

    /**
     * Removes all existing fishes from the game board and re-adds them. Status bar is
     * set to default value. Player fish is reset to default starting position.
     * Renders graphics.
     */
    public void gameSetup() {
        this.gameBoard = new GameBoard(this.size);
        this.gameBoard.setAudioPlayer(new AudioPlayer());
        this.widthProperty().set(this.size.getWidth());
        this.heightProperty().set(this.size.getHeight());

        this.size = new Dimension2D(getWidth(), getHeight());
        this.carImages = new HashMap<>();
        this.fishAnimations = new HashMap<>();
        this.mouseSteering = new MouseSteering(this, this.gameBoard.getPlayerCar());

        this.gameBoard.resetCars();
        this.gameBoard.getCars().forEach((fish -> {
            if (fish.isAnimated())
                this.fishAnimations.put(fish, getFrames(fish.getIconLocation(), fish.getExtension(), fish.getFrameCount()));
            else
                this.carImages.put(fish, getImage(fish.getIconLocation()));
        }));
        this.fishAnimations.put(gameBoard.getShark(), getFrames(gameBoard.getShark().getIconLocation(), gameBoard.getShark().getExtension(), gameBoard.getShark().getFrameCount()));
        this.carImages.put(this.gameBoard.getPlayerCar(), getImage(this.gameBoard.getPlayerCar().getIconLocation()));

        Particle.init(getFrames(Particle.getSpriteLocation(), Particle.getExtension(), Particle.getFrameCount()));

        this.corals = new AnimatedDecor(new Point2D(0, 480), getFrames("corals/coral", ".png", 8), 30);

        paint(this.graphicsContext);
        this.toolBar.resetToolBarButtonStatus(false);
    }

    /**
     * Acquires the image specified by the file path
     *
     * @param imageFilePath: an image file path that needs to be available in the resources folder of the project
     */
    private static Image getImage(String imageFilePath) {
        try {
            URL fishImageUrl = GameBoardUI.class.getClassLoader().getResource(imageFilePath);
            if (fishImageUrl == null) {
                throw new RuntimeException("Please ensure that your resources folder contains the appropriate files for this exercise. \n\t(Faulty URL: " + imageFilePath + ")");
            }
            InputStream inputStream = fishImageUrl.openStream();
            return new Image(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Image[] getImage(String[] imageFilePaths) {
        Image[] ret = new Image[imageFilePaths.length];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = getImage(imageFilePaths[i]);
        }
        return ret;
    }

    /**
     * @param framesFilePath frame file path root, (the frames should be in the same folder named as "*framesFilePath* + i + *extension*", where i is the frame index)
     * @param extension      valid image file extension
     * @param frameCount     frame count
     * @return an Image[] containing animation frames
     */
    private static Image[] getFrames(String framesFilePath, String extension, int frameCount) {
        Image[] frames = new Image[frameCount];
        for (int i = 0; i < frameCount; i++) {
            frames[i] = getImage(framesFilePath + i + extension);
        }
        return frames;
    }

    /**
     * 2D variant of getFrames
     *
     * @param framesFilePaths frame file path root, (the frames should be in the same folder named as "*framesFilePath* + i + *extension*", where i is the frame index)
     * @param extension       valid image file extension
     * @param frameCount      frame count
     * @return a 2D Image array containing animation frames according to paths
     */
    private static Image[][] getFrames(String[] framesFilePaths, String extension, int frameCount) {
        Image[][] arrayOfAnimations = new Image[framesFilePaths.length][frameCount];
        for (int i = 0; i < arrayOfAnimations.length; i++) {
            arrayOfAnimations[i] = getFrames(framesFilePaths[i], extension, frameCount);
        }
        return arrayOfAnimations;
    }


    /**
     * Starts the GameBoardUI Thread, if it wasn't running. Starts the game board,
     * which causes the fishes to change their positions (i.e. move). Renders graphics
     * and updates tool bar status.
     */
    public void startGame() {
        if (!this.gameBoard.isRunning()) {

            clock = 0;
            this.gameBoard.startGame();

            this.theThread = new Thread(this);
            this.theThread.start();

            paint(this.graphicsContext);
            this.toolBar.resetToolBarButtonStatus(true);

        }
    }

    /**
     * Render the graphics of the whole game by iterating through the fishs of the
     * game board at render each of them individually.
     *
     * @param graphics used to draw changes
     */
    private void paint(GraphicsContext graphics) {
        graphics.drawImage(backgroundImage, 0, 0);
        corals.draw(graphics, clock);

        for (Car car : this.gameBoard.getCars()) {
            paintCar(car, graphics);
        }


        if (gameBoard.isSharkAttacking()) {
            paintCar(gameBoard.getShark(), graphics);
        }

        paintCar(this.gameBoard.getPlayerCar(), graphics);

        for (Particle particle : this.gameBoard.getParticles()) {
            particle.draw(graphics, clock);
        }
    }

    /**
     * Show image of a car at the current position of the car.
     *
     * @param car      to be drawn
     * @param graphics used to draw changes
     */
    private void paintCar(Car car, GraphicsContext graphics) {
        if (car.isCrunched()) return;
        Point2D fishPosition = car.getPosition();
        Point2D canvasPosition = convertPosition(fishPosition); // convertPosition(

        if (car.getDirection() > 180) {
            if (car.isAnimated()) {
                graphics.drawImage(this.fishAnimations.get(car)[0][car.getCurrentFrame()], canvasPosition.getX(), canvasPosition.getY(),
                        car.getSize().getWidth(), car.getSize().getHeight());
                if (clock % car.getFrameDelay() == 0) {
                    car.incrementCurrentFrame();
                }
            } else {
                graphics.drawImage(this.carImages.get(
                        car)[0], canvasPosition.getX(), canvasPosition.getY(),
                        car.getSize().getWidth(), car.getSize().getHeight());
            }
        } else {
            if (car.isAnimated()) {
                graphics.drawImage(this.fishAnimations.get(car)[1][
                                car.getCurrentFrame()],
                        canvasPosition.getX(),
                        canvasPosition.getY(),
                        car.getSize().getWidth(), car.getSize().getHeight());
                if (clock % car.getFrameDelay() == 0) {
                    car.incrementCurrentFrame();
                }
            } else {
                graphics.drawImage(this.carImages.get(car)[1], canvasPosition.getX(), canvasPosition.getY(),
                        car.getSize().getWidth(), car.getSize().getHeight());
            }
        }
    }

    /**
     * Converts position of fish to position on the canvas
     *
     * @param toConvert the point to be converted
     */
    public Point2D convertPosition(Point2D toConvert) {
        return new Point2D(toConvert.getX(), getHeight() - toConvert.getY());
    }

    /**
     * Stops the game board and set the tool bar to default values.
     */
    public void stopGame() {
        if (this.gameBoard.isRunning()) {
            this.gameBoard.stopGame();
            this.toolBar.resetToolBarButtonStatus(false);
        }
    }

    /**
     * Method used to display alerts in moveFishes() Java 8 Lambda Functions: java
     * 8 lambda function without arguments Platform.runLater Function:
     * https://docs.oracle.com/javase/8/javafx/api/javafx/application/Platform.html
     *
     * @param message you want to display as a String
     */
    public void showAsyncAlert(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(message);
            alert.showAndWait();
            this.gameSetup();
        });
    }

    /**
     * Method used to display alerts in moveFishes() Java 8 Lambda Functions: java
     * 8 lambda function without arguments Platform.runLater Function:
     * https://docs.oracle.com/javase/8/javafx/api/javafx/application/Platform.html
     *
     * @param message you want to display as a String
     */
    public void showAsyncAlert(String message, Image icon) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setGraphic(new ImageView(icon));
            alert.setHeaderText(message);
            alert.showAndWait();
            this.gameSetup();
        });
    }
}

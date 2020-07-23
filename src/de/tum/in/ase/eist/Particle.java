package de.tum.in.ase.eist;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Particle {

    private Point2D position;
    private boolean alive;
    private int life;
    private int currentFrame;
    private int deltaY;
    private static Image[] frames;
    private static int frameCount = 2;
    private static int frameDelay = 2;
    private static String spriteLocation = "particles/particle";
    private static String extension = ".png";

    public Point2D getPosition() {
        return position;
    }

    /**
     *
     * @param position position of the bubbles
     */
    public Particle(Point2D position, int deltaY, int life) {
        this.position = position;
        this.deltaY = deltaY;
        alive = true;
        currentFrame = 0;
        this.life = life;
    }

    public static void init(Image[] frames) {
        Particle.frames = frames;
    }

    public void draw(GraphicsContext graphics, int clock) {
        graphics.drawImage(frames[currentFrame], position.getX(), position.getY());
        if(clock%frameDelay==0) {
            incrementCurrentFrame();
        }
        position = new Point2D(position.getX(), position.getY()+deltaY);
        life--;
        if(life < 0) setAlive(false);
    }

    public double getY() {
        return position.getY();
    }

    private void incrementCurrentFrame() {
        currentFrame = (currentFrame + 1) % frameCount;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public static String getExtension() {
        return extension;
    }

    public static String getSpriteLocation() {
        return spriteLocation;
    }

    public static int getFrameCount() {
        return frameCount;
    }

    public static int getFrameDelay() {
        return frameDelay;
    }
}

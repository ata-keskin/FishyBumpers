package de.tum.in.ase.eist;

import de.tum.in.ase.eist.gameview.GameBoardUI;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class AnimatedDecor {

    protected Point2D position;
    private Image[] frames;
    private int currentFrame;
    private int frameCount;
    private int frameDelay;

    public Point2D getPosition() {
        return position;
    }

    /**
     * Stationary animated decoration..
     *
     * @param position position of the decor
     * @param frameDelay Time between frames in milliseconds.
     */
    public AnimatedDecor(Point2D position, Image[] frames, int frameDelay) {
        this.position = position;
        this.frames = frames;
        this.frameCount = frames.length;
        this.frameDelay = frameDelay;
        currentFrame = 0;
    }
    /**
     *
     * @param graphics
     * @param clock applications internal frame number
     * @return
     */
    public void draw(GraphicsContext graphics, int clock) {
        graphics.drawImage(frames[currentFrame], position.getX(), position.getY());
        if(clock%frameDelay==0) {
            incrementCurrentFrame();
        }
    }

    private void incrementCurrentFrame() {
        currentFrame = (currentFrame + 1) % frameCount;
    }


}

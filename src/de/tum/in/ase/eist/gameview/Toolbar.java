package de.tum.in.ase.eist.gameview;

import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

import java.util.Optional;

import de.tum.in.ase.eist.BumpersApplication;

/**
 *
 * This class visualizes the tool bar with start, stop and exit buttons above
 * the game board.
 *
 */
public class Toolbar extends ToolBar {
    private BumpersApplication gameWindow;
    private Button start;
    private Button stop;
    private Label difficultyLabel;
    private ToggleGroup difficulty;
    private RadioButton easy;
    private RadioButton normal;
    private RadioButton hard;
    private RadioButton nightmare;

    public Toolbar(BumpersApplication gameWindow) {
        this.start = new Button("Start");
        this.stop = new Button("Stop");

        this.difficultyLabel = new Label("Difficulty:");

        this.difficulty = new ToggleGroup();

        this.easy = new RadioButton("Easy");
        this.easy.setToggleGroup(difficulty);

        this.normal = new RadioButton("Normal");
        this.normal.setToggleGroup(difficulty);
        this.normal.setSelected(true);

        this.hard = new RadioButton("Hard");
        this.hard.setToggleGroup(difficulty);

        this.nightmare = new RadioButton("Nightmare");
        this.nightmare.setToggleGroup(difficulty);
        this.nightmare.setVisible(false);

        initActions();
        this.getItems().addAll(start, new Separator(), stop, new Separator(), difficultyLabel, easy, normal, hard, nightmare);
        this.setGameWindow(gameWindow);
    }

    /**
     * Initialises the actions
     */
    private void initActions() {
        this.start.setOnAction(event -> getGameWindow().gameBoardUI.startGame());

        this.stop.setOnAction(event -> {
            Toolbar.this.getGameWindow().gameBoardUI.stopGame();

            ButtonType YES = new ButtonType("Yes", ButtonBar.ButtonData.YES);
            ButtonType NO = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

            Alert alert = new Alert(AlertType.CONFIRMATION, "Do you really want to stop the game ?", YES, NO);
            alert.setTitle("Stop Game Confirmation");
            alert.setHeaderText("");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == YES) {
                getGameWindow().gameBoardUI.gameSetup();
            } else {
                getGameWindow().gameBoardUI.startGame();
            }
        });

        this.easy.setOnAction(event -> {
            getGameWindow().gameBoardUI.getGameBoard().setGameDifficulty(0);
            getGameWindow().gameBoardUI.gameSetup();
        });

        this.normal.setOnAction(event -> {
            getGameWindow().gameBoardUI.getGameBoard().setGameDifficulty(1);
            getGameWindow().gameBoardUI.gameSetup();
        });

        this.hard.setOnAction(event -> {
            getGameWindow().gameBoardUI.getGameBoard().setGameDifficulty(2);
            getGameWindow().gameBoardUI.gameSetup();
        });

        this.nightmare.setOnAction(event -> {
            getGameWindow().gameBoardUI.getGameBoard().setGameDifficulty(3);
            getGameWindow().gameBoardUI.gameSetup();
        });
    }

    /**
     * Resets the toolbar button status
     * @param running Used to disable/enable buttons
     */
    public void resetToolBarButtonStatus(boolean running) {
        this.start.setDisable(running);
        this.stop.setDisable(!running);
        this.easy.setDisable(running);
        this.normal.setDisable(running);
        this.hard.setDisable(running);
        this.nightmare.setDisable(running);
        if(getGameWindow().gameBoardUI != null)
        this.nightmare.setVisible(getGameWindow().gameBoardUI.getGameBoard().isGameWonOnce());
    }

    /**
     * @return current gameWindow
     */
    public BumpersApplication getGameWindow() {
        return this.gameWindow;
    }

    /**
     * @param gameWindow New gameWindow to be set
     */
    public void setGameWindow(BumpersApplication gameWindow) {
        this.gameWindow = gameWindow;
    }
}
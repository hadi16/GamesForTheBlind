package gamesforblind.sudoku.gui;

import gamesforblind.enums.SudokuType;
import gamesforblind.sudoku.SudokuGame;
import gamesforblind.sudoku.SudokuState;
import gamesforblind.sudoku.gui.listener.SudokuKeyboardListener;
import gamesforblind.sudoku.gui.listener.SudokuMouseListener;
import gamesforblind.sudoku.gui.listener.SudokuWindowListener;

import javax.swing.*;
import java.awt.*;

import static gamesforblind.Constants.FRAME_DIMENSION;

/**
 * Class that creates the Sudoku GUI, adds the listeners to that GUI, and updates the Sudoku state of the GUI.
 */
public class SudokuFrame extends JFrame {
    private final JFrame frame;

    /**
     * A reference to the panel that contains most of the Sudoku GUI code.
     */
    private final SudokuPanel sudokuPanel;

    /**
     * A reference to the Game that contains current SudokuGame.
     */
    private final SudokuGame sudokuGame;

    /**
     * Creates a new SudokuFrame.
     *
     * @param sudokuGame   The current Sudoku game.
     * @param initialState The initial state of the game, which is used to initialize the SudokuPanel.
     * @param sudokuType   Whether the Sudoku board is a 4x4, 6x6, or 9x9.
     * @param playbackMode true if loading saved game from logs (otherwise, false).
     */
    public SudokuFrame(SudokuGame sudokuGame, SudokuState initialState, SudokuType sudokuType, boolean playbackMode) {
        this.sudokuPanel = new SudokuPanel(initialState);
        this.frame = new JFrame("Sudoku");

        this.sudokuGame = sudokuGame;

        this.initializeGui(sudokuGame);



        // If we are in playback mode, I don't want any new mouse clicks or keyboard presses to be registered.
        if (!playbackMode) {
            this.frame.addMouseListener(new SudokuMouseListener(sudokuGame, this, sudokuType));
            this.frame.addKeyListener(new SudokuKeyboardListener(initialState.getSudokuKeyboardInterface(), sudokuGame));
            this.frame.addWindowListener(new SudokuWindowListener(sudokuGame));
        }
    }

    /**
     * Used for creating the on-screen GUI.
     * This will start at a dimension of 500 by 500 pixels, but is fully resizable by dragging the window.
     */
    private void initializeGui(SudokuGame sudokuGame) {
        this.frame.add(this.sudokuPanel);

        this.frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.frame.setSize(FRAME_DIMENSION, FRAME_DIMENSION);
        this.frame.setVisible(true);

        JMenuBar menuBar = new JMenuBar();

        SudokuMenu menu = new SudokuMenu();

        // add menu to menu bar
        menuBar.add(menu.initialize(sudokuGame));


        // add menu bar to frame
        this.frame.setJMenuBar(menuBar);


    }


    /**
     * Sets the Sudoku state for the enclosed {@link SudokuPanel} & calls repaint() on it.
     *
     * @param sudokuState The {@link SudokuState} to set.
     */
    public void receiveSudokuState(SudokuState sudokuState) {
        this.sudokuPanel.setSudokuState(sudokuState);
        this.sudokuPanel.repaint();
    }

    /**
     * Getter for the enclosed {@link JFrame}'s bounds. Used to determine proper sizing of elements on the GUI.
     *
     * @return A {@link Rectangle} containing the {@link JFrame} bounds.
     */
    public Rectangle getFrameBounds() {
        return new Rectangle(this.frame.getBounds());
    }

    /**
     * Closes the previous game's {@link JFrame}
     */
    public void closeFrames() {

        this.frame.setVisible(false);
        this.frame.dispose();
    }

}

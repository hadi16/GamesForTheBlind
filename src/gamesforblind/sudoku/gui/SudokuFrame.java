package gamesforblind.sudoku.gui;

import gamesforblind.logger.LogCreator;
import gamesforblind.sudoku.SudokuGame;
import gamesforblind.sudoku.SudokuState;
import gamesforblind.sudoku.gui.listener.SudokuKeyboardListener;
import gamesforblind.sudoku.gui.listener.SudokuMouseListener;
import gamesforblind.sudoku.gui.listener.SudokuWindowListener;

import javax.swing.*;
import java.awt.*;

import static gamesforblind.Constants.FRAME_DIMENSION;

public class SudokuFrame extends JFrame {
    private final JFrame frame;
    private final SudokuPanel sudokuPanel;

    /**
     * Used for creating the GUI and activating a mouse listener to detect click locations
     *
     * @param sudokuGame
     * @param initialState
     * @param sudokuBoardSize
     */
    public SudokuFrame(SudokuGame sudokuGame, SudokuState initialState, int sudokuBoardSize, LogCreator logCreator) {
        this.sudokuPanel = new SudokuPanel(initialState);
        this.frame = new JFrame("Sudoku");

        this.initializeGui(
                new SudokuKeyboardListener(sudokuGame, sudokuBoardSize),
                new SudokuMouseListener(sudokuGame, this, sudokuBoardSize),
                new SudokuWindowListener(logCreator)
        );
    }

    /**
     * Used for creating the on screen GUI, this will start at a dimension of 500 by 500 pixels but
     * is fully changeable by dragging the screen dimensions
     *
     * @param keyboardListener
     * @param mouseListener
     */
    private void initializeGui(
            SudokuKeyboardListener keyboardListener,
            SudokuMouseListener mouseListener,
            SudokuWindowListener windowListener
    ) {
        this.frame.add(this.sudokuPanel);

        this.frame.addMouseListener(mouseListener);
        this.frame.addKeyListener(keyboardListener);
        this.frame.addWindowListener(windowListener);

        this.frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.frame.setSize(FRAME_DIMENSION, FRAME_DIMENSION);
        this.frame.setVisible(true);
    }

    public void receiveSudokuState(SudokuState state) {
        this.sudokuPanel.setSudokuState(state);
    }

    /**
     * Used for constantly updating the panel size and location as well as for inputs
     */
    public void repaintSudokuPanel() {
        this.sudokuPanel.repaint();
    }

    public Rectangle getFrameBounds() {
        return new Rectangle(this.frame.getBounds());
    }
}

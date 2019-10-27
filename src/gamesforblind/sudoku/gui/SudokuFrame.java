package gamesforblind.sudoku.gui;

import gamesforblind.sudoku.SudokuGame;
import gamesforblind.sudoku.SudokuState;
import gamesforblind.sudoku.gui.listener.SudokuKeyboardListener;
import gamesforblind.sudoku.gui.listener.SudokuMouseListener;

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
    public SudokuFrame(SudokuGame sudokuGame, SudokuState initialState, int sudokuBoardSize) {
        this.sudokuPanel = new SudokuPanel(initialState);
        this.frame = new JFrame("Sudoku");

        this.initializeGui(
                new SudokuKeyboardListener(sudokuGame, sudokuBoardSize),
                new SudokuMouseListener(sudokuGame, this, sudokuBoardSize)
        );
    }

    /**
     * Used for creating the on screen GUI, this will start at a dimension of 500 by 500 pixels but
     * is fully changeable by dragging the screen dimensions
     *
     * @param sudokuKeyboardListener
     * @param sudokuMouseListener
     */
    private void initializeGui(SudokuKeyboardListener sudokuKeyboardListener, SudokuMouseListener sudokuMouseListener) {
        this.frame.add(this.sudokuPanel);

        this.frame.addMouseListener(sudokuMouseListener);
        this.frame.addKeyListener(sudokuKeyboardListener);

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

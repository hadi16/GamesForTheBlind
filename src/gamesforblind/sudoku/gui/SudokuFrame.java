package gamesforblind.sudoku.gui;

import gamesforblind.ProgramArgs;
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
    public SudokuFrame(SudokuGame sudokuGame, SudokuState initialState, int sudokuBoardSize, ProgramArgs programArgs) {
        this.sudokuPanel = new SudokuPanel(initialState);
        this.frame = new JFrame("Sudoku");

        this.initializeGui();

        if (!programArgs.isPlaybackMode()) {
            this.frame.addMouseListener(new SudokuMouseListener(sudokuGame, this, sudokuBoardSize));
            this.frame.addKeyListener(new SudokuKeyboardListener(sudokuGame, sudokuBoardSize));
            this.frame.addWindowListener(new SudokuWindowListener(sudokuGame));
        }
    }

    /**
     * Used for creating the on screen GUI, this will start at a dimension of 500 by 500 pixels but
     * is fully changeable by dragging the screen dimensions
     */
    private void initializeGui() {
        this.frame.add(this.sudokuPanel);

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

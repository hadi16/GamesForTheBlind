package sudoku.gui;

import sudoku.SudokuGame;
import sudoku.SudokuState;
import sudoku.gui.listener.SudokuKeyboardListener;
import sudoku.gui.listener.SudokuMouseListener;
import synthesizer.AudioPlayer;

import javax.swing.*;
import java.awt.*;

public class SudokuFrame extends JFrame {
    private final JFrame frame;
    private final SudokuPanel sudokuPanel;

    /**
     * Used for creating the GUI and activating a mouse listener to detect click locations
     *
     * @param sudokuGame
     * @param initialState
     * @param sudokuBoardSize
     * @param audioPlayer
     */
    public SudokuFrame(SudokuGame sudokuGame, SudokuState initialState, int sudokuBoardSize, AudioPlayer audioPlayer) {
        this.sudokuPanel = new SudokuPanel(initialState);
        this.frame = new JFrame("Sudoku");

        this.initializeGui(
                new SudokuKeyboardListener(sudokuGame, sudokuBoardSize, audioPlayer),
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
        final int FRAME_DIMENSION = 500;

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
     * Used for constanly updating the panel size and location as well as for inputs
     */
    public void repaintSudokuPanel() {
        this.sudokuPanel.repaint();
    }

    public Rectangle getFrameBounds() {
        return new Rectangle(this.frame.getBounds());
    }
}

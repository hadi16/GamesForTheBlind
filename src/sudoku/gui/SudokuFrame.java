package sudoku.gui;

import sudoku.SudokuGame;
import sudoku.SudokuState;
import sudoku.gui.listener.SudokuKeyboardListener;
import sudoku.gui.listener.SudokuMouseListener;

import javax.swing.*;
import java.awt.*;

public class SudokuFrame extends JFrame {
    private final JFrame frame;
    private final SudokuPanel sudokuPanel;

    public SudokuFrame(SudokuGame sudokuGame, SudokuState initialState, int sudokuBoardSize) {
        this.sudokuPanel = new SudokuPanel(initialState);
        this.frame = new JFrame("Sudoku");

        this.initializeGui(
                new SudokuKeyboardListener(sudokuGame, sudokuBoardSize),
                new SudokuMouseListener(sudokuGame, this, sudokuBoardSize)
        );
    }

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

    public void repaintSudokuPanel() {
        this.sudokuPanel.repaint();
    }

    public Rectangle getFrameBounds() {
        return new Rectangle(this.frame.getBounds());
    }
}

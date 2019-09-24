package sudoku.gui;

import sudoku.SudokuGame;
import sudoku.SudokuState;

import javax.swing.*;

public class SudokuFrame extends JFrame {
    private final SudokuPanel sudokuPanel;

    public SudokuFrame(SudokuGame sudokuGame, SudokuState initialState, int sudokuBoardSize) {
        this.sudokuPanel = new SudokuPanel(initialState);
        this.initializeGui(new SudokuKeyboardListener(sudokuGame, sudokuBoardSize));
    }

    private void initializeGui(SudokuKeyboardListener sudokuKeyboardListener) {
        final int FRAME_DIMENSION = 500;

        JFrame frame = new JFrame("Sudoku");

        frame.add(new JPanel());
        frame.add(this.sudokuPanel);
        frame.addKeyListener(sudokuKeyboardListener);

        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setSize(FRAME_DIMENSION, FRAME_DIMENSION);
        frame.setVisible(true);
    }

    public void receiveSudokuState(SudokuState state) {
        this.sudokuPanel.setSudokuState(state);
    }

    public void repaintSudokuPanel() {
        this.sudokuPanel.repaint();
    }
}
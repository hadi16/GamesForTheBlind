package sudoku.gui;

import javax.swing.*;

public class SudokuBoard extends JFrame {
    public SudokuBoard(int numSudokuSquares) {
        this.initializeGui(numSudokuSquares);
    }

    private void initializeGui(int numSudokuSquares) {
        final int FRAME_DIMENSION = 500;

        JFrame frame = new JFrame("Sudoku");

        frame.add(new JPanel());
        frame.add(new SudokuPanel(numSudokuSquares));

        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setSize(FRAME_DIMENSION, FRAME_DIMENSION);
        frame.setVisible(true);
    }
}

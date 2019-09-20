package sudoku.gui;

import javax.swing.*;

public class SudokuBoard extends JFrame {
    public SudokuBoard() {
        this.initializeGui();
    }

    private void initializeGui() {
        JFrame frame = new JFrame("Sudoku");

        frame.add(new JPanel());
        frame.add(new SudokuPanel());

        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setVisible(true);
    }
}

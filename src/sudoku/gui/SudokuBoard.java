package sudoku.gui;

import javax.swing.*;

public class SudokuBoard extends JFrame {
    private void initializeGui() {
        JFrame frame = new JFrame("Sudoku");

        frame.add(new JPanel());
        frame.add(new SudokuPanel());

        frame.setVisible(true);
        frame.setSize(500, 500);
    }

    public SudokuBoard() {
        this.initializeGui();
    }
}

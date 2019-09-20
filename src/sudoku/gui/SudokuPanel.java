package sudoku.gui;

import javax.swing.*;
import java.awt.*;

public class SudokuPanel extends JPanel {
    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        Graphics2D graphics2D = (Graphics2D) graphics.create();

        final int NUM_SUDOKU_SQUARES = 4;
        final int TOTAL_BOARD_LENGTH = 200;

        int squareDimension = (TOTAL_BOARD_LENGTH - NUM_SUDOKU_SQUARES) / NUM_SUDOKU_SQUARES;
        int yPosition = (TOTAL_BOARD_LENGTH - (squareDimension * NUM_SUDOKU_SQUARES)) / 2;

        for (int rowIndex = 0; rowIndex < NUM_SUDOKU_SQUARES; rowIndex++) {
            int xPosition = (TOTAL_BOARD_LENGTH - (squareDimension * NUM_SUDOKU_SQUARES)) / 2;

            for (int columnIndex = 0; columnIndex < NUM_SUDOKU_SQUARES; columnIndex++) {
                graphics.drawRect(xPosition, yPosition, squareDimension, squareDimension);

                xPosition += squareDimension;
            }

            yPosition += squareDimension;
        }

        graphics2D.dispose();
    }
}

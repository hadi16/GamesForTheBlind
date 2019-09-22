package sudoku.gui;

import sudoku.generator.Generator;
import sudoku.generator.Grid;

import javax.swing.*;
import java.awt.*;

public class SudokuPanel extends JPanel {
    private final int numSudokuSquares;
    private final Grid sudokuGrid;

    public SudokuPanel(int numSudokuSquares) {
        this.numSudokuSquares = numSudokuSquares;

        Generator sudokuGenerator = new Generator(numSudokuSquares);
        int numberOfEmptyCells = (numSudokuSquares * numSudokuSquares) / 3;

        this.sudokuGrid = sudokuGenerator.generate(numberOfEmptyCells);
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        Rectangle bounds = graphics.getClipBounds();
        final int TOTAL_BOARD_LENGTH = Math.min(bounds.height, bounds.width);

        graphics.setFont(new Font("Arial", Font.PLAIN, TOTAL_BOARD_LENGTH / 20));

        int squareDimension = (TOTAL_BOARD_LENGTH - this.numSudokuSquares) / this.numSudokuSquares;
        int yPosition = (TOTAL_BOARD_LENGTH - (squareDimension * this.numSudokuSquares)) / 2;

        for (int rowIndex = 0; rowIndex < this.numSudokuSquares; rowIndex++) {
            int xPosition = (TOTAL_BOARD_LENGTH - (squareDimension * this.numSudokuSquares)) / 2;

            for (int columnIndex = 0; columnIndex < this.numSudokuSquares; columnIndex++) {
                graphics.drawRect(xPosition, yPosition, squareDimension, squareDimension);

                int squareNum = this.sudokuGrid.getCell(rowIndex, columnIndex).getValue();
                if (squareNum != 0) {
                    graphics.drawString(
                            Integer.toString(squareNum),
                            xPosition + squareDimension / 3,
                            yPosition + (2 * squareDimension / 3)
                    );
                }

                xPosition += squareDimension;
            }

            yPosition += squareDimension;
        }
    }
}

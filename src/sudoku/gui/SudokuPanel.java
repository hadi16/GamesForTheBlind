package sudoku.gui;

import sudoku.SudokuState;
import sudoku.generator.Grid;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Class for
 */
public class SudokuPanel extends JPanel {
    private final int sudokuBoardSize;
    private final ArrayList<Point> originallyFilledSquares;

    private SudokuState sudokuState;

    public SudokuPanel(SudokuState initialState) {
        this.sudokuState = initialState;
        this.sudokuBoardSize = initialState.getSudokuBoardSize();
        this.originallyFilledSquares = initialState.getOriginallyFilledSquares();
    }

    /**
     * Locates the square that is selected either using keyboard actions or a mouse input and highlights them
     *
     * @param graphics
     * @param rowIdx
     * @param colIdx
     * @param xPos
     * @param yPos
     * @param squareDim
     */
    private void paintHighlightedSquares(Graphics graphics, int rowIdx, int colIdx, int xPos, int yPos, int squareDim) {
        Point selectedBlockPoint = this.sudokuState.getSelectedBlockPoint();
        Point selectedSquarePoint = this.sudokuState.getSelectedSquarePoint();

        graphics.setColor(Color.YELLOW);
        int numberOfBlocks = (int) Math.sqrt(this.sudokuBoardSize);
        if (selectedSquarePoint == null && selectedBlockPoint != null) {
            int minRowIdx = selectedBlockPoint.y * numberOfBlocks;
            int maxRowIdx = (selectedBlockPoint.y + 1) * numberOfBlocks - 1;

            int minColumnIdx = selectedBlockPoint.x * numberOfBlocks;
            int maxColumnIdx = (selectedBlockPoint.x + 1) * numberOfBlocks - 1;

            if (rowIdx >= minRowIdx && rowIdx <= maxRowIdx && colIdx >= minColumnIdx && colIdx <= maxColumnIdx) {
                graphics.fillRect(xPos, yPos, squareDim, squareDim);
            }
        } else if (selectedSquarePoint != null) {
            int selectedRowIndex = selectedBlockPoint.y * numberOfBlocks + selectedSquarePoint.y;
            int selectedColumnIndex = selectedBlockPoint.x * numberOfBlocks + selectedSquarePoint.x;

            if (selectedRowIndex == rowIdx && selectedColumnIndex == colIdx) {
                graphics.fillRect(xPos, yPos, squareDim, squareDim);
            }
        }
    }

    /**
     * Allows all individual components to be painted. This includes, board dimensions, columns, rows, and
     * numbers within cells. Might look into using comic sans as a font tho, idk yet.
     *
     * @param graphics
     */
    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        Grid sudokuGrid = this.sudokuState.getSudokuGrid();

        Rectangle bounds = graphics.getClipBounds();
        final int TOTAL_BOARD_LENGTH = Math.min(bounds.height, bounds.width);

        graphics.setFont(new Font("Arial", Font.PLAIN, TOTAL_BOARD_LENGTH / 20));

        int squareDimension = (TOTAL_BOARD_LENGTH - this.sudokuBoardSize) / this.sudokuBoardSize;
        int yPosition = (TOTAL_BOARD_LENGTH - (squareDimension * this.sudokuBoardSize)) / 2;

        for (int rowIndex = 0; rowIndex < this.sudokuBoardSize; rowIndex++) {
            int xPosition = (TOTAL_BOARD_LENGTH - (squareDimension * this.sudokuBoardSize)) / 2;

            for (int columnIndex = 0; columnIndex < this.sudokuBoardSize; columnIndex++) {
                graphics.setColor(Color.GRAY);
                if (this.originallyFilledSquares.contains(new Point(columnIndex, rowIndex))) {
                    graphics.fillRect(xPosition, yPosition, squareDimension, squareDimension);
                }

                this.paintHighlightedSquares(
                        graphics, rowIndex, columnIndex, xPosition, yPosition, squareDimension
                );

                graphics.setColor(Color.BLACK);
                graphics.drawRect(xPosition, yPosition, squareDimension, squareDimension);

                int squareNum = sudokuGrid.getCell(rowIndex, columnIndex).getValue();
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

    public void setSudokuState(SudokuState sudokuState) {
        this.sudokuState = sudokuState;
    }
}

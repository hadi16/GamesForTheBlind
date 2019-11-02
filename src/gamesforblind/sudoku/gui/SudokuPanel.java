package gamesforblind.sudoku.gui;

import gamesforblind.sudoku.SudokuState;
import gamesforblind.sudoku.generator.Grid;
import gamesforblind.sudoku.interfaces.SudokuBlockSelectionInterface;
import gamesforblind.sudoku.interfaces.SudokuKeyboardInterface;

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
    private void paintHighlightedSquares(Graphics graphics, SudokuBlockSelectionInterface blockSelectionInterface,
                                         int rowIdx, int colIdx, int xPos, int yPos, int squareDim) {
        Point selectedBlockPoint = blockSelectionInterface.getSelectedBlockPoint();
        Point selectedSquarePoint = blockSelectionInterface.getSelectedSquarePoint();

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

    private void paintMainBoard(Graphics graphics, int squareDimension, int initialPosition) {
        Grid sudokuGrid = this.sudokuState.getSudokuGrid();
        SudokuKeyboardInterface keyboardInterface = this.sudokuState.getSudokuKeyboardInterface();

        int yPosition = initialPosition;
        for (int rowIndex = 0; rowIndex < this.sudokuBoardSize; rowIndex++) {
            int xPosition = initialPosition;

            for (int columnIndex = 0; columnIndex < this.sudokuBoardSize; columnIndex++) {
                graphics.setColor(Color.GRAY);
                if (this.originallyFilledSquares.contains(new Point(columnIndex, rowIndex))) {
                    graphics.fillRect(xPosition, yPosition, squareDimension, squareDimension);
                }

                if (keyboardInterface instanceof SudokuBlockSelectionInterface) {
                    var blockInterface = (SudokuBlockSelectionInterface) keyboardInterface;
                    this.paintHighlightedSquares(
                            graphics, blockInterface, rowIndex, columnIndex, xPosition, yPosition, squareDimension
                    );
                }

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

    private void paintBoardLabels(Graphics graphics, int squareDimension, int initialPosition) {
        graphics.setColor(Color.BLACK);

        int yPosition = initialPosition + squareDimension;
        for (int rowIndex = 0; rowIndex < this.sudokuBoardSize; rowIndex++) {
            graphics.drawString(
                    Integer.toString(rowIndex + 1),
                    initialPosition + squareDimension / 3,
                    yPosition + (2 * squareDimension / 3)
            );
            yPosition += squareDimension;
        }

        int xPosition = initialPosition + squareDimension;
        for (int columnIndex = 0; columnIndex < this.sudokuBoardSize; columnIndex++) {
            graphics.drawString(
                    Character.toString((char) columnIndex + 'A'),
                    xPosition + squareDimension / 3,
                    initialPosition + (2 * squareDimension / 3)
            );
            xPosition += squareDimension;
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

        Rectangle bounds = graphics.getClipBounds();
        final int TOTAL_BOARD_LENGTH = Math.min(bounds.height, bounds.width);

        graphics.setFont(new Font("Arial", Font.PLAIN, TOTAL_BOARD_LENGTH / 15));

        int squaresPerSide = this.sudokuBoardSize + 1;
        int squareDimension = (TOTAL_BOARD_LENGTH - squaresPerSide) / squaresPerSide;

        final int INITIAL_POSITION = (TOTAL_BOARD_LENGTH - (squareDimension * squaresPerSide)) / 2;

        this.paintBoardLabels(graphics, squareDimension, INITIAL_POSITION);
        this.paintMainBoard(graphics, squareDimension, INITIAL_POSITION + squareDimension);
    }

    public void setSudokuState(SudokuState sudokuState) {
        this.sudokuState = sudokuState;
    }
}

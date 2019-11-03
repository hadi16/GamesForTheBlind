package gamesforblind.sudoku.gui;

import gamesforblind.sudoku.SudokuState;
import gamesforblind.sudoku.generator.Grid;
import gamesforblind.sudoku.interfaces.SudokuBlockSelectionInterface;
import gamesforblind.sudoku.interfaces.SudokuKeyboardInterface;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Contains the main GUI code for Sudoku. Serves as a custom JPanel for Sudoku GUI (extends JPanel).
 */
public class SudokuPanel extends JPanel {
    /**
     * The number of squares on each side of the Sudoku board (e.g. 9x9 --> 9).
     */
    private final int sudokuBoardSize;

    /**
     * Which squares were originally filled on the board to highlight them in a special color for sighted users.
     */
    private final ArrayList<Point> originallyFilledSquares;

    /**
     * The current Sudoku board state.
     */
    private SudokuState sudokuState;

    /**
     * Creates a new SudokuPanel.
     *
     * @param initialState The initial state of the Sudoku game.
     */
    public SudokuPanel(SudokuState initialState) {
        this.sudokuState = initialState;
        this.sudokuBoardSize = initialState.getSudokuBoardSize();
        this.originallyFilledSquares = initialState.getOriginallyFilledSquares();
    }

    /**
     * NOTE: only used for the {@link SudokuBlockSelectionInterface}.
     * Highlights the blocks & squares that are currently highlighted in the game.
     *
     * @param graphics The {@link Graphics} object used for painting.
     * @param blockSelectionInterface The block interface, which contains the selected block & square {@link Point}s.
     * @param rowIdx The current row index.
     * @param columnIdx The current column index.
     * @param xPos The current x position (amount of pixels in x direction).
     * @param yPos The current y position (amount of pixels in y direction).
     * @param squareDimension The pixel dimension of each square on the board.
     */
    private void paintHighlightedSquares(
            Graphics graphics, SudokuBlockSelectionInterface blockSelectionInterface,
            int rowIdx, int columnIdx, int xPos, int yPos, int squareDimension
    ) {
        Point selectedBlockPoint = blockSelectionInterface.getSelectedBlockPoint();
        Point selectedSquarePoint = blockSelectionInterface.getSelectedSquarePoint();

        graphics.setColor(Color.YELLOW);

        // Since this block interface is only supported on 4x4 & 9x9 boards, this works fine.
        int numberOfBlocks = (int) Math.sqrt(this.sudokuBoardSize);

        // Case 1: the selected block is highlighted.
        if (selectedSquarePoint == null && selectedBlockPoint != null) {
            int minRowIdx = selectedBlockPoint.y * numberOfBlocks;
            int maxRowIdx = (selectedBlockPoint.y + 1) * numberOfBlocks - 1;

            int minColumnIdx = selectedBlockPoint.x * numberOfBlocks;
            int maxColumnIdx = (selectedBlockPoint.x + 1) * numberOfBlocks - 1;

            if (rowIdx >= minRowIdx && rowIdx <= maxRowIdx && columnIdx >= minColumnIdx && columnIdx <= maxColumnIdx) {
                graphics.fillRect(xPos, yPos, squareDimension, squareDimension);
            }
            return;
        }

        // Case 2: the selected square is highlighted.
        if (selectedSquarePoint != null) {
            int selectedRowIndex = selectedBlockPoint.y * numberOfBlocks + selectedSquarePoint.y;
            int selectedColumnIndex = selectedBlockPoint.x * numberOfBlocks + selectedSquarePoint.x;

            if (selectedRowIndex == rowIdx && selectedColumnIndex == columnIdx) {
                graphics.fillRect(xPos, yPos, squareDimension, squareDimension);
            }
        }

        // Implicit third case: no square OR block is highlighted.
    }

    /**
     * Paints the Sudoku board.
     *
     * @param graphics The {@link Graphics} object used for painting.
     * @param squareDimension The pixel dimension of each square on the board.
     * @param initialPosition Amount of pixels to begin painting board from (the row & column labels come before this).
     */
    private void paintMainBoard(Graphics graphics, int squareDimension, int initialPosition) {
        Grid sudokuGrid = this.sudokuState.getSudokuGrid();
        SudokuKeyboardInterface keyboardInterface = this.sudokuState.getSudokuKeyboardInterface();

        int yPosition = initialPosition;
        for (int rowIndex = 0; rowIndex < this.sudokuBoardSize; rowIndex++) {
            int xPosition = initialPosition;

            for (int columnIndex = 0; columnIndex < this.sudokuBoardSize; columnIndex++) {
                // Step 1: paint this square as an originally filled square (if applicable).
                graphics.setColor(Color.GRAY);
                if (this.originallyFilledSquares.contains(new Point(columnIndex, rowIndex))) {
                    graphics.fillRect(xPosition, yPosition, squareDimension, squareDimension);
                }

                // Step 2: if this is the block interface, paint the highlighted blocks or squares on the board.
                if (keyboardInterface instanceof SudokuBlockSelectionInterface) {
                    var blockInterface = (SudokuBlockSelectionInterface) keyboardInterface;
                    this.paintHighlightedSquares(
                            graphics, blockInterface, rowIndex, columnIndex, xPosition, yPosition, squareDimension
                    );
                }

                // Step 3: draw the basic square that holds a number on the Sudoku board.
                graphics.setColor(Color.BLACK);
                graphics.drawRect(xPosition, yPosition, squareDimension, squareDimension);

                // Step 4: fill in the square on the Sudoku board (if not empty).
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

    /**
     * Paints the row & column labels for the Sudoku board.
     * We will model this closely after Excel, which uses numbers for row labels & letters for column labels.
     *
     * @param graphics The {@link Graphics} object used for painting.
     * @param squareDimension The pixel dimension of each square on the board.
     * @param initialPosition Amount of pixels to begin painting board from.
     */
    private void paintBoardLabels(Graphics graphics, int squareDimension, int initialPosition) {
        graphics.setColor(Color.BLACK);

        // Step 1: print the row labels (numbers 1, 2, 3, etc.)
        int yPosition = initialPosition + squareDimension;
        for (int rowIndex = 0; rowIndex < this.sudokuBoardSize; rowIndex++) {
            graphics.drawString(
                    Integer.toString(rowIndex + 1),
                    initialPosition + squareDimension / 3,
                    yPosition + (2 * squareDimension / 3)
            );
            yPosition += squareDimension;
        }

        // Step 2: print the column labels (letters 'A', 'B', 'C', etc.)
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
     * When repaint() or paint() is called, paints the Sudoku GUI.
     * Might look into using comic sans as a font.
     *
     * @param graphics The {@link Graphics} object used for painting.
     */
    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        Rectangle bounds = graphics.getClipBounds();
        final int TOTAL_BOARD_LENGTH = Math.min(bounds.height, bounds.width);

        // The font used throughout the entire Sudoku GUI.
        graphics.setFont(new Font("Arial", Font.PLAIN, TOTAL_BOARD_LENGTH / 15));

        int squaresPerSide = this.sudokuBoardSize + 1;
        int squareDimension = (TOTAL_BOARD_LENGTH - squaresPerSide) / squaresPerSide;

        final int INITIAL_POSITION = (TOTAL_BOARD_LENGTH - (squareDimension * squaresPerSide)) / 2;

        // Step 1: paint the row & column labels.
        this.paintBoardLabels(graphics, squareDimension, INITIAL_POSITION);

        // Step 2: paint the main Sudoku board (which includes highlighted squares).
        this.paintMainBoard(graphics, squareDimension, INITIAL_POSITION + squareDimension);
    }

    /**
     * Setter for sudokuState
     *
     * @param sudokuState The current {@link SudokuState} of the board.
     */
    public void setSudokuState(SudokuState sudokuState) {
        this.sudokuState = sudokuState;
    }
}

package gamesforblind.sudoku.gui;

import gamesforblind.enums.SudokuType;
import gamesforblind.sudoku.SudokuState;
import gamesforblind.sudoku.generator.Grid;
import gamesforblind.sudoku.interfaces.SudokuBlockSelectionInterface;
import gamesforblind.sudoku.interfaces.SudokuKeyboardInterface;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Optional;

import static gamesforblind.Constants.EMPTY_SUDOKU_SQUARE;

/**
 * Contains the main GUI code for Sudoku. Serves as a custom JPanel for Sudoku GUI (extends JPanel).
 */
public class SudokuPanel extends JPanel {
    private static final Color BRIGHT_BLUE = new Color(89, 202, 232);
    private static final Color BRIGHT_ORANGE = new Color(249, 135, 15);
    private static final Color BRIGHT_YELLOW = new Color(255, 247, 53);

    /**
     * Whether the Sudoku game is a 4x4, 6x6, or 9x9 variant.
     */
    private final SudokuType sudokuType;

    /**
     * Which squares were originally filled on the board to highlight them in a special color for sighted users.
     */
    private final ArrayList<Point> originallyFilledSquares;

    /**
     * The current Sudoku board state.
     */
    private SudokuState sudokuState;

    private int totalBoardLength;

    /**
     * Creates a new SudokuPanel.
     *
     * @param initialState The initial state of the Sudoku game.
     */
    public SudokuPanel(SudokuState initialState) {
        this.sudokuState = initialState;
        this.sudokuType = initialState.getSudokuType();
        this.originallyFilledSquares = initialState.getOriginallyFilledSquares();
    }

    /**
     * NOTE: only used for the {@link SudokuBlockSelectionInterface}.
     * Highlights the currently selected block in the game.
     *
     * @param graphics           The {@link Graphics} object used for painting.
     * @param selectedBlockPoint The block that is currently selected (cannot be null).
     * @param rowIdx             The current row index.
     * @param columnIdx          The current column index.
     * @param xPos               The current x position (amount of pixels in x direction).
     * @param yPos               The current y position (amount of pixels in y direction).
     * @param squareDim          The pixel dimension of each square on the board.
     */
    private void paintHighlightedBlock(
            Graphics graphics, Point selectedBlockPoint, int rowIdx, int columnIdx, int xPos, int yPos, int squareDim
    ) {
        // Since this block interface is only supported on 4x4 & 9x9 boards, height or width is the same.
        int blockHeight = this.sudokuType.getBlockHeight();

        int minRowIdx = selectedBlockPoint.y * blockHeight;
        int maxRowIdx = (selectedBlockPoint.y + 1) * blockHeight - 1;

        int minColumnIdx = selectedBlockPoint.x * blockHeight;
        int maxColumnIdx = (selectedBlockPoint.x + 1) * blockHeight - 1;

        if (rowIdx >= minRowIdx && rowIdx <= maxRowIdx && columnIdx >= minColumnIdx && columnIdx <= maxColumnIdx) {
            graphics.fillRect(xPos, yPos, squareDim, squareDim);
        }
    }

    /**
     * Highlights the currently selected Sudoku square.
     *
     * @param graphics        The {@link Graphics} object used for painting.
     * @param rowIdx          The current row index.
     * @param columnIdx       The current column index.
     * @param xPos            The current x position (amount of pixels in x direction).
     * @param yPos            The current y position (amount of pixels in y direction).
     * @param squareDimension The pixel dimension of each square on the board.
     */
    private void paintHighlightedSquare(
            Graphics graphics, int rowIdx, int columnIdx, int xPos, int yPos, int squareDimension
    ) {
        Optional<Point> maybeSelectedPoint = this.sudokuState.getSudokuKeyboardInterface().getSelectedPoint();
        maybeSelectedPoint.ifPresent(selectedPoint -> {
            if (selectedPoint.y == rowIdx && selectedPoint.x == columnIdx) {
                graphics.fillRect(xPos, yPos, squareDimension, squareDimension);
            }
        });
    }

    /**
     * Paints the bolded block borders.
     *
     * @param graphics        The {@link Graphics} object used for painting.
     * @param squareDimension The pixel dimension of each square on the board.
     * @param initialPosition Amount of pixels to begin painting board from (the row & column labels come before this).
     */
    private void paintBlockBorders(Graphics graphics, int squareDimension, int initialPosition) {
        final int BLOCK_WIDTH = this.sudokuType.getBlockWidth();
        final int BLOCK_HEIGHT = this.sudokuType.getBlockHeight();

        final int BLOCK_WIDTH_DIM = squareDimension * BLOCK_WIDTH;
        final int BLOCK_HEIGHT_DIM = squareDimension * BLOCK_HEIGHT;

        int xBlockPosition = initialPosition;
        int yBlockPosition = initialPosition;
        for (int i = 0; i < BLOCK_WIDTH; i++) {
            for (int j = 0; j < BLOCK_HEIGHT; j++) {
                graphics.drawRect(xBlockPosition + 1, yBlockPosition + 1, BLOCK_WIDTH_DIM, BLOCK_HEIGHT_DIM);
                graphics.drawRect(xBlockPosition - 1, yBlockPosition - 1, BLOCK_WIDTH_DIM, BLOCK_HEIGHT_DIM);
                xBlockPosition += BLOCK_WIDTH_DIM;
            }

            yBlockPosition += BLOCK_HEIGHT_DIM;
            xBlockPosition = initialPosition;
        }
    }

    /**
     * Highlights relevant areas of the board in green (e.g. square, block).
     * NOTE: the only GUI method that needs to check for which type of keyboard interface is being used.
     *
     * @param graphics        The {@link Graphics} object used for painting.
     * @param rowIdx          The current row index.
     * @param columnIdx       The current column index.
     * @param xPos            The current x position (amount of pixels in x direction).
     * @param yPos            The current y position (amount of pixels in y direction).
     * @param squareDimension The pixel dimension of each square on the board.
     */
    private void paintAllHighlights(
            Graphics graphics, int rowIdx, int columnIdx, int xPos, int yPos, int squareDimension
    ) {
        graphics.setColor(Color.GREEN);
        SudokuKeyboardInterface keyboardInterface = this.sudokuState.getSudokuKeyboardInterface();

        // Case 1: the block selection interface is being used & block is selected.
        if (keyboardInterface instanceof SudokuBlockSelectionInterface) {
            var sudokuBlockSelectionInterface = (SudokuBlockSelectionInterface) keyboardInterface;
            Point selectedBlockPoint = sudokuBlockSelectionInterface.getSelectedBlockPoint();
            Point selectedSquarePoint = sudokuBlockSelectionInterface.getSelectedSquarePoint();

            // If a square has already been selected, don't paint the block.
            if (selectedBlockPoint != null && selectedSquarePoint == null) {
                this.paintHighlightedBlock(graphics, selectedBlockPoint, rowIdx, columnIdx, xPos, yPos, squareDimension);
                return;
            }
        }

        // Case 2: a square is selected in either interface type.
        this.paintHighlightedSquare(graphics, rowIdx, columnIdx, xPos, yPos, squareDimension);
    }

    /**
     * Paints the Sudoku board.
     *
     * @param graphics        The {@link Graphics} object used for painting.
     * @param squareDimension The pixel dimension of each square on the board.
     * @param initialPosition Amount of pixels to begin painting board from (the row & column labels come before this).
     */
    private void paintMainBoard(Graphics graphics, int squareDimension, int initialPosition) {
        int sudokuBoardSize = this.sudokuType.getSudokuBoardSize();
        Grid sudokuGrid = this.sudokuState.getSudokuGrid();

        graphics.setFont(new Font("Arial", Font.BOLD, this.totalBoardLength / sudokuBoardSize));

        int yPosition = initialPosition;
        for (int rowIndex = 0; rowIndex < sudokuBoardSize; rowIndex++) {
            int xPosition = initialPosition;

            for (int columnIndex = 0; columnIndex < sudokuBoardSize; columnIndex++) {
                // Step 1: paint this square as an originally filled square (if applicable).
                graphics.setColor(BRIGHT_BLUE);
                if (this.originallyFilledSquares.contains(new Point(columnIndex, rowIndex))) {
                    graphics.fillRect(xPosition, yPosition, squareDimension, squareDimension);
                }

                // Step 2: fill in the square on the Sudoku board (if not empty).
                int currentCellValue = sudokuGrid.getCell(rowIndex, columnIndex).getValue();
                if (currentCellValue != EMPTY_SUDOKU_SQUARE) {
                    // Draws a background under the cell to show it's been answered
                    graphics.setColor(BRIGHT_BLUE);
                    graphics.fillRect(xPosition, yPosition, squareDimension, squareDimension);
                }

                // Step 3: highlight relevant areas of the board in green (e.g. square, block).
                this.paintAllHighlights(graphics, rowIndex, columnIndex, xPosition, yPosition, squareDimension);

                // Step 4: draw the numbers over top the colored in squares
                if (currentCellValue != EMPTY_SUDOKU_SQUARE) {
                    graphics.setColor(BRIGHT_YELLOW);
                    graphics.drawString(
                            Integer.toString(currentCellValue),
                            xPosition + (sudokuBoardSize + 13) * squareDimension / 110,
                            yPosition + (1044 - 11 * sudokuBoardSize) * squareDimension / 1050
                    );
                }

                // Step 5: draw the basic square that holds a number on the Sudoku board.
                graphics.setColor(Color.BLACK);
                graphics.drawRect(xPosition, yPosition, squareDimension, squareDimension);

                xPosition += squareDimension;
            }

            yPosition += squareDimension;
        }
    }

    /**
     * Paints the row & column labels for the Sudoku board.
     * We will model this closely after Excel, which uses numbers for row labels & letters for column labels.
     *
     * @param graphics        The {@link Graphics} object used for painting.
     * @param squareDimension The pixel dimension of each square on the board.
     * @param initialPosition Amount of pixels to begin painting board from.
     */
    private void paintBoardLabels(Graphics graphics, int squareDimension, int initialPosition) {
        int sudokuBoardSize = this.sudokuType.getSudokuBoardSize();

        graphics.setColor(Color.BLACK);
        graphics.setFont(
                new Font("Arial", Font.BOLD, (93 - 7 * sudokuBoardSize) * this.totalBoardLength / 390)
        );

        // Step 1: print the row labels (numbers 1, 2, 3, etc.)
        int yPosition = initialPosition + squareDimension;
        for (int rowIndex = 0; rowIndex < sudokuBoardSize; rowIndex++) {
            graphics.drawString(
                    Integer.toString(rowIndex + 1),
                    initialPosition + 11 * squareDimension / 24,
                    yPosition + 5 * squareDimension / 6
            );
            yPosition += squareDimension;
        }

        // Step 2: print the column labels (letters 'A', 'B', 'C', etc.)
        int xPosition = initialPosition + squareDimension;
        for (int columnIndex = 0; columnIndex < sudokuBoardSize; columnIndex++) {
            graphics.drawString(
                    Character.toString((char) columnIndex + 'A'),
                    xPosition + 5 * squareDimension / 24,
                    initialPosition + 11 * squareDimension / 12
            );

            // X position was slightly shifted to the left for the rightmost column labels.
            xPosition += (51 * squareDimension / 50);
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
        this.totalBoardLength = Math.min(bounds.height, bounds.width);

        int squaresPerSide = this.sudokuType.getSudokuBoardSize() + 1;
        int squareDimension = (this.totalBoardLength - squaresPerSide) / squaresPerSide;

        final int INITIAL_POSITION = (this.totalBoardLength - (squareDimension * squaresPerSide)) / 2;

        // Step 1: paint the row & column labels (font size is slightly smaller).
        this.paintBoardLabels(graphics, squareDimension, INITIAL_POSITION);

        // Step 2: paint the main Sudoku board (which includes highlighted squares).
        this.paintMainBoard(graphics, squareDimension, INITIAL_POSITION + squareDimension);

        // Step 3: paint the bolded block borders.
        this.paintBlockBorders(graphics, squareDimension, INITIAL_POSITION + squareDimension);
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

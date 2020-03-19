package gamesforblind.sudoku.gui;

import gamesforblind.enums.SudokuType;
import gamesforblind.sudoku.SudokuState;
import gamesforblind.sudoku.generator.Grid;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;

import static gamesforblind.Constants.EMPTY_SUDOKU_SQUARE;

/**
 * Contains the main GUI code for Sudoku. Serves as a custom JPanel for Sudoku GUI (extends JPanel).
 */
public class SudokuPanel extends JPanel {
    private static final Color BRIGHT_BLUE = new Color(89, 202, 232);
    private static final Color BRIGHT_YELLOW = new Color(255, 247, 53);

    /**
     * Whether the Sudoku game is a 4x4, 6x6, or 9x9 variant.
     */
    private final SudokuType sudokuType;

    /**
     * The Sudoku board state.
     */
    private final SudokuState sudokuState;

    /**
     * This is reset every time the board is repainted (based on the bounds of the GUI window).
     */
    private int totalBoardLength;

    /**
     * Creates a new SudokuPanel.
     *
     * @param initialState The initial state of the Sudoku game.
     */
    public SudokuPanel(@NotNull SudokuState initialState) {
        this.sudokuState = initialState;
        this.sudokuType = initialState.getSudokuType();
    }

    /**
     * Paints the bolded block borders.
     *
     * @param graphics        The {@link Graphics} object used for painting.
     * @param squareDimension The pixel dimension of each square on the board.
     * @param initialPosition Amount of pixels to begin painting board from (the row & column labels come before this).
     */
    private void paintBlockBorders(@NotNull Graphics graphics, int squareDimension, int initialPosition) {
        final int BLOCK_WIDTH = this.sudokuType.getBlockWidth();
        final int BLOCK_HEIGHT = this.sudokuType.getBlockHeight();

        final int BLOCK_WIDTH_DIM = squareDimension * BLOCK_WIDTH;
        final int BLOCK_HEIGHT_DIM = squareDimension * BLOCK_HEIGHT;

        for (int i = 0; i < BLOCK_HEIGHT; i++) {
            for (int j = 0; j < BLOCK_WIDTH; j++) {
                int xPosition = initialPosition + i * BLOCK_WIDTH_DIM;
                int yPosition = initialPosition + j * BLOCK_HEIGHT_DIM;

                graphics.drawRect(xPosition + 1, yPosition + 1, BLOCK_WIDTH_DIM, BLOCK_HEIGHT_DIM);
                graphics.drawRect(xPosition - 1, yPosition - 1, BLOCK_WIDTH_DIM, BLOCK_HEIGHT_DIM);
            }
        }
    }

    /**
     * Paints the Sudoku board.
     *
     * @param graphics        The {@link Graphics} object used for painting.
     * @param squareDimension The pixel dimension of each square on the board.
     * @param initialPosition Amount of pixels to begin painting board from (the row & column labels come before this).
     */
    private void paintMainBoard(@NotNull Graphics graphics, int squareDimension, int initialPosition) {
        int sudokuBoardSize = this.sudokuType.getSudokuBoardSize();
        Grid sudokuGrid = this.sudokuState.getSudokuGrid();
        ArrayList<Point> originallyFilledSquares = this.sudokuState.getOriginallyFilledSquares();

        graphics.setFont(new Font("Arial", Font.BOLD, this.totalBoardLength / sudokuBoardSize));

        ArrayList<Point> highlightedPoints = this.sudokuState.getSudokuKeyboardInterface().getHighlightedPointList();
        for (int rowIndex = 0; rowIndex < sudokuBoardSize; rowIndex++) {
            int yPosition = initialPosition + rowIndex * squareDimension;

            for (int columnIndex = 0; columnIndex < sudokuBoardSize; columnIndex++) {
                int xPosition = initialPosition + columnIndex * squareDimension;

                // Step 1: fill in the square with the appropriate color (e.g. green for highlighted).
                Point currentPoint = new Point(columnIndex, rowIndex);
                int currentCellValue = sudokuGrid.getCell(rowIndex, columnIndex).getValue();
                if (currentCellValue == EMPTY_SUDOKU_SQUARE && highlightedPoints.contains(currentPoint)) {
                    // Case 1: the Point is highlighted & empty.
                    graphics.setColor(Color.GREEN);
                    graphics.fillRect(xPosition, yPosition, squareDimension, squareDimension);
                } else if (currentCellValue != EMPTY_SUDOKU_SQUARE) {
                    if (highlightedPoints.contains(currentPoint)) {
                        // Case 2: the Point is highlighted & not empty.
                        graphics.setColor(Color.GREEN);
                    } else {
                        // Case 3: the Point is not highlighted & not empty.
                        if (originallyFilledSquares.contains(currentPoint)) {
                            // Case 3a: the Point is an originally filled square.
                            graphics.setColor(Color.RED);
                        } else {
                            // Case 3b: the Point is NOT an originally filled square.
                            graphics.setColor(BRIGHT_BLUE);
                        }
                    }

                    graphics.fillRect(xPosition, yPosition, squareDimension, squareDimension);
                }

                // Step 2: draw the numbers over top the colored in squares
                if (currentCellValue != EMPTY_SUDOKU_SQUARE) {
                    graphics.setColor(BRIGHT_YELLOW);
                    graphics.drawString(
                            Integer.toString(currentCellValue),
                            xPosition + (sudokuBoardSize + 13) * squareDimension / 110,
                            yPosition + (1044 - 11 * sudokuBoardSize) * squareDimension / 1050
                    );
                }

                // Step 3: draw the basic square that holds a number on the Sudoku board.
                graphics.setColor(Color.BLACK);
                graphics.drawRect(xPosition, yPosition, squareDimension, squareDimension);
            }
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
    private void paintBoardLabels(@NotNull Graphics graphics, int squareDimension, int initialPosition) {
        final int sudokuBoardSize = this.sudokuType.getSudokuBoardSize();

        graphics.setColor(Color.BLACK);
        graphics.setFont(
                new Font("Arial", Font.BOLD, (93 - 7 * sudokuBoardSize) * this.totalBoardLength / 390)
        );

        // Step 1: print the row labels (numbers r1, r2, r3, etc.)
        for (int rowIndex = 0; rowIndex < sudokuBoardSize; rowIndex++) {
            graphics.drawString(
                    ("r" + (rowIndex + 1)),
                    initialPosition + (11 * squareDimension / 24) - (squareDimension / 3),
                    initialPosition + (squareDimension * rowIndex) + (11 * squareDimension / 6)
            );
        }

        // Step 2: print the column labels (letters 'c1', 'c2', 'c3', etc.)
        for (int columnIndex = 0; columnIndex < sudokuBoardSize; columnIndex++) {
            graphics.drawString(
                    "c" + (columnIndex + 1),
                    initialPosition + (51 * squareDimension * columnIndex / 50) + (29 * squareDimension / 24) - (squareDimension / 4),
                    initialPosition + (11 * squareDimension / 12)
            );
        }

        final Font MAIN_BOARD_FONT = new Font("Serif", Font.BOLD, 50);
        graphics.setFont(MAIN_BOARD_FONT);

        Duration timeElapsed = Duration.between(this.sudokuState.getTime(), Instant.now());
        int hoursElapsed = timeElapsed.toHoursPart();
        int minutesElapsed = timeElapsed.toMinutesPart();
        int secondsElapsed = timeElapsed.toSecondsPart();

        graphics.drawString(
                "Time: " + hoursElapsed + ":" + minutesElapsed + ":" + secondsElapsed,
                initialPosition + (51 * squareDimension * sudokuBoardSize / 50) + (29 * squareDimension / 24) - (squareDimension / 4),
                (1044 - 11 * sudokuBoardSize) * squareDimension / 1050
        );
    }

    /**
     * When repaint() or paint() is called, paints the Sudoku GUI.
     * Might look into using comic sans as a font.
     *
     * @param graphics The {@link Graphics} object used for painting.
     */
    @Override
    protected void paintComponent(@NotNull Graphics graphics) {
        super.paintComponent(graphics);

        Rectangle clipBounds = graphics.getClipBounds();
        this.totalBoardLength = Math.min(clipBounds.height, clipBounds.width);

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
}

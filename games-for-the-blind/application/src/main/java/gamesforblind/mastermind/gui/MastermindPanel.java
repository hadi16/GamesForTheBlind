package gamesforblind.mastermind.gui;

import gamesforblind.enums.SudokuType;
import gamesforblind.mastermind.MastermindState;
import gamesforblind.sudoku.SudokuState;
import gamesforblind.sudoku.generator.Grid;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import static gamesforblind.Constants.EMPTY_SUDOKU_SQUARE;

/**
 * Contains the main GUI code for Mastermind. Serves as a custom JPanel for Mastermind GUI (extends JPanel).
 */
public class MastermindPanel extends JPanel {
    private static final Color BRIGHT_BLUE = new Color(89, 202, 232);
    private static final Color BRIGHT_YELLOW = new Color(255, 247, 53);

    /**
     * The Mastermind board state.
     */
    private final MastermindState mastermindState;

    /**
     * This is reset every time the board is repainted (based on the bounds of the GUI window).
     */
    private int totalBoardLength;

    /**
     * Creates a new MastermindPanel.
     *
     * @param initialState The initial state of the Mastermind game.
     */
    public MastermindPanel(@NotNull MastermindState initialState) {
        this.mastermindState = initialState;
    }

    /**
     * Paints the bolded block borders.
     *
     * @param graphics        The {@link Graphics} object used for painting.
     * @param squareDimension The pixel dimension of each square on the board.
     * @param initialPosition Amount of pixels to begin painting board from (the row & column labels come before this).
     */
    private void paintBlockBorders(@NotNull Graphics graphics, int squareDimension, int initialPosition) {
        final int BLOCK_WIDTH = 200;
        final int BLOCK_HEIGHT = 200;

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
     * Paints the Mastermind board.
     *
     * @param graphics        The {@link Graphics} object used for painting.
     * @param squareDimension The pixel dimension of each square on the board.
     * @param initialPosition Amount of pixels to begin painting board from (the row & column labels come before this).
     */
    private void paintMainBoard(@NotNull Graphics graphics, int squareDimension, int initialPosition) {
        int mastermindBoardSize = 5;
        graphics.setFont(new Font("Arial", Font.BOLD, mastermindBoardSize));

        for (int rowIndex = 0; rowIndex < mastermindBoardSize; rowIndex++) {
            int yPosition = initialPosition + rowIndex * squareDimension;

            for (int columnIndex = 0; columnIndex < 3; columnIndex++) {
                int xPosition = initialPosition + columnIndex * squareDimension;

                graphics.fillRect(xPosition, yPosition, squareDimension, squareDimension);
                graphics.setColor(Color.BLACK);
                graphics.drawRect(xPosition, yPosition, squareDimension, squareDimension);
            }
        }
    }

    /**
     * Paints the row & column labels for the Mastermind board.
     * We will model this closely after Excel, which uses numbers for row labels & letters for column labels.
     *
     * @param graphics        The {@link Graphics} object used for painting.
     * @param squareDimension The pixel dimension of each square on the board.
     * @param initialPosition Amount of pixels to begin painting board from.
     */
    private void paintBoardLabels(@NotNull Graphics graphics, int squareDimension, int initialPosition) {
        int mastermindBoardSize = 5;

        graphics.setColor(Color.BLACK);
        graphics.setFont(
                new Font("Arial", Font.BOLD, (93 - 7 * mastermindBoardSize) * this.totalBoardLength / 390)
        );

        // Step 1: print the row labels (numbers 1, 2, 3, etc.)
        for (int rowIndex = 0; rowIndex < mastermindBoardSize; rowIndex++) {
            graphics.drawString(
                    Integer.toString(rowIndex + 1),
                    initialPosition + (11 * squareDimension / 24),
                    initialPosition + (squareDimension * rowIndex) + (11 * squareDimension / 6)
            );
        }

        // Step 2: print the column labels (letters 'A', 'B', 'C', etc.)
        for (int columnIndex = 0; columnIndex < 3; columnIndex++) {
            graphics.drawString(
                    Character.toString((char) columnIndex + 'A'),
                    initialPosition + (51 * squareDimension * columnIndex / 50) + (29 * squareDimension / 24),
                    initialPosition + (11 * squareDimension / 12)
            );
        }
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

        int squaresPerSide = 6;
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

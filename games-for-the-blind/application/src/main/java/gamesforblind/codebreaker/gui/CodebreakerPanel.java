package gamesforblind.codebreaker.gui;

import gamesforblind.codebreaker.CodebreakerState;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

/**
 * Contains the main GUI code for Mastermind. Serves as a custom JPanel for Mastermind GUI (extends JPanel).
 */
public class CodebreakerPanel extends JPanel {
    private final CodebreakerState codebreakerState;
    private int totalBoardLength;

    /**
     * Creates a new MastermindPanel.
     *
     * @param initialState The initial state of the Mastermind game.
     */
    public CodebreakerPanel(@NotNull CodebreakerState initialState) {
        this.codebreakerState = initialState;
    }

    /**
     * Paints the Mastermind board where the colors will be placed.
     *
     * @param graphics        The {@link Graphics} object used for painting.
     * @param squareDimension The pixel dimension of each square on the board.
     * @param initialPosition Amount of pixels to begin painting board from (the row & column labels come before this).
     */
    private void paintMainBoard(@NotNull Graphics graphics, int squareDimension, @NotNull Point initialPosition) {
        // Draws border around box
        graphics.drawRect(
                initialPosition.x - 1,
                initialPosition.y - 1,
                squareDimension * 4,
                squareDimension * 10
        );

        // Draws border
        graphics.drawRect(
                initialPosition.x + (squareDimension * 4) - 1,
                initialPosition.y - 1,
                squareDimension,
                squareDimension * 10
        );

        for (int rowIndex = 0; rowIndex < 10; rowIndex++) {
            int yPosition = initialPosition.y + rowIndex * squareDimension;

            for (int columnIndex = 0; columnIndex < 4; columnIndex++) {
                int xPosition = initialPosition.x + columnIndex * squareDimension;

                // Draw grid
                graphics.setColor(Color.BLACK);
                graphics.drawRect(xPosition, yPosition, squareDimension, squareDimension);
            }
        }
    }

    /**
     * Paints the Mastermind board where the previous results are shown.
     *
     * @param graphics        The {@link Graphics} object used for painting.
     * @param squareDimension The pixel dimension of each square on the board.
     * @param initialPosition Amount of pixels to begin painting board from (column & row labels come before this).
     */
    private void paintResultBoard(@NotNull Graphics graphics, int squareDimension, @NotNull Point initialPosition) {
        for (int rowIndex = 0; rowIndex < 20; rowIndex++) {
            int yPosition = initialPosition.y + rowIndex * squareDimension;

            for (int columnIndex = 0; columnIndex < 2; columnIndex++) {
                int xPosition = initialPosition.x + columnIndex * squareDimension;

                // Draw square of grid
                graphics.setColor(Color.BLACK);
                graphics.drawRect(xPosition, yPosition, squareDimension, squareDimension);
            }
        }
    }

    /**
     * Paints the  labels along the left and top edges of the board
     *
     * @param graphics        The {@link Graphics} object used for painting.
     * @param squareDimension The pixel dimension of each square on the board.
     * @param initialPosition Amount of pixels to begin painting board from (row & column labels come before this).
     */
    private void paintBoardLabels(@NotNull Graphics graphics, int squareDimension, Point initialPosition) {
        graphics.setColor(Color.BLACK);
        graphics.setFont(
                new Font("Arial", Font.BOLD, (93 - 7 * 10) * this.totalBoardLength / 390)
        );

        // Step 1: print the row labels (numbers 1, 2, 3, etc.)
        for (int rowIndex = 0; rowIndex < 10; rowIndex++) {
            graphics.drawString(
                    Integer.toString(rowIndex + 1),
                    initialPosition.x - (11 * squareDimension / 12),
                    initialPosition.y + (squareDimension * rowIndex) + (4 * squareDimension / 7)
            );
        }

        // Step 2: print the column labels (letters 'A', 'B', 'C', etc.)
        for (int columnIndex = 0; columnIndex < 4; columnIndex++) {
            graphics.drawString(
                    Character.toString((char) columnIndex + 'A'),
                    initialPosition.x + squareDimension * columnIndex,
                    initialPosition.y - (4 * squareDimension / 12)
            );
        }
    }

    /**
     * When repaint() or paint() is called, paints the Mastermind GUI.
     * Might look into using comic sans as a font.
     *
     * @param graphics The {@link Graphics} object used for painting.
     */
    @Override
    protected void paintComponent(@NotNull Graphics graphics) {
        super.paintComponent(graphics);

        Rectangle clipBounds = graphics.getClipBounds();
        this.totalBoardLength = Math.min(clipBounds.height, clipBounds.width);

        int squaresPerSide = 11; // 10 (for initial board) + 1
        int squareDimension = (((this.totalBoardLength - squaresPerSide) / squaresPerSide) / 2) * 2;

        final int INITIAL_POSITION = this.totalBoardLength - (squareDimension * squaresPerSide) + 3 * (squareDimension / 2);

        // Step 1: paint the board, 4x10 grid currently
        this.paintMainBoard(
                graphics, squareDimension, new Point(INITIAL_POSITION, INITIAL_POSITION - (squareDimension / 2))
        );

        //Step 2: paint small board that will display the result of previous guess
        this.paintResultBoard(
                graphics,
                squareDimension / 2,
                new Point(INITIAL_POSITION + (squareDimension * 4), INITIAL_POSITION - (squareDimension / 2))
        );

        // Step 3: paint the labels
        this.paintBoardLabels(
                graphics, squareDimension, new Point(INITIAL_POSITION, INITIAL_POSITION - (squareDimension / 2))
        );
    }
}

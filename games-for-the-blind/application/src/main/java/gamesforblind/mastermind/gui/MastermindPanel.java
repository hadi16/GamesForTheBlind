package gamesforblind.mastermind.gui;

import gamesforblind.mastermind.MastermindState;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

/**
 * Contains the main GUI code for Mastermind. Serves as a custom JPanel for Mastermind GUI (extends JPanel).
 */
public class MastermindPanel extends JPanel {
    private final MastermindState mastermindState;
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
     * Paints the Mastermind board where the colors will be placed.
     *
     * @param graphics        The {@link Graphics} object used for painting.
     * @param squareDimension The pixel dimension of each square on the board.
     * @param initialPosition Amount of pixels to begin painting board from (the row & column labels come before this).
     */
    private void paintMainBoard(@NotNull Graphics graphics, int squareDimension, int initialPosition) {
        int initialPositionY = initialPosition - (squareDimension / 2);

        // Draws border around box
        graphics.drawRect(
                initialPosition - 1,
                initialPositionY - 1,
                squareDimension * 4,
                squareDimension * 10
        );

        // Draws border
        graphics.drawRect(
                initialPosition + (squareDimension * 4) - 1,
                initialPositionY - 1,
                squareDimension,
                squareDimension * 10
        );

        for (int rowIndex = 0; rowIndex < 10; rowIndex++) {
            int yPosition = initialPositionY + rowIndex * squareDimension;

            for (int columnIndex = 0; columnIndex < 4; columnIndex++) {
                int xPosition = initialPosition + columnIndex * squareDimension;

                // Draw grid
                graphics.setColor(Color.BLACK);
                graphics.drawRect(xPosition, yPosition, squareDimension, squareDimension);
                System.out.println(xPosition + "  " + yPosition);
            }
        }
    }

    /**
     * Paints the Mastermind board where the previous results are shown.
     *
     * @param graphics         The {@link Graphics} object used for painting.
     * @param squareDimension  The pixel dimension of each square on the board.
     * @param initialPosition  Amount of pixels to begin painting board from (column labels come before this).
     * @param initialPositionY Amount of pixels to begin painting board from (the row labels come before this).
     */
    private void paintResultBoard(
            @NotNull Graphics graphics, int squareDimension, int initialPosition, int initialPositionY
    ) {
        for (int rowIndex = 0; rowIndex < 20; rowIndex++) {
            int yPosition = initialPositionY + rowIndex * squareDimension;

            for (int columnIndex = 0; columnIndex < 2; columnIndex++) {
                int xPosition = initialPosition + columnIndex * squareDimension;

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
     * @param initialPosition Amount of pixels to begin painting board from (column labels come before this).
     */
    private void paintBoardLabels(@NotNull Graphics graphics, int squareDimension, int initialPosition) {
        int initialPositionY = initialPosition - (squareDimension / 2);

        graphics.setColor(Color.BLACK);
        graphics.setFont(
                new Font("Arial", Font.BOLD, (93 - 7 * 10) * this.totalBoardLength / 390)
        );

        // Step 1: print the row labels (numbers 1, 2, 3, etc.)
        for (int rowIndex = 0; rowIndex < 10; rowIndex++) {
            graphics.drawString(
                    Integer.toString(rowIndex + 1),
                    initialPosition - (11 * squareDimension / 12),
                    initialPositionY + (squareDimension * rowIndex) + (4 * squareDimension / 7)
            );
        }

        // Step 2: print the column labels (letters 'A', 'B', 'C', etc.)
        for (int columnIndex = 0; columnIndex < 4; columnIndex++) {
            graphics.drawString(
                    Character.toString((char) columnIndex + 'A'),
                    initialPosition + squareDimension * columnIndex,
                    initialPositionY - (4 * squareDimension / 12)
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
        this.paintMainBoard(graphics, squareDimension, INITIAL_POSITION);

        //Step 2: paint small board that will display the result of previous guess
        this.paintResultBoard(
                graphics,
                squareDimension / 2,
                INITIAL_POSITION + (squareDimension * 4),
                INITIAL_POSITION - (squareDimension / 2)
        );

        // Step 3: paint the labels
        this.paintBoardLabels(graphics, squareDimension, INITIAL_POSITION);
    }
}

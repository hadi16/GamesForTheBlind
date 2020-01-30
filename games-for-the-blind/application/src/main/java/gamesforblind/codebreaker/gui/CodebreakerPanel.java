package gamesforblind.codebreaker.gui;

import gamesforblind.codebreaker.CodebreakerState;
import gamesforblind.enums.CodebreakerType;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

/**
 * Contains the main GUI code for Codebreaker. Serves as a custom JPanel for Codebreaker GUI (extends JPanel).
 */
public class CodebreakerPanel extends JPanel {
    private static final Color BRIGHT_BLUE = new Color(89, 202, 232);
    private static final Color BRIGHT_YELLOW = new Color(255, 247, 53);

    /**
     * The Codebreaker board state.
     */
    private final CodebreakerState codebreakerState;

    /**
     * Whether the Codebreaker game is a 4, 5, or 6 variant.
     */
    private final CodebreakerType codebreakerType;

    /**
     * This is reset every time the board is repainted (based on the bounds of the GUI window).
     */
    private int totalBoardLength;

    /**
     * Creates a new CodebreakerPanel.
     *
     * @param initialState The initial state of the Codebreaker game.
     */
    public CodebreakerPanel(@NotNull CodebreakerState initialState) {
        this.codebreakerType = initialState.getCodebreakerType();
        this.codebreakerState = initialState;
    }

    /**
     * Paints the Codebreaker board where the colors will be placed.
     *
     * @param graphics        The {@link Graphics} object used for painting.
     * @param squareDimension The pixel dimension of each square on the board.
     * @param initialPosition Amount of pixels to begin painting board from (the row & column labels come before this).
     */
    private void paintMainBoard(@NotNull Graphics graphics, int squareDimension, @NotNull Point initialPosition) {

        // Draws border around box
        Font myFont = new Font("Serif", Font.BOLD, 18);
        graphics.setFont(myFont);
        if(codebreakerType.getNumber() >4){
            graphics.drawRect(
                    initialPosition.x - 1,
                    initialPosition.y - 1,
                    squareDimension * codebreakerType.getNumber() + (2 * squareDimension) - (squareDimension/2) ,
                    squareDimension * 12
            );
            // Draws border
            graphics.drawRect(
                    initialPosition.x + (squareDimension * codebreakerType.getNumber()) -1,
                    initialPosition.y - 1,
                    squareDimension + (squareDimension/2),
                    squareDimension * 12
            );
        }
        else {
            graphics.drawRect(
                    initialPosition.x - 1,
                    initialPosition.y - 1,
                    squareDimension * codebreakerType.getNumber() + squareDimension,
                    squareDimension * 12
            );
            // Draws border
            graphics.drawRect(
                    initialPosition.x + (squareDimension * codebreakerType.getNumber()) - 1,
                    initialPosition.y - 1,
                    squareDimension,
                    squareDimension * 12
            );
        }

        for (int rowIndex = 0; rowIndex < 12; rowIndex++) {
            int yPosition = initialPosition.y + rowIndex * squareDimension;

            for (int columnIndex = 0; columnIndex < codebreakerType.getNumber(); columnIndex++) {
                int xPosition = initialPosition.x + columnIndex * squareDimension;

                // Draw grid
                graphics.setFont(myFont);
                graphics.drawRect(xPosition, yPosition, squareDimension, squareDimension);
            }
        }
    }

    /**
     * Paints the Codebreaker board where the previous results are shown.
     *
     * @param graphics        The {@link Graphics} object used for painting.
     * @param squareDimension The pixel dimension of each square on the board.
     * @param initialPosition Amount of pixels to begin painting board from (column & row labels come before this).
     */
    private void paintResultBoard(@NotNull Graphics graphics, int squareDimension, @NotNull Point initialPosition) {
        int columnNum = 3;
        if(codebreakerType.getNumber() == 4){
            columnNum = 2;
        }
        int counter = 0;
        for (int rowIndex = 0; rowIndex < 24; rowIndex++) {
            int yPosition = initialPosition.y + rowIndex * squareDimension;


            for (int columnIndex = 0; columnIndex < columnNum; columnIndex++) {
                int xPosition = initialPosition.x + columnIndex * squareDimension;
                if (codebreakerType.getNumber() == 5 && counter == 5){
                    counter = 0;
                    graphics.drawRect(xPosition, yPosition, squareDimension, squareDimension);
                    }
                else {
                    counter++;
                    // Draw square of grid
                    graphics.setColor(Color.BLACK);
                    graphics.drawRect(xPosition, yPosition, squareDimension, squareDimension);
                    graphics.drawRoundRect(xPosition, yPosition, squareDimension, squareDimension, squareDimension, squareDimension);
                }
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
        int rowLabel = 12;
        for (int rowIndex = 0; rowIndex < 12; rowIndex++) {
            graphics.drawString(
                    Integer.toString(rowLabel),
                    initialPosition.x - (squareDimension),
                    initialPosition.y + (squareDimension * rowIndex) + (3* squareDimension/4)
            );
            rowLabel--;
        }

        // Step 2: print the column labels (letters 'A', 'B', 'C', etc.)
        for (int columnIndex = 0; columnIndex < codebreakerType.getNumber(); columnIndex++) {
            graphics.drawString(
                    Character.toString((char) columnIndex + 'A'),
                    initialPosition.x + 10 + squareDimension * columnIndex,
                    initialPosition.y - (codebreakerType.getNumber() * squareDimension / 14)
            );
        }
    }

    /**
     * When repaint() or paint() is called, paints the Codebreaker GUI.
     * Might look into using comic sans as a font.
     *
     * @param graphics The {@link Graphics} object used for painting.
     */
    @Override
    protected void paintComponent(@NotNull Graphics graphics) {
        super.paintComponent(graphics);

        Rectangle clipBounds = graphics.getClipBounds();
        this.totalBoardLength = Math.min(clipBounds.height - 10 , clipBounds.width +5 );

        int squaresPerSide = 13; // 12 (for initial board) + 1
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
                new Point(INITIAL_POSITION + (squareDimension * codebreakerType.getNumber()), INITIAL_POSITION - (squareDimension / 2))
        );

        // Step 3: paint the labels
        this.paintBoardLabels(
                graphics, squareDimension, new Point(INITIAL_POSITION, INITIAL_POSITION - (squareDimension / 2))
        );
    }
}

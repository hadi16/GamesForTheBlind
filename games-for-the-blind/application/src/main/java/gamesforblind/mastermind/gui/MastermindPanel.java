package gamesforblind.mastermind.gui;

import gamesforblind.mastermind.MastermindState;
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
     * Paints the bolded block borders.
     *
     * @param graphics        The {@link Graphics} object used for painting.
     * @param squareDimension The pixel dimension of each square on the board.
     * @param initialPosition Amount of pixels to begin painting board from (the row & column labels come before this).
     */
    private void paintBoard(@NotNull Graphics graphics, int squareDimension, int initialPosition) {
        System.out.print("hello");

        final int BLOCK_WIDTH = 10;
        final int BLOCK_HEIGHT = 10;

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

     /** Paints the Mastermind board where the colors will be placed.
      *
      * @param graphics        The {@link Graphics} object used for painting.
      * @param squareDimension The pixel dimension of each square on the board.
      * @param initialPosition Amount of pixels to begin painting board from (the row & column labels come before this).
      */
    private void paintMainBoard(@NotNull Graphics graphics, int squareDimension, int initialPosition) {
        int initialPositionY = initialPosition - squareDimension;
        //draws border around box
        graphics.drawRect(
                initialPosition - 1,
                initialPositionY - 1,
                squareDimension * 4,
                 squareDimension * 10
        );

        //draws border
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


                //draw grid
                graphics.setColor(Color.BLACK);
                graphics.drawRect(xPosition, yPosition, squareDimension, squareDimension);
                System.out.println(xPosition+"  "+yPosition);
            }
        }
    }
    /** Paints the Mastermind board where the previous results are shown.
     *
     * @param graphics        The {@link Graphics} object used for painting.
     * @param squareDimension The pixel dimension of each square on the board.
     * @param initialPosition Amount of pixels to begin painting board from (the row & column labels come before this).
     */
    private void paintDecisionBoard(
                                    @NotNull Graphics graphics,
                                    int squareDimension,
                                    int initialPosition,
                                    int initialPositionY) {

        for (int rowIndex = 0; rowIndex < 20; rowIndex++) {
            int yPosition = initialPositionY  + rowIndex * squareDimension;

            for (int columnIndex = 0; columnIndex < 2; columnIndex++) {
                int xPosition = initialPosition + columnIndex * squareDimension;


                // Step 3: draw square of grid
                graphics.setColor(Color.BLACK);
                graphics.drawRect(xPosition, yPosition, squareDimension, squareDimension);
            }
        }
    }


    private void paintBoardLabels(@NotNull Graphics graphics, int squareDimension, int initialPosition) {
        int initialPositionY = initialPosition - squareDimension;

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
        int squareDimension = (((this.totalBoardLength - squaresPerSide) / squaresPerSide)/2)*2;

        final int INITIAL_POSITION = this.totalBoardLength -
                (squareDimension * squaresPerSide) + 3*(squareDimension/2) ;
        System.out.println(INITIAL_POSITION);




        // Step 1: paint the board, 4x10 grid currently
        this.paintMainBoard(graphics, squareDimension, INITIAL_POSITION );
        System.out.println("Dimensions 1: "+squareDimension+"Dimensions 2: " + squareDimension/2);
        this.paintDecisionBoard(
                graphics,
                squareDimension/2,
                INITIAL_POSITION + (squareDimension * 4),
                INITIAL_POSITION - squareDimension
        );

        // Step 2: paint the little board (with colors): Just currently there for show
        //this.paintColors(graphics, squareDimension, INITIAL_POSITION + squareDimension);

        // Step 3: paint the bolded block borders.
        this.paintBoardLabels(graphics, squareDimension, INITIAL_POSITION);
    }
}

package gamesforblind.codebreaker.gui;

import gamesforblind.codebreaker.CodebreakerGuess;
import gamesforblind.codebreaker.CodebreakerState;
import gamesforblind.enums.CodebreakerType;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Function;

import static gamesforblind.Constants.CODEBREAKER_MAX_AMOUNT_OF_GUESSES;

/**
 * Contains the main GUI code for Codebreaker. Serves as a custom JPanel for Codebreaker GUI (extends JPanel).
 */
public class CodebreakerPanel extends JPanel {
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
        final Font MAIN_BOARD_FONT = new Font("Serif", Font.BOLD, 50);
        graphics.setFont(MAIN_BOARD_FONT);

        final Function<Integer, Integer> GET_RECTANGLE_WIDTH = (width) -> {
            if (this.codebreakerType != CodebreakerType.FOUR) {
                return width + squareDimension / 2;
            }

            return width;
        };

        graphics.drawRect(
                initialPosition.x - 1,
                initialPosition.y - 1,
                GET_RECTANGLE_WIDTH.apply(squareDimension * this.codebreakerType.getCodeLength() + squareDimension),
                squareDimension * 12
        );

        graphics.drawRect(
                initialPosition.x + (squareDimension * this.codebreakerType.getCodeLength()) - 1,
                initialPosition.y - 1,
                GET_RECTANGLE_WIDTH.apply(squareDimension),
                squareDimension * 12
        );

        ArrayList<CodebreakerGuess> codebreakerGuesses = this.codebreakerState.getGuessList();
        Point selectedCellPoint = this.codebreakerState.getSelectedCellPoint();
        for (int rowIndex = 0; rowIndex < CODEBREAKER_MAX_AMOUNT_OF_GUESSES; rowIndex++) {
            int yPosition = initialPosition.y + rowIndex * squareDimension;

            Integer[] guessedCode = null;
            if (rowIndex <= codebreakerGuesses.size() - 1) {
                guessedCode = Arrays.stream(
                        codebreakerGuesses.get(rowIndex).getGuessedCode()
                ).boxed().toArray(Integer[]::new);
            } else if (rowIndex == codebreakerGuesses.size()) {
                guessedCode = this.codebreakerState.getCurrentGuess();
            }

            for (int columnIndex = 0; columnIndex < this.codebreakerType.getCodeLength(); columnIndex++) {
                int xPosition = initialPosition.x + columnIndex * squareDimension;

                graphics.setColor(Color.BLACK);
                graphics.drawRect(xPosition, yPosition, squareDimension, squareDimension);

                if (selectedCellPoint.y == rowIndex && selectedCellPoint.x == columnIndex) {
                    graphics.setColor(Color.GREEN);
                    graphics.fillRect(xPosition, yPosition, squareDimension, squareDimension);
                }

                if (guessedCode != null) {
                    graphics.setColor(Color.BLACK);

                    if (guessedCode[columnIndex] != null) {
                        graphics.drawString(
                                String.valueOf(guessedCode[columnIndex]),
                                xPosition + squareDimension / 3,
                                yPosition + 3 * squareDimension / 4
                        );
                    }
                }
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
        ArrayList<CodebreakerGuess> codebreakerGuesses = this.codebreakerState.getGuessList();

        final int NUMBER_OF_COLUMNS = (int) Math.ceil(Math.sqrt(this.codebreakerType.getCodeLength()));

        for (int guessIndex = 0; guessIndex < CODEBREAKER_MAX_AMOUNT_OF_GUESSES; guessIndex++) {
            CodebreakerGuess currentCodeGuess = null;
            Integer numberInCorrectPosition = null;
            Integer numberOfCorrectColor = null;

            if (guessIndex <= codebreakerGuesses.size() - 1) {
                currentCodeGuess = codebreakerGuesses.get(guessIndex);
                numberInCorrectPosition = currentCodeGuess.getNumberInCorrectPosition();
                numberOfCorrectColor = currentCodeGuess.getNumberOfCorrectColor();
            }

            for (int rowIndex = 0; rowIndex < 2; rowIndex++) {
                int yPosition = initialPosition.y + (2 * guessIndex + rowIndex) * squareDimension;

                for (int columnIndex = 0; columnIndex < NUMBER_OF_COLUMNS; columnIndex++) {
                    int xPosition = initialPosition.x + columnIndex * squareDimension;

                    // Draw square of codebreaker peg.
                    graphics.setColor(Color.BLACK);
                    graphics.drawRect(xPosition, yPosition, squareDimension, squareDimension);

                    // Special case: there is no sixth peg for the five type.
                    if (this.codebreakerType == CodebreakerType.FIVE) {
                        if (rowIndex % 2 == 1 && columnIndex == NUMBER_OF_COLUMNS - 1) {
                            continue;
                        }
                    }

                    graphics.drawRoundRect(
                            xPosition, yPosition, squareDimension, squareDimension, squareDimension, squareDimension
                    );

                    if (currentCodeGuess != null) {
                        if (numberInCorrectPosition > 0) {
                            graphics.setColor(Color.BLACK);
                            graphics.fillRoundRect(
                                    xPosition, yPosition, squareDimension, squareDimension, squareDimension, squareDimension
                            );
                            numberInCorrectPosition--;
                            continue;
                        }

                        if (numberOfCorrectColor > 0) {
                            graphics.setColor(Color.RED);
                            graphics.fillRoundRect(
                                    xPosition, yPosition, squareDimension, squareDimension, squareDimension, squareDimension
                            );
                            numberOfCorrectColor--;
                        }
                    }
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
    private void paintBoardLabels(@NotNull Graphics graphics, int squareDimension, @NotNull Point initialPosition) {
        graphics.setColor(Color.BLACK);
        graphics.setFont(
                new Font("Arial", Font.BOLD, (93 - 7 * 10) * this.totalBoardLength / 390)
        );

        // Step 1: print the row labels (numbers 1, 2, 3, etc.)
        for (int rowIndex = 0; rowIndex < CODEBREAKER_MAX_AMOUNT_OF_GUESSES; rowIndex++) {
            graphics.drawString(
                    Integer.toString(rowIndex),
                    initialPosition.x - (squareDimension),
                    initialPosition.y + (squareDimension * rowIndex) + (3 * squareDimension / 4)
            );
        }

        // Step 2: print the column labels (letters 'A', 'B', 'C', etc.)
        for (int columnIndex = 0; columnIndex < this.codebreakerType.getCodeLength(); columnIndex++) {
            graphics.drawString(
                    Character.toString((char) columnIndex + 'A'),
                    initialPosition.x + 10 + squareDimension * columnIndex,
                    initialPosition.y - (this.codebreakerType.getCodeLength() * squareDimension / 14)
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
        this.totalBoardLength = Math.min(clipBounds.height - 10, clipBounds.width + 5);

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
                new Point(
                        INITIAL_POSITION + (squareDimension * this.codebreakerType.getCodeLength()),
                        INITIAL_POSITION - (squareDimension / 2)
                )
        );

        // Step 3: paint the labels
        this.paintBoardLabels(
                graphics, squareDimension, new Point(INITIAL_POSITION, INITIAL_POSITION - (squareDimension / 2))
        );
    }
}

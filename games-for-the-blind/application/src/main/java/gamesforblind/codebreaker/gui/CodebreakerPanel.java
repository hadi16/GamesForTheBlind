package gamesforblind.codebreaker.gui;

import gamesforblind.codebreaker.CodebreakerGuess;
import gamesforblind.codebreaker.CodebreakerState;
import gamesforblind.enums.CodebreakerType;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static util.DurationUtil.*;

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
    private final JFrame popUpFrame = new JFrame("End of Game");
    private int mainBoardSquareDim = 0;
    private Point mainBoardInitialPoint = null;

    /**
     * Creates a new CodebreakerPanel.
     *
     * @param initialState The initial state of the Codebreaker game.
     */
    public CodebreakerPanel(@NotNull CodebreakerState initialState) {
        this.codebreakerType = initialState.getCodebreakerType();
        this.codebreakerState = initialState;
    }

    private int getSecondGroupXOffset(int squareDimension) {
        return squareDimension * this.codebreakerType.getCodeLength() + squareDimension * 3 + 1;
    }

    private boolean isSecondGroup(int index) {
        return index > 9;
    }

    private Point getMainBoardPoint(int rowIndex, int columnIndex) {
        boolean IS_SECOND_GROUP = this.isSecondGroup(rowIndex);

        int xPosition = this.mainBoardInitialPoint.x + columnIndex * this.mainBoardSquareDim;
        if (IS_SECOND_GROUP) {
            xPosition += this.getSecondGroupXOffset(this.mainBoardSquareDim);
        }

        int adjustedRowIndex = IS_SECOND_GROUP ? rowIndex % 10 : rowIndex;
        int yPosition = this.mainBoardInitialPoint.y + adjustedRowIndex * this.mainBoardSquareDim;

        return new Point(xPosition, yPosition);
    }

    public Optional<Point> getMouseSelectedPoint(Point mousePoint) {
        for (int rowIndex = 0; rowIndex < this.codebreakerType.getNumberOfRows(); rowIndex++) {
            for (int columnIndex = 0; columnIndex < this.codebreakerType.getCodeLength(); columnIndex++) {
                Point position = this.getMainBoardPoint(rowIndex, columnIndex);

                boolean xCorrect = mousePoint.x >= position.x && mousePoint.x < position.x + this.mainBoardSquareDim;
                boolean yCorrect = mousePoint.y >= position.y && mousePoint.y < position.y + 2 * this.mainBoardSquareDim;
                if (xCorrect && yCorrect) {
                    return Optional.of(new Point(columnIndex, rowIndex));
                }
            }
        }

        return Optional.empty();
    }

    /**
     * Paints the Codebreaker board where the colors will be placed.
     *
     * @param graphics The {@link Graphics} object used for painting.
     */
    private void paintMainBoard(@NotNull Graphics graphics) {
        final int CODE_LENGTH = this.codebreakerType.getCodeLength();
        final int SECOND_GROUP_X_OFFSET = this.getSecondGroupXOffset(this.mainBoardSquareDim);

        final Font MAIN_BOARD_FONT = new Font("Serif", Font.BOLD, 50);
        graphics.setFont(MAIN_BOARD_FONT);

        final Function<Integer, Integer> GET_RECTANGLE_WIDTH = (width) -> {
            if (this.codebreakerType != CodebreakerType.FOUR) {
                return width + this.mainBoardSquareDim / 2;
            }

            return width;
        };

        final int RECTANGLE_HEIGHT_FACTOR;
        switch (this.codebreakerType) {
            case FOUR:
                RECTANGLE_HEIGHT_FACTOR = 2;
                break;
            case FIVE:
                RECTANGLE_HEIGHT_FACTOR = 5;
                break;
            case SIX:
                RECTANGLE_HEIGHT_FACTOR = 10;
                break;
            default:
                throw new IllegalArgumentException("Invalid codebreaker type passed!");
        }

        final Duration timeElapsed = Duration.between(this.codebreakerState.getTime(), Instant.now());
        final int hoursElapsed = toHoursPart(timeElapsed);
        final int minutesElapsed = toMinutesPart(timeElapsed);
        final int secondsElapsed = toSecondsPart(timeElapsed);

        graphics.drawString(
                String.format("Time: %d:%d:%d", hoursElapsed, minutesElapsed, secondsElapsed),
                this.mainBoardInitialPoint.x + 2 * SECOND_GROUP_X_OFFSET,
                this.mainBoardInitialPoint.y + SECOND_GROUP_X_OFFSET
        );

        graphics.drawRect(
                this.mainBoardInitialPoint.x - 1,
                this.mainBoardInitialPoint.y - 1,
                GET_RECTANGLE_WIDTH.apply(this.mainBoardSquareDim * CODE_LENGTH + this.mainBoardSquareDim),
                10 * this.mainBoardSquareDim
        );

        graphics.drawRect(
                this.mainBoardInitialPoint.x + SECOND_GROUP_X_OFFSET,
                this.mainBoardInitialPoint.y - 1,
                GET_RECTANGLE_WIDTH.apply(this.mainBoardSquareDim * CODE_LENGTH + this.mainBoardSquareDim),
                RECTANGLE_HEIGHT_FACTOR * this.mainBoardSquareDim
        );

        graphics.drawRect(
                this.mainBoardInitialPoint.x + (this.mainBoardSquareDim * CODE_LENGTH) - 1,
                this.mainBoardInitialPoint.y - 1,
                GET_RECTANGLE_WIDTH.apply(this.mainBoardSquareDim),
                this.mainBoardSquareDim * 10
        );

        graphics.drawRect(
                this.mainBoardInitialPoint.x + (this.mainBoardSquareDim * CODE_LENGTH) - 1 + SECOND_GROUP_X_OFFSET,
                this.mainBoardInitialPoint.y - 1,
                GET_RECTANGLE_WIDTH.apply(this.mainBoardSquareDim),
                RECTANGLE_HEIGHT_FACTOR * this.mainBoardSquareDim
        );

        ArrayList<CodebreakerGuess> guesses = this.codebreakerState.getGuessList();
        Point selectedCellPoint = this.codebreakerState.getSelectedCellPoint();

        for (int rowIndex = 0; rowIndex < this.codebreakerType.getNumberOfRows(); rowIndex++) {
            Integer[] guessedCode = null;
            if (rowIndex <= guesses.size() - 1) {
                guessedCode = Arrays.stream(guesses.get(rowIndex).getGuessedCode()).boxed().toArray(Integer[]::new);
            } else if (rowIndex == guesses.size()) {
                guessedCode = this.codebreakerState.getCurrentGuess();
            }

            for (int columnIndex = 0; columnIndex < CODE_LENGTH; columnIndex++) {
                final Point position = this.getMainBoardPoint(rowIndex, columnIndex);

                graphics.setColor(Color.BLACK);
                graphics.drawRect(position.x, position.y, this.mainBoardSquareDim, this.mainBoardSquareDim);

                if (selectedCellPoint.y == rowIndex && selectedCellPoint.x == columnIndex) {
                    graphics.setColor(Color.GREEN);
                    graphics.fillRect(position.x, position.y, this.mainBoardSquareDim, this.mainBoardSquareDim);
                }

                if (guessedCode != null) {
                    graphics.setColor(Color.BLACK);

                    if (guessedCode[columnIndex] != null) {
                        graphics.drawString(
                                String.valueOf(guessedCode[columnIndex]),
                                position.x + this.mainBoardSquareDim / 3,
                                position.y + 3 * this.mainBoardSquareDim / 4
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
        final ArrayList<CodebreakerGuess> codebreakerGuesses = this.codebreakerState.getGuessList();
        final int CODE_LENGTH = this.codebreakerType.getCodeLength();
        final int NUMBER_OF_COLUMNS = (int) Math.ceil(Math.sqrt(CODE_LENGTH));

        CodebreakerGuess currentCodeGuess = null;
        Integer numberInCorrectPosition = null;
        Integer numberOfCorrectColor = null;

        for (int guessIndex = 0; guessIndex < this.codebreakerType.getNumberOfRows(); guessIndex++) {
            final boolean IS_SECOND_GROUP = this.isSecondGroup(guessIndex);

            if (guessIndex <= codebreakerGuesses.size() - 1) {
                currentCodeGuess = codebreakerGuesses.get(guessIndex);
                numberInCorrectPosition = currentCodeGuess.getNumberInCorrectPosition();
                numberOfCorrectColor = currentCodeGuess.getNumberOfCorrectColor();
            }

            int numberOfDrawnPegs = 0;
            for (int rowIndex = 0; rowIndex < 2; rowIndex++) {
                int adjustedGuessIndex = IS_SECOND_GROUP ? guessIndex % 10 : guessIndex;
                int yPosition = initialPosition.y + (2 * adjustedGuessIndex + rowIndex) * squareDimension;

                int xPosition;
                for (int columnIndex = 0; columnIndex < NUMBER_OF_COLUMNS; columnIndex++, numberOfDrawnPegs++) {
                    xPosition = initialPosition.x + columnIndex * squareDimension;
                    if (IS_SECOND_GROUP) {
                        if (this.codebreakerType == CodebreakerType.FOUR) {
                            xPosition += squareDimension * CODE_LENGTH + squareDimension * 10 + 1;
                        } else if (this.codebreakerType == CodebreakerType.FIVE) {
                            xPosition += (squareDimension * CODE_LENGTH) * 3 + squareDimension;
                        } else {
                            xPosition += (squareDimension * CODE_LENGTH) * 3;
                        }
                    }

                    // Draw square of codebreaker peg.
                    graphics.setColor(Color.BLACK);
                    graphics.drawRect(xPosition, yPosition, squareDimension, squareDimension);

                    // Don't want to draw too many pegs.
                    if (this.codebreakerType.getCodeLength() == numberOfDrawnPegs) {
                        continue;
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

    private void paintCode(int totalBoardLength) {
        final ArrayList<CodebreakerGuess> guessList = this.codebreakerState.getGuessList();
        final CodebreakerGuess lastGuess = guessList.get(guessList.size() - 1);

        final Font font = new Font("Arial", Font.BOLD, (93 - 7 * 10) * totalBoardLength / 490);

        final JLabel resultLabel;
        if (this.codebreakerType.getCodeLength() == lastGuess.getNumberInCorrectPosition()) {
            resultLabel = new JLabel("Congrats! You guessed the correct code!");
        } else {
            final int[] codeToBreak = this.codebreakerState.getCodeToBreak();
            final String correctCode = IntStream.of(codeToBreak)
                    .mapToObj(String::valueOf)
                    .collect(Collectors.joining());
            resultLabel = new JLabel("Correct code: " + correctCode);
        }

        final Duration timeElapsed = this.codebreakerState.getTimeElapsed();
        final JLabel timeLabel = new JLabel(String.format("Time: %d seconds", timeElapsed.getSeconds()));

        resultLabel.setFont(font);
        timeLabel.setFont(font);

        this.popUpFrame.setSize(totalBoardLength, totalBoardLength / 3);

        // Create a popup
        JPanel popUpPanel = new JPanel();
        popUpPanel.add(resultLabel);
        Popup popup = new PopupFactory().getPopup(
                this.popUpFrame, popUpPanel, totalBoardLength / 2, totalBoardLength / 2
        );

        popUpPanel.add(timeLabel);

        this.popUpFrame.add(popUpPanel);
        this.popUpFrame.setVisible(true);
        this.popUpFrame.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    CodebreakerPanel.this.popUpFrame.setVisible(false);
                    CodebreakerPanel.this.popUpFrame.dispose();
                }
            }

            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });

        popup.show();
    }

    /**
     * Paints the  labels along the left and top edges of the board
     *
     * @param graphics        The {@link Graphics} object used for painting.
     * @param squareDimension The pixel dimension of each square on the board.
     * @param initialPosition Amount of pixels to begin painting board from (row & column labels come before this).
     */
    private void paintBoardLabels(
            @NotNull Graphics graphics, int squareDimension, @NotNull Point initialPosition, int totalBoardLength
    ) {
        final int CODE_LENGTH = this.codebreakerType.getCodeLength();
        final int NUMBER_OF_ROWS = this.codebreakerType.getNumberOfRows();

        graphics.setColor(Color.BLACK);
        graphics.setFont(
                new Font("Arial", Font.BOLD, (93 - 7 * 10) * totalBoardLength / 390)
        );

        // Step 1: print the row labels (numbers r1, r2, r3, etc.)
        for (int rowIndex = 0; rowIndex < 10; rowIndex++) {
            graphics.drawString(
                    ("r" + (rowIndex + 1)),
                    initialPosition.x - (squareDimension) - (squareDimension / 4),
                    initialPosition.y + (squareDimension * rowIndex) + (3 * squareDimension / 4)
            );
        }

        for (int rowIdx = 10, secondRowIdx = 0; rowIdx < NUMBER_OF_ROWS; rowIdx++, secondRowIdx++) {
            graphics.drawString(
                    ("r" + (rowIdx + 1)),
                    initialPosition.x + squareDimension * CODE_LENGTH + squareDimension * 2 + 1 - (squareDimension / 3),
                    initialPosition.y + (squareDimension * secondRowIdx) + (3 * squareDimension / 4)
            );
        }

        // Step 2: print the column labels (letters 'c1', 'c2', 'c3', etc.)
        for (int columnIndex = 0; columnIndex < CODE_LENGTH; columnIndex++) {
            graphics.drawString(
                    ("c" + (columnIndex + 1)),
                    initialPosition.x + 5 + squareDimension * columnIndex,
                    initialPosition.y - (CODE_LENGTH * squareDimension / 14)
            );
        }

        final int X_OFFSET = initialPosition.x + this.getSecondGroupXOffset(squareDimension);
        for (int columnIndex = 0; columnIndex < CODE_LENGTH; columnIndex++) {
            graphics.drawString(
                    ("c" + (columnIndex + 1)),
                    X_OFFSET + 5 + squareDimension * columnIndex,
                    initialPosition.y - (CODE_LENGTH * squareDimension / 14)
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

        final int TOTAL_BOARD_LENGTH = Math.min(clipBounds.height - 10, clipBounds.width + 20);
        final int SQUARES_PER_SIDE = 13; // 12 (for initial board) + 1
        final int SQUARE_DIM = (((TOTAL_BOARD_LENGTH - SQUARES_PER_SIDE) / SQUARES_PER_SIDE) / 2) * 2;
        final int INITIAL_POSITION = TOTAL_BOARD_LENGTH - (SQUARE_DIM * SQUARES_PER_SIDE) + 3 * (SQUARE_DIM / 2);

        this.mainBoardInitialPoint = new Point(INITIAL_POSITION, INITIAL_POSITION - (SQUARE_DIM / 2));
        this.mainBoardSquareDim = SQUARE_DIM;

        // Step 1: paint the board, 4x10 grid currently
        this.paintMainBoard(graphics);

        //Step 2: paint small board that will display the result of previous guess
        this.paintResultBoard(
                graphics,
                SQUARE_DIM / 2,
                new Point(
                        INITIAL_POSITION + (SQUARE_DIM * this.codebreakerType.getCodeLength()),
                        INITIAL_POSITION - (SQUARE_DIM / 2)
                )
        );

        // Step 3: paint the labels
        this.paintBoardLabels(
                graphics,
                SQUARE_DIM,
                new Point(INITIAL_POSITION, INITIAL_POSITION - (SQUARE_DIM / 2)),
                TOTAL_BOARD_LENGTH
        );

        if (this.codebreakerState.isGameOver()) {
            this.paintCode(TOTAL_BOARD_LENGTH);
        }
    }

    public JFrame getPopUpFrame() {
        return this.popUpFrame;
    }
}

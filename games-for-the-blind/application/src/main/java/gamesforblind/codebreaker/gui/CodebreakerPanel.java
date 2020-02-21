package gamesforblind.codebreaker.gui;

import gamesforblind.codebreaker.CodebreakerGuess;
import gamesforblind.codebreaker.CodebreakerState;
import gamesforblind.enums.CodebreakerType;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Function;

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

    private final Function<Integer, Integer> getSecondGroupXOffset;
    private final Function<Integer, Boolean> isSecondGroup = (index) -> index > 9;

    /**
     * This is reset every time the board is repainted (based on the bounds of the GUI window).
     */
    private int totalBoardLength;
    private JFrame popUpFrame = new JFrame("End of Game");

    /**
     * Creates a new CodebreakerPanel.
     *
     * @param initialState The initial state of the Codebreaker game.
     */
    public CodebreakerPanel(@NotNull CodebreakerState initialState) {
        this.codebreakerType = initialState.getCodebreakerType();
        this.codebreakerState = initialState;

        this.getSecondGroupXOffset = (squareDimension) -> {
            return squareDimension * this.codebreakerType.getCodeLength() + squareDimension * 3 + 1;
        };
    }

    /**
     * Paints the Codebreaker board where the colors will be placed.
     *
     * @param graphics        The {@link Graphics} object used for painting.
     * @param squareDimension The pixel dimension of each square on the board.
     * @param initialPosition Amount of pixels to begin painting board from (the row & column labels come before this).
     */
    private void paintMainBoard(@NotNull Graphics graphics, int squareDimension, @NotNull Point initialPosition) {
        final int CODE_LENGTH = this.codebreakerType.getCodeLength();
        final int SECOND_GROUP_X_OFFSET = this.getSecondGroupXOffset.apply(squareDimension);

        final Font MAIN_BOARD_FONT = new Font("Serif", Font.BOLD, 50);
        graphics.setFont(MAIN_BOARD_FONT);

        final Function<Integer, Integer> GET_RECTANGLE_WIDTH = (width) -> {
            if (this.codebreakerType != CodebreakerType.FOUR) {
                return width + squareDimension / 2;
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

        graphics.drawRect(
                initialPosition.x - 1,
                initialPosition.y - 1,
                GET_RECTANGLE_WIDTH.apply(squareDimension * CODE_LENGTH + squareDimension),
                10 * squareDimension
        );

        graphics.drawRect(
                initialPosition.x + SECOND_GROUP_X_OFFSET,
                initialPosition.y - 1,
                GET_RECTANGLE_WIDTH.apply(squareDimension * CODE_LENGTH + squareDimension),
                RECTANGLE_HEIGHT_FACTOR * squareDimension
        );

        graphics.drawRect(
                initialPosition.x + (squareDimension * CODE_LENGTH) - 1,
                initialPosition.y - 1,
                GET_RECTANGLE_WIDTH.apply(squareDimension),
                squareDimension * 10
        );

        graphics.drawRect(
                initialPosition.x + (squareDimension * CODE_LENGTH) - 1 + SECOND_GROUP_X_OFFSET,
                initialPosition.y - 1,
                GET_RECTANGLE_WIDTH.apply(squareDimension),
                RECTANGLE_HEIGHT_FACTOR * squareDimension
        );

        ArrayList<CodebreakerGuess> guesses = this.codebreakerState.getGuessList();
        Point selectedCellPoint = this.codebreakerState.getSelectedCellPoint();

        for (int rowIndex = 0; rowIndex < this.codebreakerType.getNumberOfRows(); rowIndex++) {
            final boolean IS_SECOND_GROUP = this.isSecondGroup.apply(rowIndex);

            int adjustedRowIndex = IS_SECOND_GROUP ? rowIndex % 10 : rowIndex;
            int yPosition = initialPosition.y + adjustedRowIndex * squareDimension;

            Integer[] guessedCode = null;
            if (rowIndex <= guesses.size() - 1) {
                guessedCode = Arrays.stream(guesses.get(rowIndex).getGuessedCode()).boxed().toArray(Integer[]::new);
            } else if (rowIndex == guesses.size()) {
                guessedCode = this.codebreakerState.getCurrentGuess();
            }

            int xPosition;
            for (int columnIndex = 0; columnIndex < CODE_LENGTH; columnIndex++) {
                xPosition = initialPosition.x + columnIndex * squareDimension;
                if (IS_SECOND_GROUP) {
                    xPosition += SECOND_GROUP_X_OFFSET;
                }

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
        final ArrayList<CodebreakerGuess> codebreakerGuesses = this.codebreakerState.getGuessList();
        final int CODE_LENGTH = this.codebreakerType.getCodeLength();
        final int NUMBER_OF_COLUMNS = (int) Math.ceil(Math.sqrt(CODE_LENGTH));

        CodebreakerGuess currentCodeGuess = null;
        Integer numberInCorrectPosition = null;
        Integer numberOfCorrectColor = null;

        for (int guessIndex = 0; guessIndex < this.codebreakerType.getNumberOfRows(); guessIndex++) {
            final boolean IS_SECOND_GROUP = this.isSecondGroup.apply(guessIndex);

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

    public void paintCode(){
        JPanel popUpPanel = new JPanel();
        JLabel l;

        int [] code = codebreakerState.getCodeToBreak();
        String str = "";
        //convert int[] to string and append together
        for (int i =0; i < codebreakerType.getCodeLength(); i++){
            String newNum = Integer.toString(code[i]);
            str = str + newNum;
        }

        // create a label
        if(codebreakerState.getCodeToBreak().length==codebreakerState.getGuessList().get(codebreakerState.getGuessList().size() - 1).getNumberInCorrectPosition()){
            l = new JLabel("Congrats! You guessed the correct code!" );
            l.setFont( new Font("Arial", Font.BOLD, (93 - 7 * 10) * this.totalBoardLength / 490));
        }
        else {
            l = new JLabel("Correct code: " + str);
            l.setFont(new Font("Arial", Font.BOLD, (93 - 7 * 10) * this.totalBoardLength / 390));
        }

        popUpFrame.setSize(this.totalBoardLength , this.totalBoardLength/3);

        PopupFactory pf = new PopupFactory();

        popUpPanel.add(l);

        // create a popup
        Popup p = pf.getPopup(popUpFrame, popUpPanel, this.totalBoardLength/2, this.totalBoardLength/2);

        popUpFrame.add(popUpPanel);
        popUpFrame.show();
        p.show();

        // Add and define the KeyListener here!
        popUpFrame.addKeyListener(new KeyListener(){

            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_SPACE){
                    popUpFrame.setVisible(false);
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });


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
                    ("r" + Integer.toString(rowIndex + 1)),
                    initialPosition.x - (squareDimension)- (squareDimension/4),
                    initialPosition.y + (squareDimension * rowIndex) + (3 * squareDimension / 4)
            );
        }

        for (int rowIdx = 10, secondRowIdx = 0; rowIdx < NUMBER_OF_ROWS; rowIdx++, secondRowIdx++) {
            graphics.drawString(
                    ("r" + Integer.toString(rowIdx + 1)),
                    initialPosition.x + squareDimension * CODE_LENGTH + squareDimension * 2 + 1 -(squareDimension/3),
                    initialPosition.y + (squareDimension * secondRowIdx) + (3 * squareDimension / 4)
            );
        }

        // Step 2: print the column labels (letters 'c1', 'c2', 'c3', etc.)
        for (int columnIndex = 0; columnIndex < CODE_LENGTH; columnIndex++) {
            graphics.drawString(
                    ("c" + Integer.toString(columnIndex + 1)),
                    initialPosition.x +5 + squareDimension * columnIndex,
                    initialPosition.y - (CODE_LENGTH * squareDimension / 14)
            );
        }

        final int X_OFFSET = initialPosition.x + this.getSecondGroupXOffset.apply(squareDimension);
        for (int columnIndex = 0; columnIndex < CODE_LENGTH; columnIndex++) {
            graphics.drawString(
                    ("c" + Integer.toString(columnIndex + 1)),
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

        // Step 1: paint the board, 4x10 grid currently
        this.paintMainBoard(
                graphics, SQUARE_DIM, new Point(INITIAL_POSITION, INITIAL_POSITION - (SQUARE_DIM / 2))
        );

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

        if(codebreakerState.isGameOver() == true){
            paintCode();

        }
    }

    public JFrame getPopUpFrame(){return this.popUpFrame;}
}

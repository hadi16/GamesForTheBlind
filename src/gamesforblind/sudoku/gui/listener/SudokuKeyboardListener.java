package gamesforblind.sudoku.gui.listener;

import gamesforblind.enums.InputType;
import gamesforblind.enums.SudokuSection;
import gamesforblind.enums.SudokuType;
import gamesforblind.sudoku.SudokuGame;
import gamesforblind.sudoku.action.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Map;

/**
 * Sudoku keyboard listener class that will receive keyboard inputs and appropriately call the proper
 * action class
 */
public class SudokuKeyboardListener implements KeyListener {
    //potentially create the ability to choose own keys for each box later in project
    private static final Map<Integer, Map<Character, Point>> BOARD_SIZE_TO_CHAR_TO_POINT = Map.of(
            4, Map.of(
                    'S', new Point(0, 0),
                    'D', new Point(1, 0),
                    'X', new Point(0, 1),
                    'C', new Point(1, 1)
            ),
            9, Map.of(
                    'W', new Point(0, 0),
                    'E', new Point(1, 0),
                    'R', new Point(2, 0),
                    'S', new Point(0, 1),
                    'D', new Point(1, 1),
                    'F', new Point(2, 1),
                    'X', new Point(0, 2),
                    'C', new Point(1, 2),
                    'V', new Point(2, 2)
            )
    );

    private final SudokuGame sudokuGame;
    private final Map<Character, Point> charToPoint;

    public SudokuKeyboardListener(SudokuGame sudokuGame, SudokuType sudokuType) {
        int sudokuBoardSize = sudokuType.getSudokuBoardSize();

        if (!BOARD_SIZE_TO_CHAR_TO_POINT.containsKey(sudokuBoardSize)) {
            //gives exception for board selection size (4,9)
            throw new IllegalArgumentException(
                    "Improper Sudoku board size passed to keyboard listener: " + sudokuBoardSize
            );
        }

        this.sudokuGame = sudokuGame;
        this.charToPoint = BOARD_SIZE_TO_CHAR_TO_POINT.get(sudokuBoardSize);
    }

    /**
     * used to receive an input key, checks to make sure it's a valid input
     */
    @Override
    public void keyPressed(KeyEvent e) {
        //makes sure input is a number
        if (Character.isDigit(e.getKeyChar())) {
            //calls fill action to input the number, this will call the action to make sure its a valid move
            this.sudokuGame.receiveAction(
                    new SudokuFillAction(Character.getNumericValue(e.getKeyChar()))
            );
            return;
        }

        //for mapped key check,
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            //highlights the correct box for the inputted mapped key
            this.sudokuGame.receiveAction(
                    new SudokuHighlightAction(null, InputType.KEYBOARD)
            );
            return;
        }

        //for unknown key inputs to send an error
        char selectedKeyChar = Character.toUpperCase(e.getKeyChar());

        if (selectedKeyChar == 'I') {
            this.sudokuGame.receiveAction(new SudokuInstructionsAction());
            return;
        }
        if (selectedKeyChar == 'H') {
            this.sudokuGame.receiveAction(new SudokuHintKeyAction());
            return;
        }

        Point currentSelectedPoint = this.charToPoint.get(selectedKeyChar);
        if (currentSelectedPoint == null) {
            // Reads the entire row that the player is in
            final Map<Character, SudokuSection> KEY_TO_SECTION = Map.of(
                    'J', SudokuSection.ROW,
                    'K', SudokuSection.COLUMN,
                    'L', SudokuSection.BLOCK
            );

            SudokuSection sudokuSection = KEY_TO_SECTION.get(selectedKeyChar);
            if (sudokuSection != null) {
                this.sudokuGame.receiveAction(new SudokuReadPositionAction(sudokuSection));
                return;
            }

            this.sudokuGame.receiveAction(new SudokuUnrecognizedKeyAction(e.getKeyCode()));
            return;
        }

        //sends the action of the above accepted if statement
        this.sudokuGame.receiveAction(
                new SudokuHighlightAction(currentSelectedPoint, InputType.KEYBOARD)
        );
    }

    //honestly no idea lol
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}

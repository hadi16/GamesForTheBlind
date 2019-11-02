package gamesforblind.sudoku.gui.listener;

import gamesforblind.enums.InputType;
import gamesforblind.enums.SudokuSection;
import gamesforblind.sudoku.SudokuGame;
import gamesforblind.sudoku.action.*;
import gamesforblind.sudoku.interfaces.SudokuKeyboardInterface;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Map;

/**
 * Sudoku keyboard listener class that will receive keyboard inputs and appropriately call the proper
 * action class
 */
public class SudokuKeyboardListener implements KeyListener {
    private final SudokuGame sudokuGame;
    private final Map<Integer, Point> keyCodeToPoint;

    public SudokuKeyboardListener(SudokuKeyboardInterface keyboardInterface, SudokuGame sudokuGame) {
        this.sudokuGame = sudokuGame;
        this.keyCodeToPoint = keyboardInterface.getKeyCodeToPointMapping();
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

        //for mapped key check
        int selectedKeyCode = e.getKeyCode();
        if (selectedKeyCode == KeyEvent.VK_SPACE) {
            //highlights the correct box for the inputted mapped key
            this.sudokuGame.receiveAction(
                    new SudokuHighlightAction(null, InputType.KEYBOARD)
            );
            return;
        }

        if (selectedKeyCode == KeyEvent.VK_I) {
            this.sudokuGame.receiveAction(new SudokuInstructionsAction());
            return;
        }

        if (selectedKeyCode == KeyEvent.VK_H) {
            this.sudokuGame.receiveAction(new SudokuHintKeyAction());
            return;
        }

        Point currentSelectedPoint = this.keyCodeToPoint.get(selectedKeyCode);
        if (currentSelectedPoint != null) {
            this.sudokuGame.receiveAction(
                    new SudokuHighlightAction(currentSelectedPoint, InputType.KEYBOARD)
            );
            return;
        }

        // Reads the entire row that the player is in
        final Map<Integer, SudokuSection> KEY_TO_SECTION = Map.of(
                KeyEvent.VK_J, SudokuSection.ROW,
                KeyEvent.VK_K, SudokuSection.COLUMN,
                KeyEvent.VK_L, SudokuSection.BLOCK
        );

        SudokuSection sudokuSection = KEY_TO_SECTION.get(selectedKeyCode);
        if (sudokuSection != null) {
            this.sudokuGame.receiveAction(new SudokuReadPositionAction(sudokuSection));
            return;
        }

        this.sudokuGame.receiveAction(new SudokuUnrecognizedKeyAction(e.getKeyCode()));
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}

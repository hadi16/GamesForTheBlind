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
 * Keyboard listener for Sudoku that receives keyboard inputs and appropriately calls the proper action class
 */
public class SudokuKeyboardListener implements KeyListener {
    /**
     * The current Sudoku game.
     */
    private final SudokuGame sudokuGame;

    /**
     * A mapping between key codes & corresponding {@link Point}s. Used to have multiple keyboard interfaces.
     */
    private final Map<Integer, Point> keyCodeToPoint;

    /**
     * Creates a new SudokuKeyboardListener
     *
     * @param keyboardInterface The given keyboard interface that the user has chosen (arrow keys, etc.).
     * @param sudokuGame        The current Sudoku game.
     */
    public SudokuKeyboardListener(SudokuKeyboardInterface keyboardInterface, SudokuGame sudokuGame) {
        this.sudokuGame = sudokuGame;
        this.keyCodeToPoint = keyboardInterface.getKeyCodeToPointMapping();
    }

    /**
     * Triggered when a key is pressed in the Sudoku game.
     *
     * @param e The event that was triggered by the key press.
     */
    @Override
    public void keyPressed(KeyEvent e) {
        // Case 1: pressed key is a number.
        if (Character.isDigit(e.getKeyChar())) {
            // Calls FillAction to input the number (the state will determine whether it is a valid action).
            this.sudokuGame.receiveAction(
                    new SudokuFillAction(Character.getNumericValue(e.getKeyChar()))
            );
            return;
        }

        int selectedKeyCode = e.getKeyCode();

        // Case 2: the selected key is the SPACE BAR
        if (selectedKeyCode == KeyEvent.VK_SPACE) {
            // If it is a SPACE BAR, then pass null to the pointToHighlight.
            this.sudokuGame.receiveAction(
                    new SudokuHighlightAction(null, InputType.KEYBOARD)
            );
            return;
        }

        // Case 3: the selected key is the 'I' key (play the instructions).
        if (selectedKeyCode == KeyEvent.VK_I) {
            this.sudokuGame.receiveAction(new SudokuInstructionsAction());
            return;
        }

        // Case 4: the selected key is the 'H' key (triggers a hint).
        if (selectedKeyCode == KeyEvent.VK_H) {
            this.sudokuGame.receiveAction(new SudokuHintKeyAction());
            return;
        }

        // Case 5: the selected key exists within the passed mapping of key codes & Points.
        Point currentSelectedPoint = this.keyCodeToPoint.get(selectedKeyCode);
        if (currentSelectedPoint != null) {
            this.sudokuGame.receiveAction(
                    new SudokuHighlightAction(currentSelectedPoint, InputType.KEYBOARD)
            );
            return;
        }

        final Map<Integer, SudokuSection> KEY_TO_SECTION = Map.of(
                KeyEvent.VK_J, SudokuSection.ROW,
                KeyEvent.VK_K, SudokuSection.COLUMN,
                KeyEvent.VK_L, SudokuSection.BLOCK
        );

        // Case 6: the selected key is a J, K, or L (read row, column, or block, respectively).
        SudokuSection sudokuSection = KEY_TO_SECTION.get(selectedKeyCode);
        if (sudokuSection != null) {
            this.sudokuGame.receiveAction(new SudokuReadPositionAction(sudokuSection));
            return;
        }

        // Case 7: the selected key is unrecognized.
        this.sudokuGame.receiveAction(new SudokuUnrecognizedKeyAction(e.getKeyCode()));
    }

    /* These methods are required to be overridden by the KeyListener, but they are unused. */
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}

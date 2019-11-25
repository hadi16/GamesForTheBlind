package gamesforblind.sudoku.gui.listener;

import gamesforblind.enums.InputType;
import gamesforblind.enums.SudokuSection;
import gamesforblind.sudoku.SudokuGame;
import gamesforblind.sudoku.action.*;
import gamesforblind.sudoku.interfaces.SudokuKeyboardInterface;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
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
     * Mapping between key codes (as defined in {@link KeyEvent}) to hot key actions.
     * Note: the CTRL key must be pressed down to trigger any hot key.
     */
    private final Map<Integer, SudokuHotKeyAction> keyCodeToHotKeyAction;

    /**
     * For returning to main menu or restarting the game, I want to make sure that the user pressed the given key TWICE.
     */
    private final ArrayList<Integer> pressedKeyCodeList = new ArrayList<>();

    /**
     * Creates a new SudokuKeyboardListener
     *
     * @param keyboardInterface The given keyboard interface that the user has chosen (arrow keys, etc.).
     * @param sudokuGame        The current Sudoku game.
     */
    public SudokuKeyboardListener(@NotNull SudokuKeyboardInterface keyboardInterface, @NotNull SudokuGame sudokuGame) {
        this.sudokuGame = sudokuGame;
        this.keyCodeToPoint = keyboardInterface.getKeyCodeToPointMapping();
        this.keyCodeToHotKeyAction = keyboardInterface.keyCodeToHotKeyAction;
    }

    /**
     * Triggered when a key is pressed in the Sudoku game.
     *
     * @param e The event that was triggered by the key press.
     */
    @Override
    public void keyPressed(@NotNull KeyEvent e) {
        int selectedKeyCode = e.getKeyCode();
        this.pressedKeyCodeList.add(selectedKeyCode);

        // Case 1: hot key was registered (using the Ctrl key).
        // If no hot key mappings exist, don't check (e.g. block selection interface).
        if (!this.keyCodeToHotKeyAction.isEmpty() && e.isControlDown()) {
            SudokuHotKeyAction sudokuHotKeyAction = this.keyCodeToHotKeyAction.get(e.getKeyCode());
            if (sudokuHotKeyAction != null) {
                this.sudokuGame.receiveAction(sudokuHotKeyAction);
            }

            // Prevents the game from saying that CTRL is an unrecognized key.
            return;
        }

        // Case 2: pressed key is a number.
        if (Character.isDigit(e.getKeyChar())) {
            // Calls FillAction to input the number (the state will determine whether it is a valid action).
            this.sudokuGame.receiveAction(
                    new SudokuFillAction(Character.getNumericValue(e.getKeyChar()))
            );
            return;
        }

        // Case 3: the selected key is the SPACE BAR
        if (selectedKeyCode == KeyEvent.VK_SPACE) {
            // If it is a SPACE BAR, then pass null to the pointToHighlight.
            this.sudokuGame.receiveAction(
                    new SudokuHighlightAction(null, InputType.KEYBOARD)
            );
            return;
        }

        // Case 4: the selected key is the 'I' key (play the instructions).
        if (selectedKeyCode == KeyEvent.VK_I) {
            this.sudokuGame.receiveAction(new SudokuInstructionsAction());
            return;
        }

        // Case 5: the selected key is the 'H' key (triggers a hint).
        if (selectedKeyCode == KeyEvent.VK_H) {
            this.sudokuGame.receiveAction(new SudokuHintKeyAction());
            return;
        }

        // Case 6: the selected key exists within the passed mapping of key codes & Points.
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

        // Case 7: the selected key is a J, K, or L (read row, column, or block, respectively).
        SudokuSection sudokuSection = KEY_TO_SECTION.get(selectedKeyCode);
        if (sudokuSection != null) {
            this.sudokuGame.receiveAction(new SudokuReadPositionAction(sudokuSection));
            return;
        }

        int numberOfPressedKeys = this.pressedKeyCodeList.size();

        // Case 8: the user wants to return to the main menu.
        if (selectedKeyCode == KeyEvent.VK_M) {
            // The last element is the CURRENT key pressed, so I want the second to last element.
            if (numberOfPressedKeys > 1 && this.pressedKeyCodeList.get(numberOfPressedKeys - 2) == KeyEvent.VK_M) {
                this.sudokuGame.receiveAction(new SudokuMainMenuAction());
                this.pressedKeyCodeList.clear();
            }

            return;
        }

        // Case 9: the user wants to restart the current Sudoku board.
        if (selectedKeyCode == KeyEvent.VK_Q) {
            // The last element is the CURRENT key pressed, so I want the second to last element.
            if (numberOfPressedKeys > 1 && this.pressedKeyCodeList.get(numberOfPressedKeys - 2) == KeyEvent.VK_Q) {
                this.sudokuGame.receiveAction(new SudokuRestartAction());
                this.pressedKeyCodeList.clear();
            }

            return;
        }

        // Case 10: the selected key is unrecognized.
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

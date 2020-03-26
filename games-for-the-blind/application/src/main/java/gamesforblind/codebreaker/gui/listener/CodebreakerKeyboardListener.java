package gamesforblind.codebreaker.gui.listener;

import gamesforblind.codebreaker.CodebreakerGame;
import gamesforblind.codebreaker.action.*;
import gamesforblind.enums.ArrowKeyDirection;
import org.jetbrains.annotations.NotNull;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Map;

import static gamesforblind.Constants.CODEBREAKER_MAX_CODE_INT;
import static util.MapUtil.entry;
import static util.MapUtil.map;

/**
 * Keyboard listener for Codebreaker that receives keyboard inputs and appropriately calls the proper action class
 */
public class CodebreakerKeyboardListener implements KeyListener {
    /**
     * The current Codebreaker game.
     */
    private final CodebreakerGame codebreakerGame;

    /**
     * For returning to main menu or restarting the game, I want to make sure that the user pressed the given key TWICE.
     */
    private final ArrayList<Integer> pressedKeyCodeList = new ArrayList<>();

    /**
     * Creates a new CodebreakerKeyboardListener
     *
     * @param codebreakerGame The current Codebreaker game.
     */
    public CodebreakerKeyboardListener(@NotNull CodebreakerGame codebreakerGame) {
        this.codebreakerGame = codebreakerGame;
    }

    /**
     * Triggered when a key is pressed in the Codebreaker game.
     *
     * @param e The event that was triggered by the key press.
     */
    @Override
    public void keyPressed(@NotNull KeyEvent e) {
        int selectedKeyCode = e.getKeyCode();
        this.pressedKeyCodeList.add(selectedKeyCode);

        // Case 1: ALT + M is pressed (go to main menu).
        if (e.isAltDown() && selectedKeyCode == KeyEvent.VK_M) {
            this.codebreakerGame.receiveAction(new CodebreakerMainMenuAction());
            return;
        }

        // Case 2: control is pressed (stop reading phrases).
        if (e.isControlDown()) {
            this.codebreakerGame.receiveAction(new CodebreakerStopReadingAction());
            return;
        }

        // Case 3: pressed key is a number.
        if (Character.isDigit(e.getKeyChar())) {
            int selectedNumber = Character.getNumericValue(e.getKeyChar());
            if (selectedNumber > 0 && selectedNumber <= CODEBREAKER_MAX_CODE_INT) {
                this.codebreakerGame.receiveAction(new CodebreakerSetSingleNumberAction(selectedNumber));
                return;
            }
        }

        final Map<Integer, ArrowKeyDirection> ARROW_KEY_MAP = map(
                entry(KeyEvent.VK_LEFT, ArrowKeyDirection.LEFT),
                entry(KeyEvent.VK_RIGHT, ArrowKeyDirection.RIGHT),
                entry(KeyEvent.VK_UP, ArrowKeyDirection.UP),
                entry(KeyEvent.VK_DOWN, ArrowKeyDirection.DOWN)
        );

        // Case 4: pressed key is an arrow key.
        ArrowKeyDirection maybeSelectedArrowKeyDirection = ARROW_KEY_MAP.get(e.getKeyCode());
        if (maybeSelectedArrowKeyDirection != null) {
            this.codebreakerGame.receiveAction(new CodebreakerArrowKeyAction(maybeSelectedArrowKeyDirection));
            return;
        }


        // Case 5: the selected key is the 'I' key (play the instructions).
        if (selectedKeyCode == KeyEvent.VK_I) {
            this.codebreakerGame.receiveAction(new CodebreakerInstructionsAction());
            return;
        }

        // Case 6: the selected key is the 'H' key (triggers a hint).
        if (selectedKeyCode == KeyEvent.VK_H) {
            this.codebreakerGame.receiveAction(new CodebreakerHintKeyAction());
            return;
        }

        int numberOfPressedKeys = this.pressedKeyCodeList.size();

        // Case 7: the user wants to restart the current Codebreaker board.
        if (selectedKeyCode == KeyEvent.VK_Q) {
            // The last element is the CURRENT key pressed, so I want the second to last element.
            if (numberOfPressedKeys > 1 && this.pressedKeyCodeList.get(numberOfPressedKeys - 2) == KeyEvent.VK_Q) {
                this.codebreakerGame.receiveAction(new CodebreakerRestartAction());
                this.pressedKeyCodeList.clear();
            }
            return;
        }

        // Case 8: the user wants to save their current code guess.
        if (selectedKeyCode == KeyEvent.VK_SPACE) {
            this.codebreakerGame.receiveAction(new CodebreakerSetGuessAction());
            return;
        }

        // Case 9: The user presses A to have the row read off.
        if (selectedKeyCode == KeyEvent.VK_A) {
            this.codebreakerGame.receiveAction(new CodebreakerReadBackAction());
            return;
        }

        // Case 10: the user wants to read off the currently selected location & value.
        if (selectedKeyCode == KeyEvent.VK_DOWN) {
            // The last element is the CURRENT key pressed, so I want the second to last element.
            if (numberOfPressedKeys > 1 && this.pressedKeyCodeList.get(numberOfPressedKeys - 2) == KeyEvent.VK_INSERT) {
                this.codebreakerGame.receiveAction(new CodebreakerLocationAction());
                this.pressedKeyCodeList.clear();
                return;
            }
        }

        // Case 11: the selected key is unrecognized.
        if (selectedKeyCode != KeyEvent.VK_INSERT) {
            this.codebreakerGame.receiveAction(new CodebreakerUnrecognizedKeyAction(e.getKeyCode()));
        }
    }

    /* These methods are required to be overridden by the KeyListener, but they are unused. */
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}

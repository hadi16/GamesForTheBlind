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

        // Case 1: pressed key is a number.
        if (Character.isDigit(e.getKeyChar())) {
            int selectedNumber = Character.getNumericValue(e.getKeyChar());
            if (selectedNumber > 0 && selectedNumber <= CODEBREAKER_MAX_CODE_INT) {
                this.codebreakerGame.receiveAction(new CodebreakerSetSingleNumberAction(selectedNumber));
                return;
            }
        }

        final Map<Integer, ArrowKeyDirection> ARROW_KEY_MAP = Map.of(
                KeyEvent.VK_LEFT, ArrowKeyDirection.LEFT,
                KeyEvent.VK_RIGHT, ArrowKeyDirection.RIGHT,
                KeyEvent.VK_UP, ArrowKeyDirection.UP,
                KeyEvent.VK_DOWN, ArrowKeyDirection.DOWN
        );

        // Case 2: pressed key is an arrow key.
        ArrowKeyDirection maybeSelectedArrowKeyDirection = ARROW_KEY_MAP.get(e.getKeyCode());
        if (maybeSelectedArrowKeyDirection != null) {
            this.codebreakerGame.receiveAction(new CodebreakerArrowKeyAction(maybeSelectedArrowKeyDirection));
            return;
        }



        // Case 3: the selected key is the 'I' key (play the instructions).
        if (selectedKeyCode == KeyEvent.VK_I) {
            this.codebreakerGame.receiveAction(new CodebreakerInstructionsAction());
            return;
        }

        // Case 4: the selected key is the 'H' key (triggers a hint).
        if (selectedKeyCode == KeyEvent.VK_H) {
            this.codebreakerGame.receiveAction(new CodebreakerHintKeyAction());
            return;
        }

        int numberOfPressedKeys = this.pressedKeyCodeList.size();

        // Case 5: the user wants to return to the main menu.
        if (selectedKeyCode == KeyEvent.VK_M) {
            // The last element is the CURRENT key pressed, so I want the second to last element.
            if (numberOfPressedKeys > 1 && this.pressedKeyCodeList.get(numberOfPressedKeys - 2) == KeyEvent.VK_M) {
                this.codebreakerGame.receiveAction(new CodebreakerMainMenuAction());
                this.pressedKeyCodeList.clear();
            }
            return;
        }

        // Case 6: the user wants to restart the current Codebreaker board.
        if (selectedKeyCode == KeyEvent.VK_Q) {
            // The last element is the CURRENT key pressed, so I want the second to last element.
            if (numberOfPressedKeys > 1 && this.pressedKeyCodeList.get(numberOfPressedKeys - 2) == KeyEvent.VK_Q) {
                this.codebreakerGame.receiveAction(new CodebreakerRestartAction());
                this.pressedKeyCodeList.clear();
            }
            return;
        }

        // Case 7: the user wants to save their current code guess.
        if (selectedKeyCode == KeyEvent.VK_SPACE) {
            this.codebreakerGame.receiveAction(new CodebreakerSetGuessAction());
            return;
        }
        // Case 8: The user presses INSERT to have the row read off
        if(selectedKeyCode == KeyEvent.VK_Y){
            this.codebreakerGame.receiveAction(new CodebreakerReadBackAction());
            return;
        }

        // Case 9: the selected key is unrecognized.
        this.codebreakerGame.receiveAction(new CodebreakerUnrecognizedKeyAction(e.getKeyCode()));
    }

    /* These methods are required to be overridden by the KeyListener, but they are unused. */
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}

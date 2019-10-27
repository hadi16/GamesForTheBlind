package gamesforblind.loader.gui.listener;

import gamesforblind.loader.enums.ArrowKeyDirection;
import gamesforblind.loader.GameLoader;
import gamesforblind.loader.action.LoaderArrowKeyAction;
import gamesforblind.loader.action.LoaderUnrecognizedKeyAction;
import gamesforblind.loader.gui.LoaderFrame;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * The keyboard listener for the loader GUI. A {@link KeyEventDispatcher} is used to prevent the need to
 * add the listener to every component in the GUI (this would be needed with a {@link java.awt.event.KeyListener}).
 * Inherits from the {@link LoaderListener} abstract class.
 */
public class LoaderKeyboardListener extends LoaderListener implements KeyEventDispatcher {
    /**
     * A reference to the {@link LoaderFrame} instance for the loader GUI.
     * For now, I use this to handle the cases when the user selects a button in the GUI.
     * TODO: remove the need to have this reference (get the button text directly from the {@link KeyEvent}).
     */
    private final LoaderFrame loaderFrame;

    /**
     * Creates a new {@link LoaderKeyboardListener}.
     * @param gameLoader Reference to the game loader. Needed to send the appropriate action performed by the user.
     * @param loaderFrame A reference to the {@link LoaderFrame} instance for the loader GUI.
     */
    public LoaderKeyboardListener(GameLoader gameLoader, LoaderFrame loaderFrame) {
        super(gameLoader);
        this.loaderFrame = loaderFrame;
    }

    /**
     * Triggered when the user selects a key in the game. If key was pressed, call a private helper method w/ the event.
     * @param e The event that was triggered by the user pressing a key in the game.
     * @return Whether the {@link KeyEvent} was fully dispatched (see {@link KeyEventDispatcher} docs for details).
     */
    @Override
    public boolean dispatchKeyEvent(KeyEvent e) {
        if (e.getID() == KeyEvent.KEY_PRESSED) {
            this.sendActionBasedOnKeyPressed(e);
        }

        // The KeyEvent was fully dispatched & no further action should
        // be taken (see KeyEventDispatcher documentation for details).
        return true;
    }

    /**
     * Helper method that sends an appropriate action to the game loader based on which key the user pressed.
     * @param e The event that was triggered by the user pressing a key in the game.
     */
    private void sendActionBasedOnKeyPressed(KeyEvent e) {
        // Case 1: the user has selected the currently highlighted button.
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            // TODO: get the highlighted button text directly from the KeyEvent (causes bugs w/ current implementation).
            this.sendSelectionActionBasedOnButtonText(this.loaderFrame.getCurrentlyHighlightedButtonText());
            return;
        }

        // Attempt to map the key code to one of the four arrow keys.
        ArrowKeyDirection arrowKeyDirection = null;
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                arrowKeyDirection = ArrowKeyDirection.LEFT;
                break;
            case KeyEvent.VK_RIGHT:
                arrowKeyDirection = ArrowKeyDirection.RIGHT;
                break;
            case KeyEvent.VK_UP:
                arrowKeyDirection = ArrowKeyDirection.UP;
                break;
            case KeyEvent.VK_DOWN:
                arrowKeyDirection = ArrowKeyDirection.DOWN;
                break;
        }

        // Case 2: If the user didn't select an arrow key, then an unrecognized key was pressed by the user.
        if (arrowKeyDirection == null) {
            this.gameLoader.receiveAction(new LoaderUnrecognizedKeyAction(e.getKeyCode()));
            return;
        }

        // Case 3: The user has pressed an arrow key to change the currently selected button in the game.
        this.gameLoader.receiveAction(new LoaderArrowKeyAction(arrowKeyDirection));
    }
}

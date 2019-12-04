package gamesforblind.mastermind.gui.listener;

import gamesforblind.mastermind.MastermindGame;
import gamesforblind.mastermind.action.MastermindExitAction;
import org.jetbrains.annotations.NotNull;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * Window Listener that calls the exit action for Mastermind when the game is terminated.
 * This is also called when pressing COMMAND + Q, since a System property was set in main (apple.eawt.quitStrategy).
 */
public class MastermindWindowListener implements WindowListener {
    /**
     * The current {@link MastermindGame}.
     */
    private final MastermindGame mastermindGame;

    /**
     * Creates a new MastermindWindowListener.
     *
     * @param mastermindGame The current {@link MastermindGame}.
     */
    public MastermindWindowListener(@NotNull MastermindGame mastermindGame) {
        this.mastermindGame = mastermindGame;
    }

    /**
     * When the window closing event is triggered, just send the exit action to the {@link MastermindGame} instance.
     *
     * @param e The event that was triggered by the window.
     */
    @Override
    public void windowClosing(WindowEvent e) {
        this.mastermindGame.receiveAction(new MastermindExitAction());
    }

    /* These methods are required to be overridden by the WindowListener, but they are unused. */
    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }
}

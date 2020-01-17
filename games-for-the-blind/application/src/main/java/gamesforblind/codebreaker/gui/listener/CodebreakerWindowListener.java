package gamesforblind.codebreaker.gui.listener;

import gamesforblind.codebreaker.CodebreakerGame;
import gamesforblind.codebreaker.action.CodebreakerExitAction;
import org.jetbrains.annotations.NotNull;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * Window Listener that calls the exit action for Codebreaker when the game is terminated.
 * This is also called when pressing COMMAND + Q, since a System property was set in main (apple.eawt.quitStrategy).
 */
public class CodebreakerWindowListener implements WindowListener {
    /**
     * The current {@link CodebreakerGame}.
     */
    private final CodebreakerGame codebreakerGame;

    /**
     * Creates a new CodebreakerWindowListener.
     *
     * @param codebreakerGame The current {@link CodebreakerGame}.
     */
    public CodebreakerWindowListener(@NotNull CodebreakerGame codebreakerGame) {
        this.codebreakerGame = codebreakerGame;
    }

    /**
     * When the window closing event is triggered, just send the exit action to the {@link CodebreakerGame} instance.
     *
     * @param e The event that was triggered by the window.
     */
    @Override
    public void windowClosing(WindowEvent e) {
        this.codebreakerGame.receiveAction(new CodebreakerExitAction());
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

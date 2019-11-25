package gamesforblind.loader.gui.listener;

import gamesforblind.loader.GameLoader;
import gamesforblind.loader.action.LoaderExitAction;
import org.jetbrains.annotations.NotNull;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * Window Listener that calls the exit action for the loader when it is closed.
 * This is also called when pressing COMMAND + Q, since a System property was set in main (apple.eawt.quitStrategy).
 */
public class LoaderWindowListener implements WindowListener {
    /**
     * The current {@link GameLoader}.
     */
    private final GameLoader gameLoader;

    /**
     * Creates a new LoaderWindowListener.
     *
     * @param gameLoader The current {@link GameLoader}.
     */
    public LoaderWindowListener(@NotNull GameLoader gameLoader) {
        this.gameLoader = gameLoader;
    }

    /**
     * When the window closing event is triggered, just send the exit action to the {@link GameLoader} instance.
     *
     * @param e The event that was triggered by the window.
     */
    @Override
    public void windowClosing(WindowEvent e) {
        this.gameLoader.receiveAction(new LoaderExitAction());
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

package gamesforblind.loader.gui.listener;

import gamesforblind.loader.GameLoader;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * ActionListener for the loader GUI (handles any mouse actions by "sighted onlookers").
 * Inherits from the {@link LoaderListener} abstract class.
 */
public class LoaderActionListener extends LoaderListener implements ActionListener {
    /**
     * Creates a new {@link LoaderActionListener}. Just calls the LoaderListener constructor.
     * @param gameLoader An instance to the game loader, which is needed to instance the superclass.
     */
    public LoaderActionListener(GameLoader gameLoader) {
        super(gameLoader);
    }

    /**
     * Triggered when the user clicks somewhere on the loader GUI.
     * @param e The mouse event that was triggered.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // Just call into the superclass' method for sending an action to the game loader.
        // This allows for consistency between the mouse listener & keyboard listener in the game loader.
        this.sendSelectionActionBasedOnButtonText(e.getActionCommand());
    }
}

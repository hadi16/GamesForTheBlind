package gamesforblind.mastermind.gui.listener;

import gamesforblind.enums.GameMenuItem;
import gamesforblind.mastermind.MastermindGame;
import gamesforblind.mastermind.action.MastermindAction;
import gamesforblind.mastermind.action.MastermindMainMenuAction;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

/**
 * The {@link ActionListener} for each of the {@link JMenuItem}s in the Mastermind menu.
 */
public class MastermindMenuItemListener implements ActionListener {
    /**
     * The {@link MastermindGame}, which receives actions in the game.
     */
    private final MastermindGame mastermindGame;

    /**
     * Creates a new MastermindMenuListener.
     *
     * @param mastermindGame The {@link MastermindGame}, which receives actions in the game.
     */
    public MastermindMenuItemListener(@NotNull MastermindGame mastermindGame) {
        this.mastermindGame = mastermindGame;
    }

    /**
     * Triggered when the user clicks on a {@link JMenuItem} in the Mastermind menu GUI.
     *
     * @param e The {@link ActionEvent} that was triggered in the GUI.
     */
    @Override
    public void actionPerformed(@NotNull ActionEvent e) {
        String selectedMenuText = e.getActionCommand();

        final Map<String, MastermindAction> SELECTED_MENU_TEXT_TO_ACTION = Map.of(
                GameMenuItem.RETURN_TO_MAIN_MENU.toString(), new MastermindMainMenuAction()
        );

        MastermindAction mastermindAction = SELECTED_MENU_TEXT_TO_ACTION.get(selectedMenuText);
        if (mastermindAction != null) {
            this.mastermindGame.receiveAction(mastermindAction);
        }
    }
}

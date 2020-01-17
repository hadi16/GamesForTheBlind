package gamesforblind.codebreaker.gui.listener;

import gamesforblind.codebreaker.CodebreakerGame;
import gamesforblind.codebreaker.action.CodebreakerAction;
import gamesforblind.codebreaker.action.CodebreakerMainMenuAction;
import gamesforblind.enums.GameMenuItem;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

/**
 * The {@link ActionListener} for each of the {@link JMenuItem}s in the Codebreaker menu.
 */
public class CodebreakerMenuItemListener implements ActionListener {
    /**
     * The {@link CodebreakerGame}, which receives actions in the game.
     */
    private final CodebreakerGame codebreakerGame;

    /**
     * Creates a new CodebreakerMenuListener.
     *
     * @param codebreakerGame The {@link CodebreakerGame}, which receives actions in the game.
     */
    public CodebreakerMenuItemListener(@NotNull CodebreakerGame codebreakerGame) {
        this.codebreakerGame = codebreakerGame;
    }

    /**
     * Triggered when the user clicks on a {@link JMenuItem} in the Codebreaker menu GUI.
     *
     * @param e The {@link ActionEvent} that was triggered in the GUI.
     */
    @Override
    public void actionPerformed(@NotNull ActionEvent e) {
        String selectedMenuText = e.getActionCommand();

        final Map<String, CodebreakerAction> SELECTED_MENU_TEXT_TO_ACTION = Map.of(
                GameMenuItem.RETURN_TO_MAIN_MENU.toString(), new CodebreakerMainMenuAction()
        );

        CodebreakerAction codebreakerAction = SELECTED_MENU_TEXT_TO_ACTION.get(selectedMenuText);
        if (codebreakerAction != null) {
            this.codebreakerGame.receiveAction(codebreakerAction);
        }
    }
}

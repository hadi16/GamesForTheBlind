package gamesforblind.sudoku.gui.listener;

import gamesforblind.enums.GameMenuItem;
import gamesforblind.sudoku.SudokuGame;
import gamesforblind.sudoku.action.*;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import static util.MapUtil.entry;
import static util.MapUtil.map;

/**
 * The {@link ActionListener} for each of the {@link JMenuItem}s in the Sudoku menu.
 */
public class SudokuMenuItemListener implements ActionListener {
    /**
     * The {@link SudokuGame}, which receives actions in the game.
     */
    private final SudokuGame sudokuGame;

    /**
     * Creates a new SudokuMenuListener.
     *
     * @param sudokuGame The {@link SudokuGame}, which receives actions in the game.
     */
    public SudokuMenuItemListener(@NotNull SudokuGame sudokuGame) {
        this.sudokuGame = sudokuGame;
    }

    /**
     * Triggered when the user clicks on a {@link JMenuItem} in the Sudoku menu GUI.
     *
     * @param e The {@link ActionEvent} that was triggered in the GUI.
     */
    @Override
    public void actionPerformed(@NotNull ActionEvent e) {
        String selectedMenuText = e.getActionCommand();

        // TODO: Support the language option.
        final Map<String, SudokuAction> SELECTED_MENU_TEXT_TO_ACTION = map(
                entry(GameMenuItem.HINT.toString(), new SudokuHintKeyAction()),
                entry(GameMenuItem.INSTRUCTIONS.toString(), new SudokuInstructionsAction()),
                entry(GameMenuItem.RETURN_TO_MAIN_MENU.toString(), new SudokuMainMenuAction()),
                entry(GameMenuItem.RESTART.toString(), new SudokuRestartAction())
        );

        SudokuAction sudokuAction = SELECTED_MENU_TEXT_TO_ACTION.get(selectedMenuText);
        if (sudokuAction != null) {
            this.sudokuGame.receiveAction(sudokuAction);
        }
    }
}

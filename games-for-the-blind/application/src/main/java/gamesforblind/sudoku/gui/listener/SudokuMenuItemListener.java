package gamesforblind.sudoku.gui.listener;

import gamesforblind.enums.SudokuMenuItem;
import gamesforblind.sudoku.SudokuGame;
import gamesforblind.sudoku.action.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

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
    public SudokuMenuItemListener(SudokuGame sudokuGame) {
        this.sudokuGame = sudokuGame;
    }

    /**
     * Triggered when the user clicks on a {@link JMenuItem} in the Sudoku menu GUI.
     *
     * @param e The {@link ActionEvent} that was triggered in the GUI.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String selectedMenuText = e.getActionCommand();

        // TODO: Support the language option.
        final Map<String, SudokuAction> SELECTED_MENU_TEXT_TO_ACTION = Map.of(
                SudokuMenuItem.HINT.toString(), new SudokuHintKeyAction(),
                SudokuMenuItem.INSTRUCTIONS.toString(), new SudokuInstructionsAction(),
                SudokuMenuItem.RETURN_TO_MAIN_MENU.toString(), new SudokuMainMenuAction(),
                SudokuMenuItem.RESTART.toString(), new SudokuRestartAction()
        );

        SudokuAction sudokuAction = SELECTED_MENU_TEXT_TO_ACTION.get(selectedMenuText);
        if (sudokuAction != null) {
            this.sudokuGame.receiveAction(sudokuAction);
        }
    }
}

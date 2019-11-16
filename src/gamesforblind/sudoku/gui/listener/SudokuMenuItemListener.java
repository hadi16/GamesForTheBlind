package gamesforblind.sudoku.gui.listener;

import gamesforblind.enums.SudokuMenuItem;
import gamesforblind.sudoku.SudokuGame;
import gamesforblind.sudoku.action.SudokuMenuAction;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
    public void actionPerformed(ActionEvent e) {
        String selectedMenuText = e.getActionCommand();

        SudokuMenuItem selectedMenuItem = null;
        for (SudokuMenuItem menuItem : SudokuMenuItem.values()) {
            if (selectedMenuText.equals(menuItem.toString())) {
                selectedMenuItem = menuItem;
                break;
            }
        }

        if (selectedMenuItem != null) {
            this.sudokuGame.receiveAction(new SudokuMenuAction(selectedMenuItem));
        }
    }
}

package gamesforblind.sudoku.gui.listener;

import gamesforblind.enums.SudokuMenuItem;
import gamesforblind.sudoku.SudokuGame;
import gamesforblind.sudoku.action.SudokuMenuAction;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SudokuMenuItemListener implements ActionListener {
    private final SudokuGame sudokuGame;

    public SudokuMenuItemListener(SudokuGame sudokuGame) {
        this.sudokuGame = sudokuGame;
    }

    /**
     * @param e
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

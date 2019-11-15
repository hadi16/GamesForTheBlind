package gamesforblind.sudoku.gui;

import gamesforblind.enums.SudokuMenuItem;
import gamesforblind.sudoku.SudokuGame;
import gamesforblind.sudoku.gui.listener.SudokuMenuItemListener;

import javax.swing.*;

/**
 * // Class that creates the menu, adds the listeners to that menu items.
 */
public class SudokuMenu {
    /**
     * sets up the menu, adds all the items and listeners to the items.
     *
     * @param sudokuGame The current Sudoku game.
     */
    public JMenu getInitializedMenu(SudokuGame sudokuGame) {
        JMenu mainMenu = new JMenu("Menu");
        SudokuMenuItemListener sudokuMenuItemListener = new SudokuMenuItemListener(sudokuGame);

        /* Add menu items to menu */
        JMenuItem hintItem = new JMenuItem(SudokuMenuItem.HINT.toString());
        hintItem.addActionListener(sudokuMenuItemListener);
        mainMenu.add(hintItem);

        JMenuItem instructionsItem = new JMenuItem(SudokuMenuItem.INSTRUCTIONS.toString());
        instructionsItem.addActionListener(sudokuMenuItemListener);
        mainMenu.add(instructionsItem);

        JMenuItem languageItem = new JMenuItem(SudokuMenuItem.LANGUAGE.toString());
        languageItem.addActionListener(sudokuMenuItemListener);
        mainMenu.add(languageItem);

        JMenuItem restartItem = new JMenuItem(SudokuMenuItem.RESTART.toString());
        restartItem.addActionListener(sudokuMenuItemListener);
        mainMenu.add(restartItem);

        JMenuItem returnToMenuItem = new JMenuItem(SudokuMenuItem.RETURN_TO_MAIN_MENU.toString());
        returnToMenuItem.addActionListener(sudokuMenuItemListener);
        mainMenu.add(returnToMenuItem);

        return mainMenu;
    }
}

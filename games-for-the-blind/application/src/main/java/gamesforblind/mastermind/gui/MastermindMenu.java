package gamesforblind.mastermind.gui;

import gamesforblind.enums.SudokuMenuItem;
import gamesforblind.mastermind.MastermindGame;
import gamesforblind.mastermind.gui.listener.MastermindMenuItemListener;
import gamesforblind.sudoku.SudokuGame;
import gamesforblind.sudoku.gui.listener.SudokuMenuItemListener;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * Class that creates the main menu & adds the listeners to those menu items.
 */
public class MastermindMenu {
    /**
     * Sets up the {@link JMenu}. Adds all the {@link JMenuItem}s & {@link SudokuMenuItemListener} to these items.
     *
     * @param mastermindGame The current Sudoku game.
     * @return The initialized {@link JMenu} for the Sudoku game.
     */
    public JMenu getInitializedMenu(@NotNull MastermindGame mastermindGame) {
        JMenu mainMenu = new JMenu("Menu");
        MastermindMenuItemListener mastermindMenuItemListener = new MastermindMenuItemListener(mastermindGame);

        /* Add menu items to menu. */
        JMenuItem instructionsItem = new JMenuItem(SudokuMenuItem.INSTRUCTIONS.toString());
        instructionsItem.addActionListener(mastermindMenuItemListener);
        mainMenu.add(instructionsItem);

        JMenuItem languageItem = new JMenuItem(SudokuMenuItem.LANGUAGE.toString());
        languageItem.addActionListener(mastermindMenuItemListener);
        mainMenu.add(languageItem);

        JMenuItem restartItem = new JMenuItem(SudokuMenuItem.RESTART.toString());
        restartItem.addActionListener(mastermindMenuItemListener);
        mainMenu.add(restartItem);

        JMenuItem returnToMenuItem = new JMenuItem(SudokuMenuItem.RETURN_TO_MAIN_MENU.toString());
        returnToMenuItem.addActionListener(mastermindMenuItemListener);
        mainMenu.add(returnToMenuItem);

        return mainMenu;
    }
}

package gamesforblind.sudoku.gui;

import gamesforblind.sudoku.SudokuGame;
import gamesforblind.sudoku.action.SudokuMenuAction;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
// Class that creates the menu, adds the listeners to that menu items.
 */
public class SudokuMenu  {


    /**
     * sets up the menu, adds all the items and listeners to the items.
     *
     * @param sudokuGame   The current Sudoku game.
     */
    public JMenu initialize (SudokuGame sudokuGame){

    JMenu x = new JMenu("Menu");

    // add menu items to menu
    JMenuItem hint = new JMenuItem("Hint");
        x.add(hint);
        hint.setActionCommand("Hint");

    JMenuItem instructions = new JMenuItem("Instructions");
        x.add(instructions);
        instructions.setActionCommand("Instructions");

    JMenuItem language = new JMenuItem("Language");
        x.add(language);
        language.setActionCommand("Language");

    JMenuItem restart = new JMenuItem("Restart");
        x.add(restart);
        restart.setActionCommand("Restart");

    JMenuItem returnHome = new JMenuItem("Return to Main Menu");
        x.add(returnHome);
        returnHome.setActionCommand("Return to Main Menu");

        MenuItemListener menuItemListener = new MenuItemListener(sudokuGame);
        hint.addActionListener(menuItemListener);
        instructions.addActionListener(menuItemListener);
        language.addActionListener(menuItemListener);
        restart.addActionListener(menuItemListener);
        returnHome.addActionListener(menuItemListener);

        return x;
    }
}


/**
 Class that implements the listeners and sends the proper action to SudokuGame
 */
class MenuItemListener implements ActionListener {

    /**
     * A reference to the Game that contains current SudokuGame.
     */
    private final SudokuGame sudokuGame;

    /**
     * Deals with sending the correct action to the current Sudoku game
     *
     * @param sudokuGame   The current Sudoku game.
     */
    public MenuItemListener(SudokuGame sudokuGame) {
        this.sudokuGame = sudokuGame;
    }
        public void actionPerformed(ActionEvent e) {

            if(e.getActionCommand().equals("Hint")){
                this.sudokuGame.receiveAction(new SudokuMenuAction(1));
            }
            if(e.getActionCommand().equals("Instructions")){
                this.sudokuGame.receiveAction(new SudokuMenuAction(2));
            }
            if(e.getActionCommand().equals("Language")){
                this.sudokuGame.receiveAction(new SudokuMenuAction(3));
            }
            if(e.getActionCommand().equals("Restart")){
                this.sudokuGame.receiveAction(new SudokuMenuAction(4));
            }
            if(e.getActionCommand().equals("Return to Main Menu")){
                this.sudokuGame.receiveAction(new SudokuMenuAction(5));
            }
        }
    }




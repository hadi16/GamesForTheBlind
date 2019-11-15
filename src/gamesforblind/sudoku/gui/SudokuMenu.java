package gamesforblind.sudoku.gui;

import gamesforblind.sudoku.SudokuGame;
import gamesforblind.sudoku.action.SudokuMenuAction;
import gamesforblind.sudoku.gui.listener.SudokuMouseListener;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;



public class SudokuMenu implements ActionListener {

    public JMenu initialize (){
    JMenu x = new JMenu("Menu");

    // add menu items to menu
    JMenuItem hint = new JMenuItem("Hint");
        x.add(hint);
        hint.addActionListener(this);

    JMenuItem instructions = new JMenuItem("Instructions");
        x.add(instructions);
        instructions.addActionListener(this);

    JMenuItem language = new JMenuItem("Language");
        x.add(language);
        language.addActionListener(this);

    JMenuItem restart = new JMenuItem("Restart");
        x.add(restart);
        restart.addActionListener(this);

    JMenuItem returnHome = new JMenuItem("Return to Main Menu");
        x.add(returnHome);
        returnHome.addActionListener(this);
        return x;
}

    public void actionPerformed(ActionEvent e, SudokuGame sudokuGame) {
        if(e.getSource()=="hint")
            sudokuGame.receiveAction(new SudokuMenuAction(1));
        if(e.getSource()=="Instructions")
            sudokuGame.receiveAction(new SudokuMenuAction(2));
        if(e.getSource()=="Language")
            sudokuGame.receiveAction(new SudokuMenuAction(3));
        if(e.getSource()=="Restart")
            sudokuGame.receiveAction(new SudokuMenuAction(4));
        if(e.getSource()=="Return to Main Menu")
            sudokuGame.receiveAction(new SudokuMenuAction(5));
    }

}


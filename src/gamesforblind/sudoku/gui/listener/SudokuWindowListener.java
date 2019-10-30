package gamesforblind.sudoku.gui.listener;

import gamesforblind.sudoku.SudokuGame;
import gamesforblind.sudoku.action.SudokuExitAction;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class SudokuWindowListener implements WindowListener {
    private final SudokuGame sudokuGame;

    public SudokuWindowListener(SudokuGame sudokuGame) {
        this.sudokuGame = sudokuGame;
    }

    @Override
    public void windowClosing(WindowEvent e) {
        this.sudokuGame.receiveAction(new SudokuExitAction());
    }

    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }
}

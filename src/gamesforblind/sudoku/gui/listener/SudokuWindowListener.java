package gamesforblind.sudoku.gui.listener;

import gamesforblind.logger.LogCreator;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class SudokuWindowListener implements WindowListener {
    private final LogCreator logCreator;

    public SudokuWindowListener(LogCreator logCreator) {
        this.logCreator = logCreator;
    }

    @Override
    public void windowClosing(WindowEvent e) {
        this.logCreator.saveProgramActions();
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

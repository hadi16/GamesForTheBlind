package gamesforblind.sudoku.gui.listener;

import gamesforblind.sudoku.SudokuGame;
import gamesforblind.sudoku.action.SudokuExitAction;
import org.jetbrains.annotations.NotNull;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * Window Listener that calls the exit action for Sudoku when the game is terminated.
 * This is also called when pressing COMMAND + Q, since a System property was set in main (apple.eawt.quitStrategy).
 */
public class SudokuWindowListener implements WindowListener {
    /**
     * The current {@link SudokuGame}.
     */
    private final SudokuGame sudokuGame;

    /**
     * Creates a new SudokuWindowListener.
     *
     * @param sudokuGame The current {@link SudokuGame}.
     */
    public SudokuWindowListener(@NotNull SudokuGame sudokuGame) {
        this.sudokuGame = sudokuGame;
    }

    /**
     * When the window closing event is triggered, just send the exit action to the {@link SudokuGame} instance.
     *
     * @param e The event that was triggered by the window.
     */
    @Override
    public void windowClosing(WindowEvent e) {
        this.sudokuGame.receiveAction(new SudokuExitAction());
    }

    /* These methods are required to be overridden by the WindowListener, but they are unused. */
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

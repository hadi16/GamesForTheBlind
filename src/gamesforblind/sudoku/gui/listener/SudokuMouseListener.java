package gamesforblind.sudoku.gui.listener;

import gamesforblind.enums.InputType;
import gamesforblind.enums.SudokuType;
import gamesforblind.sudoku.SudokuGame;
import gamesforblind.sudoku.action.SudokuHighlightAction;
import gamesforblind.sudoku.gui.SudokuFrame;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Mouse listener for Sudoku that receives mouse inputs and appropriately calls the proper action class
 */
public class SudokuMouseListener implements MouseListener {
    /**
     * The current {@link SudokuGame}
     */
    private final SudokuGame sudokuGame;

    /**
     * A reference to the class containing the main JFrame (for the Sudoku GUI).
     */
    private final SudokuFrame sudokuFrame;

    /**
     * The amount of squares on each side of the Sudoku board (e.g. 9x9 --> 9).
     */
    private final int sudokuBoardSize;

    /**
     * Creates a new SudokuMouseListener.
     *
     * @param sudokuGame  The current {@link SudokuGame}
     * @param sudokuFrame A reference to the class containing the main JFrame (for the Sudoku GUI).
     * @param sudokuType  Whether the given Sudoku board is a 4x4, 6x6, or 9x9.
     */
    public SudokuMouseListener(SudokuGame sudokuGame, SudokuFrame sudokuFrame, SudokuType sudokuType) {
        this.sudokuGame = sudokuGame;
        this.sudokuFrame = sudokuFrame;
        this.sudokuBoardSize = sudokuType.getSudokuBoardSize();
    }

    /**
     * Triggered when the mouse clicks on the main Sudoku GUI.
     * Reads in mouse clicks & obtains the click coordinates, which allows the proper action to be called.
     *
     * @param e The event that was triggered by a mouse click.
     */
    @Override
    public void mouseClicked(MouseEvent e) {

        Point selectedPoint = e.getPoint();
        Rectangle frameBounds = this.sudokuFrame.getFrameBounds();

        // The labels on the rows & columns make this like a "10x10" board per say (in the case of a 9x9)
        int squareDimension = Math.min(frameBounds.height, frameBounds.width) / (this.sudokuBoardSize + 1);
        this.sudokuGame.receiveAction(
                new SudokuHighlightAction(
                        new Point(selectedPoint.x / squareDimension, selectedPoint.y / squareDimension),
                        InputType.MOUSE
                )
        );
    }

    /* These methods are required to be overridden by the MouseListener, but they are unused. */
    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}

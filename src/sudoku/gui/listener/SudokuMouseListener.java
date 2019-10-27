package sudoku.gui.listener;

import sudoku.InputType;
import sudoku.SudokuGame;
import sudoku.action.SudokuHighlightAction;
import sudoku.gui.SudokuFrame;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


/**
 * Sudoku mouse listener class that will receive mouse inputs and appropriately call the proper
 * action class
 */
public class SudokuMouseListener implements MouseListener {
    private final SudokuGame sudokuGame;
    private final SudokuFrame sudokuFrame;
    private final int sudokuBoardSize;

    //sets the inputs to be contained within "this"
    public SudokuMouseListener(SudokuGame sudokuGame, SudokuFrame sudokuFrame, int sudokuBoardSize) {
        this.sudokuGame = sudokuGame;
        this.sudokuFrame = sudokuFrame;
        this.sudokuBoardSize = sudokuBoardSize;
    }

    /**
     * Reads in mouse clicks, obtains their coordinates allowing them to properly call an
     * action to that cell
     *
     * @param e :which is a mouse event
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        Point selectedPoint = e.getPoint();
        Rectangle frameBounds = this.sudokuFrame.getFrameBounds();

        int squareDimension = Math.min(frameBounds.height, frameBounds.width) / this.sudokuBoardSize;
        this.sudokuGame.receiveAction(
                new SudokuHighlightAction(
                        new Point(selectedPoint.x / squareDimension, selectedPoint.y / squareDimension),
                        InputType.MOUSE
                )
        );
    }


    //overrides for mouse clicks
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

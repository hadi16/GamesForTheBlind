package sudoku;

import sudoku.action.SudokuAction;
import sudoku.action.SudokuHighlightAction;
import sudoku.gui.SudokuFrame;

public class SudokuGame {
    private final SudokuState sudokuState;
    private final SudokuFrame sudokuFrame;

    public SudokuGame(int sudokuBoardSize) {
        this.sudokuState = new SudokuState(sudokuBoardSize);
        this.sudokuFrame = new SudokuFrame(this);
    }

    public void receiveAction(SudokuAction sudokuAction) {
        if (sudokuAction instanceof SudokuHighlightAction) {
            SudokuHighlightAction sudokuHighlightAction = (SudokuHighlightAction) sudokuAction;
            this.sudokuState.setHighlightedDirection(sudokuHighlightAction.getDirectionToHighlight());
            this.sudokuFrame.repaintSudokuPanel();
            return;
        }

        System.err.println("An unrecognized form of a Sudoku action was received by the game!");
    }

    public SudokuState getSudokuState() {
        return this.sudokuState;
    }
}

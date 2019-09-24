package sudoku;

import sudoku.action.SudokuAction;
import sudoku.action.SudokuFillAction;
import sudoku.action.SudokuHighlightAction;
import sudoku.gui.SudokuFrame;

public class SudokuGame {
    private final SudokuState sudokuState;
    private final SudokuFrame sudokuFrame;

    public SudokuGame(int sudokuBoardSize) {
        this.sudokuState = new SudokuState(sudokuBoardSize);
        this.sudokuFrame = new SudokuFrame(this, this.sudokuState, sudokuBoardSize);
    }

    public void receiveAction(SudokuAction sudokuAction) {
        if (sudokuAction instanceof SudokuHighlightAction) {
            SudokuHighlightAction sudokuHighlightAction = (SudokuHighlightAction) sudokuAction;
            this.sudokuState.setHighlightedDirection(sudokuHighlightAction.getDirectionToHighlight());

            this.sendStateToGui();
            return;
        }

        if (sudokuAction instanceof SudokuFillAction) {
            SudokuFillAction sudokuFillAction = (SudokuFillAction) sudokuAction;
            this.sudokuState.setSquareNumber(sudokuFillAction.getNumberToFill());

            this.sendStateToGui();
            return;
        }

        System.err.println("An unrecognized form of a Sudoku action was received by the game!");
    }

    private void sendStateToGui() {
        this.sudokuFrame.receiveSudokuState(new SudokuState(this.sudokuState));
        this.sudokuFrame.repaintSudokuPanel();
    }
}

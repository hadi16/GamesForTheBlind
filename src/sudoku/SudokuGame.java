package sudoku;

import sudoku.action.SudokuAction;
import sudoku.action.SudokuFillAction;
import sudoku.action.SudokuHighlightAction;
import sudoku.generator.Cell;
import sudoku.generator.Solver;
import sudoku.generator.Grid;

import sudoku.gui.SudokuFrame;
import synthesizer.AudioPlayer;

public class SudokuGame {
    private final SudokuState sudokuState;
    private final SudokuFrame sudokuFrame;
    //private final Grid sudokuBoard;

    public SudokuGame(int sudokuBoardSize, AudioPlayer audioPlayer) {
        this.sudokuState = new SudokuState(sudokuBoardSize, audioPlayer);
        this.sudokuFrame = new SudokuFrame(this, this.sudokuState, sudokuBoardSize, audioPlayer);
    }

    public void receiveAction(SudokuAction sudokuAction) {
        if (sudokuAction instanceof SudokuHighlightAction) {
            SudokuHighlightAction sudokuHighlightAction = (SudokuHighlightAction) sudokuAction;
            this.sudokuState.setHighlightedPoint(
                    sudokuHighlightAction.getPointToHighlight(), sudokuHighlightAction.getInputType()
            );

            this.sendStateToGui();
            return;
        }

        if (sudokuAction instanceof SudokuFillAction) {
            SudokuFillAction sudokuFillAction = (SudokuFillAction) sudokuAction;
            this.sudokuState.setSquareNumber(sudokuFillAction.getNumberToFill());
            //if (new Solver(this.grid)) {
                this.sendStateToGui();
                return;
            //}
        }

        System.err.println("An unrecognized form of a Sudoku action was received by the game!");
    }

    private void sendStateToGui() {
        this.sudokuFrame.receiveSudokuState(new SudokuState(this.sudokuState));
        this.sudokuFrame.repaintSudokuPanel();
    }
}

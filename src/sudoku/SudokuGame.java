package sudoku;

import sudoku.action.*;
import sudoku.gui.SudokuFrame;
import synthesizer.AudioPlayer;

public class SudokuGame {
    private final SudokuState sudokuState;
    private final SudokuFrame sudokuFrame;

    public SudokuGame(int sudokuBoardSize, AudioPlayer audioPlayer) {
        this.sudokuState = new SudokuState(sudokuBoardSize, audioPlayer);
        this.sudokuFrame = new SudokuFrame(this, this.sudokuState, sudokuBoardSize);
    }

    public void receiveAction(SudokuAction sudokuAction) {
        if (this.sudokuState.isGameOver()) {
            return;
        }

        if (sudokuAction instanceof SudokuInstructionsAction) {
            this.sudokuState.readInstructions();
            return;
        }

        if (sudokuAction instanceof SudokuUnrecognizedKeyAction) {
            SudokuUnrecognizedKeyAction sudokuUnrecognizedKeyAction = (SudokuUnrecognizedKeyAction) sudokuAction;
            this.sudokuState.readUnrecognizedKey(sudokuUnrecognizedKeyAction.getUnrecognizedKey());
            return;
        }

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

            this.sendStateToGui();
            return;
        }

        if (sudokuAction instanceof SudokuReadPositionAction) {
            SudokuReadPositionAction sudokuReadPositionAction = (SudokuReadPositionAction) sudokuAction;
            this.sudokuState.readRowOrColumn(sudokuReadPositionAction.isReadRow());
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

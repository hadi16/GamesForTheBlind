package sudoku;

import sudoku.action.SudokuAction;
import sudoku.action.SudokuFillAction;
import sudoku.action.SudokuHighlightAction;
import sudoku.gui.SudokuFrame;
import synthesizer.AudioPlayer;

/**
 * Game class used for lots of functionality within the game such as initiating board, calling the action classes
 * and sending some errors. Also used for updating the GUI properly
 */
public class SudokuGame {
    private final SudokuState sudokuState;
    private final SudokuFrame sudokuFrame;

    public SudokuGame(int sudokuBoardSize, AudioPlayer audioPlayer) {
        this.sudokuState = new SudokuState(sudokuBoardSize, audioPlayer);
        this.sudokuFrame = new SudokuFrame(this, this.sudokuState, sudokuBoardSize);
    }

    /**
     * Given an action as an input and from there calls the proper action function and sends the information to the GUI
     *
     * @param sudokuAction
     */
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
            this.sudokuState.readUnrecognizedKey(sudokuUnrecognizedKeyAction.getKeyCode());
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
            this.sudokuState.readBoardSection(sudokuReadPositionAction.getSudokuSection());
            this.sendStateToGui();
            return;
        }

        System.err.println("An unrecognized form of a Sudoku action was received by the game!");
    }

    /**
     * Calls the painter class to repaint the Sudoku board based on the current state
     */
    private void sendStateToGui() {
        this.sudokuFrame.receiveSudokuState(new SudokuState(this.sudokuState));
        this.sudokuFrame.repaintSudokuPanel();
    }
}

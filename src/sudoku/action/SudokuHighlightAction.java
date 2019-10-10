package sudoku.action;

import sudoku.InputType;

import java.awt.*;

public class SudokuHighlightAction extends SudokuAction {
    private final Point pointToHighlight;
    private final InputType inputType;

    public SudokuHighlightAction(Point pointToHighlight, InputType inputType) {
        this.pointToHighlight = pointToHighlight;
        this.inputType = inputType;
    }

    public Point getPointToHighlight() {
        return this.pointToHighlight;
    }

    public InputType getInputType() {
        return this.inputType;
    }
}

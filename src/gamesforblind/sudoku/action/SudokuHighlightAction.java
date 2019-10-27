package gamesforblind.sudoku.action;

import gamesforblind.sudoku.enums.InputType;

import java.awt.*;

/**
 * Highlight class used for highlighting the cells and boxes upon keyboard and mouse navigation
 */
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

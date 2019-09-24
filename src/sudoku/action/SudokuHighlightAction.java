package sudoku.action;

import sudoku.Direction;

public class SudokuHighlightAction extends SudokuAction {
    private final Direction directionToHighlight;

    public SudokuHighlightAction(Direction directionToHighlight) {
        this.directionToHighlight = directionToHighlight;
    }

    public Direction getDirectionToHighlight() {
        return this.directionToHighlight;
    }
}

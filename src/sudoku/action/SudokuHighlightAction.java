package sudoku.action;

import java.awt.*;

public class SudokuHighlightAction extends SudokuAction {
    private final Point pointToHighlight;

    public SudokuHighlightAction(Point pointToHighlight) {
        this.pointToHighlight = pointToHighlight;
    }

    public Point getPointToHighlight() {
        return this.pointToHighlight;
    }
}

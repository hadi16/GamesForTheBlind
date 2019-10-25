package sudoku.action;

public class SudokuReadPositionAction extends SudokuAction {
    private final boolean readRow;

    public SudokuReadPositionAction(boolean readRow) {
        this.readRow = readRow;
    }

    public boolean isReadRow() {
        return this.readRow;
    }
}

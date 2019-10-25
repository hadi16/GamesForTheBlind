package sudoku.action;

public class SudokuUnrecognizedKeyAction extends SudokuAction {
    private final char unrecognizedKey;

    public SudokuUnrecognizedKeyAction(char unrecognizedKey) {
        this.unrecognizedKey = unrecognizedKey;
    }

    public char getUnrecognizedKey() {
        return this.unrecognizedKey;
    }
}

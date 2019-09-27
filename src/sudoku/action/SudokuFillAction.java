package sudoku.action;

public class SudokuFillAction extends SudokuAction {
    private final int numberToFill;

    public SudokuFillAction(int numberToFill) {
        this.numberToFill = numberToFill;
    }

    public int getNumberToFill() {
        return this.numberToFill;
    }
}

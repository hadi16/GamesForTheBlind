package gamesforblind.sudoku.action;

/**
 * Fill action class used for inputting numbers into the proper cells
 */
public class SudokuFillAction extends SudokuAction {
    private final int numberToFill;

    public SudokuFillAction(int numberToFill) {
        this.numberToFill = numberToFill;
    }

    public int getNumberToFill() {
        return this.numberToFill;
    }
}

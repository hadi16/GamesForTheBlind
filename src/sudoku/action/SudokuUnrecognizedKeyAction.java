package sudoku.action;

public class SudokuUnrecognizedKeyAction extends SudokuAction {
    private final int keyCode;

    public SudokuUnrecognizedKeyAction(int keyCode) {
        this.keyCode = keyCode;
    }

    public int getKeyCode() {
        return keyCode;
    }
}

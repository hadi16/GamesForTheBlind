package gamesforblind.loader;

public enum SudokuType {
    FOUR_BY_FOUR(4), NINE_BY_NINE(9);

    private final int sudokuBoardSize;

    SudokuType(int sudokuBoardSize) {
        this.sudokuBoardSize = sudokuBoardSize;
    }

    public int getSudokuBoardSize() {
        return this.sudokuBoardSize;
    }
}

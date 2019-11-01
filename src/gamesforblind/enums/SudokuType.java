package gamesforblind.enums;

/**
 * Enumeration for the different types of Sudoku that are supported in the game (e.g. 4x4 and 9x9).
 */
public enum SudokuType {
    FOUR_BY_FOUR(2, 2, 4),
    SIX_BY_SIX(3, 2, 6),
    NINE_BY_NINE(3, 3, 9);

    private final int blockWidth;
    private final int blockHeight;
    private final int sudokuBoardSize;

    SudokuType(int blockWidth, int blockHeight, int sudokuBoardSize) {
        this.blockWidth = blockWidth;
        this.blockHeight = blockHeight;
        this.sudokuBoardSize = sudokuBoardSize;
    }

    public int getBlockWidth() {
        return this.blockWidth;
    }

    public int getBlockHeight() {
        return this.blockHeight;
    }

    public int getSudokuBoardSize() {
        return this.sudokuBoardSize;
    }
}

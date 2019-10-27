package gamesforblind.loader.enums;

/**
 * Enumeration for the different types of Sudoku that are supported in the game (e.g. 4x4 and 9x9).
 */
public enum SudokuType {
    FOUR_BY_FOUR(4), NINE_BY_NINE(9);

    /**
     * The size of the given Sudoku game board (e.g. 4x4 --> sudokuBoardSize = 4).
     */
    private final int sudokuBoardSize;

    /**
     * Creates a new {@link SudokuType}
     *
     * @param sudokuBoardSize The size of the given Sudoku game board (e.g. 4x4 --> sudokuBoardSize = 4).
     */
    SudokuType(int sudokuBoardSize) {
        this.sudokuBoardSize = sudokuBoardSize;
    }

    /**
     * Getter for the sudokuBoardSize instance variable.
     *
     * @return The size of the given Sudoku game board (e.g. 4x4 --> sudokuBoardSize = 4).
     */
    public int getSudokuBoardSize() {
        return this.sudokuBoardSize;
    }
}

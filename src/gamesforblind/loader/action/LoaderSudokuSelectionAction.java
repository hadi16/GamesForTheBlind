package gamesforblind.loader.action;

import gamesforblind.loader.enums.SudokuType;

/**
 * Action for when the user makes a selection between a 4x4 and 9x9 Sudoku game.
 */
public class LoaderSudokuSelectionAction extends LoaderAction {
    /**
     * Which Sudoku game the user has selected (e.g. 4x4 or 9x9).
     */
    private final SudokuType sudokuType;

    /**
     * Creates a new {@link LoaderSudokuSelectionAction}
     * @param sudokuType The type of Sudoku game that the user has selected (e.g. 4x4 or 9x9).
     */
    public LoaderSudokuSelectionAction(SudokuType sudokuType) {
        this.sudokuType = sudokuType;
    }

    /**
     * Getter for the sudokuType instance variable.
     * @return Which Sudoku game the user has selected (e.g. 4x4 or 9x9).
     */
    public SudokuType getSudokuType() {
        return this.sudokuType;
    }
}

package gamesforblind.sudoku.interfaces;

import gamesforblind.enums.SudokuType;
import gamesforblind.sudoku.generator.Grid;

/**
 * The new arrow key interface for our Sudoku game. Inherits from {@link SudokuKeyboardInterface}.
 * TODO: Implement this.
 */
public abstract class SudokuArrowKeyInterface extends SudokuKeyboardInterface {
    /**
     * Creates a new SudokuArrowKeyInterface.
     *
     * @param sudokuType Whether the Sudoku board is a 4x4, 6x6, or 9x9.
     * @param sudokuGrid The Sudoku board as a {@link Grid} object.
     */
    public SudokuArrowKeyInterface(SudokuType sudokuType, Grid sudokuGrid) {
        super(sudokuType, sudokuGrid);
    }
}

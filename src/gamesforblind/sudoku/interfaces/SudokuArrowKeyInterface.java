package gamesforblind.sudoku.interfaces;

import gamesforblind.enums.SudokuType;
import gamesforblind.sudoku.generator.Grid;

public abstract class SudokuArrowKeyInterface extends SudokuKeyboardInterface {
    public SudokuArrowKeyInterface(SudokuType sudokuType, Grid sudokuGrid) {
        super(sudokuType, sudokuGrid);
    }
}

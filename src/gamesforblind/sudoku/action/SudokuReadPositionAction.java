package gamesforblind.sudoku.action;

import gamesforblind.sudoku.enums.SudokuSection;

public class SudokuReadPositionAction extends SudokuAction {
    private final SudokuSection sudokuSection;

    public SudokuReadPositionAction(SudokuSection sudokuSection) {
        this.sudokuSection = sudokuSection;
    }

    public SudokuSection getSudokuSection() {
        return this.sudokuSection;
    }
}

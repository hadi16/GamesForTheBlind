package gamesforblind.loader.action;

import gamesforblind.loader.SudokuType;

public class LoaderSudokuSelectionAction extends LoaderAction {
    private final SudokuType sudokuType;

    public LoaderSudokuSelectionAction(SudokuType sudokuType) {
        this.sudokuType = sudokuType;
    }

    public SudokuType getSudokuType() {
        return this.sudokuType;
    }
}

package sudoku;

import sudoku.generator.Generator;
import sudoku.generator.Grid;

public class SudokuState {
    private final int sudokuBoardSize;
    private final Grid sudokuGrid;

    private Direction selectedBlockDirection;
    private Direction selectedSquareDirection;

    public SudokuState(int sudokuBoardSize) {
        int numberOfEmptyCells = (sudokuBoardSize * sudokuBoardSize) / 3;

        this.sudokuBoardSize = sudokuBoardSize;
        this.sudokuGrid = new Generator(sudokuBoardSize).generate(numberOfEmptyCells);
    }

    public void setHighlightedDirection(Direction directionToSet) {
        if (directionToSet == null) {
            if (this.selectedSquareDirection != null) {
                this.selectedSquareDirection = null;
            } else {
                this.selectedBlockDirection = null;
            }
            return;
        }

        if (this.selectedBlockDirection != null && this.selectedSquareDirection != null) {
            System.err.println("You have already selected both a block & square on the board!");
            return;
        }

        if (this.selectedBlockDirection == null) {
            this.selectedBlockDirection = directionToSet;
            return;
        }

        this.selectedSquareDirection = directionToSet;
    }

    public int getSudokuBoardSize() {
        return this.sudokuBoardSize;
    }

    public Grid getSudokuGrid() {
        return this.sudokuGrid;
    }

    public Direction getSelectedBlockDirection() {
        return this.selectedBlockDirection;
    }

    public Direction getSelectedSquareDirection() {
        return this.selectedSquareDirection;
    }
}

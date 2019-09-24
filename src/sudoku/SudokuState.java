package sudoku;

import sudoku.enums.Direction;
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

    public SudokuState(SudokuState originalState) {
        this.sudokuBoardSize = originalState.sudokuBoardSize;
        this.sudokuGrid = new Grid(originalState.sudokuGrid);

        // Enums are immutable set (don't need copy constructor for them).
        this.selectedBlockDirection = originalState.selectedBlockDirection;
        this.selectedSquareDirection = originalState.selectedSquareDirection;
    }

    public void setSquareNumber(int numberToFill) {
        if (this.selectedSquareDirection == null || this.selectedBlockDirection == null) {
            System.err.println("YOu didn't select a square to fill first!");
            return;
        }
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

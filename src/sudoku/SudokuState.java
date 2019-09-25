package sudoku;

import sudoku.enums.Direction;
import sudoku.generator.Cell;
import sudoku.generator.Generator;
import sudoku.generator.Grid;

import java.awt.*;
import java.util.ArrayList;

public class SudokuState {
    private final int sudokuBoardSize;
    private final Grid sudokuGrid;

    private final ArrayList<Point> originallyFilledSquares;

    private Direction selectedBlockDirection;
    private Direction selectedSquareDirection;

    public SudokuState(int sudokuBoardSize) {
        int numberOfEmptyCells = (sudokuBoardSize * sudokuBoardSize) / 3;

        this.sudokuBoardSize = sudokuBoardSize;
        this.sudokuGrid = new Generator(sudokuBoardSize).generate(numberOfEmptyCells);

        this.originallyFilledSquares = this.initializeOriginallyFilledSquares();
    }

    public SudokuState(SudokuState originalState) {
        this.sudokuBoardSize = originalState.sudokuBoardSize;
        this.sudokuGrid = new Grid(originalState.sudokuGrid);

        // Enums are immutable set (don't need copy constructor for them).
        this.selectedBlockDirection = originalState.selectedBlockDirection;
        this.selectedSquareDirection = originalState.selectedSquareDirection;

        ArrayList<Point> originallyFilledSquares = new ArrayList<>();
        for (Point point : originalState.originallyFilledSquares) {
            originallyFilledSquares.add(new Point(point));
        }
        this.originallyFilledSquares = originallyFilledSquares;
    }

    private ArrayList<Point> initializeOriginallyFilledSquares() {
        ArrayList<Point> originallyFilledSquares = new ArrayList<>();
        for (int rowIdx = 0; rowIdx < this.sudokuBoardSize; rowIdx++) {
            for (int columnIdx = 0; columnIdx < this.sudokuBoardSize; columnIdx++) {
                Cell cellAtPosition = this.sudokuGrid.getCell(rowIdx, columnIdx);
                if (cellAtPosition.getValue() != 0) {
                    originallyFilledSquares.add(new Point(columnIdx, rowIdx));
                }
            }
        }
        return originallyFilledSquares;
    }

    public void setSquareNumber(int numberToFill) {
        if (!(numberToFill > 0 && numberToFill <= this.sudokuBoardSize)) {
            System.err.println("The number to fill must be between 1 and " + this.sudokuBoardSize);
            return;
        }

        if (this.selectedSquareDirection == null || this.selectedBlockDirection == null) {
            System.err.println("You didn't select a square to fill first!");
            return;
        }

        Point squarePointToSet = Direction.directionsToSudokuPoint(
                (int) Math.sqrt(this.sudokuBoardSize), this.selectedBlockDirection, this.selectedSquareDirection
        );

        Cell cellToSet = this.sudokuGrid.getCell(squarePointToSet.y, squarePointToSet.x);
        if (cellToSet.getValue() != 0) {
            System.err.println("This cell is already set!");
            return;
        }

        if (!this.sudokuGrid.isValidValueForCell(cellToSet, numberToFill)) {
            System.err.println("This value is invalid for the cell!");
            return;
        }

        cellToSet.setValue(numberToFill);
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

    public ArrayList<Point> getOriginallyFilledSquares() {
        return this.originallyFilledSquares;
    }
}

package gamesforblind.sudoku.interfaces;

import gamesforblind.enums.InputType;
import gamesforblind.enums.SudokuType;
import gamesforblind.sudoku.generator.Cell;
import gamesforblind.sudoku.generator.Grid;

import java.awt.*;
import java.util.Map;
import java.util.Optional;

public abstract class SudokuKeyboardInterface {
    protected final SudokuType sudokuType;
    private final Grid sudokuGrid;

    protected SudokuKeyboardInterface(SudokuType sudokuType, Grid sudokuGrid) {
        this.sudokuType = sudokuType;
        this.sudokuGrid = sudokuGrid;
    }

    public abstract Optional<Point> getSelectedPoint();

    public abstract void setHighlightedPoint(Point pointToSet, InputType inputType);

    public abstract Map<Integer, Point> getKeyCodeToPointMapping();

    public Optional<Cell> getSelectedCell() {
        Optional<Point> selectedPoint = this.getSelectedPoint();

        if (selectedPoint.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(
                this.sudokuGrid.getCell(selectedPoint.get().y, selectedPoint.get().x)
        );
    }
}

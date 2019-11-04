package gamesforblind.sudoku.interfaces;

import gamesforblind.enums.InputType;
import gamesforblind.enums.SudokuType;
import gamesforblind.sudoku.action.SudokuHighlightAction;
import gamesforblind.sudoku.generator.Cell;
import gamesforblind.sudoku.generator.Grid;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Map;
import java.util.Optional;

/**
 * An abstract class that all Sudoku keyboard interfaces inherit from.
 * Allows multiple keyboard interfaces to exist seamlessly in the game.
 */
public abstract class SudokuKeyboardInterface {
    /**
     * Whether the Sudoku board is a 4x4, 6x6, or 9x9.
     */
    protected final SudokuType sudokuType;

    /**
     * The Sudoku board as a {@link Grid} object.
     */
    private final Grid sudokuGrid;

    /**
     * Creates a new SudokuKeyboardInterface.
     *
     * @param sudokuType Whether the Sudoku board is a 4x4, 6x6, or 9x9.
     * @param sudokuGrid The Sudoku board as a {@link Grid} object.
     */
    protected SudokuKeyboardInterface(SudokuType sudokuType, Grid sudokuGrid) {
        this.sudokuType = sudokuType;
        this.sudokuGrid = sudokuGrid;
    }

    /**
     * Gets the currently selected {@link Point} in the game. Note: value is wrapped by an {@link Optional}.
     *
     * @return The currently selected Point wrapped by an {@link Optional}. Or, empty() if no square is selected.
     */
    public abstract Optional<Point> getSelectedPoint();

    /**
     * Sets the currently highlighted {@link Point} in the game.
     *
     * @param pointToSet The {@link Point} that was stored in a {@link SudokuHighlightAction}.
     * @param inputType  Whether this action was made with a keyboard or a mouse.
     */
    public abstract void setHighlightedPoint(Point pointToSet, InputType inputType);

    /**
     * Some keyCodes (as defined in {@link KeyEvent}) map to a given {@link Point} as determined by this mapping.
     * Note: this is done with key codes instead of characters to allow for the arrow keys to be mapped.
     *
     * @return A mapping between key codes as defined in {@link KeyEvent} & given {@link Point}s.
     */
    public abstract Map<Integer, Point> getKeyCodeToPointMapping();

    /**
     * Gets the currently selected {@link Cell} in the game. Note: value is wrapped by an {@link Optional}.
     *
     * @return The currently selected Cell wrapped by an {@link Optional}. Or, empty() if no square is selected.
     */
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

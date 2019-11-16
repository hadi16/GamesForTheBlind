package gamesforblind.sudoku.interfaces;

import gamesforblind.enums.ArrowKeyDirection;
import gamesforblind.enums.InputType;
import gamesforblind.sudoku.SudokuState;
import gamesforblind.sudoku.action.SudokuHighlightAction;
import gamesforblind.sudoku.action.SudokuHotKeyAction;
import gamesforblind.sudoku.generator.Cell;
import gamesforblind.sudoku.generator.Grid;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

/**
 * An abstract class that all Sudoku keyboard interfaces inherit from.
 * Allows multiple keyboard interfaces to exist seamlessly in the game.
 */
public abstract class SudokuKeyboardInterface {
    /**
     * Mapping between key codes (as defined in {@link KeyEvent}) to hot key actions.
     * Note: the CTRL key must be pressed down to trigger any hot key.
     */
    public final Map<Integer, SudokuHotKeyAction> keyCodeToHotKeyAction;

    protected final SudokuState sudokuState;

    /**
     * Creates a new SudokuKeyboardInterface.
     *
     * @param sudokuState           The state of the Sudoku game.
     * @param keyCodeToHotKeyAction Mapping between key codes (as defined in {@link KeyEvent}) to hot key actions.
     */
    protected SudokuKeyboardInterface(SudokuState sudokuState, Map<Integer, SudokuHotKeyAction> keyCodeToHotKeyAction) {
        this.sudokuState = sudokuState;
        this.keyCodeToHotKeyAction = keyCodeToHotKeyAction;
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
     * Sets the currently highlighted {@link Point} in the game with a hot key.
     *
     * @param arrowKeyDirection The {@link ArrowKeyDirection} that was pressed with this hot key (e.g. left arrow key).
     */
    public abstract void setHighlightedPoint(ArrowKeyDirection arrowKeyDirection);

    /**
     * Some keyCodes (as defined in {@link KeyEvent}) map to a given {@link Point} as determined by this mapping.
     * Note: this is done with key codes instead of characters to allow for the arrow keys to be mapped.
     *
     * @return A mapping between key codes as defined in {@link KeyEvent} & given {@link Point}s.
     */
    public abstract Map<Integer, Point> getKeyCodeToPointMapping();

    /**
     * Gets a list of {@link Point}s that should be highlighted in green on the Sudoku board.
     *
     * @return List of {@link Point}s that should be highlighted by the Sudoku GUI.
     */
    public abstract ArrayList<Point> getHighlightedPointList();

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

        Grid sudokuGrid = this.sudokuState.getSudokuGrid();
        return Optional.of(sudokuGrid.getCell(selectedPoint.get().y, selectedPoint.get().x));
    }

    /**
     * Determines if passed {@link Point} is in bounds. Ultimately prevents an {@link ArrayIndexOutOfBoundsException}.
     * Each {@link Point} coordinate should be between 0 & sudokuBoardSize.
     *
     * @param pointToSet The point that the user wishes to set (may be out of bounds).
     * @return true if the given {@link Point} is in bounds (otherwise, false).
     */
    protected boolean selectedPointInBounds(Point pointToSet) {
        int boardSize = this.sudokuState.getSudokuType().getSudokuBoardSize();
        return pointToSet.x >= 0 && pointToSet.y >= 0 && pointToSet.x < boardSize && pointToSet.y < boardSize;
    }
}

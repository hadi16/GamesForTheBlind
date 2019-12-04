package gamesforblind.sudoku.interfaces;

import gamesforblind.enums.ArrowKeyDirection;
import gamesforblind.enums.InputType;
import gamesforblind.sudoku.SudokuState;
import gamesforblind.sudoku.action.SudokuAction;
import gamesforblind.sudoku.action.SudokuHighlightAction;
import gamesforblind.sudoku.action.SudokuHotKeyAction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
    protected final SudokuState sudokuState;
    /**
     * Mapping between key codes (as defined in {@link KeyEvent}) to hot key actions.
     * Note: the CTRL key must be pressed down to trigger any hot key.
     */
    private final Map<Integer, SudokuHotKeyAction> keyCodeToHotKeyAction;
    /**
     * The {@link SudokuAction} to send when the space bar is pressed.
     */
    private final SudokuAction spacebarAction;

    /**
     * Creates a new SudokuKeyboardInterface.
     *
     * @param sudokuState           The state of the Sudoku game.
     * @param keyCodeToHotKeyAction Mapping between key codes (as defined in {@link KeyEvent}) to hot key actions.
     * @param spacebarAction        The {@link SudokuAction} to send when the space bar is pressed.
     */
    protected SudokuKeyboardInterface(
            @NotNull SudokuState sudokuState,
            @NotNull Map<Integer, SudokuHotKeyAction> keyCodeToHotKeyAction,
            @NotNull SudokuAction spacebarAction
    ) {
        this.sudokuState = sudokuState;
        this.keyCodeToHotKeyAction = keyCodeToHotKeyAction;
        this.spacebarAction = spacebarAction;
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
    public abstract void setHighlightedPoint(@Nullable Point pointToSet, @NotNull InputType inputType);

    /**
     * Sets the currently highlighted {@link Point} in the game with a hot key.
     *
     * @param arrowKeyDirection The {@link ArrowKeyDirection} that was pressed with this hot key (e.g. left arrow key).
     */
    public abstract void setHighlightedPoint(@NotNull ArrowKeyDirection arrowKeyDirection);

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
     * Determines if passed {@link Point} is in bounds. Ultimately prevents an {@link ArrayIndexOutOfBoundsException}.
     * Each {@link Point} coordinate should be between 0 & sudokuBoardSize.
     *
     * @param pointToSet The point that the user wishes to set (may be out of bounds).
     * @return true if the given {@link Point} is in bounds (otherwise, false).
     */
    protected boolean selectedPointInBounds(@NotNull Point pointToSet) {
        int boardSize = this.sudokuState.getSudokuType().getSudokuBoardSize();
        return pointToSet.x >= 0 && pointToSet.y >= 0 && pointToSet.x < boardSize && pointToSet.y < boardSize;
    }

    /**
     * Getter for keyCodeToHotKeyAction
     *
     * @return Mapping between key codes (as defined in {@link KeyEvent}) to hot key actions.
     */
    public Map<Integer, SudokuHotKeyAction> getKeyCodeToHotKeyAction() {
        return this.keyCodeToHotKeyAction;
    }

    /**
     * Getter for spacebarAction
     *
     * @return The {@link SudokuAction} to send when the space bar is pressed.
     */
    public SudokuAction getSpacebarAction() {
        return this.spacebarAction;
    }
}

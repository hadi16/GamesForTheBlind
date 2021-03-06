package gamesforblind.sudoku.interfaces;

import gamesforblind.enums.ArrowKeyDirection;
import gamesforblind.enums.InputType;
import gamesforblind.enums.SudokuType;
import gamesforblind.sudoku.SudokuState;
import gamesforblind.sudoku.action.SudokuHighlightAction;
import gamesforblind.sudoku.action.SudokuHotKeyAction;
import gamesforblind.sudoku.action.SudokuLocationAction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.*;

import static util.MapUtil.entry;
import static util.MapUtil.map;

/**
 * The new arrow key interface for our Sudoku game. Inherits from {@link SudokuKeyboardInterface}.
 */
public class SudokuArrowKeyInterface extends SudokuKeyboardInterface {
    /**
     * This maps directly to the given square on the board.
     * <p>
     * Examples:
     * - (2, 2) means the third column & third row.
     * - (3, 4) means the fourth column & fifth row.
     */
    private Point selectedPoint = new Point();

    /**
     * Creates a new SudokuArrowKeyInterface.
     *
     * @param sudokuState The state of the Sudoku game.
     */
    public SudokuArrowKeyInterface(@NotNull SudokuState sudokuState) {
        super(
                sudokuState,
                map(
                        entry(KeyEvent.VK_LEFT, new SudokuHotKeyAction(ArrowKeyDirection.LEFT)),
                        entry(KeyEvent.VK_RIGHT, new SudokuHotKeyAction(ArrowKeyDirection.RIGHT)),
                        entry(KeyEvent.VK_UP, new SudokuHotKeyAction(ArrowKeyDirection.UP)),
                        entry(KeyEvent.VK_DOWN, new SudokuHotKeyAction(ArrowKeyDirection.DOWN))
                ),
                // Read off the location of the selected square when the space bar is pressed.
                new SudokuLocationAction()
        );
    }

    /**
     * Gets the currently selected {@link Point} in the game. Note: value is wrapped by an {@link Optional}.
     *
     * @return The currently selected Point wrapped by an {@link Optional}.
     */
    @Override
    public Optional<Point> getSelectedPoint() {
        return Optional.of(this.selectedPoint);
    }

    /**
     * Sets the currently highlighted {@link Point} in the game.
     *
     * @param pointToSet The {@link Point} that was stored in a {@link SudokuHighlightAction} (should not be null).
     * @param inputType  Whether this action was made with a keyboard or a mouse.
     */
    @Override
    public void setHighlightedPoint(@Nullable Point pointToSet, @NotNull InputType inputType) {
        Objects.requireNonNull(pointToSet, "Cannot pass null Point to the arrow key interface!");

        // Case 1: the user clicks the mouse.
        if (inputType == InputType.MOUSE) {
            // Offset needed for the row (1, 2, etc.) & column labels ('A', 'B', etc.)
            Point selectedPoint = new Point(pointToSet.x - 1, pointToSet.y - 1);
            if (this.selectedPointInBounds(selectedPoint)) {
                this.selectedPoint = selectedPoint;
            }
            return;
        }

        // Case 2: the user presses an arrow key.
        Point selectedPoint = new Point(this.selectedPoint.x + pointToSet.x, this.selectedPoint.y + pointToSet.y);
        if (this.selectedPointInBounds(selectedPoint)) {
            this.selectedPoint = selectedPoint;
        }
    }

    /**
     * Sets the currently highlighted {@link Point} in the game with a hot key.
     *
     * @param arrowKeyDirection The {@link ArrowKeyDirection} that was pressed with this hot key (e.g. left arrow key).
     */
    @Override
    public void setHighlightedPoint(@NotNull ArrowKeyDirection arrowKeyDirection) {
        SudokuType sudokuType = this.sudokuState.getSudokuType();
        int maxPointIndex = sudokuType.getSudokuBoardSize() - 1;

        // Highlight square accordingly (e.g. LEFT --> all the way to the left)
        switch (arrowKeyDirection) {
            case LEFT:
                this.selectedPoint = new Point(0, this.selectedPoint.y);
                break;
            case RIGHT:
                this.selectedPoint = new Point(maxPointIndex, this.selectedPoint.y);
                break;
            case UP:
                this.selectedPoint = new Point(this.selectedPoint.x, 0);
                break;
            case DOWN:
                this.selectedPoint = new Point(this.selectedPoint.x, maxPointIndex);
                break;
        }
    }

    /**
     * Some keyCodes (as defined in {@link KeyEvent}) map to a given {@link Point} as determined by this mapping.
     * Note: this is done with key codes instead of characters to allow for the arrow keys to be mapped.
     *
     * @return A mapping between key codes as defined in {@link KeyEvent} & given {@link Point}s.
     */
    @Override
    public Map<Integer, Point> getKeyCodeToPointMapping() {
        // Left arrow key moves the selection left by a single square, etc.
        return map(
                entry(KeyEvent.VK_LEFT, new Point(-1, 0)),
                entry(KeyEvent.VK_RIGHT, new Point(1, 0)),
                entry(KeyEvent.VK_UP, new Point(0, -1)),
                entry(KeyEvent.VK_DOWN, new Point(0, 1))
        );
    }

    /**
     * Gets a list of {@link Point}s that should be highlighted in green on the Sudoku board.
     * For the arrow key GUI, this is always a single square on the board.
     *
     * @return List of {@link Point}s that should be highlighted by the GUI (for this interface, always single point).
     */
    @Override
    public ArrayList<Point> getHighlightedPointList() {
        return new ArrayList<>(Collections.singletonList(this.selectedPoint));
    }
}

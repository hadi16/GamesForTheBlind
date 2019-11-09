package gamesforblind.sudoku.interfaces;

import gamesforblind.enums.InputType;
import gamesforblind.enums.SudokuType;
import gamesforblind.sudoku.action.SudokuHighlightAction;
import gamesforblind.sudoku.generator.Grid;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Map;
import java.util.Optional;

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
     * @param sudokuType Whether the Sudoku board is a 4x4, 6x6, or 9x9.
     * @param sudokuGrid The Sudoku board as a {@link Grid} object.
     */
    public SudokuArrowKeyInterface(SudokuType sudokuType, Grid sudokuGrid) {
        super(sudokuType, sudokuGrid);
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
     * @param pointToSet The {@link Point} that was stored in a {@link SudokuHighlightAction}.
     * @param inputType  Whether this action was made with a keyboard or a mouse.
     */
    @Override
    public void setHighlightedPoint(Point pointToSet, InputType inputType) {
        if (inputType == InputType.MOUSE) {
            // Offset needed for the row (1, 2, etc.) & column labels ('A', 'B', etc.)
            this.selectedPoint = new Point(pointToSet.x - 1, pointToSet.y - 1);
            return;
        }

        this.selectedPoint = new Point(this.selectedPoint.x + pointToSet.x, this.selectedPoint.y + pointToSet.y);
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
        return Map.of(
                KeyEvent.VK_LEFT, new Point(-1, 0),
                KeyEvent.VK_RIGHT, new Point(1, 0),
                KeyEvent.VK_UP, new Point(0, -1),
                KeyEvent.VK_DOWN, new Point(0, 1)
        );
    }
}

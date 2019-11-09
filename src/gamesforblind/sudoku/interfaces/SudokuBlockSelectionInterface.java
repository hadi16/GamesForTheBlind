package gamesforblind.sudoku.interfaces;

import gamesforblind.enums.InputType;
import gamesforblind.enums.SudokuType;
import gamesforblind.sudoku.action.SudokuHighlightAction;
import gamesforblind.sudoku.generator.Cell;
import gamesforblind.sudoku.generator.Grid;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

/**
 * Old "block selection" interface where user first highlights a block on the board, then a square within that block.
 */
public class SudokuBlockSelectionInterface extends SudokuKeyboardInterface {
    /**
     * The keys 'S', 'D', 'X', and 'C' create a 4x4 square on the keyboard,
     * which maps to the points (0, 0), (1, 0), (0, 1), and (1, 1) respectively.
     * <p>
     * (This principle is closely modeled for the 9x9 key mappings).
     */
    private static final Map<SudokuType, Map<Integer, Point>> TYPE_TO_KEY_TO_POINT = Map.of(
            SudokuType.FOUR_BY_FOUR, Map.of(
                    KeyEvent.VK_S, new Point(0, 0),
                    KeyEvent.VK_D, new Point(1, 0),
                    KeyEvent.VK_X, new Point(0, 1),
                    KeyEvent.VK_C, new Point(1, 1)
            ),
            SudokuType.NINE_BY_NINE, Map.of(
                    KeyEvent.VK_W, new Point(0, 0),
                    KeyEvent.VK_E, new Point(1, 0),
                    KeyEvent.VK_R, new Point(2, 0),
                    KeyEvent.VK_S, new Point(0, 1),
                    KeyEvent.VK_D, new Point(1, 1),
                    KeyEvent.VK_F, new Point(2, 1),
                    KeyEvent.VK_X, new Point(0, 2),
                    KeyEvent.VK_C, new Point(1, 2),
                    KeyEvent.VK_V, new Point(2, 2)
            )
    );

    /**
     * The currently selected block.
     * <p>
     * x & y are always between 0 & block height/width - 1. For the 9x9, this would be 0 - 2.
     * For the 9x9, the lower rightmost block has a value of (2, 2), while the center block has a value of (1, 1).
     */
    private Point selectedBlockPoint;

    /**
     * The currently selected square within the selected block.
     * Note: if this is not null, then {@link #selectedBlockPoint} should not be null.
     * <p>
     * x & y are always between 0 & block height/width - 1. For the 9x9, this would be 0 - 2.
     * <p>
     * For the 9x9, the lower rightmost square within ANY block has a value of (2, 2),
     * while the center block within ANY block has a value of (1, 1).
     */
    private Point selectedSquarePoint;

    /**
     * Creates a new SudokuBlockSelectionInterface object.
     *
     * @param sudokuType Whether the board is a 4x4, 6x6, or 9x9.
     * @param sudokuGrid The Sudoku {@link Grid}.
     */
    public SudokuBlockSelectionInterface(SudokuType sudokuType, Grid sudokuGrid) {
        super(sudokuType, sudokuGrid);
    }

    /**
     * Gets a list of {@link Point}s that should be highlighted in green on the Sudoku board.
     * For the block selection GUI, this can be nothing, a single square, or a single block.
     *
     * @return List of {@link Point}s that should be highlighted by the Sudoku GUI.
     */
    @Override
    public ArrayList<Point> getHighlightedPointList() {
        // Case 1: nothing is selected.
        if (this.selectedBlockPoint == null && this.selectedSquarePoint == null) {
            return new ArrayList<>();
        }

        // Case 2: individual square is selected
        Optional<Point> maybeSelectedPoint = this.getSelectedPoint();
        if (maybeSelectedPoint.isPresent()) {
            return new ArrayList<>(Collections.singletonList(maybeSelectedPoint.get()));
        }

        // Case 3: block is selected
        ArrayList<Point> highlightedPointList = new ArrayList<>();
        int blockWidth = this.sudokuType.getBlockWidth();
        int blockHeight = this.sudokuType.getBlockHeight();
        for (int rowIndex = 0; rowIndex < blockHeight; rowIndex++) {
            for (int columnIndex = 0; columnIndex < blockWidth; columnIndex++) {
                highlightedPointList.add(new Point(
                        this.selectedBlockPoint.x * blockWidth + columnIndex,
                        this.selectedBlockPoint.y * blockHeight + rowIndex
                ));
            }
        }
        return highlightedPointList;
    }

    /**
     * Gets the currently selected {@link Point} in the game. Note: value is wrapped by an {@link Optional}.
     *
     * @return The currently selected Point wrapped by an {@link Optional}. Or, empty() if no square is selected.
     */
    @Override
    public Optional<Point> getSelectedPoint() {
        if (!this.isSquareHighlighted()) {
            return Optional.empty();
        }

        // This helps generalize to any board, including ones that don't have blocks
        // that are the square of a given number (example: 6x6).
        int sudokuBoardSize = this.sudokuType.getSudokuBoardSize();
        int numberOfBlocksWidth = sudokuBoardSize / this.sudokuType.getBlockWidth();
        int numberOfBlocksHeight = sudokuBoardSize / this.sudokuType.getBlockHeight();

        return Optional.of(new Point(
                this.selectedBlockPoint.x * numberOfBlocksWidth + this.selectedSquarePoint.x,
                this.selectedBlockPoint.y * numberOfBlocksHeight + this.selectedSquarePoint.y
        ));
    }

    /**
     * Sets the currently highlighted {@link Point} in the game.
     *
     * @param pointToSet The {@link Point} that was stored in a {@link SudokuHighlightAction}.
     * @param inputType  Whether this action was made with a keyboard or a mouse.
     */
    @Override
    public void setHighlightedPoint(Point pointToSet, InputType inputType) {
        /* Case 1: input is via a mouse. */
        if (inputType == InputType.MOUSE) {
            // Offset needed for the row (1, 2, etc.) & column labels ('A', 'B', etc.)
            pointToSet = new Point(pointToSet.x - 1, pointToSet.y - 1);

            // This helps generalize to any board, including ones that don't have blocks
            // that are the square of a given number (example: 6x6).
            int numberOfBlocksWidth = this.sudokuType.getSudokuBoardSize() / this.sudokuType.getBlockWidth();
            int numberOfBlocksHeight = this.sudokuType.getSudokuBoardSize() / this.sudokuType.getBlockHeight();

            Point blockPointToSet = new Point(
                    pointToSet.x / numberOfBlocksWidth, pointToSet.y / numberOfBlocksHeight
            );
            Point squarePointToSet = new Point(
                    pointToSet.x % numberOfBlocksWidth, pointToSet.y % numberOfBlocksHeight
            );

            // Case 1a: the user wishes to deselect the currently highlighted square.
            if (blockPointToSet.equals(this.selectedBlockPoint) && squarePointToSet.equals(this.selectedSquarePoint)) {
                this.selectedBlockPoint = null;
                this.selectedSquarePoint = null;
                return;
            }

            // Case 1b: the user wishes to select a square on the Sudoku board.
            this.selectedBlockPoint = blockPointToSet;
            this.selectedSquarePoint = squarePointToSet;
            return;
        }

        /* Case 2: input is via a keyboard. */

        // Case 2a: the point to set is null (reset either selected block or square point).
        if (pointToSet == null) {
            if (this.selectedSquarePoint != null) {
                this.selectedSquarePoint = null;
            } else {
                this.selectedBlockPoint = null;
            }
            return;
        }

        // Case 2b: the point to set is not null, but there is a cell that is already selected (do nothing).
        Optional<Cell> selectedCell = this.getSelectedCell();
        if (selectedCell.isPresent()) {
            return;
        }

        // Case 2c: the selected block point is null (meaning the selected square point must also be null).
        if (this.selectedBlockPoint == null) {
            this.selectedBlockPoint = pointToSet;
            return;
        }

        // Case 2d: both the selected block point & selected square point are null.
        this.selectedSquarePoint = pointToSet;
    }

    /**
     * For the block selection interface, I want to return a mapping that is appropriate for the given board type.
     * For example, the 4x4 boards map the keys 'S', 'D', 'X', and 'C'.
     * <p>
     * Some keyCodes (as defined in {@link KeyEvent}) map to a given {@link Point} as determined by this mapping.
     * Note: this is done with key codes instead of characters to allow for the arrow keys to be mapped.
     *
     * @return A mapping between key codes as defined in {@link KeyEvent} & given {@link Point}s.
     */
    @Override
    public Map<Integer, Point> getKeyCodeToPointMapping() {
        Map<Integer, Point> keyCodeToPointMapping = TYPE_TO_KEY_TO_POINT.get(this.sudokuType);
        if (keyCodeToPointMapping == null) {
            throw new IllegalArgumentException(
                    "Invalid Sudoku type passed to block selection interface: " + this.sudokuType
            );
        }
        return keyCodeToPointMapping;
    }

    /**
     * Helper function that determines whether a square is highlighted on the board.
     *
     * @return true if a square is highlighted (otherwise, false).
     */
    private boolean isSquareHighlighted() {
        return this.selectedBlockPoint != null && this.selectedSquarePoint != null;
    }
}

package gamesforblind.sudoku.interfaces;

import gamesforblind.enums.InputType;
import gamesforblind.enums.SudokuType;
import gamesforblind.sudoku.generator.Cell;
import gamesforblind.sudoku.generator.Grid;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Map;
import java.util.Optional;

public class SudokuBlockSelectionInterface extends SudokuKeyboardInterface {
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

    private Point selectedBlockPoint;
    private Point selectedSquarePoint;

    public SudokuBlockSelectionInterface(SudokuType sudokuType, Grid sudokuGrid) {
        super(sudokuType, sudokuGrid);
    }

    @Override
    public Optional<Point> getSelectedPoint() {
        if (!this.isSquareHighlighted()) {
            return Optional.empty();
        }

        int sudokuBoardSize = this.sudokuType.getSudokuBoardSize();
        int numberOfBlocksWidth = sudokuBoardSize / this.sudokuType.getBlockWidth();
        int numberOfBlocksHeight = sudokuBoardSize / this.sudokuType.getBlockHeight();

        return Optional.of(new Point(
                this.selectedBlockPoint.x * numberOfBlocksWidth + this.selectedSquarePoint.x,
                this.selectedBlockPoint.y * numberOfBlocksHeight + this.selectedSquarePoint.y
        ));
    }

    @Override
    public void setHighlightedPoint(Point pointToSet, InputType inputType) {
        if (inputType == InputType.MOUSE) {
            // Offset needed for the column labels ("A", "B", etc). For some reason,
            // the same offset is not needed for the row labels (not sure why).
            pointToSet = new Point(pointToSet.x, pointToSet.y - 1);

            int numberOfBlocksWidth = this.sudokuType.getSudokuBoardSize() / this.sudokuType.getBlockWidth();
            int numberOfBlocksHeight = this.sudokuType.getSudokuBoardSize() / this.sudokuType.getBlockHeight();

            Point blockPointToSet = new Point(
                    pointToSet.x / numberOfBlocksWidth, pointToSet.y / numberOfBlocksHeight
            );
            Point squarePointToSet = new Point(
                    pointToSet.x % numberOfBlocksWidth, pointToSet.y % numberOfBlocksHeight
            );

            if (blockPointToSet.equals(this.selectedBlockPoint) && squarePointToSet.equals(this.selectedSquarePoint)) {
                this.selectedBlockPoint = null;
                this.selectedSquarePoint = null;
                return;
            }

            this.selectedBlockPoint = blockPointToSet;
            this.selectedSquarePoint = squarePointToSet;
            return;
        }

        if (pointToSet == null) {
            if (this.selectedSquarePoint != null) {
                this.selectedSquarePoint = null;
            } else {
                this.selectedBlockPoint = null;
            }
            return;
        }

        Optional<Cell> selectedCell = this.getSelectedCell();
        if (selectedCell.isPresent()) {
            return;
        }

        if (this.selectedBlockPoint == null) {
            this.selectedBlockPoint = pointToSet;
            return;
        }

        this.selectedSquarePoint = pointToSet;
    }

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

    private boolean isSquareHighlighted() {
        return this.selectedBlockPoint != null && this.selectedSquarePoint != null;
    }

    public Point getSelectedBlockPoint() {
        return this.selectedBlockPoint;
    }

    public Point getSelectedSquarePoint() {
        return this.selectedSquarePoint;
    }
}

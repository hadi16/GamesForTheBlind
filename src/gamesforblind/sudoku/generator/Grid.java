package gamesforblind.sudoku.generator;

import gamesforblind.enums.SudokuType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * This class represents a Sudoku Grid consisting of a NxN matrix containing blocks of {@link Cell}s.
 */
public class Grid {
    /**
     * Whether the {@link Grid} represents a 4x4, 6x6, or 9x9 board.
     */
    private final SudokuType sudokuType;

    private final Cell[][] grid;

    /**
     * Creates a new Grid
     *
     * @param grid       The {@link Cell}s that represent the Sudoku board.
     * @param sudokuType Whether the Grid represents a 4x4, 6x6, or 9x9 board.
     */
    private Grid(Cell[][] grid, SudokuType sudokuType) {
        this.grid = grid;
        this.sudokuType = sudokuType;
    }

    /**
     * Copy constructor for Grid (makes a deep copy).
     *
     * @param originalGrid The original Grid that needs to be copied over.
     */
    public Grid(Grid originalGrid) {
        int[][] gridNumbers = new int[originalGrid.grid.length][];
        for (int i = 0; i < gridNumbers.length; i++) {
            gridNumbers[i] = new int[originalGrid.grid[i].length];
            for (int j = 0; j < gridNumbers[0].length; j++) {
                gridNumbers[i][j] = originalGrid.grid[i][j].getValue();
            }
        }

        Grid newGrid = Grid.of(gridNumbers, originalGrid.sudokuType);

        this.sudokuType = newGrid.sudokuType;
        this.grid = newGrid.grid;
    }

    /**
     * A factory method which returns a Grid of a given two-dimensional array of integers.
     *
     * @param grid       a two-dimensional int-array representation of a Grid
     * @param sudokuType Whether the Grid represents a 4x4, 6x6, or 9x9 board.
     * @return a Grid instance corresponding to the provided two-dimensional int-array
     */
    public static Grid of(int[][] grid, SudokuType sudokuType) {
        int sudokuBoardSize = sudokuType.getSudokuBoardSize();

        // Just checks a few things (null, etc.) & throws null if any are true.
        verifyGrid(grid, sudokuBoardSize);

        Cell[][] cells = new Cell[sudokuBoardSize][sudokuBoardSize];
        List<List<Cell>> rows = new ArrayList<>();
        List<List<Cell>> columns = new ArrayList<>();
        List<List<Cell>> boxes = new ArrayList<>();

        for (int i = 0; i < sudokuBoardSize; i++) {
            rows.add(new ArrayList<>());
            columns.add(new ArrayList<>());
            boxes.add(new ArrayList<>());
        }

        Cell lastCell = null;
        for (int row = 0; row < grid.length; row++) {
            for (int column = 0; column < grid[row].length; column++) {
                Cell cell = new Cell(grid[row][column]);
                cells[row][column] = cell;

                rows.get(row).add(cell);
                columns.get(column).add(cell);

                // Modified this to support generation of 6x6 boards. Needed modification
                // since the blocks are rectangles (2x3), which is unlike 3x3 or 2x2 blocks.
                int blockHeight = sudokuType.getBlockHeight();
                boxes.get((row / blockHeight) * blockHeight + column / sudokuType.getBlockWidth()).add(cell);

                if (lastCell != null) {
                    lastCell.setNextCell(cell);
                }

                lastCell = cell;
            }
        }

        for (int i = 0; i < sudokuBoardSize; i++) {
            List<Cell> row = rows.get(i);
            for (Cell cell : row) {
                List<Cell> rowNeighbors = new ArrayList<>(row);
                rowNeighbors.remove(cell);

                cell.setRowNeighbors(rowNeighbors);
            }

            List<Cell> column = columns.get(i);
            for (Cell cell : column) {
                List<Cell> columnNeighbors = new ArrayList<>(column);
                columnNeighbors.remove(cell);

                cell.setColumnNeighbors(columnNeighbors);
            }

            List<Cell> box = boxes.get(i);
            for (Cell cell : box) {
                List<Cell> boxNeighbors = new ArrayList<>(box);
                boxNeighbors.remove(cell);

                cell.setBoxNeighbors(boxNeighbors);
            }
        }

        return new Grid(cells, sudokuType);
    }

    /**
     * A static factory method which returns an empty Grid.
     *
     * @param sudokuType Whether the Grid represents a 4x4, 6x6, or 9x9 board.
     * @return an empty Grid
     */
    public static Grid emptyGrid(SudokuType sudokuType) {
        int[][] emptyGrid = new int[sudokuType.getSudokuBoardSize()][sudokuType.getSudokuBoardSize()];
        return Grid.of(emptyGrid, sudokuType);
    }

    /**
     * A static method that just makes sure that the generated Grid meets certain requirements (e.g. not null).
     *
     * @param grid            The Sudoku grid to check.
     * @param sudokuBoardSize The number of squares on each dimension of the given Sudoku board (e.g. 9x9 --> 9).
     */
    private static void verifyGrid(int[][] grid, int sudokuBoardSize) {
        if (grid == null) {
            throw new IllegalArgumentException("Grid must not be null");
        }

        if (grid.length != sudokuBoardSize) {
            throw new IllegalArgumentException("Grid must have " + sudokuBoardSize + " rows");
        }

        for (int[] row : grid) {
            if (row.length != sudokuBoardSize) {
                throw new IllegalArgumentException("Grid must have " + sudokuBoardSize + " columns");
            }

            for (int value : row) {
                if (value < 0 || value > sudokuBoardSize) {
                    throw new IllegalArgumentException("grid must contain values from 0-" + sudokuBoardSize);
                }
            }
        }
    }

    /**
     * Converts the current Grid instance to a 2D array of ints.
     *
     * @return A 2D array of ints representing the current Sudoku grid.
     */
    public int[][] toIntArray() {
        int[][] mappedGrid = new int[this.grid.length][];
        for (int i = 0; i < this.grid.length; i++) {
            mappedGrid[i] = new int[this.grid[i].length];
            for (int j = 0; j < this.grid[i].length; j++) {
                mappedGrid[i][j] = this.grid[i][j].getValue();
            }
        }
        return mappedGrid;
    }

    /**
     * Returns the size of this Grid. This method is useful if you want to iterate over all {@link Cell}s.
     * To access one cell use {@link #getCell(int, int)}. Note: This is the size of one dimension.
     * This Grid contains size x size {@link Cell}s.
     *
     * @return the size of this Grid
     */
    int getSize() {
        return this.grid.length;
    }

    /**
     * Returns the {@link Cell} at the given position within the Grid.
     * This Grid has 0 to {@link #getSize()} rows and 0 to {@link #getSize()} columns.
     *
     * @param row    the row which contains the {@link Cell}
     * @param column the column which contains the {@link Cell}
     * @return the {@link Cell} at the given position
     */
    public Cell getCell(int row, int column) {
        return this.grid[row][column];
    }

    /**
     * Checks if a given value is valid for a certain {@link Cell}.
     * A value is valid if it does not already exist in the same row, column and box.
     *
     * @param cell  the {@link Cell} to check
     * @param value the value to validate
     * @return true if the given value is valid or false otherwise
     */
    public boolean isValidValueForCell(Cell cell, int value) {
        return this.isValidInRow(cell, value) && this.isValidInColumn(cell, value) && this.isValidInBox(cell, value);
    }

    /**
     * Checks to see whether the number would be valid in the {@link Cell}'s row.
     *
     * @param cell  The {@link Cell} to validate.
     * @param value The value of the cell to check.
     * @return true if the number would be valid in the current row (otherwise, false).
     */
    private boolean isValidInRow(Cell cell, int value) {
        return !this.getRowValuesOf(cell).contains(value);
    }

    /**
     * Checks to see whether the number would be valid in the {@link Cell}'s column.
     *
     * @param cell  The {@link Cell} to validate.
     * @param value The value of the cell to check.
     * @return true if the number would be valid in the current column (otherwise, false).
     */
    private boolean isValidInColumn(Cell cell, int value) {
        return !this.getColumnValuesOf(cell).contains(value);
    }

    /**
     * Checks to see whether the number would be valid in the {@link Cell}'s block.
     *
     * @param cell  The {@link Cell} to validate.
     * @param value The value of the cell to check.
     * @return true if the number would be valid in the current block (otherwise, false).
     */
    private boolean isValidInBox(Cell cell, int value) {
        return !this.getBoxValuesOf(cell).contains(value);
    }

    /**
     * Gets all values for the given {@link Cell}'s row.
     *
     * @param cell The {@link Cell} to check.
     * @return A Collection of Integers that represents all of the values in the passed {@link Cell}'s row.
     */
    private Collection<Integer> getRowValuesOf(Cell cell) {
        List<Integer> rowValues = new ArrayList<>();
        for (Cell neighbor : cell.getRowNeighbors()) {
            rowValues.add(neighbor.getValue());
        }
        return rowValues;
    }

    /**
     * Gets all values for the given {@link Cell}'s column.
     *
     * @param cell The {@link Cell} to check.
     * @return A Collection of Integers that represents all of the values in the passed {@link Cell}'s column.
     */
    private Collection<Integer> getColumnValuesOf(Cell cell) {
        List<Integer> columnValues = new ArrayList<>();
        for (Cell neighbor : cell.getColumnNeighbors()) {
            columnValues.add(neighbor.getValue());
        }
        return columnValues;
    }

    /**
     * Gets all values for the given {@link Cell}'s block.
     *
     * @param cell The {@link Cell} to check.
     * @return A Collection of Integers that represents all of the values in the passed {@link Cell}'s block.
     */
    private Collection<Integer> getBoxValuesOf(Cell cell) {
        List<Integer> boxValues = new ArrayList<>();
        for (Cell neighbor : cell.getBoxNeighbors()) {
            boxValues.add(neighbor.getValue());
        }
        return boxValues;
    }

    /**
     * Returns the first empty {@link Cell} of this e. Note: The result is wrapped by an {@link Optional}.
     *
     * @return a non-null value containing the first empty {@link Cell} if present
     */
    public Optional<Cell> getFirstEmptyCell() {
        Cell firstCell = this.grid[0][0];
        if (firstCell.isEmpty()) {
            return Optional.of(firstCell);
        }

        return this.getNextEmptyCellOf(firstCell);
    }

    /**
     * Returns the next empty {@link Cell} consecutively to the given {@link Cell} in this Grid.
     * Note: The result is wrapped by an {@link Optional}.
     *
     * @param cell the {@link Cell} of which the next empty {@link Cell} should be obtained
     * @return a non-null value containing the next empty {@link Cell} if present
     */
    Optional<Cell> getNextEmptyCellOf(Cell cell) {
        Cell nextEmptyCell = null;

        while ((cell = cell.getNextCell()) != null) {
            if (!cell.isEmpty()) {
                continue;
            }

            nextEmptyCell = cell;
            break;
        }

        return Optional.ofNullable(nextEmptyCell);
    }

    /**
     * Returns a {@link String} representation of this Grid.
     *
     * @return a {@link String} representation of this Grid.
     */
    @Override
    public String toString() {
        return StringConverter.toString(this, (int) Math.sqrt(this.sudokuType.getSudokuBoardSize()));
    }
}

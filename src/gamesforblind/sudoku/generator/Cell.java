package gamesforblind.sudoku.generator;

import java.util.Collection;

/**
 * This class represents a Cell within a Sudoku {@link Grid}. <br><br> It features a couple of
 * convenient methods.
 */
public class Cell {
    private int value;
    private Collection<Cell> rowNeighbors;
    private Collection<Cell> columnNeighbors;
    private Collection<Cell> boxNeighbors;
    private Cell nextCell;

    public Cell(int value) {
        this.value = value;
    }

    /**
     * Returns the value of the Cell. <br><br> The value is a digit (1, 2,...) or 0 if the Cell is
     * empty.
     *
     * @return the value of the Cell.
     */
    public int getValue() {
        return this.value;
    }

    /**
     * Allows to change the value of the Cell.
     *
     * @param value the new value of the Cell
     */
    public void setValue(int value) {
        this.value = value;
    }

    /**
     * Indicates whether the Cell is empty or not.
     *
     * @return true if the Cell is empty, false otherwise
     */
    public boolean isEmpty() {
        return this.value == 0;
    }

    /**
     * Returns a {@link Collection} of all other Cells in the same row than this Cell.
     *
     * @return a {@link Collection} of row neighbors
     */
    public Collection<Cell> getRowNeighbors() {
        return this.rowNeighbors;
    }

    /**
     * Allows to set a {@link Collection} of Cells, which are interpreted to be in the same row.
     *
     * @param rowNeighbors a {@link Collection} of row neighbors
     */
    public void setRowNeighbors(Collection<Cell> rowNeighbors) {
        this.rowNeighbors = rowNeighbors;
    }

    /**
     * Returns a {@link Collection} of all other Cells in the same column than this Cell.
     *
     * @return a {@link Collection} of column neighbors
     */
    public Collection<Cell> getColumnNeighbors() {
        return this.columnNeighbors;
    }

    /**
     * Allows to set a {@link Collection} of Cells, which are interpreted to be in the same column.
     *
     * @param columnNeighbors a {@link Collection} of column neighbors
     */
    public void setColumnNeighbors(Collection<Cell> columnNeighbors) {
        this.columnNeighbors = columnNeighbors;
    }

    /**
     * Returns a {@link Collection} of all other Cells in the same box than this Cell.
     *
     * @return a {@link Collection} of box neighbors
     */
    public Collection<Cell> getBoxNeighbors() {
        return this.boxNeighbors;
    }

    /**
     * Allows to set a {@link Collection} of Cells, which are interpreted to be in the same box.
     *
     * @param boxNeighbors a {@link Collection} of box neighbors
     */
    public void setBoxNeighbors(Collection<Cell> boxNeighbors) {
        this.boxNeighbors = boxNeighbors;
    }

    /**
     * Returns the next Cell consecutive to this Cell. <br><br> This function returns the Cell to
     * the right of each Cell if the Cell is not the last Cell in a row. It returns the first Cell
     * of the next row of each Cell if the Cell is the last Cell in a row. For the very last Cell in
     * the very last row this function returns null.
     *
     * @return the next Cell consecutive to this Cell or null if it is the last Cell.
     */
    public Cell getNextCell() {
        return this.nextCell;
    }

    /**
     * Allows to set a Cell which is interpreted to be the next Cell consecutive to this Cell.
     *
     * @param nextCell the next Cell consecutive to this Cell.
     */
    public void setNextCell(Cell nextCell) {
        this.nextCell = nextCell;
    }
}

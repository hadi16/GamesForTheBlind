package gamesforblind.sudoku;

import org.jetbrains.annotations.NotNull;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Holds the original state of the Sudoku board.
 * The logger uses this to hold the original state of the board for XML serializing/de-serializing w/ JAXB.
 */
@XmlRootElement(name = "OriginalSudokuGrid")
@XmlAccessorType(XmlAccessType.NONE)
public class OriginalSudokuGrid {
    /**
     * A 2D array of ints representing the original state of the Sudoku board.
     */
    @XmlElement
    private int[][] grid;

    /**
     * Static factory method that creates a new instance of OriginalSudokuGrid using a 2D array of ints.
     *
     * @param originalGrid The 2D array of ints to initialize the class with.
     * @return A OriginalSudokuGrid containing the passed 2D array of ints.
     */
    public static OriginalSudokuGrid of(@NotNull int[][] originalGrid) {
        OriginalSudokuGrid originalSudokuGrid = new OriginalSudokuGrid();
        originalSudokuGrid.grid = originalGrid;
        return originalSudokuGrid;
    }

    /**
     * Getter for grid
     *
     * @return A 2D array of ints representing the original state of the Sudoku board.
     */
    public int[][] getGrid() {
        return this.grid;
    }
}

package gamesforblind.sudoku;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "OriginalSudokuGrid")
@XmlAccessorType(XmlAccessType.NONE)
public class OriginalSudokuGrid {
    @XmlElement
    private int[][] grid;

    private OriginalSudokuGrid() {
    }

    public OriginalSudokuGrid(OriginalSudokuGrid original) {
        this.grid = new int[original.grid.length][];
        for (int i = 0; i < original.grid.length; i++) {
            this.grid[i] = new int[original.grid[i].length];
            for (int j = 0; j < original.grid[i].length; j++) {
                this.grid[i][j] = original.grid[i][j];
            }
        }
    }

    public static OriginalSudokuGrid of(int[][] originalGrid) {
        OriginalSudokuGrid originalSudokuGrid = new OriginalSudokuGrid();
        originalSudokuGrid.grid = originalGrid;
        return originalSudokuGrid;
    }

    public int[][] getGrid() {
        return this.grid;
    }
}

package gamesforblind.sudoku.generator;

import gamesforblind.enums.SudokuType;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

import static gamesforblind.Constants.EMPTY_SUDOKU_SQUARE;

/**
 * A Generator to generate random Sudoku {@link Grid} instances.
 */
public class Generator {
    private final SudokuType sudokuType;
    private final Solver solver;

    /**
     * Constructs a new Generator instance.
     *
     * @param sudokuType Whether the Sudoku game is a 4x4, 6x6, or 9x9.
     */
    public Generator(@NotNull SudokuType sudokuType) {
        this.solver = new Solver(sudokuType.getSudokuBoardSize());
        this.sudokuType = sudokuType;
    }

    /**
     * Generates a random {@link Grid} instance with the given number of empty {@link Cell}s.
     * Note: The complexity for a human player increases with an higher amount of empty {@link Cell}s.
     *
     * @param numberOfEmptyCells the number of empty {@link Cell}s
     * @return a randomly filled Sudoku {@link Grid} with the given number of empty {@link Cell}s
     */
    public Grid generate(int numberOfEmptyCells) {
        Grid grid = this.generate();

        this.eraseCells(grid, numberOfEmptyCells);

        return grid;
    }

    /**
     * Erases the given amount of cell values from the specified {@link Grid}.
     *
     * @param grid               The Grid to delete cell values from.
     * @param numberOfEmptyCells The number of cell values to erase.
     */
    private void eraseCells(@NotNull Grid grid, int numberOfEmptyCells) {
        Random random = new Random();
        for (int i = 0; i < numberOfEmptyCells; i++) {
            int randomRow = random.nextInt(this.sudokuType.getSudokuBoardSize());
            int randomColumn = random.nextInt(this.sudokuType.getSudokuBoardSize());

            Cell cell = grid.getCell(randomRow, randomColumn);
            if (!cell.isEmpty()) {
                cell.setValue(EMPTY_SUDOKU_SQUARE);
            } else {
                i--;
            }
        }
    }

    /**
     * Generates a solved & random {@link Grid}.
     *
     * @return The generated & solved {@link Grid}.
     */
    private Grid generate() {
        Grid grid = Grid.emptyGrid(this.sudokuType);

        this.solver.solve(grid);

        return grid;
    }
}

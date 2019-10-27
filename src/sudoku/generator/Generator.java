package sudoku.generator;

import java.util.Random;

/**
 * A Generator to generate random Sudoku {@link Grid} instances.
 */
public class Generator {
    private final int sudokuBoardSize;
    private final Solver solver;

    /**
     * Constructs a new Generator instance.
     */
    public Generator(int sudokuBoardSize) {
        this.solver = new Solver(sudokuBoardSize);
        this.sudokuBoardSize = sudokuBoardSize;
    }

    /**
     * Generates a random {@link Grid} instance with the given number of empty {@link Cell}s.
     * <br><br>
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

    private void eraseCells(Grid grid, int numberOfEmptyCells) {
        Random random = new Random();
        for (int i = 0; i < numberOfEmptyCells; i++) {
            int randomRow = random.nextInt(this.sudokuBoardSize);
            int randomColumn = random.nextInt(this.sudokuBoardSize);

            Cell cell = grid.getCell(randomRow, randomColumn);
            if (!cell.isEmpty()) {
                cell.setValue(0);
            } else {
                i--;
            }
        }
    }

    private Grid generate() {
        Grid grid = Grid.emptyGrid(this.sudokuBoardSize);

        this.solver.solve(grid);

        return grid;
    }
}
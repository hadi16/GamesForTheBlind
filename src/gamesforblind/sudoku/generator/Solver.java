package gamesforblind.sudoku.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static gamesforblind.Constants.EMPTY_SUDOKU_SQUARE;

/**
 * A Solver is capable of solving a given Sudoku {@link Grid}.
 */
public class Solver {
    /**
     * The number of squares on each side of the Sudoku board (e.g. 6x6 --> 6).
     */
    private final int sudokuBoardSize;

    private final int[] values;

    /**
     * Constructs a new Solver instance.
     *
     * @param sudokuBoardSize The number of squares on each side of the Sudoku board (e.g. 6x6 --> 6).
     */
    public Solver(int sudokuBoardSize) {
        this.sudokuBoardSize = sudokuBoardSize;
        this.values = this.generateRandomValues();
    }

    /**
     * Solves a given {@link Grid} using backtracking.
     *
     * @param grid the {@link Grid} to solve
     * @throws IllegalStateException in case the provided {@link Grid} is invalid.
     */
    public void solve(Grid grid) {
        boolean solvable = this.solve(grid, grid.getFirstEmptyCell().orElse(null));
        if (!solvable) {
            throw new IllegalStateException("The provided grid is not solvable.");
        }
    }

    /*
     * Currently undoing solving stuff, need to use copy constructor - Callum
     * Custom solver much like above solve method, returns a boolean
     */
    /*public boolean superSolver(Grid grid) {
        return this.superSolver(grid, grid.getFirstEmptyCell().orElse(null));
    }

    // Custom solver to run recursively on input and make sure a solution is available
    private boolean superSolver(Grid grid, Cell cell) {
        if (cell == null) {
            return true;
        }
        for (int value : this.values) {
            if (grid.isValidValueForCell(cell, value)) {
                if (this.superSolver(grid, grid.getNextEmptyCellOf(cell).orElse(null))) {
                    return true;
                }
                cell.setValue(EMPTY);
            }
        }
        return false;
    }*/

    /**
     * Helper method to solve a {@link Cell} of the given Sudoku grid using backtracking.
     *
     * @param grid The Sudoku grid to solve.
     * @param cell The {@link Cell} on the Sudoku grid that needs to be solved.
     * @return true if the Grid is solvable, otherwise false.
     */
    private boolean solve(Grid grid, Cell cell) {
        if (cell == null) {
            return true;
        }

        for (int value : this.values) {
            if (grid.isValidValueForCell(cell, value)) {
                cell.setValue(value);

                if (this.solve(grid, grid.getNextEmptyCellOf(cell).orElse(null))) {
                    return true;
                }

                cell.setValue(EMPTY_SUDOKU_SQUARE);
            }
        }

        return false;
    }

    /**
     * Generates an array of random values between 1 and {@link #sudokuBoardSize}.
     *
     * @return An array of random values between 1 and {@link #sudokuBoardSize}.
     */
    private int[] generateRandomValues() {
        List<Integer> values = new ArrayList<>(EMPTY_SUDOKU_SQUARE);
        for (int i = 0; i < this.sudokuBoardSize; i++) {
            values.add(i + 1);
        }

        Random random = new Random();
        int i = 0, j = random.nextInt(this.sudokuBoardSize), tmp = values.get(j);
        for (; i < values.size(); i++, j = random.nextInt(this.sudokuBoardSize), tmp = values.get(j)) {
            if (i == j) {
                continue;
            }

            values.set(j, values.get(i));
            values.set(i, tmp);
        }

        return values.stream().mapToInt(Integer::intValue).toArray();
    }
}

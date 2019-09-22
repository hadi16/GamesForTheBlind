/*
 * Copyright (c) 2015 André Diermann
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package sudoku.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A Solver is capable of solving a given Sudoku {@link Grid}.
 */
public class Solver {
    private final int numSudokuSquares;

    private static final int EMPTY = 0;

    private final int[] values;

    /**
     * Constructs a new Solver instance.
     */
    public Solver(int numSudokuSquares) {
        this.numSudokuSquares = numSudokuSquares;
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

                cell.setValue(EMPTY);
            }
        }

        return false;
    }

    private int[] generateRandomValues() {
        List<Integer> values = new ArrayList<>(EMPTY);
        for (int i = 0; i < this.numSudokuSquares; i++) {
            values.add(i + 1);
        }

        Random random = new Random();
        for (int i = 0, j = random.nextInt(this.numSudokuSquares), tmp = values.get(j); i < values.size();
             i++, j = random.nextInt(this.numSudokuSquares), tmp = values.get(j)) {
            if (i == j) {
                continue;
            }

            values.set(j, values.get(i));
            values.set(i, tmp);
        }

        return values.stream().mapToInt(Integer::intValue).toArray();
    }
}

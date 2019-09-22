/*
 * Copyright (c) 2015 André Diermann
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package sudoku.generator;

import java.util.Random;

/**
 * A Solver is capable of solving a given Sudoku {@link Grid}.
 */
public class Solver {
    private static final int EMPTY = 0;

    private final int[] values;

    /**
     * Constructs a new Solver instance.
     */
    public Solver() {
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
        int[] values = {EMPTY, 1, 2, 3, 4, 5, 6, 7, 8, 9};

        Random random = new Random();
        for (int i = 0, j = random.nextInt(9), tmp = values[j]; i < values.length;
             i++, j = random.nextInt(9), tmp = values[j]) {
            if (i == j) {
                continue;
            }

            values[j] = values[i];
            values[i] = tmp;
        }

        return values;
    }
}

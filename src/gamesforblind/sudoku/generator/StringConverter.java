package gamesforblind.sudoku.generator;

import static gamesforblind.Constants.EMPTY_SUDOKU_SQUARE;

/**
 * Creates a String representation of a {@link Grid}.
 */
public class StringConverter {
    /**
     * Static factory method that returns a String representation of the passed {@link Grid}.
     *
     * @param grid           The {@link Grid} to create the String representation for.
     * @param numberOfBlocks The sqrt of the Sudoku board dimension (e.g. 9x9 --> 3). TODO: make this work for 6x6.
     * @return A String representation of the passed {@link Grid}.
     */
    public static String toString(Grid grid, int numberOfBlocks) {
        StringBuilder builder = new StringBuilder();
        int gridSize = grid.getSize();

        printTopBorder(builder);
        for (int row = 0; row < gridSize; row++) {
            printRowBorder(builder);
            for (int column = 0; column < gridSize; column++) {
                printValue(builder, grid, row, column);
                printRightColumnBorder(builder, column + 1, gridSize, numberOfBlocks);
            }
            printRowBorder(builder);
            builder.append("\n");
            printBottomRowBorder(builder, row + 1, gridSize, numberOfBlocks);
        }
        printBottomBorder(builder);

        return builder.toString();
    }

    /**
     * Appends the top border of the board to the StringBuilder.
     *
     * @param builder The StringBuilder to use for creating the String representation of the board.
     */
    private static void printTopBorder(StringBuilder builder) {
        builder.append("╔═══╤═══╤═══╦═══╤═══╤═══╦═══╤═══╤═══╗\n");
    }

    /**
     * Appends the row border of the board to the StringBuilder.
     *
     * @param builder The StringBuilder to use for creating the String representation of the board.
     */
    private static void printRowBorder(StringBuilder builder) {
        builder.append("║");
    }

    /**
     * Appends the value of the given {@link Cell} (at given row & column) in the {@link Grid} to the StringBuilder.
     *
     * @param builder The StringBuilder to use for creating the String representation of the board.
     * @param grid    The Sudoku grid to fetch the {@link Cell} from.
     * @param row     The applicable row value.
     * @param column  The applicable column value.
     */
    private static void printValue(StringBuilder builder, Grid grid, int row, int column) {
        int value = grid.getCell(row, column).getValue();
        String output = value != EMPTY_SUDOKU_SQUARE ? String.valueOf(value) : " ";
        builder.append(" ").append(output).append(" ");
    }

    /**
     * Appends the right column border of the board to the StringBuilder.
     *
     * @param builder        The StringBuilder to use for creating the String representation of the board.
     * @param column         The current column number.
     * @param size           The size of the Sudoku {@link Grid}.
     * @param numberOfBlocks The sqrt of the number of squares on each side of the board. TODO: make this work for 6x6.
     */
    private static void printRightColumnBorder(StringBuilder builder, int column, int size, int numberOfBlocks) {
        if (column == size) {
            return;
        }

        if (column % numberOfBlocks == 0) {
            builder.append("║");
        } else {
            builder.append("│");
        }
    }

    /**
     * Appends the bottom row border of the board to the StringBuilder.
     *
     * @param builder        The StringBuilder to use for creating the String representation of the board.
     * @param row            The current row number.
     * @param size           The size of the Sudoku {@link Grid}.
     * @param numberOfBlocks The sqrt of the number of squares on each side of the board. TODO: make this work for 6x6.
     */
    private static void printBottomRowBorder(StringBuilder builder, int row, int size, int numberOfBlocks) {
        if (row == size) {
            return;
        }

        if (row % numberOfBlocks == 0) {
            builder.append("╠═══╪═══╪═══╬═══╪═══╪═══╬═══╪═══╪═══╣\n");
        } else {
            builder.append("╟───┼───┼───╫───┼───┼───╫───┼───┼───╢\n");
        }
    }

    /**
     * Appends the bottom border of the board to the StringBuilder.
     *
     * @param builder The StringBuilder to use for creating the String representation of the board.
     */
    private static void printBottomBorder(StringBuilder builder) {
        builder.append("╚═══╧═══╧═══╩═══╧═══╧═══╩═══╧═══╧═══╝\n");
    }
}

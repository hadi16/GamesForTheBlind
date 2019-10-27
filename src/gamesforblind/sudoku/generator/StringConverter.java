package gamesforblind.sudoku.generator;

public class StringConverter {
    public static String toString(Grid grid, int numberOfBlocks) {
        StringBuilder builder = new StringBuilder();
        int size = grid.getSize();

        printTopBorder(builder);
        for (int row = 0; row < size; row++) {
            printRowBorder(builder);
            for (int column = 0; column < size; column++) {
                printValue(builder, grid, row, column);
                printRightColumnBorder(builder, column + 1, size, numberOfBlocks);
            }
            printRowBorder(builder);
            builder.append("\n");
            printBottomRowBorder(builder, row + 1, size, numberOfBlocks);
        }
        printBottomBorder(builder);

        return builder.toString();
    }

    private static void printTopBorder(StringBuilder builder) {
        builder.append("╔═══╤═══╤═══╦═══╤═══╤═══╦═══╤═══╤═══╗\n");
    }

    private static void printRowBorder(StringBuilder builder) {
        builder.append("║");
    }

    private static void printValue(StringBuilder builder, Grid grid, int row, int column) {
        int value = grid.getCell(row, column).getValue();
        String output = value != 0 ? String.valueOf(value) : " ";
        builder.append(" ").append(output).append(" ");
    }

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

    private static void printBottomBorder(StringBuilder builder) {
        builder.append("╚═══╧═══╧═══╩═══╧═══╧═══╩═══╧═══╧═══╝\n");
    }
}

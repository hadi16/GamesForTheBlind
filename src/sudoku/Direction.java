package sudoku;

import java.util.ArrayList;
import java.util.Collections;

public enum Direction {
    CENTER(0, 0),
    NORTH(0, -1),
    NORTHEAST(1, -1),
    EAST(1, 0),
    SOUTHEAST(1, 1),
    SOUTH(0, 1),
    SOUTHWEST(-1, 1),
    WEST(-1, 0),
    NORTHWEST(-1, -1);

    public final int x;
    public final int y;

    Direction(int x, int y) {
        this.x = x;
        this.y = y;
    }

    private static ArrayList<Integer> getValidBlockIndicesForDirectionIndex(int directionIndex, int numberOfBlocks) {
        ArrayList<Integer> validIndicesForDirection = new ArrayList<>();

        int currentIndexToAdd;
        switch (numberOfBlocks) {
            case 2:
                // -1 --> [0, 1]
                // 1 --> [2, 3]
                currentIndexToAdd = directionIndex + 1;
                break;
            case 3:
                // -1 --> [0, 1, 2]
                // 0 --> [3, 4, 5]
                // 1 --> [6, 7, 8]
                currentIndexToAdd = (directionIndex + 1) * 3;
                break;
            default:
                throw new IllegalArgumentException("Invalid value provided for the number of Sudoku blocks!");
        }

        while (validIndicesForDirection.size() != numberOfBlocks) {
            validIndicesForDirection.add(currentIndexToAdd);
            currentIndexToAdd++;
        }

        return validIndicesForDirection;
    }

    public static boolean blockInDirection(int rowIdx, int columnIdx, int numberOfBlocks, Direction blockDirection) {
        ArrayList<Integer> validIndicesForRow = getValidBlockIndicesForDirectionIndex(blockDirection.y, numberOfBlocks);
        ArrayList<Integer> validIndicesForCol = getValidBlockIndicesForDirectionIndex(blockDirection.x, numberOfBlocks);

        return validIndicesForRow.contains(rowIdx) && validIndicesForCol.contains(columnIdx);
    }

    public static boolean squareInDirection(
            int rowIdx, int columnIdx, int numberOfBlocks, Direction blockDirection, Direction squareDirection
    ) {
        if (!blockInDirection(rowIdx, columnIdx, numberOfBlocks, blockDirection)) {
            return false;
        }

        ArrayList<Integer> upperRighthandCornerIndices = new ArrayList<>(Collections.singletonList(0));
        while (upperRighthandCornerIndices.size() != numberOfBlocks) {
            upperRighthandCornerIndices.add(
                    Collections.max(upperRighthandCornerIndices) + 1
            );
        }

        int equivalentRowIdx = rowIdx;
        while (!upperRighthandCornerIndices.contains(equivalentRowIdx)) {
            equivalentRowIdx -= numberOfBlocks;
        }

        int equivalentColIdx = columnIdx;
        while (!upperRighthandCornerIndices.contains(equivalentColIdx)) {
            equivalentColIdx -= numberOfBlocks;
        }

        switch (numberOfBlocks) {
            case 2:
                return 2 * equivalentRowIdx - 1 == squareDirection.y && 2 * equivalentColIdx - 1 == squareDirection.x;
            case 3:
                return equivalentRowIdx - squareDirection.y == 1 && equivalentColIdx - squareDirection.x == 1;
            default:
                throw new IllegalArgumentException("Invalid value provided for the number of Sudoku blocks!");
        }
    }
}

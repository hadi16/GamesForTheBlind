package sudoku.enums;

import java.awt.*;
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

    private final int x;
    private final int y;

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

        ArrayList<Integer> upperRightHandCornerIndices = new ArrayList<>(Collections.singletonList(0));
        while (upperRightHandCornerIndices.size() != numberOfBlocks) {
            upperRightHandCornerIndices.add(
                    Collections.max(upperRightHandCornerIndices) + 1
            );
        }

        int equivalentRowIdx = rowIdx;
        while (!upperRightHandCornerIndices.contains(equivalentRowIdx)) {
            equivalentRowIdx -= numberOfBlocks;
        }

        int equivalentColIdx = columnIdx;
        while (!upperRightHandCornerIndices.contains(equivalentColIdx)) {
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

    private static Point getBlockPoint(int numberOfBlocks, Direction blockDirection) {
        for (int rowIdx = 0; rowIdx < numberOfBlocks; rowIdx++) {
            for (int columnIdx = 0; columnIdx < numberOfBlocks; columnIdx++) {
                boolean blockInDirection = blockInDirection(
                        rowIdx * numberOfBlocks,
                        columnIdx * numberOfBlocks,
                        numberOfBlocks,
                        blockDirection
                );
                if (blockInDirection) {
                    return new Point(columnIdx, rowIdx);
                }
            }
        }

        throw new IllegalArgumentException("Could not find the selected block point!");
    }

    private static ArrayList<Integer> getSquareIndicesToCheck(int blockPointIdx, int numberOfBlocks) {
        ArrayList<Integer> indicesToCheck = new ArrayList<>();
        int currentIdx = blockPointIdx * numberOfBlocks;
        while (indicesToCheck.size() != numberOfBlocks) {
            indicesToCheck.add(currentIdx);
            currentIdx++;
        }
        return indicesToCheck;
    }

    public static Point directionsToSudokuPoint(
            int numberOfBlocks, Direction blockDirection, Direction squareDirection
    ) {
        Point blockPoint = getBlockPoint(numberOfBlocks, blockDirection);

        ArrayList<Integer> rowIndicesToCheck = getSquareIndicesToCheck(blockPoint.y, numberOfBlocks);
        ArrayList<Integer> columnIndicesToCheck = getSquareIndicesToCheck(blockPoint.x, numberOfBlocks);

        for (int rowIdx : rowIndicesToCheck) {
            for (int columnIdx : columnIndicesToCheck) {
                if (squareInDirection(rowIdx, columnIdx, numberOfBlocks, blockDirection, squareDirection)) {
                    return new Point(columnIdx, rowIdx);
                }
            }
        }

        throw new IllegalArgumentException("Could not find the square row & column idx!");
    }
}

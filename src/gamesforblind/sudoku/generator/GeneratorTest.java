package gamesforblind.sudoku.generator;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GeneratorTest {
    /**
     * Tests that the correct number of {@link Cell}s are being deleted
     */
    @Test
    public void generateCorrectNumEmpty() {
        final int SUDOKU_BOARD_SIZE = 4;
        final int NUMBER_OF_EMPTY_CELLS = 4;

        Grid testGrid = new Generator(SUDOKU_BOARD_SIZE).generate(NUMBER_OF_EMPTY_CELLS);

        int actualNumberOfEmptyCells = 0;
        for (int rowIndex = 0; rowIndex < SUDOKU_BOARD_SIZE; rowIndex++) {
            for (int columnIndex = 0; columnIndex < SUDOKU_BOARD_SIZE; columnIndex++) {
                Cell currentCell = testGrid.getCell(rowIndex, columnIndex);
                if (currentCell.isEmpty()) {
                    actualNumberOfEmptyCells++;
                }
            }
        }

        assertEquals("Correct number of empty cells", NUMBER_OF_EMPTY_CELLS, actualNumberOfEmptyCells);
    }
}

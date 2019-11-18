import gamesforblind.enums.SudokuType;
import gamesforblind.sudoku.generator.Cell;
import gamesforblind.sudoku.generator.Generator;
import gamesforblind.sudoku.generator.Grid;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test class for {@link Generator}.
 */
public class GeneratorTest {
    /**
     * Tests that the correct number of {@link Cell}s are being deleted.
     */
    @Test
    public void generateCorrectNumEmpty() {
        final SudokuType SUDOKU_TYPE = SudokuType.FOUR_BY_FOUR;
        final int NUMBER_OF_EMPTY_CELLS = 4;

        Grid testGrid = new Generator(SUDOKU_TYPE).generate(NUMBER_OF_EMPTY_CELLS);

        int actualNumberOfEmptyCells = 0;
        for (int rowIndex = 0; rowIndex < SUDOKU_TYPE.getSudokuBoardSize(); rowIndex++) {
            for (int columnIndex = 0; columnIndex < SUDOKU_TYPE.getSudokuBoardSize(); columnIndex++) {
                Cell currentCell = testGrid.getCell(rowIndex, columnIndex);
                if (currentCell.isEmpty()) {
                    actualNumberOfEmptyCells++;
                }
            }
        }

        assertEquals("Correct number of empty cells", NUMBER_OF_EMPTY_CELLS, actualNumberOfEmptyCells);
    }
}

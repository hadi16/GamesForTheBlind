package sudoku.generator;

import junit.framework.TestCase;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GeneratorTest extends TestCase {
    Solver testSolver;
    Generator testGen = new Generator(4);

    /**
     * Tests that the correct number of {@Link cell}s are being deleted
     *
     */

    @Test
    void generateCorrectNumEmpty() {
        Grid testGrid = testGen.generate(4);
        int numEmpty = 0;
        for(int rowIndex = 0; rowIndex < 4; rowIndex ++){
            for(int colIndex = 0; colIndex < 4; colIndex++ ){
                Cell curCell = testGrid.getCell(rowIndex, colIndex);
                if (curCell.isEmpty() ){
                    numEmpty++;
                }
            }
        }

        assertEquals("Correct number of empty cells", 4, numEmpty);

    }
}
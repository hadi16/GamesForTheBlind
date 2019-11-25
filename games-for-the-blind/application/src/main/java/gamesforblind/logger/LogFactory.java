package gamesforblind.logger;

import gamesforblind.ProgramAction;
import gamesforblind.sudoku.OriginalSudokuGrid;
import gamesforblind.sudoku.generator.Grid;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static gamesforblind.Constants.LOG_FILES_DIRECTORY;

/**
 * Class that stores the objects/values that are essential to make a log.
 */
public class LogFactory {
    /**
     * All of the ProgramActions that have been made in the loader & game.
     */
    private final ArrayList<ProgramAction> programActionList = new ArrayList<>();

    /**
     * Need the original Sudoku boards, since they are typically randomly generated.
     */
    private final ArrayList<OriginalSudokuGrid> originalSudokuGridList = new ArrayList<>();

    /**
     * Creates a new LogFactory
     */
    public LogFactory() {
        if (!LOG_FILES_DIRECTORY.exists()) {
            // Recursively create any folders that are needed to create the log files directory.
            if (!LOG_FILES_DIRECTORY.mkdirs()) {
                throw new IllegalArgumentException("The log files directory could not be created properly!");
            }
        }
    }

    /**
     * Add a {@link ProgramAction} to the list of all actions in the loader & game.
     *
     * @param actionToAdd The {@link ProgramAction} to add to the list.
     */
    public void addProgramAction(@NotNull ProgramAction actionToAdd) {
        this.programActionList.add(actionToAdd);
    }

    /**
     * Removes a stored {@link OriginalSudokuGrid} from the front of the enclosed list of {@link OriginalSudokuGrid}s.
     *
     * @return An original state of the {@link Grid} in a logged game.
     */
    public OriginalSudokuGrid popOriginalGridFromFront() {
        if (this.originalSudokuGridList.isEmpty()) {
            throw new IllegalArgumentException("There are no more original Sudoku grids left to get!");
        }

        return this.originalSudokuGridList.remove(0);
    }

    /**
     * Adds an {@link OriginalSudokuGrid} to the end of the list.
     *
     * @param originalGridToAdd The object that holds an original state of the {@link Grid} in a logged game.
     */
    public void addOriginalSudokuGrid(@NotNull OriginalSudokuGrid originalGridToAdd) {
        this.originalSudokuGridList.add(originalGridToAdd);
    }

    /**
     * Getter for originalSudokuGridList
     *
     * @return The list of {@link OriginalSudokuGrid}s that were logged in the series of played Sudoku games.
     */
    public ArrayList<OriginalSudokuGrid> getOriginalSudokuGridList() {
        return this.originalSudokuGridList;
    }

    /**
     * Getter for programActionList
     *
     * @return A list of all of the ProgramActions in the loader & game.
     */
    public ArrayList<ProgramAction> getProgramActionList() {
        return this.programActionList;
    }
}

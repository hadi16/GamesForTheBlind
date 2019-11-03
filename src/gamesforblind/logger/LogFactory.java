package gamesforblind.logger;

import gamesforblind.ProgramAction;
import gamesforblind.sudoku.OriginalSudokuGrid;
import gamesforblind.sudoku.generator.Grid;

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
     * Need the original Sudoku board, since they are typically randomly generated.
     */
    private OriginalSudokuGrid originalSudokuGrid;

    /**
     * Creates a new {@link LogFactory}
     */
    public LogFactory() {
        if (!LOG_FILES_DIRECTORY.exists()) {
            // Recursively create any folders that are needed to create the log files directory.
            LOG_FILES_DIRECTORY.mkdirs();
        }
    }

    /**
     * Add a {@link ProgramAction} to the list of all actions in the loader & game.
     *
     * @param actionToAdd The {@link ProgramAction} to add to the list.
     */
    public void addProgramAction(ProgramAction actionToAdd) {
        this.programActionList.add(actionToAdd);
    }

    /**
     * Getter for originalSudokuGrid
     *
     * @return An object that holds the original state of the {@link Grid} in the logged game.
     */
    public OriginalSudokuGrid getOriginalSudokuGrid() {
        return this.originalSudokuGrid;
    }

    /**
     * Setter for originalSudokuGrid
     *
     * @param originalSudokuGrid The object that holds the original state of the {@link Grid} in the logged game.
     */
    public void setOriginalSudokuGrid(OriginalSudokuGrid originalSudokuGrid) {
        this.originalSudokuGrid = originalSudokuGrid;
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

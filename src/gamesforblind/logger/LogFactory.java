package gamesforblind.logger;

import gamesforblind.Constants;
import gamesforblind.ProgramAction;
import gamesforblind.sudoku.OriginalSudokuGrid;

import java.util.ArrayList;

import static gamesforblind.Constants.LOG_FILES_DIRECTORY;

public class LogFactory {
    private OriginalSudokuGrid originalSudokuGrid;
    private final ArrayList<ProgramAction> programActionList = new ArrayList<>();

    public LogFactory() {
        if (!LOG_FILES_DIRECTORY.exists()) {
            LOG_FILES_DIRECTORY.mkdirs();
        }
    }

    public void addProgramAction(ProgramAction actionToAdd) {
        if (!Constants.SAVE_LOGS) {
            return;
        }

        this.programActionList.add(actionToAdd);
    }

    public void setOriginalSudokuGrid(OriginalSudokuGrid originalSudokuGrid) {
        this.originalSudokuGrid = originalSudokuGrid;
    }

    public OriginalSudokuGrid getOriginalSudokuGrid() {
        return this.originalSudokuGrid;
    }

    public ArrayList<ProgramAction> getProgramActionList() {
        return this.programActionList;
    }
}
package gamesforblind.sudoku;

import gamesforblind.ProgramArgs;
import gamesforblind.enums.InterfaceType;
import gamesforblind.enums.SudokuType;
import gamesforblind.loader.GameLoader;
import gamesforblind.loader.action.LoaderExitAction;
import gamesforblind.logger.LogFactory;
import gamesforblind.sudoku.action.*;
import gamesforblind.sudoku.gui.SudokuFrame;
import gamesforblind.synthesizer.AudioPlayerExecutor;

/**
 * Game class that is called directly from the {@link GameLoader} class.
 * It receives actions & sends these actions to the {@link SudokuState} for the game.
 */
public class SudokuGame {
    private final GameLoader gameLoader;
    private final SudokuState sudokuState;
    private final SudokuFrame sudokuFrame;
    private final LogFactory logFactory;
    private final ProgramArgs programArgs;

    /**
     * Creates a new SudokuGame.
     *
     * @param gameLoader          The "main menu" for the games, which is needed when reopening this menu.
     * @param sudokuType          Whether the game is a 4x4, 6x6, or 9x9 variant.
     * @param audioPlayerExecutor Class used to execute the threaded audio player.
     * @param logFactory          Where all of the logs are stored or read from (depending on whether in playback mode).
     * @param programArgs         The program arguments that were passed.
     */
    public SudokuGame(GameLoader gameLoader, SudokuType sudokuType, AudioPlayerExecutor audioPlayerExecutor,
                      LogFactory logFactory, ProgramArgs programArgs) {
        this.gameLoader = gameLoader;
        this.programArgs = programArgs;
        this.logFactory = logFactory;

        InterfaceType selectedInterfaceType = programArgs.getSelectedInterfaceType();
        if (programArgs.isPlaybackMode()) {
            // Case 1: the program is in playback mode (call state constructor with the original state of the board).
            this.sudokuState = new SudokuState(
                    selectedInterfaceType, sudokuType, audioPlayerExecutor, logFactory.popOriginalGridFromFront()
            );
        } else {
            // Case 2: the program is not in playback mode (set the log factory's original Sudoku state).
            this.sudokuState = new SudokuState(selectedInterfaceType, sudokuType, audioPlayerExecutor);
            this.logFactory.addOriginalSudokuGrid(this.sudokuState.getOriginalGrid());
        }

        this.sudokuFrame = new SudokuFrame(
                this, this.sudokuState, sudokuType, programArgs.isPlaybackMode()
        );
    }

    /**
     * Receives an action as input and calls the proper {@link SudokuState} function.
     * Records the received action into the log factory (if not in playback mode).
     * Oftentimes, the updated state is sent to the GUI & the GUI is repainted.
     *
     * @param sudokuAction The action that was received.
     */
    public void receiveAction(SudokuAction sudokuAction) {
        if (!this.programArgs.isPlaybackMode()) {
            this.logFactory.addProgramAction(sudokuAction);
        }

        // Case 1: the user wishes to exit the game.
        if (sudokuAction instanceof SudokuExitAction) {
            this.gameLoader.receiveAction(new LoaderExitAction());
            return;
        }

        // If the game is over, go to main menu
        if (this.sudokuState.isGameOver()) {
            this.sudokuFrame.closeFrames();
            this.gameLoader.openLoaderInterface();
            return;
        }

        // Case 2: the user wants the instructions for the game to be read.
        if (sudokuAction instanceof SudokuInstructionsAction) {
            this.sudokuState.readInstructions();
            return;
        }

        // Case 3: the user wants a hint.
        if (sudokuAction instanceof SudokuHintKeyAction) {
            this.sudokuState.giveHint();
            this.sudokuFrame.repaintSudokuPanel();
            return;
        }

        // Case 4: the user has pressed an unrecognized key on the keyboard.
        if (sudokuAction instanceof SudokuUnrecognizedKeyAction) {
            SudokuUnrecognizedKeyAction sudokuUnrecognizedKeyAction = (SudokuUnrecognizedKeyAction) sudokuAction;
            this.sudokuState.readUnrecognizedKey(sudokuUnrecognizedKeyAction.getKeyCode());
            return;
        }

        // Case 5: the user hits a hot key (e.g. Ctrl + LEFT)
        if (sudokuAction instanceof SudokuHotKeyAction) {
            SudokuHotKeyAction sudokuHotKeyAction = (SudokuHotKeyAction) sudokuAction;
            this.sudokuState.setHighlightedPoint(sudokuHotKeyAction.getArrowKeyDirection());
            this.sudokuState.readSelectedSquare();
            this.sudokuFrame.repaintSudokuPanel();
            return;
        }

        // Case 6: the user wants to highlight a square/block on the board.
        if (sudokuAction instanceof SudokuHighlightAction) {
            SudokuHighlightAction highlightAction = (SudokuHighlightAction) sudokuAction;
            this.sudokuState.setHighlightedPoint(highlightAction.getPointToHighlight(), highlightAction.getInputType());
            this.sudokuState.readSelectedSquare();
            this.sudokuFrame.repaintSudokuPanel();
            return;
        }

        // Case 7: the user wants to fill a square on the board.
        if (sudokuAction instanceof SudokuFillAction) {
            SudokuFillAction sudokuFillAction = (SudokuFillAction) sudokuAction;
            this.sudokuState.setSquareNumber(sudokuFillAction.getNumberToFill());
            this.sudokuFrame.repaintSudokuPanel();
            return;
        }

        // Case 8: the user wants the current row/column/block to be read.
        if (sudokuAction instanceof SudokuReadPositionAction) {
            SudokuReadPositionAction sudokuReadPositionAction = (SudokuReadPositionAction) sudokuAction;
            this.sudokuState.readBoardSection(sudokuReadPositionAction.getSudokuSection());
            this.sudokuFrame.repaintSudokuPanel();
            return;
        }

        // Case 9: return to main menu.
        if (sudokuAction instanceof SudokuMainMenuAction) {
            this.sudokuFrame.closeFrames();
            this.gameLoader.openLoaderInterface();
            return;
        }

        // Case 10: restart the current Sudoku board.
        if (sudokuAction instanceof SudokuRestartAction) {
            if (this.programArgs.isPlaybackMode()) {
                this.sudokuState.resetSudokuState(this.logFactory.popOriginalGridFromFront());
            } else {
                this.sudokuState.resetSudokuState(null);
                this.logFactory.addOriginalSudokuGrid(this.sudokuState.getOriginalGrid());
            }

            this.sudokuFrame.repaintSudokuPanel();
            return;
        }

        // Case 11: error
        System.err.println("An unrecognized form of a Sudoku action was received by the game!");
    }
}
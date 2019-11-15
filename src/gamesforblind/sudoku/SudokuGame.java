package gamesforblind.sudoku;

import gamesforblind.ProgramArgs;
import gamesforblind.enums.InterfaceType;
import gamesforblind.enums.SudokuType;
import gamesforblind.loader.GameLoader;
import gamesforblind.logger.LogFactory;
import gamesforblind.logger.LogWriter;
import gamesforblind.sudoku.action.*;
import gamesforblind.sudoku.gui.SudokuFrame;
import gamesforblind.synthesizer.AudioPlayerExecutor;

/**
 * Game class that is called directly from the {@link GameLoader} class.
 * It receives actions & sends these actions to the {@link SudokuState} for the game.
 */
public class SudokuGame {
    private final SudokuState sudokuState;
    private final SudokuFrame sudokuFrame;
    private final LogFactory logFactory;
    private final ProgramArgs programArgs;

    /**
     * Creates a new SudokuGame.
     *
     * @param sudokuType          Whether the game is a 4x4, 6x6, or 9x9 variant.
     * @param audioPlayerExecutor Class used to execute the threaded audio player.
     * @param logFactory          Where all of the logs are stored or read from (depending on whether it's in playback mode).
     * @param programArgs         The program arguments that were passed.
     */
    public SudokuGame(
            SudokuType sudokuType, AudioPlayerExecutor audioPlayerExecutor, LogFactory logFactory, ProgramArgs programArgs
    ) {
        this.programArgs = programArgs;
        this.logFactory = logFactory;

        InterfaceType selectedInterfaceType = programArgs.getSelectedInterfaceType();
        if (programArgs.isPlaybackMode()) {
            // Case 1: the program is in playback mode (call state constructor with the original state of the board).
            this.sudokuState = new SudokuState(
                    selectedInterfaceType, sudokuType, audioPlayerExecutor, logFactory.getOriginalSudokuGrid()
            );
        } else {
            // Case 2: the program is not in playback mode (set the log factory's original Sudoku state).
            this.sudokuState = new SudokuState(selectedInterfaceType, sudokuType, audioPlayerExecutor);
            this.logFactory.setOriginalSudokuGrid(this.sudokuState.getOriginalGrid());
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
            // I don't want to actually exit the game if it is in playback mode (see the ending state of the game).
            if (!this.programArgs.isPlaybackMode()) {
                LogWriter logWriter = new LogWriter(this.logFactory);
                logWriter.saveGameLog();
                System.exit(0);
            }
        }

        // If the game is over, go to main menu
        if (this.sudokuState.isGameOver()) {
            this.sudokuFrame.closeFrames();
            new GameLoader(this.programArgs);
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
            this.sendStateToGui();
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
            this.sendStateToGui();
            return;
        }

        // Case 6: the user wants to highlight a square/block on the board.
        if (sudokuAction instanceof SudokuHighlightAction) {
            SudokuHighlightAction highlightAction = (SudokuHighlightAction) sudokuAction;
            this.sudokuState.setHighlightedPoint(highlightAction.getPointToHighlight(), highlightAction.getInputType());
            this.sudokuState.readSelectedSquare();
            this.sendStateToGui();
            return;
        }

        // Case 7: the user wants to fill a square on the board.
        if (sudokuAction instanceof SudokuFillAction) {
            SudokuFillAction sudokuFillAction = (SudokuFillAction) sudokuAction;
            this.sudokuState.setSquareNumber(sudokuFillAction.getNumberToFill());
            this.sendStateToGui();
            return;
        }

        // Case 8: the user wants the current row/column/block to be read.
        if (sudokuAction instanceof SudokuReadPositionAction) {
            SudokuReadPositionAction sudokuReadPositionAction = (SudokuReadPositionAction) sudokuAction;
            this.sudokuState.readBoardSection(sudokuReadPositionAction.getSudokuSection());
            this.sendStateToGui();
            return;
        }

        // Case 9: the user uses the menu to navigate
        if (sudokuAction instanceof SudokuMenuAction) {
            SudokuMenuAction sudokuMenuAction = (SudokuMenuAction) sudokuAction;

            switch(sudokuMenuAction.getAction()){
                case 1:
                    //hint
                    this.sudokuState.giveHint();
                    this.sendStateToGui();
                    break;
                case 2:
                    //instructions
                    this.sudokuState.readInstructions();
                    break;
                case 3:
                    //language not complete yet
                    break;
                case 4:
                    //restart
                    this.sudokuFrame.closeFrames();
                    this.sudokuState.reset(sudokuState.getSudokuType(), sudokuState.getAudioPlayerExecutor(),
                            this.logFactory, this.programArgs);
                    break;
                case 5:
                    //return to main menu
                    this.sudokuFrame.closeFrames();
                    new GameLoader(this.programArgs);
                    break;
            }
            return;
        }

        // Case 10: error
        System.err.println("An unrecognized form of a Sudoku action was received by the game!");
    }

    /**
     * Sends the updated state to the Sudoku GUI (also calls repaint() on the Sudoku panel).
     */
    private void sendStateToGui() {
        this.sudokuFrame.receiveSudokuState(this.sudokuState);
    }
}

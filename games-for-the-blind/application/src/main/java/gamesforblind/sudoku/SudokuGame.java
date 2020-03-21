package gamesforblind.sudoku;

import gamesforblind.ProgramArgs;
import gamesforblind.enums.InterfaceType;
import gamesforblind.enums.SudokuType;
import gamesforblind.loader.GameLoader;
import gamesforblind.logger.LogFactory;
import gamesforblind.sudoku.action.*;
import gamesforblind.sudoku.gui.SudokuFrame;
import gamesforblind.synthesizer.AudioPlayerExecutor;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.AbstractMap;
import java.util.Map;

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
    public SudokuGame(
            @NotNull GameLoader gameLoader,
            @NotNull SudokuType sudokuType,
            @NotNull AudioPlayerExecutor audioPlayerExecutor,
            @NotNull LogFactory logFactory,
            @NotNull ProgramArgs programArgs
    ) {
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
    public void receiveAction(@NotNull SudokuAction sudokuAction) {
        if (!this.programArgs.isPlaybackMode()) {
            this.logFactory.addProgramAction(sudokuAction);
        }

        // If the game is over, go to main menu
        if (this.sudokuState.isGameOver()) {
            this.sudokuFrame.closeFrames();
            this.gameLoader.openLoaderInterface();
            return;
        }

        final Map<Class<? extends SudokuAction>, Runnable> SUDOKU_ACTION_TO_RUNNABLE = Map.ofEntries(
                // Case 1: the user wishes to exit the game.
                new AbstractMap.SimpleEntry<>(
                        SudokuExitAction.class,
                        this.gameLoader::exitApplication
                ),

                // Case 2: the user wants the instructions for the game to be read.
                new AbstractMap.SimpleEntry<>(
                        SudokuInstructionsAction.class,
                        this.sudokuState::readInstructions
                ),

                // Case 3: the user wants a hint.
                new AbstractMap.SimpleEntry<>(
                        SudokuHintKeyAction.class,
                        this::giveHint
                ),

                // Case 4: the user has pressed an unrecognized key on the keyboard.
                new AbstractMap.SimpleEntry<>(
                        SudokuUnrecognizedKeyAction.class,
                        () -> this.readUnrecognizedKey((SudokuUnrecognizedKeyAction) sudokuAction)
                ),

                // Case 5: the user hits a hot key (e.g. Ctrl + LEFT)
                new AbstractMap.SimpleEntry<>(
                        SudokuHotKeyAction.class,
                        () -> this.processHotKey((SudokuHotKeyAction) sudokuAction)
                ),

                // Case 6: the user wants to highlight a square/block on the board.
                new AbstractMap.SimpleEntry<>(
                        SudokuHighlightAction.class,
                        () -> this.changeHighlightedSquare((SudokuHighlightAction) sudokuAction)
                ),

                // Case 7: the user wants to fill a square on the board.
                new AbstractMap.SimpleEntry<>(
                        SudokuFillAction.class,
                        () -> this.fillSudokuSquare((SudokuFillAction) sudokuAction)
                ),

                // Case 8: the user wants the current row/column/block to be read.
                new AbstractMap.SimpleEntry<>(
                        SudokuReadPositionAction.class,
                        () -> this.readBoardSection((SudokuReadPositionAction) sudokuAction)
                ),

                // Case 9: the user wants to read off the location of the currently selected square.
                new AbstractMap.SimpleEntry<>(
                        SudokuLocationAction.class,
                        this.sudokuState::readSelectedLocation
                ),

                // Case 10: return to main menu.
                new AbstractMap.SimpleEntry<>(
                        SudokuMainMenuAction.class,
                        this::returnToMainMenu
                ),

                // Case 11: the user wants the phrases to stop being read.
                new AbstractMap.SimpleEntry<>(
                        SudokuStopReadingAction.class,
                        this.sudokuState::stopReadingPhrases
                ),

                // Case 12: restart the current Sudoku board.
                new AbstractMap.SimpleEntry<>(
                        SudokuRestartAction.class,
                        this::restartSudokuBoard
                )
        );

        Runnable functionToExecute = SUDOKU_ACTION_TO_RUNNABLE.get(sudokuAction.getClass());
        if (functionToExecute != null) {
            functionToExecute.run();
        } else {
            System.err.println("An unrecognized form of a Sudoku action was received by the game!");
        }
    }

    /**
     * Helper method to give the user a hint & repaint the enclosed Sudoku panel reference.
     */
    private void giveHint() {
        this.sudokuState.giveHint();
        this.sudokuFrame.repaintSudokuPanel();
    }

    /**
     * Helper method to read the unrecognized key that a blind user pressed.
     *
     * @param sudokuUnrecognizedKeyAction The {@link SudokuUnrecognizedKeyAction} that was sent to the game.
     */
    private void readUnrecognizedKey(@NotNull SudokuUnrecognizedKeyAction sudokuUnrecognizedKeyAction) {
        this.sudokuState.readUnrecognizedKey(sudokuUnrecognizedKeyAction.getKeyCode());
    }

    /**
     * Sets highlighted point based on pressed hot key, reads off new selected square, and repaints the Sudoku panel.
     *
     * @param sudokuHotKeyAction The {@link SudokuHotKeyAction} that was sent to the game.
     */
    private void processHotKey(@NotNull SudokuHotKeyAction sudokuHotKeyAction) {
        this.sudokuState.setHighlightedPoint(sudokuHotKeyAction.getArrowKeyDirection());
        this.sudokuState.readSelectedSquare();
        this.sudokuFrame.repaintSudokuPanel();
    }

    /**
     * Sets the highlighted point (not based on hot key), reads off selected square, and repaints the Sudoku panel.
     *
     * @param sudokuHighlightAction The {@link SudokuHighlightAction} that was sent to the game.
     */
    private void changeHighlightedSquare(@NotNull SudokuHighlightAction sudokuHighlightAction) {
        this.sudokuState.setHighlightedPoint(
                sudokuHighlightAction.getPointToHighlight(), sudokuHighlightAction.getInputType()
        );
        this.sudokuState.readSelectedSquare();
        this.sudokuFrame.repaintSudokuPanel();
    }

    /**
     * Tells the {@link SudokuState} to fill the currently selected {@link Point} on the board.
     *
     * @param sudokuFillAction The {@link SudokuFillAction} that was sent to the game.
     */
    private void fillSudokuSquare(@NotNull SudokuFillAction sudokuFillAction) {
        this.sudokuState.setSquareNumber(sudokuFillAction.getNumberToFill());
        this.sudokuFrame.repaintSudokuPanel();
    }

    /**
     * Reads the row, column, or block of the currently selected {@link Point}.
     *
     * @param sudokuReadPositionAction The {@link SudokuReadPositionAction} that was sent to the game.
     */
    private void readBoardSection(@NotNull SudokuReadPositionAction sudokuReadPositionAction) {
        this.sudokuState.readBoardSection(sudokuReadPositionAction.getSudokuSection());
        this.sudokuFrame.repaintSudokuPanel();
    }

    /**
     * Restarts the game at the main menu.
     */
    private void returnToMainMenu() {
        this.sudokuFrame.closeFrames();
        this.gameLoader.openLoaderInterface();
    }

    /**
     * Restarts the current Sudoku board from the beginning (generates a new board as well).
     */
    private void restartSudokuBoard() {
        if (this.programArgs.isPlaybackMode()) {
            this.sudokuState.resetSudokuState(this.logFactory.popOriginalGridFromFront());
        } else {
            this.sudokuState.resetSudokuState(null);
            this.logFactory.addOriginalSudokuGrid(this.sudokuState.getOriginalGrid());
        }

        this.sudokuFrame.repaintSudokuPanel();
    }
}

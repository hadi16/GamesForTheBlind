package gamesforblind.mastermind;

import gamesforblind.ProgramArgs;
import gamesforblind.enums.InterfaceType;
import gamesforblind.loader.GameLoader;
import gamesforblind.logger.LogFactory;
import gamesforblind.mastermind.action.MastermindAction;
import gamesforblind.mastermind.gui.MastermindFrame;
import gamesforblind.sudoku.SudokuState;
import gamesforblind.synthesizer.AudioPlayerExecutor;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * Game class that is called directly from the {@link GameLoader} class.
 * It receives actions & sends these actions to the {@link SudokuState} for the game.
 */
public class MastermindGame {
    private final GameLoader gameLoader;
    private final MastermindState mastermindState;
    private final MastermindFrame mastermindFrame;
    private final LogFactory logFactory;
    private final ProgramArgs programArgs;

    /**
     * Creates a new MastermindGame.
     *
     * @param gameLoader          The "main menu" for the games, which is needed when reopening this menu.
     * @param audioPlayerExecutor Class used to execute the threaded audio player.
     * @param logFactory          Where all of the logs are stored or read from (depending on whether in playback mode).
     * @param programArgs         The program arguments that were passed.
     */
    public MastermindGame(
            @NotNull GameLoader gameLoader,
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
            this.mastermindState = new MastermindState(audioPlayerExecutor);
        } else {
            // Case 2: the program is not in playback mode (set the log factory's original Sudoku state).
            this.mastermindState = new MastermindState(audioPlayerExecutor);
        }

        this.mastermindFrame = new MastermindFrame(
                this, this.mastermindState, programArgs.isPlaybackMode()
        );
    }

    /**
     * Receives an action as input and calls the proper {@link MastermindState} function.
     * Oftentimes, the updated state is sent to the GUI & the GUI is repainted.
     *
     * @param mastermindAction The action that was received.
     */
    public void receiveAction(@NotNull MastermindAction mastermindAction) {
        // If the game is over, go to main menu
        if (this.mastermindState.isGameOver()) {
            this.mastermindFrame.closeFrames();
            this.gameLoader.openLoaderInterface();
            return;
        }

        final Map<Class, Runnable> MASTERMIND_ACTION_TO_RUNNABLE = Map.of(


        );

        Runnable functionToExecute = MASTERMIND_ACTION_TO_RUNNABLE.get(mastermindAction.getClass());
        if (functionToExecute != null) {
            functionToExecute.run();
        } else {
            System.err.println("An unrecognized form of a Sudoku action was received by the game!");
        }
    }

}

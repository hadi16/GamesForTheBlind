package gamesforblind.codebreaker;

import gamesforblind.ProgramArgs;
import gamesforblind.enums.CodebreakerType;
import gamesforblind.loader.GameLoader;
import gamesforblind.logger.LogFactory;
import gamesforblind.codebreaker.action.CodebreakerAction;
import gamesforblind.codebreaker.action.CodebreakerExitAction;
import gamesforblind.codebreaker.action.CodebreakerMainMenuAction;
import gamesforblind.codebreaker.gui.CodebreakerFrame;
import gamesforblind.synthesizer.AudioPlayerExecutor;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * Game class that is called directly from the {@link GameLoader} class.
 * It receives actions & sends these actions to the {@link CodebreakerState} for the game.
 */
public class CodebreakerGame {
    private final GameLoader gameLoader;
    private final CodebreakerType codebreakerType;
    private final CodebreakerState codebreakerState;
    private final CodebreakerFrame codebreakerFrame;
    private final LogFactory logFactory;
    private final ProgramArgs programArgs;

    /**
     * Creates a new MastermindGame.
     *
     * @param gameLoader          The "main menu" for the games, which is needed when reopening this menu.
     * @param codebreakerType      int describing the number of boxes/difficulty to use
     * @param audioPlayerExecutor Class used to execute the threaded audio player.
     * @param logFactory          Where all of the logs are stored or read from (depending on whether in playback mode).
     * @param programArgs         The program arguments that were passed.
     */
    public CodebreakerGame(
            @NotNull GameLoader gameLoader,
            @NotNull CodebreakerType codebreakerType,
            @NotNull AudioPlayerExecutor audioPlayerExecutor,
            @NotNull LogFactory logFactory,
            @NotNull ProgramArgs programArgs
    ) {
        this.gameLoader = gameLoader;
        this.programArgs = programArgs;
        this.logFactory = logFactory;
        this.codebreakerType = codebreakerType;

        if (programArgs.isPlaybackMode()) {
            // Case 1: the program is in playback mode (call state constructor with the original state of the board).
            this.codebreakerState = new CodebreakerState(audioPlayerExecutor);
        } else {
            // Case 2: the program is not in playback mode (set the log factory's original Sudoku state).
            this.codebreakerState = new CodebreakerState(audioPlayerExecutor);
        }

        this.codebreakerFrame = new CodebreakerFrame(
                this, this.codebreakerState, programArgs.isPlaybackMode()
        );
    }

    /**
     * Receives an action as input and calls the proper {@link CodebreakerState} function.
     * Oftentimes, the updated state is sent to the GUI & the GUI is repainted.
     *
     * @param codebreakerAction The action that was received.
     */
    public void receiveAction(@NotNull CodebreakerAction codebreakerAction) {
        if (!this.programArgs.isPlaybackMode()) {
            this.logFactory.addProgramAction(codebreakerAction);
        }

        // If the game is over, go to main menu
        if (this.codebreakerState.isGameOver()) {
            this.codebreakerFrame.closeFrames();
            this.gameLoader.openLoaderInterface();
            return;
        }

        final Map<Class<? extends MastermindAction>, Runnable> MASTERMIND_ACTION_TO_RUNNABLE = Map.of(
                // Case 1: return to main menu.
                CodebreakerMainMenuAction.class, this::returnToMainMenu,

                // Case 2: exit the game.
                CodebreakerExitAction.class, this.gameLoader::exitApplication
        );

        Runnable functionToExecute = MASTERMIND_ACTION_TO_RUNNABLE.get(codebreakerAction.getClass());
        if (functionToExecute != null) {
            functionToExecute.run();
        } else {
            System.err.println("An unrecognized form of a Mastermind action was received by the game!");
        }
    }

    /**
     * Restarts the game at the main menu.
     */
    private void returnToMainMenu() {
        this.codebreakerFrame.closeFrames();
        this.gameLoader.openLoaderInterface();
    }
}

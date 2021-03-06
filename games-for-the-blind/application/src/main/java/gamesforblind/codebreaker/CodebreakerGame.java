package gamesforblind.codebreaker;

import gamesforblind.ProgramArgs;
import gamesforblind.codebreaker.action.*;
import gamesforblind.codebreaker.gui.CodebreakerFrame;
import gamesforblind.enums.CodebreakerType;
import gamesforblind.loader.GameLoader;
import gamesforblind.logger.LogFactory;
import gamesforblind.synthesizer.AudioPlayerExecutor;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

import static util.MapUtil.entry;
import static util.MapUtil.map;

/**
 * Game class that is called directly from the {@link GameLoader} class.
 * It receives actions & sends these actions to the {@link CodebreakerState} for the game.
 */
public class CodebreakerGame {
    private final GameLoader gameLoader;
    private final CodebreakerState codebreakerState;
    private final CodebreakerFrame codebreakerFrame;
    private final LogFactory logFactory;
    private final ProgramArgs programArgs;

    /**
     * Creates a new CodebreakerGame.
     *
     * @param gameLoader          The "main menu" for the games, which is needed when reopening this menu.
     * @param codebreakerType     int describing the number of boxes/difficulty to use
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

        this.codebreakerState = new CodebreakerState(audioPlayerExecutor, codebreakerType);
        this.codebreakerFrame = new CodebreakerFrame(this, this.codebreakerState, programArgs.isPlaybackMode());
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
            this.codebreakerFrame.repaintCodebreakerPanel();
            this.codebreakerFrame.closeFrames();
            this.gameLoader.openLoaderInterface();
            return;
        }

        final Map<Class<? extends CodebreakerAction>, Runnable> CODEBREAKER_ACTION_TO_RUNNABLE = map(
                // Case 1: return to main menu.
                entry(CodebreakerMainMenuAction.class, this::returnToMainMenu),

                // Case 2: exit the game.
                entry(CodebreakerExitAction.class, this.gameLoader::exitApplication),

                // Case 3: an arrow key is pressed.
                entry(
                        CodebreakerArrowKeyAction.class,
                        () -> this.changeSelectedCellPoint((CodebreakerArrowKeyAction) codebreakerAction)
                ),

                // Case 4: a single number is set by the user.
                entry(
                        CodebreakerSetSingleNumberAction.class,
                        () -> this.setSingleNumber((CodebreakerSetSingleNumberAction) codebreakerAction)
                ),

                // Case 5: the user wants the current row to be read off.
                entry(CodebreakerReadBackAction.class, this.codebreakerState::readBackRow),

                // Case 6: the user wants the instructions to be read.
                entry(CodebreakerInstructionsAction.class, this.codebreakerState::readInstructions),

                // Case 7: the user wants the current Codebreaker guess to be set.
                entry(
                        CodebreakerSetGuessAction.class,
                        () -> {
                            this.codebreakerState.setCodebreakerGuess();
                            this.codebreakerFrame.repaintCodebreakerPanel();
                        }
                ),

                // Case 8: the user wants to restart the current Codebreaker game.
                entry(
                        CodebreakerRestartAction.class,
                        () -> {
                            this.codebreakerState.initNewCodebreakerGame();
                            this.codebreakerFrame.repaintCodebreakerPanel();
                        }
                ),

                // Case 9: the user has clicked on the Codebreaker board.
                entry(
                        CodebreakerMouseAction.class,
                        () -> this.registerMouseAction((CodebreakerMouseAction) codebreakerAction)
                ),

                // Case 10: the user wants the phrases to stop being read.
                entry(CodebreakerStopReadingAction.class, this.codebreakerState::stopReadingPhrases),

                // Case 11: the user has pressed hint key.
                entry(CodebreakerHintKeyAction.class, this::giveHint),

                // Case 12: the user wants the current location & value read off.
                entry(CodebreakerLocationAction.class, this.codebreakerState::readSelectedLocationWithValue),

                // Case 13: the user has pressed an unrecognized key in the game.
                entry(
                        CodebreakerUnrecognizedKeyAction.class,
                        () -> this.readUnrecognizedKey((CodebreakerUnrecognizedKeyAction) codebreakerAction)
                )
        );

        Runnable functionToExecute = CODEBREAKER_ACTION_TO_RUNNABLE.get(codebreakerAction.getClass());
        if (functionToExecute != null) {
            functionToExecute.run();
        } else {
            System.err.println("An unrecognized form of a Codebreaker action was received by the game!");
        }
    }

    private void registerMouseAction(CodebreakerMouseAction codebreakerMouseAction) {
        Optional<Point> maybeSelectedPoint = this.codebreakerFrame.getMouseSelectedPoint(
                codebreakerMouseAction.getSelectedPoint()
        );

        maybeSelectedPoint.ifPresent(selectedPoint -> {
            ArrayList<CodebreakerGuess> guessList = this.codebreakerState.getGuessList();
            if (selectedPoint.y <= guessList.size()) {
                this.codebreakerState.setSelectedCellPoint(selectedPoint);
            }
        });

        this.codebreakerFrame.repaintCodebreakerPanel();
    }

    private void giveHint() {
        if (this.codebreakerState.getHintNum() != 0) {
            this.codebreakerState.setSingleNumber(this.codebreakerState.getHint());
            this.codebreakerFrame.repaintCodebreakerPanel();
        } else {
            this.codebreakerState.playNoHint();
        }
    }

    private void readUnrecognizedKey(@NotNull CodebreakerUnrecognizedKeyAction codebreakerUnrecognizedKeyAction) {
        this.codebreakerState.readUnrecognizedKey(codebreakerUnrecognizedKeyAction.getKeyCode());
    }

    private void changeSelectedCellPoint(@NotNull CodebreakerArrowKeyAction codebreakerArrowKeyAction) {
        this.codebreakerState.changeSelectedCellPoint(codebreakerArrowKeyAction.getArrowKeyDirection());

        this.codebreakerFrame.repaintCodebreakerPanel();

    }

    private void setSingleNumber(@NotNull CodebreakerSetSingleNumberAction codebreakerSetSingleNumberAction) {
        this.codebreakerState.setSingleNumber(codebreakerSetSingleNumberAction.getNumberToSet());
        this.codebreakerFrame.repaintCodebreakerPanel();
    }

    /**
     * Restarts the game at the main menu.
     */
    private void returnToMainMenu() {
        this.codebreakerFrame.closeFrames();
        this.gameLoader.openLoaderInterface();
    }
}

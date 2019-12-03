package gamesforblind.loader;

import gamesforblind.ProgramAction;
import gamesforblind.ProgramArgs;
import gamesforblind.enums.InterfaceType;
import gamesforblind.enums.SelectedGame;
import gamesforblind.enums.SudokuType;
import gamesforblind.loader.action.*;
import gamesforblind.loader.gui.LoaderFrame;
import gamesforblind.loader.gui.LogFileSelectionGui;
import gamesforblind.loader.gui.listener.LoaderActionListener;
import gamesforblind.loader.gui.listener.LoaderKeyboardListener;
import gamesforblind.logger.LogFactory;
import gamesforblind.logger.LogReader;
import gamesforblind.logger.LogWriter;
import gamesforblind.mastermind.MastermindGame;
import gamesforblind.mastermind.action.MastermindAction;
import gamesforblind.sudoku.SudokuGame;
import gamesforblind.sudoku.action.SudokuAction;
import gamesforblind.synthesizer.AudioPlayer;
import gamesforblind.synthesizer.AudioPlayerExecutor;
import org.jetbrains.annotations.NotNull;
import phrase.Phrase;

import javax.sound.sampled.LineUnavailableException;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static gamesforblind.Constants.*;

/**
 * Universal loader for the program. Instantiated solely in {@link gamesforblind.Main}.
 */
public class GameLoader {
    /**
     * The audio player thread (needed to wait for finish before gracefully exiting the program).
     */
    private final Thread audioPlayerThread;

    /**
     * In playback mode, read the stored actions one-by-one.
     * In regular mode, store the actions taken in the game one-by-one.
     */
    private final LogFactory logFactory;

    private final AudioPlayerExecutor audioPlayerExecutor;

    private final ProgramArgs programArgs;

    private LoaderFrame loaderFrame;
    private SudokuGame sudokuGame;
    private MastermindGame mastermindGame;

    /**
     * Creates a new GameLoader (this is only called directly from main()).
     *
     * @param programArgs The program arguments that were passed.
     */
    public GameLoader(@NotNull ProgramArgs programArgs) {
        // Initialize the audio player & start the audio player thread.
        AudioPlayer audioPlayer = this.initializeAudioPlayer();
        this.audioPlayerExecutor = new AudioPlayerExecutor(audioPlayer);
        this.audioPlayerThread = new Thread(audioPlayer);
        this.audioPlayerThread.start();

        this.programArgs = programArgs;
        this.logFactory = this.initializeLogFactory();

        this.openLoaderInterface();

        // If we are in playback mode, this step is crucial
        // (allows us to replay the actions one-by-one in realtime).
        if (this.programArgs.isPlaybackMode()) {
            this.loopThroughSavedProgramActions();
        }
    }

    /**
     * Opens the GUI for the loader. Allows the game loader to reopen when the user decides to go back to the main menu.
     */
    public void openLoaderInterface() {
        this.audioPlayerExecutor.replacePhraseAndPrint(Phrase.PLAY_OR_EXIT);
        this.loaderFrame = new LoaderFrame(this, this.programArgs);
    }

    /**
     * Helper method to loop through all of the stored {@link ProgramAction}s in realtime.
     * Enables realtime playback functionality for Sudoku.
     */
    private void loopThroughSavedProgramActions() {
        ArrayList<ProgramAction> programActions = this.logFactory.getProgramActionList();
        for (int i = 0; i < programActions.size(); i++) {
            ProgramAction currentAction = programActions.get(i);

            if (currentAction instanceof LoaderAction) {
                this.receiveAction((LoaderAction) currentAction);
            } else if (currentAction instanceof SudokuAction) {
                // The game should have been initialized by the LoaderSudokuSelectionAction in the XML log file.
                this.sudokuGame.receiveAction((SudokuAction) currentAction);
            } else if(currentAction instanceof MastermindAction){
                this.mastermindGame.receiveAction((MastermindAction) currentAction);
            }

            // If we are on the last iteration/action, then no need to sleep anymore.
            if (i == programActions.size() - 1) {
                break;
            }

            /* Sleep for the duration between the current action & the next action. */
            ProgramAction nextAction = programActions.get(i + 1);
            long millisToSleep = ChronoUnit.MILLIS.between(
                    currentAction.getLocalDateTime(), nextAction.getLocalDateTime()
            );

            try {
                Thread.sleep(millisToSleep);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Get a new {@link LogFactory} object if the program isn't in playback mode. Otherwise, trigger
     * the log file selection GUI & initialize the LogFactory with the contents of this XML file.
     *
     * @return The initialized {@link LogFactory} object.
     */
    private LogFactory initializeLogFactory() {
        if (!this.programArgs.isPlaybackMode()) {
            return new LogFactory();
        }

        LogFileSelectionGui logFileSelectionGui = new LogFileSelectionGui();
        Optional<String> maybeLogFilePath = logFileSelectionGui.getSelectedLogFilePath();

        // If the user didn't select a XML log file from the GUI.
        if (maybeLogFilePath.isEmpty()) {
            System.err.println("Could not load XML log file!");
            System.exit(1);
        }

        // Let LogReader read in the program actions & Sudoku board from the XML log file.
        return new LogReader().restoreLoggedProgram(maybeLogFilePath.get());
    }

    /**
     * Receives an action that was sent by either {@link LoaderActionListener} or {@link LoaderKeyboardListener}.
     * Modifies the GUI appropriately based on the given action that was sent.
     *
     * @param loaderAction The action that was sent to the game loader.
     */
    public void receiveAction(@NotNull LoaderAction loaderAction) {
        if (!this.programArgs.isPlaybackMode()) {
            this.logFactory.addProgramAction(loaderAction);
        }

        final Map<Class, Runnable> LOADER_ACTION_TO_RUNNABLE = Map.of(
                // Case 1: the user pressed one of the arrow keys in the loader GUI.
                LoaderArrowKeyAction.class,
                () -> this.highlightDifferentLoaderButton((LoaderArrowKeyAction) loaderAction),

                // Case 2: the user changed the currently selected game in the loader GUI.
                LoaderGameSelectionAction.class,
                () -> this.changeCurrentGame((LoaderGameSelectionAction) loaderAction),

                // Case 3: the user selected one of the Sudoku board sizes in the loader GUI (4x4 or 9x9).
                LoaderSudokuSelectionAction.class,
                () -> this.loadSudokuGame((LoaderSudokuSelectionAction) loaderAction),

                // Case 4: the user pressed an unrecognized key on the keyboard.
                LoaderUnrecognizedKeyAction.class,
                () -> this.readUnrecognizedKey((LoaderUnrecognizedKeyAction) loaderAction),

                // Case 5: the user has decided to exit the loader GUI.
                LoaderExitAction.class,
                this::exitApplication
        );

        Runnable functionToExecute = LOADER_ACTION_TO_RUNNABLE.get(loaderAction.getClass());
        if (functionToExecute != null) {
            functionToExecute.run();
        } else {
            System.err.println("An unrecognized form of loader action was received!");
        }
    }

    /**
     * Highlights a different button within the loader interface.
     *
     * @param loaderArrowKeyAction The {@link LoaderArrowKeyAction} that was sent to the loader.
     */
    private void highlightDifferentLoaderButton(@NotNull LoaderArrowKeyAction loaderArrowKeyAction) {
        final Map<String, Phrase> BUTTON_TEXT_TO_PHRASE = Map.of(
                PLAY_SUDOKU_BUTTON, Phrase.SPACE_FOR_SUDOKU,
                PLAY_MASTERMIND_BUTTON, Phrase.SPACE_FOR_MASTERMIND,
                EXIT_BUTTON, Phrase.SPACE_FOR_EXIT,
                BACK_BUTTON, Phrase.GO_BACK_TO_GAME_SELECTION,
                FOUR_BY_FOUR_SUDOKU_BUTTON, Phrase.SELECT_SUDOKU_FOUR,
                SIX_BY_SIX_SUDOKU_BUTTON, Phrase.SELECT_SUDOKU_SIX,
                NINE_BY_NINE_SUDOKU_BUTTON, Phrase.SELECT_SUDOKU_NINE
        );

        this.loaderFrame.changeHighlightedButton(loaderArrowKeyAction.getArrowKeyDirection());
        this.audioPlayerExecutor.replacePhraseAndPrint(
                BUTTON_TEXT_TO_PHRASE.get(this.loaderFrame.getCurrentlyHighlightedButtonText())
        );
    }

    /**
     * Changes the current game in the loader.
     *
     * @param loaderGameSelectionAction The {@link LoaderGameSelectionAction} that was sent to the loader.
     */
    private void changeCurrentGame(@NotNull LoaderGameSelectionAction loaderGameSelectionAction) {
        SelectedGame selectedGame = loaderGameSelectionAction.getSelectedGame();

        if(selectedGame == selectedGame.MASTERMIND){
            this.loadMastermindGame();
        }

        Phrase relevantPhrase;
        if (selectedGame == SelectedGame.SUDOKU) {
            if (this.programArgs.getSelectedInterfaceType() == InterfaceType.ARROW_KEY_INTERFACE) {
                relevantPhrase = Phrase.WHICH_SUDOKU_GAME_ALL;
            } else {
                relevantPhrase = Phrase.WHICH_SUDOKU_GAME_NO_SIX;
            }
        }
        else {
            relevantPhrase = Phrase.PLAY_OR_EXIT;
        }
        this.audioPlayerExecutor.replacePhraseAndPrint(relevantPhrase);

        this.loaderFrame.setLoaderGuiBasedOnSelectedGame(selectedGame);
    }

    /**
     * Loads the Sudoku game type that the user requested.
     *
     * @param loaderSudokuSelectionAction The {@link LoaderSudokuSelectionAction} that was sent to the game.
     */
    private void loadSudokuGame(@NotNull LoaderSudokuSelectionAction loaderSudokuSelectionAction) {
        SudokuType sudokuType = loaderSudokuSelectionAction.getSudokuType();

        List<InterfaceType> supportedInterfaces = sudokuType.getSupportedSudokuInterfaces();
        InterfaceType interfaceType = this.programArgs.getSelectedInterfaceType();
        if (!supportedInterfaces.contains(interfaceType)) {
            System.err.println(
                    "Interface type " + interfaceType + " not supported for Sudoku type " + sudokuType
            );
            System.exit(1);
        }

        this.loaderFrame.closeLoaderFrames();
        this.sudokuGame = new SudokuGame(
                this, sudokuType, this.audioPlayerExecutor, this.logFactory, this.programArgs
        );
    }

    /**
     * Loads the Mastermind game that the user requested.
     */
    private void loadMastermindGame() {
        this.loaderFrame.closeLoaderFrames();
        this.mastermindGame = new MastermindGame(
                this, this.audioPlayerExecutor, this.logFactory, this.programArgs
        );
    }

    /**
     * Reads an unrecognized key that was pressed in the loader.
     *
     * @param loaderUnrecognizedKeyAction The {@link LoaderUnrecognizedKeyAction} that was sent to the loader.
     */
    private void readUnrecognizedKey(@NotNull LoaderUnrecognizedKeyAction loaderUnrecognizedKeyAction) {
        this.audioPlayerExecutor.replacePhraseAndPrint(new ArrayList<>(Arrays.asList(
                Phrase.UNRECOGNIZED_KEY, Phrase.keyCodeToPhrase(loaderUnrecognizedKeyAction.getKeyCode())
        )));
    }

    /**
     * Exit from the application. If we are in playback mode, don't exit (to see the ending state of the game).
     */
    public void exitApplication() {
        this.audioPlayerExecutor.replacePhraseAndPrint(Phrase.EXITING);

        // I don't want to exit the game if it is in playback mode (see the ending state of the game).
        if (this.programArgs.isPlaybackMode()) {
            return;
        }

        this.audioPlayerExecutor.terminateAudioPlayer();

        LogWriter logWriter = new LogWriter(this.logFactory);
        logWriter.saveGameLog();

        // Wait for the audio player thread to end.
        try {
            this.audioPlayerThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.exit(0);
    }

    /**
     * Initializes the audio player and returns it.
     *
     * @return The {@link AudioPlayer} for the entire program.
     */
    private AudioPlayer initializeAudioPlayer() {
        // Set to null to prevent compiler from complaining about lack of initialization below.
        AudioPlayer audioPlayer = null;
        try {
            audioPlayer = new AudioPlayer();
        } catch (LineUnavailableException e) {
            // If the audio player couldn't be created, we need to exit in error
            // (audio is used throughout the game for blind users).
            e.printStackTrace();
            System.exit(1);
        }

        return audioPlayer;
    }
}

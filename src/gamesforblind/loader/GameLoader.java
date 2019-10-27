package gamesforblind.loader;

import gamesforblind.loader.action.*;
import gamesforblind.loader.enums.SelectedGame;
import gamesforblind.loader.gui.LoaderFrame;
import gamesforblind.loader.gui.LoaderGuiConstants;
import gamesforblind.loader.gui.listener.LoaderActionListener;
import gamesforblind.loader.gui.listener.LoaderKeyboardListener;
import gamesforblind.sudoku.SudokuGame;
import gamesforblind.synthesizer.AudioPlayer;
import gamesforblind.synthesizer.AudioPlayerExecutor;
import gamesforblind.synthesizer.Phrase;

import javax.sound.sampled.LineUnavailableException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;

/**
 * Universal loader for the program. Instantiated solely in {@link gamesforblind.Main}.
 */
public class GameLoader {
    /**
     * The Scanner that is used throughout the entire program (needed for keyboard input).
     */
    private final Scanner keyboard = new Scanner(System.in);

    /**
     * The audio player thread (needed to wait for finish before gracefully exiting the program).
     */
    private final Thread audioPlayerThread;

    private final LoaderFrame loaderFrame;
    private final AudioPlayerExecutor audioPlayerExecutor;

    /**
     * Creates a new {@link GameLoader} (this is only called directly from main()).
     */
    public GameLoader() {
        // Initialize the audio player & start the audio player thread.
        AudioPlayer audioPlayer = this.initializeAudioPlayer();
        this.audioPlayerExecutor = new AudioPlayerExecutor(audioPlayer);
        this.audioPlayerThread = new Thread(audioPlayer);
        this.audioPlayerThread.start();

        this.loaderFrame = new LoaderFrame(this);
        this.audioPlayerExecutor.replacePhraseAndPrint(Phrase.PLAY_OR_EXIT);
    }

    /**
     * Receives an action that was sent by either {@link LoaderActionListener} or {@link LoaderKeyboardListener}.
     * Modifies the GUI appropriately based on the given action that was sent.
     *
     * @param action The action that was sent to the game loader.
     */
    public void receiveAction(LoaderAction action) {
        // Case 1: the user pressed one of the arrow keys in the game.
        if (action instanceof LoaderArrowKeyAction) {
            final Map<String, Phrase> BUTTON_TEXT_TO_PHRASE = Map.of(
                    LoaderGuiConstants.PLAY_SUDOKU_BUTTON,          Phrase.SPACE_FOR_SUDOKU,
                    LoaderGuiConstants.EXIT_BUTTON,                 Phrase.SPACE_FOR_EXIT,
                    LoaderGuiConstants.BACK_BUTTON,                 Phrase.GO_BACK_TO_GAME_SELECTION,
                    LoaderGuiConstants.FOUR_BY_FOUR_SUDOKU_BUTTON,  Phrase.SELECT_SUDOKU_FOUR,
                    LoaderGuiConstants.NINE_BY_NINE_SUDOKU_BUTTON,  Phrase.SELECT_SUDOKU_NINE
            );

            LoaderArrowKeyAction loaderArrowKeyAction = (LoaderArrowKeyAction) action;
            this.loaderFrame.changeHighlightedButton(loaderArrowKeyAction.getArrowKeyDirection());
            this.audioPlayerExecutor.replacePhraseAndPrint(
                    BUTTON_TEXT_TO_PHRASE.get(this.loaderFrame.getCurrentlyHighlightedButtonText())
            );
            return;
        }

        // Case 2: the user changed the currently selected game in the loader GUI.
        if (action instanceof LoaderGameSelectionAction) {
            LoaderGameSelectionAction gameAction = (LoaderGameSelectionAction) action;

            this.audioPlayerExecutor.replacePhraseAndPrint(
                    gameAction.getSelectedGame() == SelectedGame.SUDOKU ? Phrase.WHICH_SUDOKU_GAME : Phrase.PLAY_OR_EXIT
            );

            this.loaderFrame.setLoaderGuiBasedOnSelectedGame(gameAction.getSelectedGame());
            return;
        }

        // Case 3: the user selected one of the Sudoku board sizes in the loader GUI (4x4 or 9x9).
        if (action instanceof LoaderSudokuSelectionAction) {
            LoaderSudokuSelectionAction loaderSudokuSelectionAction = (LoaderSudokuSelectionAction) action;
            int sudokuBoardSize = loaderSudokuSelectionAction.getSudokuType().getSudokuBoardSize();

            this.loaderFrame.closeLoaderFrames();
            new SudokuGame(sudokuBoardSize, this.audioPlayerExecutor);
            return;
        }

        // Case 4: the user pressed an unrecognized key on the keyboard.
        if (action instanceof LoaderUnrecognizedKeyAction) {
            LoaderUnrecognizedKeyAction loaderUnrecognizedKeyAction = (LoaderUnrecognizedKeyAction) action;
            this.audioPlayerExecutor.replacePhraseAndPrint(new ArrayList<>(Arrays.asList(
                    Phrase.UNRECOGNIZED_KEY, Phrase.keyCodeToPhrase(loaderUnrecognizedKeyAction.getKeyCode())
            )));
            return;
        }

        // Case 5: the user has decided to exit the loader GUI.
        if (action instanceof LoaderExitAction) {
            this.audioPlayerExecutor.replacePhraseAndPrint(Phrase.EXITING);
            this.audioPlayerExecutor.terminateAudioPlayer();

            // Wait for the audio player thread to end.
            try {
                this.audioPlayerThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            this.keyboard.close();
            System.exit(0);
        }
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

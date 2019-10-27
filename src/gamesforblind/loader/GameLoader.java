package gamesforblind.loader;

import gamesforblind.loader.action.*;
import gamesforblind.loader.gui.LoaderFrame;
import gamesforblind.loader.gui.LoaderGuiConstants;
import gamesforblind.sudoku.SudokuGame;
import gamesforblind.synthesizer.AudioPlayer;
import gamesforblind.synthesizer.AudioPlayerExecutor;
import gamesforblind.synthesizer.Phrase;

import javax.sound.sampled.LineUnavailableException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class GameLoader {
    private final Scanner keyboard = new Scanner(System.in);
    private final LoaderFrame loaderFrame;
    private final AudioPlayerExecutor audioPlayerExecutor;

    private Thread audioPlayerThread;

    public GameLoader() {
        this.audioPlayerExecutor = new AudioPlayerExecutor(this.initializeAudioPlayer());
        this.loaderFrame = new LoaderFrame(this);

        this.audioPlayerExecutor.replacePhraseAndPrint(Phrase.PLAY_OR_EXIT);
    }

    public void receiveAction(LoaderAction action) {
        if (action instanceof LoaderArrowKeyAction) {
            LoaderArrowKeyAction loaderArrowKeyAction = (LoaderArrowKeyAction) action;
            this.loaderFrame.changeHighlightedButton(loaderArrowKeyAction.getArrowKeyDirection());

            switch (this.loaderFrame.getCurrentlyHighlightedButtonText()) {
                case LoaderGuiConstants.PLAY_SUDOKU_BUTTON:
                    this.audioPlayerExecutor.replacePhraseAndPrint(Phrase.SPACE_FOR_SUDOKU);
                    break;
                case LoaderGuiConstants.EXIT_BUTTON:
                    this.audioPlayerExecutor.replacePhraseAndPrint(Phrase.SPACE_FOR_EXIT);
                    break;
                case LoaderGuiConstants.BACK_BUTTON:
                    this.audioPlayerExecutor.replacePhraseAndPrint(Phrase.GO_BACK_TO_GAME_SELECTION);
                    break;
                case LoaderGuiConstants.FOUR_BY_FOUR_SUDOKU_BUTTON:
                    this.audioPlayerExecutor.replacePhraseAndPrint(Phrase.SELECT_SUDOKU_FOUR);
                    break;
                case LoaderGuiConstants.NINE_BY_NINE_SUDOKU_BUTTON:
                    this.audioPlayerExecutor.replacePhraseAndPrint(Phrase.SELECT_SUDOKU_NINE);
                    break;
            }

            return;
        }

        if (action instanceof LoaderGameSelectionAction) {
            LoaderGameSelectionAction selectionAction = (LoaderGameSelectionAction) action;

            this.audioPlayerExecutor.replacePhraseAndPrint(
                    selectionAction.getSelectedGame() == GameType.SUDOKU ? Phrase.WHICH_SUDOKU_GAME : Phrase.PLAY_OR_EXIT
            );

            this.loaderFrame.setSelectedGameComponent(selectionAction.getSelectedGame());
            return;
        }

        if (action instanceof LoaderSudokuSelectionAction) {
            LoaderSudokuSelectionAction loaderSudokuSelectionAction = (LoaderSudokuSelectionAction) action;
            int sudokuBoardSize = loaderSudokuSelectionAction.getSudokuType().getSudokuBoardSize();

            this.loaderFrame.closeLoaderFrames();
            new SudokuGame(sudokuBoardSize, this.audioPlayerExecutor);
            return;
        }

        if (action instanceof LoaderUnrecognizedKeyAction) {
            LoaderUnrecognizedKeyAction loaderUnrecognizedKeyAction = (LoaderUnrecognizedKeyAction) action;
            this.audioPlayerExecutor.replacePhraseAndPrint(new ArrayList<>(Arrays.asList(
                    Phrase.UNRECOGNIZED_KEY, Phrase.keyCodeToPhrase(loaderUnrecognizedKeyAction.getKeyCode())
            )));
            return;
        }

        if (action instanceof LoaderExitAction) {
            this.audioPlayerExecutor.replacePhraseAndPrint(Phrase.EXITING);
            this.audioPlayerExecutor.terminateAudioPlayer();

            try {
                this.audioPlayerThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            this.keyboard.close();
            System.exit(0);
        }
    }

    private AudioPlayer initializeAudioPlayer() {
        AudioPlayer audioPlayer = null;
        try {
            audioPlayer = new AudioPlayer();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
            System.exit(1);
        }

        this.audioPlayerThread = new Thread(audioPlayer);
        this.audioPlayerThread.start();

        return audioPlayer;
    }
}

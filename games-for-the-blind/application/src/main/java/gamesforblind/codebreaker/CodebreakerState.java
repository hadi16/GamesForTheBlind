package gamesforblind.codebreaker;

import gamesforblind.synthesizer.AudioPlayerExecutor;
import org.jetbrains.annotations.NotNull;
import phrase.Phrase;

import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Class that contains information about the current state of the Sudoku board.
 * Also handles any calls into the {@link AudioPlayerExecutor} for Sudoku.
 */
public class CodebreakerState {
    private static final int MAX_AMOUNT_OF_GUESSES = 12;

    private final int codeSize;
    private final Color[] codeToBreak;
    private final boolean gameOver;
    private final AudioPlayerExecutor audioPlayerExecutor;
    private final ArrayList<CodebreakerGuess> guessList = new ArrayList<>();

    /**
     * Creates a new CodebreakerState.
     *
     * @param audioPlayerExecutor Calls into the threaded audio player for the game.
     */
    public CodebreakerState(@NotNull AudioPlayerExecutor audioPlayerExecutor) {
        this.gameOver = false;

        // TODO: Change these (needed for now to compile unit test).
        this.codeToBreak = new Color[]{};
        this.codeSize = 100;
        this.audioPlayerExecutor = audioPlayerExecutor;
    }

    public static boolean checkThatGameIsOver(Color[] codeToBreak, ArrayList<CodebreakerGuess> guessList) {
        Objects.requireNonNull(codeToBreak, "Code to break cannot be null!");
        Objects.requireNonNull(guessList, "Guess list must not be null!");

        if (guessList.size() == MAX_AMOUNT_OF_GUESSES) {
            return true;
        }

        return codeToBreak.length == guessList.get(guessList.size() - 1).getNumberInCorrectPosition();
    }


    /**
     * Reads the instructions for the 4x4, 6x6, or 9x9 game.
     */
    public void readInstructions() {
        Phrase instructionsPhrase = null;

        //currently commented out beacause I need Polina's updates first to get codebreakerType and I don't want
        //to mess anything up

        /*switch (this.codebreakerType) {
            case 4:*/
                instructionsPhrase = Phrase.INSTRUCTIONS_CODEBREAKER_4;
                /*break;
            case 5:
                instructionsPhrase = Phrase.INSTRUCTIONS_CODEBREAKER_5;
                break;
            case 6:
                instructionsPhrase = Phrase.INSTRUCTIONS_CODEBREAKER_6;
                break;
        }*/

        this.audioPlayerExecutor.replacePhraseAndPrint(instructionsPhrase);
    }

    /**
     * Getter for gameOver
     *
     * @return true if the game is over (otherwise, false).
     */
    public boolean isGameOver() {
        return this.gameOver;
    }

    public int getCodeSize() {
        return this.codeSize;
    }

    public Color[] getCodeToBreak() {
        return this.codeToBreak;
    }
}

package gamesforblind.codebreaker;

import gamesforblind.synthesizer.AudioPlayerExecutor;
import org.jetbrains.annotations.NotNull;

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

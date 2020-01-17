package gamesforblind.codebreaker;

import gamesforblind.synthesizer.AudioPlayerExecutor;

/**
 * Class that contains information about the current state of the Sudoku board.
 * Also handles any calls into the {@link AudioPlayerExecutor} for Sudoku.
 */
public class CodebreakerState {
    private final boolean gameOver;

    /**
     * Creates a new MastermindState.
     *
     * @param audioPlayerExecutor Calls into the threaded audio player for the game.
     */
    public CodebreakerState(AudioPlayerExecutor audioPlayerExecutor) {
        this.gameOver = false;
    }

    /**
     * Getter for gameOver
     *
     * @return true if the game is over (otherwise, false).
     */
    public boolean isGameOver() {
        return this.gameOver;
    }
}

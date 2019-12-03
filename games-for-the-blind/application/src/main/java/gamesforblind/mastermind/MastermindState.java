package gamesforblind.mastermind;

import gamesforblind.enums.*;
import gamesforblind.mastermind.interfaces.MastermindKeyboardInterface;
import gamesforblind.sudoku.OriginalSudokuGrid;
import gamesforblind.sudoku.generator.Cell;
import gamesforblind.sudoku.generator.Generator;
import gamesforblind.sudoku.generator.Grid;
import gamesforblind.sudoku.generator.Solver;
import gamesforblind.sudoku.interfaces.SudokuArrowKeyInterface;
import gamesforblind.sudoku.interfaces.SudokuBlockSelectionInterface;
import gamesforblind.sudoku.interfaces.SudokuKeyboardInterface;
import gamesforblind.synthesizer.AudioPlayerExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import phrase.Phrase;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static gamesforblind.Constants.EMPTY_SUDOKU_SQUARE;

/**
 * Class that contains information about the current state of the Sudoku board.
 * Also handles any calls into the {@link AudioPlayerExecutor} for Sudoku.
 */
public class MastermindState {
    private final AudioPlayerExecutor audioPlayerExecutor;

    private boolean gameOver;

    /**
     * Creates a new MastermindState. Important: only used when the game is in playback mode.
     *
     * @param audioPlayerExecutor Calls into the threaded audio player for the game.
     */
    public MastermindState(AudioPlayerExecutor audioPlayerExecutor) {
        this.gameOver = false;
        this.audioPlayerExecutor = audioPlayerExecutor;

    }

    public boolean isGameOver() {
        return gameOver;
    }
}

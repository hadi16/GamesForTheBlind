package gamesforblind.codebreaker;

import gamesforblind.enums.ArrowKeyDirection;
import gamesforblind.enums.CodebreakerType;
import gamesforblind.synthesizer.AudioPlayerExecutor;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

import static gamesforblind.Constants.CODEBREAKER_MAX_AMOUNT_OF_GUESSES;

/**
 * Class that contains information about the current state of the Sudoku board.
 * Also handles any calls into the {@link AudioPlayerExecutor} for Sudoku.
 */
public class CodebreakerState {
    private final CodebreakerType codebreakerType;
    private final int[] codeToBreak;
    private final boolean gameOver;
    private final ArrayList<CodebreakerGuess> guessList = new ArrayList<>();
    private final Point selectedCellPoint = new Point();

    private Integer[] currentGuess;

    public CodebreakerState(@NotNull AudioPlayerExecutor audioPlayerExecutor, @NotNull CodebreakerType codebreakerType) {
        this.gameOver = false;
        this.codebreakerType = codebreakerType;
        this.codeToBreak = this.generateCodeToBreak();
        this.currentGuess = new Integer[this.codebreakerType.getCodeLength()];
    }

    public static boolean checkThatGameIsOver(int[] codeToBreak, ArrayList<CodebreakerGuess> guessList) {
        Objects.requireNonNull(codeToBreak, "Code to break cannot be null!");
        Objects.requireNonNull(guessList, "Guess list must not be null!");

        if (guessList.size() == CODEBREAKER_MAX_AMOUNT_OF_GUESSES) {
            return true;
        }

        return codeToBreak.length == guessList.get(guessList.size() - 1).getNumberInCorrectPosition();
    }

    private int[] generateCodeToBreak() {
        final Random RANDOM = new Random();

        int[] codeToBreak = new int[this.codebreakerType.getCodeLength()];
        for (int i = 0; i < codeToBreak.length; i++) {
            codeToBreak[i] = RANDOM.nextInt(this.codebreakerType.getCodeLength());
        }
        return codeToBreak;
    }

    public void setCodebreakerGuess() {
        this.guessList.add(new CodebreakerGuess(this.codeToBreak, this.currentGuess));
        this.currentGuess = new Integer[this.codebreakerType.getCodeLength()];
    }

    public void setSingleNumber(int numberToSet) {
        this.currentGuess[this.selectedCellPoint.x] = numberToSet;
    }

    public void changeSelectedCellPoint(ArrowKeyDirection arrowKeyDirection) {
        switch (arrowKeyDirection) {
            case LEFT:
                if (this.selectedCellPoint.x == 0) {
                    this.selectedCellPoint.x = this.codebreakerType.getCodeLength() - 1;
                } else {
                    this.selectedCellPoint.x--;
                }
                break;
            case RIGHT:
                if (this.selectedCellPoint.x == this.codebreakerType.getCodeLength() - 1) {
                    this.selectedCellPoint.x = 0;
                } else {
                    this.selectedCellPoint.x++;
                }
                break;
            case UP:
                if (this.selectedCellPoint.y != CODEBREAKER_MAX_AMOUNT_OF_GUESSES - 1) {
                    this.selectedCellPoint.y++;
                }
                break;
            case DOWN:
                if (this.selectedCellPoint.y != 0) {
                    this.selectedCellPoint.y--;
                }
                break;
        }
    }

    /**
     * Getter for gameOver
     *
     * @return true if the game is over (otherwise, false).
     */
    public boolean isGameOver() {
        return this.gameOver;
    }

    public int[] getCodeToBreak() {
        return this.codeToBreak;
    }

    public CodebreakerType getCodebreakerType() {
        return this.codebreakerType;
    }

    public Integer[] getCurrentGuess() {
        return this.currentGuess;
    }

    public ArrayList<CodebreakerGuess> getGuessList() {
        return this.guessList;
    }
}

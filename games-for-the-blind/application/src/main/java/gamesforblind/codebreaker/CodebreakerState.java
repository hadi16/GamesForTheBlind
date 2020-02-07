package gamesforblind.codebreaker;

import gamesforblind.enums.ArrowKeyDirection;
import gamesforblind.enums.CodebreakerType;
import gamesforblind.synthesizer.AudioPlayerExecutor;
import org.jetbrains.annotations.NotNull;
import phrase.Phrase;

import java.awt.*;
import java.util.*;

import static gamesforblind.Constants.CODEBREAKER_MAX_AMOUNT_OF_GUESSES;
import static gamesforblind.Constants.CODEBREAKER_MAX_CODE_INT;

/**
 * Class that contains information about the current state of the Sudoku board.
 * Also handles any calls into the {@link AudioPlayerExecutor} for Sudoku.
 */
public class CodebreakerState {
    private final CodebreakerType codebreakerType;
    private final int[] codeToBreak;
    private final boolean gameOver;
    private final AudioPlayerExecutor audioPlayerExecutor;
    private final ArrayList<CodebreakerGuess> guessList = new ArrayList<>();
    private final Point selectedCellPoint = new Point();

    private Integer[] currentGuess;

    public CodebreakerState(@NotNull AudioPlayerExecutor audioPlayerExecutor, @NotNull CodebreakerType codebreakerType) {
        this.audioPlayerExecutor = audioPlayerExecutor;
        this.gameOver = false;
        this.codebreakerType = codebreakerType;
        this.codeToBreak = this.generateCorrectCode();
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

    private int[] generateCorrectCode() {
        final Random RANDOM = new Random();
        final int CODE_LENGTH = this.codebreakerType.getCodeLength();

        int[] codeToBreak = new int[CODE_LENGTH];
        for (int i = 0; i < codeToBreak.length; i++) {
            codeToBreak[i] = RANDOM.nextInt(CODEBREAKER_MAX_CODE_INT) + 1;
        }
        return codeToBreak;
    }

    public void setCodebreakerGuess() {
        for (Integer i : this.currentGuess) {
            if (i == null) {
                this.audioPlayerExecutor.replacePhraseAndPrint(Phrase.CODEBREAKER_NEED_CODE);
                return;
            }
        }

        CodebreakerGuess currentCodebreakerGuess = new CodebreakerGuess(this.codeToBreak, this.currentGuess);
        this.guessList.add(currentCodebreakerGuess);

        ArrayList<Phrase> relevantPhrases = new ArrayList<>(Collections.singletonList(Phrase.PLACED_CODEBREAKER_CODE));
        for (int i : this.currentGuess) {
            relevantPhrases.add(Phrase.convertIntegerToPhrase(i));
        }

        Phrase[] phrasesToAdd = {
                Phrase.CODEBREAKER_GUESS_NUMBER, Phrase.convertIntegerToPhrase(this.guessList.size()),
                Phrase.CODEBREAKER_NUMBER_CORRECT_POSITION,
                Phrase.convertIntegerToPhrase(currentCodebreakerGuess.getNumberInCorrectPosition()),
                Phrase.CODEBREAKER_NUMBER_ONLY,
                Phrase.convertIntegerToPhrase(currentCodebreakerGuess.getNumberOfCorrectColor())
        };
        relevantPhrases.addAll(Arrays.asList(phrasesToAdd));

        this.audioPlayerExecutor.replacePhraseAndPrint(relevantPhrases);
        this.currentGuess = new Integer[this.codebreakerType.getCodeLength()];

        this.selectedCellPoint.x = 0;
        this.selectedCellPoint.y++;
    }

    public void setSingleNumber(int numberToSet) {
        this.audioPlayerExecutor.replacePhraseAndPrint(
                new ArrayList<>(Arrays.asList(Phrase.PLACED_NUM, Phrase.convertIntegerToPhrase(numberToSet)))
        );

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
        }
    }

    /**
     * Reads the instructions for the 4x4, 6x6, or 9x9 game.
     */
    public void readInstructions() {
        ArrayList<Phrase> instructions_Phrases;
        switch (this.codebreakerType) {
            case FOUR:
                instructions_Phrases = new ArrayList<>(Arrays.asList(
                        Phrase.INSTRUCTIONS_CODEBREAKER_4,
                        Phrase.INSTRUCTIONS_CODEBREAKER_MIDDLE_SAME,
                        Phrase.INSTRUCTIONS_CODEBREAKER_4_SECOND,
                        Phrase.INSTRUCTIONS_CODEBREAKER_ENDING_SAME));
                break;
            case FIVE:
                instructions_Phrases = new ArrayList<>(Arrays.asList(
                        Phrase.INSTRUCTIONS_CODEBREAKER_5,
                        Phrase.INSTRUCTIONS_CODEBREAKER_MIDDLE_SAME,
                        Phrase.INSTRUCTIONS_CODEBREAKER_5_SECOND,
                        Phrase.INSTRUCTIONS_CODEBREAKER_ENDING_SAME));
                break;
            case SIX:
                instructions_Phrases = new ArrayList<>(Arrays.asList(
                        Phrase.INSTRUCTIONS_CODEBREAKER_6,
                        Phrase.INSTRUCTIONS_CODEBREAKER_MIDDLE_SAME,
                        Phrase.INSTRUCTIONS_CODEBREAKER_6_SECOND,
                        Phrase.INSTRUCTIONS_CODEBREAKER_ENDING_SAME));
                break;
            default:
                throw new IllegalArgumentException(
                        String.format("Invalid codebreaker type: '%s'!", this.codebreakerType)
                );
        }

        this.audioPlayerExecutor.replacePhraseAndPrint(instructions_Phrases);
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

    //not implemented yet
    public boolean restart(){return false;}

    public int hint(int value){return 0;}

    public CodebreakerType getCodebreakerType() {
        return this.codebreakerType;
    }

    public Integer[] getCurrentGuess() {
        return this.currentGuess;
    }

    public ArrayList<CodebreakerGuess> getGuessList() {
        return this.guessList;
    }

    public Point getSelectedCellPoint() {
        return this.selectedCellPoint;
    }
}

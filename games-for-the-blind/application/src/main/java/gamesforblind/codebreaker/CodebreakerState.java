package gamesforblind.codebreaker;

import gamesforblind.enums.ArrowKeyDirection;
import gamesforblind.enums.CodebreakerType;
import gamesforblind.synthesizer.AudioPlayerExecutor;
import org.jetbrains.annotations.NotNull;
import phrase.Phrase;

import java.awt.*;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

import static gamesforblind.Constants.CODEBREAKER_MAX_CODE_INT;

/**
 * Class that contains information about the current state of the Codebreaker board.
 * Also handles any calls into the {@link AudioPlayerExecutor} for Codebreaker.
 */
public class CodebreakerState {
    private final CodebreakerType codebreakerType;
    private final AudioPlayerExecutor audioPlayerExecutor;
    private boolean gameOver;
    private int[] codeToBreak;
    private Integer[] currentGuess;
    private ArrayList<CodebreakerGuess> guessList;
    private Point selectedCellPoint;
    private int hintNum;
    private Instant startingInstant;
    private Duration timeElapsed;

    public CodebreakerState(@NotNull AudioPlayerExecutor audioPlayerExecutor, @NotNull CodebreakerType codebreakerType) {
        this.audioPlayerExecutor = audioPlayerExecutor;
        this.codebreakerType = codebreakerType;


        this.initNewCodebreakerGame();
    }

    public static boolean checkThatGameIsOver(
            @NotNull int[] codeToBreak, @NotNull ArrayList<CodebreakerGuess> guessList) {
        Objects.requireNonNull(codeToBreak, "Code to break cannot be null!");
        Objects.requireNonNull(guessList, "Guess list must not be null!");

        CodebreakerType codebreakerType = null;
        for (CodebreakerType type : CodebreakerType.values()) {
            if (codeToBreak.length == type.getCodeLength()) {
                codebreakerType = type;
            }
        }

        if (codebreakerType == null) {
            throw new IllegalArgumentException("Invalid code length passed to checker function!");
        }

        if (guessList.size() == codebreakerType.getNumberOfRows()) {
            return true;
        }

        return codeToBreak.length == guessList.get(guessList.size() - 1).getNumberInCorrectPosition();
    }

    public void initNewCodebreakerGame() {
        this.gameOver = false;
        this.codeToBreak = this.generateCorrectCode();
        this.currentGuess = new Integer[this.codebreakerType.getCodeLength()];
        this.guessList = new ArrayList<>();
        this.selectedCellPoint = new Point();

        this.startingInstant = Instant.now();
        this.timeElapsed = null;
        this.hintNum = 3;
    }

    public void readUnrecognizedKey(int keyCode) {
        ArrayList<Phrase> phrasesToRead = new ArrayList<>(Arrays.asList(
                Phrase.UNRECOGNIZED_KEY, Phrase.keyCodeToPhrase(keyCode)
        ));
        this.audioPlayerExecutor.replacePhraseAndPrint(phrasesToRead);
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

    public void stopReadingPhrases() {
        this.audioPlayerExecutor.replacePhraseAndPrint(new ArrayList<>());
    }

    public void setCodebreakerGuess() {
        if (this.selectedCellPoint.y != this.guessList.size()) {
            return;
        }

        for (Integer i : this.currentGuess) {
            if (i == null) {
                this.audioPlayerExecutor.replacePhraseAndPrint(Phrase.CODEBREAKER_NEED_CODE);
                return;
            }
        }

        CodebreakerGuess currentCodebreakerGuess = new CodebreakerGuess(this.codeToBreak, this.currentGuess);
        this.guessList.add(currentCodebreakerGuess);

        if (checkThatGameIsOver(this.codeToBreak, this.guessList)) {
            // If the game is over, give message
            this.gameOver = true;
            this.timeElapsed = Duration.between(this.startingInstant, Instant.now());

            this.feedbackGameOver();
        } else {
            // If the game is not over, give the player feedback
            this.feedbackGuess(currentCodebreakerGuess);
        }

        this.currentGuess = new Integer[this.codebreakerType.getCodeLength()];
        this.selectedCellPoint.x = 0;
        this.selectedCellPoint.y++;
    }

    private void feedbackGuess(CodebreakerGuess currentGuess) {
        ArrayList<Phrase> relevantPhrases = new ArrayList<>(Collections.singletonList(Phrase.PLACED_CODEBREAKER_CODE));
        for (int i : this.currentGuess) {
            relevantPhrases.add(Phrase.convertIntegerToPhrase(i, true));
        }

        Phrase[] phrasesToAdd = {
                Phrase.CODEBREAKER_NUMBER_CORRECT_POSITION,
                Phrase.convertIntegerToPhrase(currentGuess.getNumberInCorrectPosition(), true),
                Phrase.CODEBREAKER_NUMBER_ONLY,
                Phrase.convertIntegerToPhrase(currentGuess.getNumberOfCorrectColor(), true)
        };
        relevantPhrases.addAll(Arrays.asList(phrasesToAdd));

        this.audioPlayerExecutor.replacePhraseAndPrint(relevantPhrases);

    }

    private void feedbackGameOver() {
        ArrayList<Phrase> relevantPhrases;

        final CodebreakerGuess lastCodebreakerGuess = this.guessList.get(this.guessList.size() - 1);
        if (this.codeToBreak.length == lastCodebreakerGuess.getNumberInCorrectPosition()) {
            // If code is guessed correctly
            relevantPhrases = new ArrayList<>(Collections.singletonList(Phrase.CONGRATS));
        } else {
            // If player ran out of guesses
            relevantPhrases = new ArrayList<>(Collections.singletonList(Phrase.NO_MORE_GUESSES));
            for (int i = 0; i < this.codebreakerType.getCodeLength(); i++) {
                relevantPhrases.add(Phrase.convertIntegerToPhrase(this.codeToBreak[i], true));
            }
        }

        // Indicates that an error occurred with the time measurement.
        if (this.timeElapsed.isNegative()) {
            relevantPhrases.add(Phrase.SPACE_FOR_EXIT);
            this.audioPlayerExecutor.replacePhraseAndPrint(relevantPhrases);
        }

        relevantPhrases.addAll(Phrase.getTimeElapsedPhrases(this.timeElapsed));
        relevantPhrases.add(Phrase.SPACE_FOR_EXIT);

        this.audioPlayerExecutor.replacePhraseAndPrint(relevantPhrases);
    }

    public void setSingleNumber(int numberToSet) {
        if (this.selectedCellPoint.y != this.guessList.size()) {
            return;
        }

        this.audioPlayerExecutor.replacePhraseAndPrint(Phrase.convertIntegerToPhrase(numberToSet, true));


        this.currentGuess[this.selectedCellPoint.x] = numberToSet;
        if (this.selectedCellPoint.x == this.codebreakerType.getCodeLength() - 1) {
            this.selectedCellPoint.x = 0;
        } else {
            this.selectedCellPoint.x = this.selectedCellPoint.x + 1;
        }
    }

    public void changeSelectedCellPoint(@NotNull ArrowKeyDirection arrowKeyDirection) {
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
                if (this.selectedCellPoint.y != 0) {
                    this.selectedCellPoint.y--;
                } else {
                    this.audioPlayerExecutor.replacePhraseAndPrint(Phrase.CODEBREAKER_LAST_ROW);
                }
                break;
            case DOWN:
                final int numberOfRows = this.codebreakerType.getNumberOfRows();
                if (this.selectedCellPoint.y < this.guessList.size() && this.selectedCellPoint.y != numberOfRows - 1) {
                    this.selectedCellPoint.y++;
                } else if (this.selectedCellPoint.y == numberOfRows - 1) {
                    this.audioPlayerExecutor.replacePhraseAndPrint(Phrase.CODEBREAKER_FIRST_ROW);
                }
                break;
        }
    }

    /**
     * Reads the instructions for the codebreaker game of difficulty 4, 5, 6 respectively
     */
    public void readInstructions() {
        final Phrase codeLengthPhrase = Phrase.convertIntegerToPhrase(this.codebreakerType.getCodeLength());

        ArrayList<Phrase> instructionsPhrases = new ArrayList<>(Arrays.asList(
                Phrase.INSTRUCTIONS_CODEBREAKER_1,
                codeLengthPhrase,
                Phrase.INSTRUCTIONS_CODEBREAKER_2,
                codeLengthPhrase,
                Phrase.INSTRUCTIONS_CODEBREAKER_3
        ));

        this.audioPlayerExecutor.replacePhraseAndPrint(instructionsPhrases);
    }

    public void readBackRow() {
        final ArrayList<Phrase> readBackPhrases;
        final int selectedRowIndex = this.selectedCellPoint.y;

        if (this.guessList.size() <= selectedRowIndex) {
            final Integer[] currentGuess;
            if (this.guessList.size() == selectedRowIndex) {
                currentGuess = this.currentGuess;
            } else {
                currentGuess = new Integer[this.codebreakerType.getCodeLength()];
            }

            readBackPhrases = new ArrayList<>(Arrays.asList(
                    Phrase.CODEBREAKER_READ_ROW,
                    Phrase.convertIntegerToPhrase(selectedRowIndex + 1, true), Phrase.CODEBREAKER_UNKNOWN_GUESS
            ));

            for (Integer maybeGuessNumber : currentGuess) {
                if (maybeGuessNumber == null) {
                    readBackPhrases.add(Phrase.ZERO);
                    continue;
                }

                readBackPhrases.add(Phrase.convertIntegerToPhrase(maybeGuessNumber, true));
            }
        } else {
            readBackPhrases = new ArrayList<>(Arrays.asList(
                    Phrase.CODEBREAKER_GUESS_NUMBER_RESPONSE,
                    Phrase.convertIntegerToPhrase(selectedRowIndex + 1, true),
                    Phrase.CODEBREAKER_GUESS_WAS
            ));

            final CodebreakerGuess guess = this.guessList.get(selectedRowIndex);
            for (int guessNumber : guess.getGuessedCode()) {
                readBackPhrases.add(Phrase.convertIntegerToPhrase(guessNumber, true));
            }

            readBackPhrases.addAll(Arrays.asList(
                    Phrase.CODEBREAKER_NUMBER_CORRECT_POSITION,
                    Phrase.convertIntegerToPhrase(guess.getNumberInCorrectPosition(), true),
                    Phrase.CODEBREAKER_NUMBER_ONLY,
                    Phrase.convertIntegerToPhrase(guess.getNumberOfCorrectColor(), true)
            ));
        }

        this.audioPlayerExecutor.replacePhraseAndPrint(readBackPhrases);
    }

    /**
     * Reads the value of the currently selected cell
     */
    public void readSelectedSquare() {
        this.audioPlayerExecutor.replacePhraseAndPrint(Phrase.convertPointToLocationPhrase(this.selectedCellPoint));
    }

    /**
     * Provides the answer for the highlighted cell
     */
    public int getHint() {
        this.hintNum--;
        return this.codeToBreak[this.selectedCellPoint.x];
    }

    public int getHintNum() {
        return this.hintNum;
    }

    public void playNoHint() {
        this.audioPlayerExecutor.replacePhraseAndPrint(Phrase.CODEBREAKER_NO_MORE_HINTS);
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

    public Point getSelectedCellPoint() {
        return this.selectedCellPoint;
    }

    public void setSelectedCellPoint(Point selectedCellPoint) {
        this.selectedCellPoint = selectedCellPoint;
    }

    public Instant getTime() {
        return this.startingInstant;
    }

    public Duration getTimeElapsed() {
        return this.timeElapsed;
    }
}

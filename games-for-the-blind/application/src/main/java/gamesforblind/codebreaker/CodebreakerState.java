package gamesforblind.codebreaker;

import gamesforblind.enums.ArrowKeyDirection;
import gamesforblind.enums.CodebreakerType;
import gamesforblind.synthesizer.AudioPlayerExecutor;
import org.jetbrains.annotations.NotNull;
import phrase.Phrase;

import java.awt.*;
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

    public CodebreakerState(@NotNull AudioPlayerExecutor audioPlayerExecutor, @NotNull CodebreakerType codebreakerType) {
        this.audioPlayerExecutor = audioPlayerExecutor;
        this.codebreakerType = codebreakerType;

        this.initNewCodebreakerGame();
    }

    public boolean initNewCodebreakerGame() {
        this.gameOver = false;
        this.codeToBreak = this.generateCorrectCode();
        this.currentGuess = new Integer[this.codebreakerType.getCodeLength()];
        this.guessList = new ArrayList<>();
        this.selectedCellPoint = new Point();

        return true;
    }

    public void readUnrecognizedKey(int keyCode) {
        ArrayList<Phrase> phrasesToRead = new ArrayList<>(Arrays.asList(
                Phrase.UNRECOGNIZED_KEY, Phrase.keyCodeToPhrase(keyCode)
        ));
        this.audioPlayerExecutor.replacePhraseAndPrint(phrasesToRead);
    }

    public static boolean checkThatGameIsOver(
            @NotNull int[] codeToBreak, @NotNull ArrayList<CodebreakerGuess> guessList
    ) {
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

//            //if guess is correct say correct code
//            ArrayList<Phrase> relevantPhrasesEndGame = new ArrayList<>(Collections.singletonList(Phrase.PLACED_CODEBREAKER_CODE));
//            //relevantPhrasesEndGame.add(Phrase.THE_CORRECT_CODE_IS);
//            for (int i =0; i < codebreakerType.getCodeLength(); i++) {
//                relevantPhrasesEndGame.add(Phrase.convertIntegerToPhrase(codeToBreak[i]));
//            }
//            this.audioPlayerExecutor.replacePhraseAndPrint(relevantPhrasesEndGame);

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

       if(checkThatGameIsOver(codeToBreak, guessList)){
            gameOver = true;
        }

    }

    public void setSingleNumber(int numberToSet) {
        this.audioPlayerExecutor.replacePhraseAndPrint(
                new ArrayList<>(Arrays.asList(Phrase.PLACED_NUM, Phrase.convertIntegerToPhrase(numberToSet)))
        );

        this.currentGuess[this.selectedCellPoint.x] = numberToSet;
        if(this.selectedCellPoint.x == codebreakerType.getCodeLength()-1){
            this.selectedCellPoint.x = 0;
        }
        else{
            this.selectedCellPoint.x =  this.selectedCellPoint.x+1;
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
        }
    }

    /**
     * Reads the instructions for the 4x4, 6x6, or 9x9 game.
     */
    public void readInstructions() {
        ArrayList<Phrase> instructionsPhrases;
        switch (this.codebreakerType) {
            case FOUR:
                instructionsPhrases = new ArrayList<>(Arrays.asList(
                        Phrase.INSTRUCTIONS_CODEBREAKER_4,
                        Phrase.INSTRUCTIONS_CODEBREAKER_MIDDLE_SAME,
                        Phrase.INSTRUCTIONS_CODEBREAKER_4_SECOND,
                        Phrase.INSTRUCTIONS_CODEBREAKER_ENDING_SAME));
                break;
            case FIVE:
                instructionsPhrases = new ArrayList<>(Arrays.asList(
                        Phrase.INSTRUCTIONS_CODEBREAKER_5,
                        Phrase.INSTRUCTIONS_CODEBREAKER_MIDDLE_SAME,
                        Phrase.INSTRUCTIONS_CODEBREAKER_5_SECOND,
                        Phrase.INSTRUCTIONS_CODEBREAKER_ENDING_SAME));
                break;
            case SIX:
                instructionsPhrases = new ArrayList<>(Arrays.asList(
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

        this.audioPlayerExecutor.replacePhraseAndPrint(instructionsPhrases);
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

    public int hint(int value) {
        throw new UnsupportedOperationException("Hint not implemented yet!");
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
}
